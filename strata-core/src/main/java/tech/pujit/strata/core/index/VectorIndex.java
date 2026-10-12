package tech.pujit.strata.core.index;

import java.util.List;

/**
 * Common abstraction for all in-memory and segment-backed vector indexes.
 */
public interface VectorIndex extends AutoCloseable {

    /**
     * Number of vector points indexed.
     */
    long size();

    /**
     * Vector dimension expected by this index.
     */
    int dimension();

    /**
     * Add or update a vector in this index.
     * Note: In persistent segments, indexes are sealed and immutable.
     */
    void add(long pointId, float[] vector);

    /**
     * Search nearest neighbors for the given query vector.
     *
     * @param query  the query vector
     * @param topK   maximum results to return
     * @return ranked list of search results
     */
    List<SearchResult> search(float[] query, int topK);

    @Override
    void close();
}
