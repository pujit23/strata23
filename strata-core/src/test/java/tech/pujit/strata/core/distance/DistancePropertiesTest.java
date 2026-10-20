package tech.pujit.strata.core.distance;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

import static org.assertj.core.api.Assertions.assertThat;

class DistancePropertiesTest {

    private static boolean isWithinTolerance(float a, float b) {
        float diff = Math.abs(a - b);
        float maxVal = Math.max(1.0f, Math.max(Math.abs(a), Math.abs(b)));
        return diff <= 1e-4f * maxVal;
    }

    @Provide
    Arbitrary<float[][]> vectorPairs() {
        return Arbitraries.integers().between(1, 512).flatMap(dim -> {
            Arbitrary<Float> floats = Arbitraries.floats().between(-100.0f, 100.0f);
            Arbitrary<float[]> vector = floats.array(float[].class).ofSize(dim);
            return Combinators.combine(vector, vector).as((a, b) -> new float[][]{a, b});
        });
    }

    @Provide
    Arbitrary<float[]> singleVectors() {
        return Arbitraries.integers().between(1, 512).flatMap(dim -> {
            Arbitrary<Float> floats = Arbitraries.floats().between(-100.0f, 100.0f);
            return floats.array(float[].class).ofSize(dim);
        });
    }

    @Property
    void squaredEuclideanSymmetry(@ForAll("vectorPairs") float[][] pair) {
        float[] a = pair[0];
        float[] b = pair[1];
        float ab = VectorDistance.squaredEuclidean(a, b);
        float ba = VectorDistance.squaredEuclidean(b, a);
        assertThat(isWithinTolerance(ab, ba))
            .withFailMessage("squaredEuclidean(a, b) = %f, squaredEuclidean(b, a) = %f", ab, ba)
            .isTrue();
    }

    @Property
    void squaredEuclideanSelfIsZero(@ForAll("singleVectors") float[] a) {
        float dist = VectorDistance.squaredEuclidean(a, a);
        assertThat(dist).isEqualTo(0.0f);
    }

    @Property
    void squaredEuclideanNonNegative(@ForAll("vectorPairs") float[][] pair) {
        float[] a = pair[0];
        float[] b = pair[1];
        float dist = VectorDistance.squaredEuclidean(a, b);
        assertThat(dist).isGreaterThanOrEqualTo(0.0f);
    }

    @Property
    void dotProductSymmetry(@ForAll("vectorPairs") float[][] pair) {
        float[] a = pair[0];
        float[] b = pair[1];
        float ab = VectorDistance.dotProduct(a, b);
        float ba = VectorDistance.dotProduct(b, a);
        assertThat(isWithinTolerance(ab, ba))
            .withFailMessage("dotProduct(a, b) = %f, dotProduct(b, a) = %f", ab, ba)
            .isTrue();
    }
}
