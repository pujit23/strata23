package tech.pujit.strata.storage.segment;

/**
 * Versioned delete tombstone store.
 * Allows readers to filter deleted points according to epoch visibility.
 */
public interface TombstoneStore {

    /**
     * Mark a point ID as deleted at the given mutation LSN.
     */
    void markDeleted(String pointId, long deletedAtLsn);

    /**
     * Check if the given point ID is deleted as of the specified reader LSN epoch.
     */
    boolean isDeleted(String pointId, long readerEpochLsn);
}
