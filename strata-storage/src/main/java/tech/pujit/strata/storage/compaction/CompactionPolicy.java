package tech.pujit.strata.storage.compaction;

import tech.pujit.strata.storage.segment.Segment;

import java.util.List;

/**
 * Segment compaction policy.
 * Decides when multiple sealed segments should be merged into a larger segment
 * and dead points purged from tombstones.
 */
public interface CompactionPolicy {

    boolean shouldCompact(List<Segment> sealedSegments);

    List<Segment> selectCandidates(List<Segment> sealedSegments);
}
