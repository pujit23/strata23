package tech.pujit.strata.query.trace;

import tech.pujit.strata.query.planner.ExecutionStrategy;

/**
 * Detailed telemetry and performance trace for query execution.
 */
public record QueryTrace(
    ExecutionStrategy strategyUsed,
    long planningNanos,
    long executionNanos,
    long candidatesConsidered,
    long pointsReturned
) {
}
