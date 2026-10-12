package tech.pujit.strata.engine.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RequestValidatorTest {

    @Test
    @DisplayName("Valid collection names pass validation")
    void testValidCollectionName() {
        assertThatCode(() -> RequestValidator.validateCollectionName("documents_v1"))
            .doesNotThrowAnyException();
        assertThatCode(() -> RequestValidator.validateCollectionName("user-embeddings-128"))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Blank or special character collection names fail validation")
    void testInvalidCollectionName() {
        assertThatThrownBy(() -> RequestValidator.validateCollectionName(""))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> RequestValidator.validateCollectionName("bad name with spaces"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Dimension boundaries are strictly enforced")
    void testDimensionBounds() {
        assertThatCode(() -> RequestValidator.validateDimension(128))
            .doesNotThrowAnyException();
        assertThatThrownBy(() -> RequestValidator.validateDimension(0))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> RequestValidator.validateDimension(100000))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Vector length matching dimension succeeds")
    void testVectorValidation() {
        float[] vec = new float[]{1.0f, 2.0f, 3.0f};
        assertThatCode(() -> RequestValidator.validateVector(vec, 3))
            .doesNotThrowAnyException();
        assertThatThrownBy(() -> RequestValidator.validateVector(vec, 4))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
