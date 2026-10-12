# Design 002: HNSW Graph Implementation

## Hierarchical Navigable Small World Index

- **Layer Structure**: Probabilistic multi-layer skip-list graph. Top layers allow rapid coarse navigation; bottom layer 0 contains all points with dense local connectivity.
- **Off-Heap Neighbor Lists**: Graph adjacency lists are stored off-heap in continuous memory blocks to maximize L1/L2 cache locality and eliminate GC overhead.
- **Parameters**:
  - `M`: Maximum number of bidirectional connections per element per layer.
  - `efConstruction`: Size of dynamic candidate list during graph insertion.
  - `efSearch`: Size of dynamic candidate list during nearest-neighbor retrieval.
