package tech.pujit.strata.query.hybrid;

import tech.pujit.strata.core.index.SearchResult;

import java.util.List;

/**
 * Combines dense vector rankings with sparse BM25 keyword rankings.
 * TODO(phase-5): Implement Reciprocal Rank Fusion (RRF) and convex score combination.
 */
public class HybridSearchCombiner {

    public static List<SearchResult> rrf(List<SearchResult> denseResults, List<SearchResult> sparseResults, int k) {
        // TODO(phase-5): Reciprocal Rank Fusion formula: 1.0 / (60 + rank)
        return denseResults;
    }
}
