package tech.pujit.strata.core.distance;

/**
 * Reference and fallback implementation used to validate the SIMD kernels.
 * Uses plain left-to-right scalar loops and does not import Vector API classes.
 */
public final class ScalarDistance {

    private ScalarDistance() {
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
     * Dot product between vectors {@code a} and {@code b}.
     */
    public static float dotProduct(float[] a, float[] b) {
        float sum = 0.0f;
        for (int i = 0; i < a.length; i++) {
            sum += a[i] * b[i];
        }
        return sum;
    }

    /**
     * Squared Euclidean distance between vectors {@code a} and {@code b}.
     */
    public static float squaredEuclidean(float[] a, float[] b) {
        float sum = 0.0f;
        for (int i = 0; i < a.length; i++) {
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

        for (int i = 0; i < a.length; i++) {
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
