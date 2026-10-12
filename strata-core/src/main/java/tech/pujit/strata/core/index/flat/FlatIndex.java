package tech.pujit.strata.core.index.flat;

import tech.pujit.strata.core.distance.DistanceFunction;
import tech.pujit.strata.core.index.SearchResult;
import tech.pujit.strata.core.index.VectorIndex;

import java.util.Collections;
import java.util.List;

/**
 * Exact exhaustive scan index (oracle baseline for recall measurement).
 * TODO(phase-1): Complete exact flat vector storage and brute-force SIMD scan.
 */
public class FlatIndex implements VectorIndex {

    private final int dimension;
    private final DistanceFunction distanceFunction;

    public FlatIndex(int dimension, DistanceFunction distanceFunction) {
        this.dimension = dimension;
        this.distanceFunction = distanceFunction;
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
        // TODO(phase-1): Store vector points in off-heap or continuous arrays
    }

    @Override
    public List<SearchResult> search(float[] query, int topK) {
        // TODO(phase-1): Brute-force distance computation across all points
        return Collections.emptyList();
    }

    @Override
    public void close() {
        // Resource clean up
    }
}
