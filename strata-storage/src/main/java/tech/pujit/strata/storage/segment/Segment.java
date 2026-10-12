package tech.pujit.strata.storage.segment;

import java.io.Closeable;

/**
 * Storage segment representing a partition of vector data.
 * Contract:
 * 1. Segment data is immutable after sealing.
 * 2. Deletes use versioned tombstone state rather than in-place mutation.
 */
public interface Segment extends Closeable {

    String segmentId();

    long pointCount();

    long minLsn();

    long maxLsn();

    boolean isSealed();

    void seal();
}
