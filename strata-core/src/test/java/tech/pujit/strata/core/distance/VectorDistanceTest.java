package tech.pujit.strata.core.distance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tech.pujit.strata.core.memory.OffHeapMemorySegment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.offset;

class VectorDistanceTest {

    @Test
    @DisplayName("Dot product matches manual scalar calculation")
    void testDotProduct() {
        float[] a = new float[]{1.0f, 2.0f, 3.0f, 4.0f};
        float[] b = new float[]{2.0f, 0.5f, -1.0f, 3.0f};

        // 1*2 + 2*0.5 + 3*(-1) + 4*3 = 2 + 1 - 3 + 12 = 12
        float result = VectorDistance.dotProduct(a, b);
        assertThat(result).isCloseTo(12.0f, offset(1e-5f));
    }

    @Test
    @DisplayName("Squared Euclidean distance between identical vectors is zero")
    void testSquaredEuclideanIdentical() {
        float[] a = new float[]{1.5f, 2.5f, 3.5f, 4.5f};
        float dist = VectorDistance.squaredEuclidean(a, a);
        assertThat(dist).isCloseTo(0.0f, offset(1e-5f));
    }

    @Test
    @DisplayName("Squared Euclidean distance matches known value")
    void testSquaredEuclideanKnownValue() {
        float[] a = new float[]{0.0f, 0.0f};
        float[] b = new float[]{3.0f, 4.0f};
        float dist = VectorDistance.squaredEuclidean(a, b);
        assertThat(dist).isEqualTo(25.0f);
    }

    @Test
    @DisplayName("Compute with EUCLIDEAN delegates to squaredEuclidean")
    void testComputeEuclideanDelegation() {
        float[] a = new float[]{1.0f, 2.0f, 3.0f};
        float[] b = new float[]{4.0f, 5.0f, 6.0f};
        float computed = VectorDistance.compute(DistanceFunction.EUCLIDEAN, a, b);
        float direct = VectorDistance.squaredEuclidean(a, b);
        assertThat(computed).isEqualTo(direct);
    }

    @Test
    @DisplayName("Cosine distance between orthogonal vectors is 1.0")
    void testCosineDistanceOrthogonal() {
        float[] a = new float[]{1.0f, 0.0f};
        float[] b = new float[]{0.0f, 1.0f};

        float dist = VectorDistance.cosineDistance(a, b);
        assertThat(dist).isCloseTo(1.0f, offset(1e-5f));
    }

    @Test
    @DisplayName("Dimension mismatch throws IllegalArgumentException")
    void testDimensionMismatch() {
        float[] a = new float[]{1.0f, 2.0f};
        float[] b = new float[]{1.0f};

        assertThatThrownBy(() -> VectorDistance.compute(DistanceFunction.EUCLIDEAN, a, b))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Vector dimension mismatch");
    }

    @Test
    @DisplayName("Off-heap memory segment allocates and closes cleanly using FFM")
    void testOffHeapAllocation() {
        try (OffHeapMemorySegment segment = new OffHeapMemorySegment(1024)) {
            segment.setFloat(0, 42.0f);
            assertThat(segment.getFloat(0)).isEqualTo(42.0f);
            assertThat(segment.byteSize()).isEqualTo(1024);
        }
    }
}
