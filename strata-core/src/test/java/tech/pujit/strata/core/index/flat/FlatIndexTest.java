package tech.pujit.strata.core.index.flat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import tech.pujit.strata.core.distance.DistanceFunction;
import tech.pujit.strata.core.distance.ScalarDistance;
import tech.pujit.strata.core.distance.VectorDistance;
import tech.pujit.strata.core.index.SearchResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FlatIndexTest {

    private static boolean isWithinTolerance(float a, float b) {
        float diff = Math.abs(a - b);
        float maxVal = Math.max(1.0f, Math.max(Math.abs(a), Math.abs(b)));
        return diff <= 1e-4f * maxVal;
    }

    private static float[] randomVector(Random random, int dim, float min, float max) {
        float[] vec = new float[dim];
        float range = max - min;
        for (int i = 0; i < dim; i++) {
            vec[i] = min + random.nextFloat() * range;
        }
        return vec;
    }

    // --- Oracle Tests ---

    @ParameterizedTest
    @EnumSource(DistanceFunction.class)
    @DisplayName("Oracle exact match with contiguous IDs (0..1999)")
    void testOracleContiguousIds(DistanceFunction function) {
        runOracleTest(function, false);
    }

    @ParameterizedTest
    @EnumSource(DistanceFunction.class)
    @DisplayName("Oracle exact match with non-contiguous IDs (id * 13 + 5)")
    void testOracleNonContiguousIds(DistanceFunction function) {
        runOracleTest(function, true);
    }

    private void runOracleTest(DistanceFunction function, boolean nonContiguousIds) {
        final int numVectors = 2000;
        final int dim = 64;
        final int numQueries = 50;
        final int topK = 10;

        Random random = new Random(7);
        FlatIndex index = new FlatIndex(dim, function);

        long[] ids = new long[numVectors];
        float[][] vectors = new float[numVectors][];
        Map<Long, float[]> idToVector = new HashMap<>(numVectors);

        for (int i = 0; i < numVectors; i++) {
            long pointId = nonContiguousIds ? ((long) i * 13 + 5) : i;
            float[] vec = randomVector(random, dim, -5.0f, 5.0f);
            ids[i] = pointId;
            vectors[i] = vec;
            idToVector.put(pointId, vec);
            index.add(pointId, vec);
        }

        // Warm up JIT compiler to ensure consistent SIMD instruction generation
        float[] dummyA = new float[dim];
        float[] dummyB = new float[dim];
        for (int i = 0; i < 15_000; i++) {
            VectorDistance.compute(function, dummyA, dummyB);
        }

        assertThat(index.size()).isEqualTo(numVectors);

        for (int q = 0; q < numQueries; q++) {
            float[] query = randomVector(random, dim, -5.0f, 5.0f);

            // Compute oracle ranking independently
            List<SearchResult> oracle = new ArrayList<>(numVectors);
            for (int i = 0; i < numVectors; i++) {
                float dist = VectorDistance.compute(function, query, vectors[i]);
                oracle.add(new SearchResult(ids[i], dist));
            }
            Collections.sort(oracle);
            List<SearchResult> expected = oracle.subList(0, topK);

            // Search in FlatIndex
            List<SearchResult> actual = index.search(query, topK);

            assertThat(actual).hasSize(topK);

            for (int k = 0; k < topK; k++) {
                SearchResult act = actual.get(k);
                SearchResult exp = expected.get(k);

                assertThat(act.pointId())
                    .withFailMessage("Query %d, rank %d: ID mismatch. Expected %d, got %d", q, k, exp.pointId(), act.pointId())
                    .isEqualTo(exp.pointId());

                assertThat(isWithinTolerance(act.score(), exp.score()))
                    .withFailMessage("Query %d, rank %d: Score mismatch. Expected %f, got %f", q, k, exp.score(), act.score())
                    .isTrue();

                // Second check: recompute with ScalarDistance and check relative tolerance
                float scalarDist = ScalarDistance.compute(function, query, idToVector.get(act.pointId()));
                assertThat(isWithinTolerance(act.score(), scalarDist))
                    .withFailMessage("Query %d, rank %d: SIMD score %f vs Scalar %f out of tolerance", q, k, act.score(), scalarDist)
                    .isTrue();
            }
        }
    }

    // --- Behaviour Tests ---

    @Test
    @DisplayName("Empty index returns empty list")
    void testEmptyIndex() {
        FlatIndex index = new FlatIndex(4, DistanceFunction.EUCLIDEAN);
        assertThat(index.size()).isEqualTo(0);
        List<SearchResult> results = index.search(new float[]{1.0f, 2.0f, 3.0f, 4.0f}, 5);
        assertThat(results).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("topK > size returns all size results, sorted")
    void testTopKGreaterThanSize() {
        FlatIndex index = new FlatIndex(2, DistanceFunction.EUCLIDEAN);
        index.add(1L, new float[]{0.0f, 1.0f});
        index.add(2L, new float[]{0.0f, 3.0f});
        index.add(3L, new float[]{0.0f, 2.0f});

        List<SearchResult> results = index.search(new float[]{0.0f, 0.0f}, 10);
        assertThat(results).hasSize(3);
        assertThat(results).extracting(SearchResult::pointId).containsExactly(1L, 3L, 2L);
        assertThat(results).extracting(SearchResult::score).containsExactly(1.0f, 4.0f, 9.0f);
    }

    @Test
    @DisplayName("topK == 1 returns the single nearest")
    void testTopKEqualsOne() {
        FlatIndex index = new FlatIndex(2, DistanceFunction.EUCLIDEAN);
        index.add(10L, new float[]{0.0f, 5.0f});
        index.add(20L, new float[]{0.0f, 1.0f});
        index.add(30L, new float[]{0.0f, 10.0f});

        List<SearchResult> results = index.search(new float[]{0.0f, 0.0f}, 1);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).pointId()).isEqualTo(20L);
        assertThat(results.get(0).score()).isEqualTo(1.0f);
    }

    @Test
    @DisplayName("topK <= 0 throws IllegalArgumentException")
    void testInvalidTopK() {
        FlatIndex index = new FlatIndex(2, DistanceFunction.EUCLIDEAN);
        float[] query = new float[]{1.0f, 2.0f};

        assertThatThrownBy(() -> index.search(query, 0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("topK must be positive");

        assertThatThrownBy(() -> index.search(query, -1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("topK must be positive");
    }

    @Test
    @DisplayName("Wrong dimension on add and search throws IllegalArgumentException")
    void testDimensionMismatch() {
        FlatIndex index = new FlatIndex(3, DistanceFunction.EUCLIDEAN);

        assertThatThrownBy(() -> index.add(1L, new float[]{1.0f, 2.0f}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Vector dimension mismatch");

        assertThatThrownBy(() -> index.search(new float[]{1.0f, 2.0f, 3.0f, 4.0f}, 5))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Query dimension mismatch");
    }

    @Test
    @DisplayName("NaN and Infinity in add and search throw IllegalArgumentException")
    void testNaNAndInfinity() {
        FlatIndex index = new FlatIndex(2, DistanceFunction.EUCLIDEAN);

        assertThatThrownBy(() -> index.add(1L, new float[]{Float.NaN, 1.0f}))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> index.add(2L, new float[]{Float.POSITIVE_INFINITY, 1.0f}))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> index.add(3L, new float[]{Float.NEGATIVE_INFINITY, 1.0f}))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> index.search(new float[]{Float.NaN, 1.0f}, 5))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> index.search(new float[]{Float.POSITIVE_INFINITY, 1.0f}, 5))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> index.search(new float[]{Float.NEGATIVE_INFINITY, 1.0f}, 5))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Null vector on add and null query on search throw IllegalArgumentException")
    void testNullVectors() {
        FlatIndex index = new FlatIndex(2, DistanceFunction.EUCLIDEAN);

        assertThatThrownBy(() -> index.add(1L, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Vector must not be null");

        assertThatThrownBy(() -> index.search(null, 5))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Query vector must not be null");
    }

    @Test
    @DisplayName("Self-query with EUCLIDEAN returns own id first with score exactly 0f")
    void testSelfQueryEuclidean() {
        FlatIndex index = new FlatIndex(4, DistanceFunction.EUCLIDEAN);
        float[] v1 = new float[]{1.0f, 2.0f, 3.0f, 4.0f};
        float[] v2 = new float[]{5.0f, 6.0f, 7.0f, 8.0f};

        index.add(100L, v1);
        index.add(200L, v2);

        List<SearchResult> results = index.search(v1, 2);
        assertThat(results.get(0).pointId()).isEqualTo(100L);
        assertThat(results.get(0).score()).isEqualTo(0.0f);
    }

    @Test
    @DisplayName("Upsert keeps size unchanged and searches reflect new vector")
    void testUpsert() {
        FlatIndex index = new FlatIndex(2, DistanceFunction.EUCLIDEAN);
        float[] vOriginal = new float[]{10.0f, 10.0f};
        float[] vUpdated = new float[]{1.0f, 1.0f};

        index.add(42L, vOriginal);
        assertThat(index.size()).isEqualTo(1);

        index.add(42L, vUpdated);
        assertThat(index.size()).isEqualTo(1);

        // Query with vUpdated: score should be 0
        List<SearchResult> res1 = index.search(vUpdated, 1);
        assertThat(res1.get(0).pointId()).isEqualTo(42L);
        assertThat(res1.get(0).score()).isEqualTo(0.0f);

        // Query with vOriginal: distance should be (10-1)^2 + (10-1)^2 = 81 + 81 = 162
        List<SearchResult> res2 = index.search(vOriginal, 1);
        assertThat(res2.get(0).pointId()).isEqualTo(42L);
        assertThat(res2.get(0).score()).isEqualTo(162.0f);
    }

    @Test
    @DisplayName("Defensive copy: mutating array after add does not change index")
    void testDefensiveCopy() {
        FlatIndex index = new FlatIndex(2, DistanceFunction.EUCLIDEAN);
        float[] vec = new float[]{1.0f, 2.0f};
        index.add(1L, vec);

        vec[0] = 999.0f; // Mutate caller array

        List<SearchResult> results = index.search(new float[]{1.0f, 2.0f}, 1);
        assertThat(results.get(0).score()).isEqualTo(0.0f);
    }

    @Test
    @DisplayName("Tie determinism: equal vectors return smaller pointId first")
    void testTieDeterminism() {
        FlatIndex index = new FlatIndex(2, DistanceFunction.EUCLIDEAN);
        float[] vec = new float[]{2.0f, 3.0f};
        index.add(9L, vec);
        index.add(3L, vec);

        List<SearchResult> results = index.search(vec, 2);
        assertThat(results).hasSize(2);
        assertThat(results.get(0).pointId()).isEqualTo(3L);
        assertThat(results.get(1).pointId()).isEqualTo(9L);
        assertThat(results.get(0).score()).isEqualTo(0.0f);
        assertThat(results.get(1).score()).isEqualTo(0.0f);
    }

    @Test
    @DisplayName("Constructor validation: dimension <= 0 or null function throws IllegalArgumentException")
    void testConstructorValidation() {
        assertThatThrownBy(() -> new FlatIndex(0, DistanceFunction.EUCLIDEAN))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new FlatIndex(-1, DistanceFunction.EUCLIDEAN))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new FlatIndex(4, null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("close() is idempotent and resets size to 0")
    void testCloseIdempotent() {
        FlatIndex index = new FlatIndex(2, DistanceFunction.EUCLIDEAN);
        index.add(1L, new float[]{1.0f, 2.0f});
        assertThat(index.size()).isEqualTo(1);

        index.close();
        assertThat(index.size()).isEqualTo(0);

        index.close(); // Second close should not throw
        assertThat(index.size()).isEqualTo(0);
    }
}
