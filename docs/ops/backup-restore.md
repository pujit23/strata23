# Operations: Backup and Restore

## 1. Snapshot Creation

STRATA provides atomic point-in-time snapshots:
1. Engine halts segment sealing briefly or creates a read-lock on the manifest.
2. Hard-links or copies all sealed segments and the current manifest file into the snapshot directory.
3. Records the current `snapshot_lsn`.

## 2. Point-in-Time Recovery (PITR)

To restore a collection:
1. Restore sealed segments and manifest from the snapshot.
2. Replay subsequent WAL records starting from `snapshot_lsn` up to the target recovery timestamp or LSN.
3. Validate CRC32 checksums during replay.
