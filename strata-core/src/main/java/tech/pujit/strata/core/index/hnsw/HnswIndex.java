package tech.pujit.strata.core.index.hnsw;

import tech.pujit.strata.core.distance.DistanceFunction;
import tech.pujit.strata.core.index.SearchResult;
import tech.pujit.strata.core.index.VectorIndex;

import java.util.Collections;
import java.util.List;

/**
 * Hierarchical Navigable Small World (HNSW) graph index.
 * TODO(phase-2): Implement multi-layer graph, entry-point routing, heuristic edge selection.
 */
public class HnswIndex implements VectorIndex {

    private final int dimension;
    private final DistanceFunction distanceFunction;
    private final int m;
    private final int efConstruction;

    public HnswIndex(int dimension, DistanceFunction distanceFunction, int m, int efConstruction) {
        this.dimension = dimension;
        this.distanceFunction = distanceFunction;
        this.m = m;
        this.efConstruction = efConstruction;
    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public int dimension() {
        return dimension;
    }

    @Override
    public void add(long pointId, float[] vector) {
        // TODO(phase-2): Insert point into layered graph
    }

    @Override
    public List<SearchResult> search(float[] query, int topK) {
        // TODO(phase-2): Greedy beam search with efSearch
        return Collections.emptyList();
    }

    public List<SearchResult> search(float[] query, int topK, int efSearch) {
        // TODO(phase-2): Search with custom efSearch parameter
        return Collections.emptyList();
    }

    @Override
    public void close() {
        // Graph resources clean up
    }
}
