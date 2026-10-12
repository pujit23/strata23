package tech.pujit.strata.query.planner;

import tech.pujit.strata.query.filter.Predicate;

/**
 * Filter-aware Query Planner interface.
 * Cost-based optimization choosing between pre-filtering, post-filtering,
 * iterative index search, and brute-force flat scan based on estimated selectivity.
 */
public interface QueryPlanner {

    /**
     * Compile a query plan given collection statistics and filter predicates.
     */
    QueryPlan plan(String collectionName, Predicate filter, int topK, double estimatedSelectivity);
}
