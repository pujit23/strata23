package tech.pujit.strata.query.filter;

/**
 * Filter predicate evaluating payload attributes.
 */
@FunctionalInterface
public interface Predicate {
    boolean test(String payloadJson);
}
