package tech.pujit.strata.query.filter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tech.pujit.strata.query.bm25.Bm25Scorer;

import static org.assertj.core.api.Assertions.assertThat;

class FilterEvaluatorTest {

    @Test
    @DisplayName("alwaysTrue predicate returns true for any payload")
    void testAlwaysTrue() {
        Predicate predicate = FilterEvaluator.alwaysTrue();
        assertThat(predicate.test(null)).isTrue();
        assertThat(predicate.test("{\"tag\":\"fast\"}")).isTrue();
    }

    @Test
    @DisplayName("containsSubstring predicate accurately matches JSON text")
    void testContainsSubstring() {
        Predicate predicate = FilterEvaluator.containsSubstring("vector");
        assertThat(predicate.test("{\"type\":\"vector_database\"}")).isTrue();
        assertThat(predicate.test("{\"type\":\"relational\"}")).isFalse();
        assertThat(predicate.test(null)).isFalse();
    }

    @Test
    @DisplayName("BM25 scorer produces non-negative score for matching term")
    void testBm25Score() {
        Bm25Scorer scorer = new Bm25Scorer(1.2f, 0.75f);
        float score = scorer.score(2, 50, 45.0f, 1.5f);
        assertThat(score).isGreaterThan(0.0f);
    }
}
