package tech.pujit.strata.core.index;

/**
 * Representation of a matched vector candidate.
 */
public record SearchResult(
    long pointId,
    float score
) implements Comparable<SearchResult> {

    @Override
    public int compareTo(SearchResult o) {
        // Natural ordering: highest score first
        return Float.compare(o.score, this.score);
    }
}
