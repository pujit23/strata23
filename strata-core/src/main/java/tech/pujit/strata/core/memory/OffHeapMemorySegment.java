package tech.pujit.strata.core.memory;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

/**
 * Off-heap memory wrapper backed by Java Foreign Function & Memory (FFM) API (Arena / MemorySegment).
 * Guarantees zero-copy native memory access and deterministic lifecycle reclamation without GC pauses.
 */
public class OffHeapMemorySegment implements AutoCloseable {

    private final Arena arena;
    private final MemorySegment segment;
    private final long byteSize;

    public OffHeapMemorySegment(long byteSize) {
        if (byteSize <= 0) {
            throw new IllegalArgumentException("Byte size must be positive: " + byteSize);
        }
        this.byteSize = byteSize;
        this.arena = Arena.ofShared();
        this.segment = arena.allocate(byteSize);
    }

    public long byteSize() {
        return byteSize;
    }

    public MemorySegment rawSegment() {
        return segment;
    }

    public void setFloat(long offsetBytes, float value) {
        segment.set(ValueLayout.JAVA_FLOAT, offsetBytes, value);
    }

    public float getFloat(long offsetBytes) {
        return segment.get(ValueLayout.JAVA_FLOAT, offsetBytes);
    }

    @Override
    public void close() {
        arena.close();
    }
}
