package tech.pujit.strata.core.index;

/**
 * Representation of a matched vector candidate.
 * Convention: {@code score} is a distance; lower is closer (for DOT_PRODUCT it is the negated dot product).
 * Natural ordering: lowest score (closest) first, ties by pointId.
 */
public record SearchResult(
    long pointId,
    float score
) implements Comparable<SearchResult> {

    @Override
    public int compareTo(SearchResult o) {
        // Note: The proto contract defines IP score as higher-is-better;
        // conversion happens at the engine/gRPC boundary later, not here.
        // Natural ordering: lowest score (closest) first, ties by pointId.
        int cmp = Float.compare(this.score, o.score);
        if (cmp != 0) {
            return cmp;
        }
        return Long.compare(this.pointId, o.pointId);
    }
}
