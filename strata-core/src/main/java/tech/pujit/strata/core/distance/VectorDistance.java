package tech.pujit.strata.core.distance;

import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

/**
 * SIMD-accelerated and scalar-fallback vector distance computation kernel.
 * Uses Java Vector API (jdk.incubator.vector) when available, falling back
 * to unrolled scalar loops where hardware or dimensions dictate.
 */
public final class VectorDistance {

    private static final VectorSpecies<Float> SPECIES = FloatVector.SPECIES_PREFERRED;

    private VectorDistance() {
        // static utility
    }

    /**
     * Compute distance between vectors {@code a} and {@code b} using the specified function.
     */
    public static float compute(DistanceFunction function, float[] a, float[] b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Vectors must not be null");
        }
        if (a.length != b.length) {
            throw new IllegalArgumentException("Vector dimension mismatch: " + a.length + " != " + b.length);
        }

        return switch (function) {
            case EUCLIDEAN -> squaredEuclidean(a, b);
            case COSINE -> cosineDistance(a, b);
            case DOT_PRODUCT -> -dotProduct(a, b); // negative dot product for distance ranking
        };
    }

    /**
     * Dot product between vectors {@code a} and {@code b} using Vector API SIMD with scalar tail.
     */
    public static float dotProduct(float[] a, float[] b) {
        int i = 0;
        float sum = 0.0f;
        int length = a.length;
        int bound = SPECIES.loopBound(length);

        FloatVector vsum = FloatVector.zero(SPECIES);
        for (; i < bound; i += SPECIES.length()) {
            FloatVector va = FloatVector.fromArray(SPECIES, a, i);
            FloatVector vb = FloatVector.fromArray(SPECIES, b, i);
            vsum = va.fma(vb, vsum);
        }
        sum += vsum.reduceLanes(VectorOperators.ADD);

        // Scalar fallback loop for tail elements
        for (; i < length; i++) {
            sum += a[i] * b[i];
        }
        return sum;
    }

    /**
     * Squared Euclidean distance. Callers apply sqrt when the true distance is needed.
     */
    public static float squaredEuclidean(float[] a, float[] b) {
        int i = 0;
        float sum = 0.0f;
        int length = a.length;
        int bound = SPECIES.loopBound(length);

        FloatVector vsum = FloatVector.zero(SPECIES);
        for (; i < bound; i += SPECIES.length()) {
            FloatVector va = FloatVector.fromArray(SPECIES, a, i);
            FloatVector vb = FloatVector.fromArray(SPECIES, b, i);
            FloatVector diff = va.sub(vb);
            vsum = diff.fma(diff, vsum);
        }
        sum += vsum.reduceLanes(VectorOperators.ADD);

        // Scalar tail loop
        for (; i < length; i++) {
            float diff = a[i] - b[i];
            sum += diff * diff;
        }
        return sum;
    }

    /**
     * Cosine distance (1.0 - cosine_similarity).
     */
    public static float cosineDistance(float[] a, float[] b) {
        float dot = 0.0f;
        float normA = 0.0f;
        float normB = 0.0f;

        int i = 0;
        int length = a.length;
        int bound = SPECIES.loopBound(length);

        FloatVector vdot = FloatVector.zero(SPECIES);
        FloatVector vna = FloatVector.zero(SPECIES);
        FloatVector vnb = FloatVector.zero(SPECIES);

        for (; i < bound; i += SPECIES.length()) {
            FloatVector va = FloatVector.fromArray(SPECIES, a, i);
            FloatVector vb = FloatVector.fromArray(SPECIES, b, i);
            vdot = va.fma(vb, vdot);
            vna = va.fma(va, vna);
            vnb = vb.fma(vb, vnb);
        }
        dot += vdot.reduceLanes(VectorOperators.ADD);
        normA += vna.reduceLanes(VectorOperators.ADD);
        normB += vnb.reduceLanes(VectorOperators.ADD);

        for (; i < length; i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }

        if (normA <= 0.0f || normB <= 0.0f) {
            return 1.0f;
        }
        float similarity = dot / ((float) Math.sqrt(normA) * (float) Math.sqrt(normB));
        return 1.0f - similarity;
    }
}
