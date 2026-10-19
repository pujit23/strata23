package tech.pujit.strata.core.index;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SearchResultTest {

    @Test
    @DisplayName("Sorting a list puts smaller score first (ascending distance order)")
    void testSortAscendingByScore() {
        List<SearchResult> results = new ArrayList<>(List.of(
            new SearchResult(1L, 5.0f),
            new SearchResult(2L, 1.0f),
            new SearchResult(3L, 3.5f),
            new SearchResult(4L, 0.2f)
        ));

        Collections.sort(results);

        assertThat(results).containsExactly(
            new SearchResult(4L, 0.2f),
            new SearchResult(2L, 1.0f),
            new SearchResult(3L, 3.5f),
            new SearchResult(1L, 5.0f)
        );
    }

    @Test
    @DisplayName("Equal scores order by smaller pointId (tie-breaking)")
    void testTieBreakByPointId() {
        List<SearchResult> results = new ArrayList<>(List.of(
            new SearchResult(10L, 2.0f),
            new SearchResult(3L, 2.0f),
            new SearchResult(7L, 2.0f),
            new SearchResult(1L, 2.0f)
        ));

        Collections.sort(results);

        assertThat(results).containsExactly(
            new SearchResult(1L, 2.0f),
            new SearchResult(3L, 2.0f),
            new SearchResult(7L, 2.0f),
            new SearchResult(10L, 2.0f)
        );
    }

    @Test
    @DisplayName("Combined score ordering and tie-breaking")
    void testScoreAndTieBreakCombined() {
        List<SearchResult> results = new ArrayList<>(List.of(
            new SearchResult(9L, 1.5f),
            new SearchResult(5L, 0.5f),
            new SearchResult(2L, 1.5f),
            new SearchResult(8L, 0.5f)
        ));

        Collections.sort(results);

        assertThat(results).containsExactly(
            new SearchResult(5L, 0.5f),
            new SearchResult(8L, 0.5f),
            new SearchResult(2L, 1.5f),
            new SearchResult(9L, 1.5f)
        );
    }
}
