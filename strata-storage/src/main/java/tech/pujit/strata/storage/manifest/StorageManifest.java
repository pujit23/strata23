package tech.pujit.strata.storage.manifest;

import java.util.List;

/**
 * Manifest representing the durable metadata of a collection.
 * Tracks active segments, current sealed state, and durable checkpoint LSN.
 */
public record StorageManifest(
    String collectionName,
    int schemaVersion,
    long checkpointLsn,
    List<String> activeSegmentIds,
    long lastUpdatedNanos
) {
}
