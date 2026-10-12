package tech.pujit.strata.query.planner;

/**
 * Compiled physical query plan with cost and selectivity estimates.
 */
public record QueryPlan(
    ExecutionStrategy strategy,
    double estimatedSelectivity,
    int topK,
    int efSearchOverride
) {
}
