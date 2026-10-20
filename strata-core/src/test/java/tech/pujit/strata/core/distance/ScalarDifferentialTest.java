package tech.pujit.strata.core.distance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScalarDifferentialTest {

    private static final int[] DIMENSIONS = {1, 2, 3, 7, 8, 15, 16, 17, 33, 100, 128, 768, 1000};
    private static final int PAIRS_PER_TEST = 200;

    public static boolean isWithinTolerance(float simd, float scalar) {
        float diff = Math.abs(simd - scalar);
        float maxVal = Math.max(1.0f, Math.max(Math.abs(simd), Math.abs(scalar)));
        return diff <= 1e-4f * maxVal;
    }

    record TestConfig(int dimension, DistanceFunction function, float minVal, float maxVal) {
        @Override
        public String toString() {
            return "dim=" + dimension + ", func=" + function + ", range=[" + minVal + ", " + maxVal + "]";
        }
    }

    static Stream<TestConfig> differentialConfigs() {
        List<TestConfig> configs = new ArrayList<>();
        float[][] ranges = {
            {-1.0f, 1.0f},
            {-1000.0f, 1000.0f}
        };

        for (int dim : DIMENSIONS) {
            for (DistanceFunction func : DistanceFunction.values()) {
                for (float[] range : ranges) {
                    configs.add(new TestConfig(dim, func, range[0], range[1]));
                }
            }
        }
        return configs.stream();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("differentialConfigs")
    @DisplayName("SIMD kernel matches scalar reference within relative tolerance")
    void testDifferential(TestConfig config) {
        Random random = new Random(42);
        for (int p = 0; p < PAIRS_PER_TEST; p++) {
            float[] a = randomVector(random, config.dimension(), config.minVal(), config.maxVal());
            float[] b = randomVector(random, config.dimension(), config.minVal(), config.maxVal());

            float simd = VectorDistance.compute(config.function(), a, b);
            float scalar = ScalarDistance.compute(config.function(), a, b);

            assertThat(isWithinTolerance(simd, scalar))
                .withFailMessage(
                    "Mismatch for %s: pair=%d, simd=%f, scalar=%f, diff=%f, maxVal=%f",
                    config, p, simd, scalar, Math.abs(simd - scalar), Math.max(1.0f, Math.max(Math.abs(simd), Math.abs(scalar)))
                )
                .isTrue();
        }
    }

    @Test
    @DisplayName("Zero vector vs non-zero for COSINE returns exactly 1.0f in both implementations")
    void testCosineZeroVector() {
        Random random = new Random(42);
        for (int dim : DIMENSIONS) {
            float[] zero = new float[dim];
            float[] nonZero = randomVector(random, dim, -10.0f, 10.0f);
            // Ensure at least one element is non-zero
            nonZero[0] = 1.0f;

            float simdZeroA = VectorDistance.compute(DistanceFunction.COSINE, zero, nonZero);
            float scalarZeroA = ScalarDistance.compute(DistanceFunction.COSINE, zero, nonZero);
            assertThat(simdZeroA).isEqualTo(1.0f);
            assertThat(scalarZeroA).isEqualTo(1.0f);

            float simdZeroB = VectorDistance.compute(DistanceFunction.COSINE, nonZero, zero);
            float scalarZeroB = ScalarDistance.compute(DistanceFunction.COSINE, nonZero, zero);
            assertThat(simdZeroB).isEqualTo(1.0f);
            assertThat(scalarZeroB).isEqualTo(1.0f);

            float simdBothZero = VectorDistance.compute(DistanceFunction.COSINE, zero, zero);
            float scalarBothZero = ScalarDistance.compute(DistanceFunction.COSINE, zero, zero);
            assertThat(simdBothZero).isEqualTo(1.0f);
            assertThat(scalarBothZero).isEqualTo(1.0f);
        }
    }

    @Test
    @DisplayName("Identical vectors return consistent distances in both implementations")
    void testIdenticalVectors() {
        Random random = new Random(42);
        for (int dim : DIMENSIONS) {
            float[] a = randomVector(random, dim, -100.0f, 100.0f);
            // Ensure non-zero for cosine
            a[0] = 1.0f;

            float simdEuclid = VectorDistance.compute(DistanceFunction.EUCLIDEAN, a, a);
            float scalarEuclid = ScalarDistance.compute(DistanceFunction.EUCLIDEAN, a, a);
            assertThat(simdEuclid).isEqualTo(0.0f);
            assertThat(scalarEuclid).isEqualTo(0.0f);

            float simdCosine = VectorDistance.compute(DistanceFunction.COSINE, a, a);
            float scalarCosine = ScalarDistance.compute(DistanceFunction.COSINE, a, a);
            assertThat(isWithinTolerance(simdCosine, scalarCosine)).isTrue();
            assertThat(simdCosine).isCloseTo(0.0f, org.assertj.core.api.Assertions.offset(1e-4f));

            float simdDot = VectorDistance.compute(DistanceFunction.DOT_PRODUCT, a, a);
            float scalarDot = ScalarDistance.compute(DistanceFunction.DOT_PRODUCT, a, a);
            assertThat(isWithinTolerance(simdDot, scalarDot)).isTrue();
        }
    }

    @Test
    @DisplayName("Null input throws IllegalArgumentException with identical message")
    void testNullInput() {
        float[] valid = new float[]{1.0f, 2.0f};

        for (DistanceFunction func : DistanceFunction.values()) {
            assertThatThrownBy(() -> VectorDistance.compute(func, null, valid))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Vectors must not be null");

            assertThatThrownBy(() -> ScalarDistance.compute(func, null, valid))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Vectors must not be null");

            assertThatThrownBy(() -> VectorDistance.compute(func, valid, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Vectors must not be null");

            assertThatThrownBy(() -> ScalarDistance.compute(func, valid, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Vectors must not be null");
        }
    }

    @Test
    @DisplayName("Mismatched lengths throw IllegalArgumentException with identical message")
    void testMismatchedLengths() {
        float[] a = new float[]{1.0f, 2.0f, 3.0f};
        float[] b = new float[]{1.0f, 2.0f};

        for (DistanceFunction func : DistanceFunction.values()) {
            assertThatThrownBy(() -> VectorDistance.compute(func, a, b))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Vector dimension mismatch: 3 != 2");

            assertThatThrownBy(() -> ScalarDistance.compute(func, a, b))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Vector dimension mismatch: 3 != 2");
        }
    }

    private static float[] randomVector(Random random, int dim, float min, float max) {
        float[] vec = new float[dim];
        float range = max - min;
        for (int i = 0; i < dim; i++) {
            vec[i] = min + random.nextFloat() * range;
        }
        return vec;
    }
}
