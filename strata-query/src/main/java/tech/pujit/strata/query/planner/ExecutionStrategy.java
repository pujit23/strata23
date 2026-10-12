package tech.pujit.strata.query.planner;

/**
 * Filter-aware execution strategy selected by the query optimizer.
 */
public enum ExecutionStrategy {
    EXACT_SCAN,
    PRE_FILTER,
    POST_FILTER,
    ITERATIVE_INDEX
}
