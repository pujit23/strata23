package tech.pujit.strata.engine.validation;

/**
 * Validates vector engine requests against dimension and schema constraints.
 */
public final class RequestValidator {

    private RequestValidator() {}

    public static void validateCollectionName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Collection name must not be blank");
        }
        if (!name.matches("^[a-zA-Z0-9_-]{1,64}$")) {
            throw new IllegalArgumentException("Invalid collection name format: " + name);
        }
    }

    public static void validateDimension(int dimension) {
        if (dimension <= 0 || dimension > 65536) {
            throw new IllegalArgumentException("Vector dimension must be between 1 and 65536, got: " + dimension);
        }
    }

    public static void validateVector(float[] vector, int expectedDimension) {
        if (vector == null) {
            throw new IllegalArgumentException("Vector array must not be null");
        }
        if (vector.length != expectedDimension) {
            throw new IllegalArgumentException("Vector length " + vector.length + " does not match expected dimension " + expectedDimension);
        }
    }
}
