# Design 003: Filter-Aware Search Strategies

## Execution Approaches

1. **Pre-Filtering (Filter-First)**:
   - When filter selectivity is extremely tight (< 1%), evaluating the filter produces a small candidate bitset.
   - Exact vector distance scan over the candidate subset beats graph traversal.

2. **Post-Filtering (Search-First)**:
   - When selectivity is broad (> 80%), perform standard HNSW beam search oversampled by factor `1 / selectivity`, then discard non-matching points.

3. **Iterative Index Search (Single-Stage Filtered HNSW)**:
   - For intermediate selectivity (1% - 80%), traverse HNSW graph while skipping non-matching nodes during distance ranking but allowing them as routing hops.
