package tech.pujit.strata.core.index.flat;

import tech.pujit.strata.core.distance.DistanceFunction;
import tech.pujit.strata.core.distance.VectorDistance;
import tech.pujit.strata.core.index.SearchResult;
import tech.pujit.strata.core.index.VectorIndex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Exact exhaustive scan index (oracle baseline for recall measurement).
 * Performs brute-force SIMD scan over stored vector points.
 *
 * <p>This implementation is not thread-safe.
 *
 * // TODO(phase-1b): off-heap MemorySegment storage
 */
public class FlatIndex implements VectorIndex {

    private static final int DEFAULT_CAPACITY = 16;

    private final int dimension;
    private final DistanceFunction distanceFunction;

    // TODO(phase-1b): off-heap MemorySegment storage
    private long[] ids;
    private float[][] vectors;
    private final Map<Long, Integer> idToSlot;
    private int size;

    public FlatIndex(int dimension, DistanceFunction distanceFunction) {
        if (dimension <= 0) {
            throw new IllegalArgumentException("Dimension must be positive: " + dimension);
        }
        if (distanceFunction == null) {
            throw new IllegalArgumentException("DistanceFunction must not be null");
        }
        this.dimension = dimension;
        this.distanceFunction = distanceFunction;
        this.ids = new long[DEFAULT_CAPACITY];
        this.vectors = new float[DEFAULT_CAPACITY][];
        this.idToSlot = new HashMap<>();
        this.size = 0;
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public int dimension() {
        return dimension;
    }

    @Override
    public void add(long pointId, float[] vector) {
        if (vector == null) {
            throw new IllegalArgumentException("Vector must not be null");
        }
        if (vector.length != dimension) {
            throw new IllegalArgumentException(
                "Vector dimension mismatch: expected " + dimension + ", got " + vector.length
            );
        }
        for (float v : vector) {
            if (Float.isNaN(v) || Float.isInfinite(v)) {
                throw new IllegalArgumentException("Vector elements must be finite numbers; found: " + v);
            }
        }

        float[] copy = vector.clone();
        Integer slot = idToSlot.get(pointId);
        if (slot != null) {
            vectors[slot] = copy;
        } else {
            ensureCapacity(size + 1);
            int newSlot = size;
            ids[newSlot] = pointId;
            vectors[newSlot] = copy;
            idToSlot.put(pointId, newSlot);
            size++;
        }
    }

    @Override
    public List<SearchResult> search(float[] query, int topK) {
        if (query == null) {
            throw new IllegalArgumentException("Query vector must not be null");
        }
        if (query.length != dimension) {
            throw new IllegalArgumentException(
                "Query dimension mismatch: expected " + dimension + ", got " + query.length
            );
        }
        if (topK <= 0) {
            throw new IllegalArgumentException("topK must be positive: " + topK);
        }
        for (float q : query) {
            if (Float.isNaN(q) || Float.isInfinite(q)) {
                throw new IllegalArgumentException("Query elements must be finite numbers; found: " + q);
            }
        }

        if (size == 0) {
            return new ArrayList<>();
        }

        int k = Math.min(topK, size);
        // Max-heap: worst candidate at root (largest score, tie-break by largest pointId)
        PriorityQueue<SearchResult> heap = new PriorityQueue<>(k, Collections.reverseOrder());

        for (int i = 0; i < size; i++) {
            float dist = VectorDistance.compute(distanceFunction, query, vectors[i]);
            SearchResult candidate = new SearchResult(ids[i], dist);
            if (heap.size() < k) {
                heap.add(candidate);
            } else {
                SearchResult worst = heap.peek();
                if (candidate.compareTo(worst) < 0) {
                    heap.poll();
                    heap.add(candidate);
                }
            }
        }

        List<SearchResult> results = new ArrayList<>(heap);
        Collections.sort(results);
        return results;
    }

    @Override
    public void close() {
        idToSlot.clear();
        ids = new long[0];
        vectors = new float[0][];
        size = 0;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > ids.length) {
            int newCapacity = Math.max(minCapacity, ids.length * 2);
            ids = Arrays.copyOf(ids, newCapacity);
            vectors = Arrays.copyOf(vectors, newCapacity);
        }
    }
}
