package tech.pujit.strata.query.filter;

/**
 * Filter evaluator evaluating field conditions against JSON payloads.
 * TODO(phase-4): Implement full filter AST parsing and inverted-index bitset evaluation.
 */
public class FilterEvaluator {

    /**
     * Match-all predicate when no filter is provided.
     */
    public static Predicate alwaysTrue() {
        return payloadJson -> true;
    }

    /**
     * Simple substring/exact match predicate scaffold.
     */
    public static Predicate containsSubstring(String term) {
        return payloadJson -> payloadJson != null && payloadJson.contains(term);
    }
}
