package tech.pujit.strata.storage.snapshot;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Manages point-in-time consistent snapshots of collection segments and manifests.
 */
public interface SnapshotManager {

    record SnapshotMeta(
        String snapshotId,
        long snapshotLsn,
        Path snapshotPath,
        long timestampNanos
    ) {}

    SnapshotMeta createSnapshot(String collectionName) throws IOException;

    void restoreSnapshot(String snapshotId, Path targetDir) throws IOException;
}
