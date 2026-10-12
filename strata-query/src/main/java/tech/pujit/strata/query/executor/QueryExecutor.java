package tech.pujit.strata.query.executor;

import tech.pujit.strata.core.index.SearchResult;
import tech.pujit.strata.query.filter.Predicate;
import tech.pujit.strata.query.planner.QueryPlan;
import tech.pujit.strata.query.trace.QueryTrace;

import java.util.List;

/**
 * Executes a planned query across active segments and in-memory indexes.
 */
public interface QueryExecutor {

    record ExecutionResult(
        List<SearchResult> results,
        QueryTrace trace
    ) {}

    ExecutionResult execute(String collectionName, float[] queryVector, Predicate filter, QueryPlan plan);
}
