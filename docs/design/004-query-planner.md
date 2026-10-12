# Design 004: Query Planner & Cost-Based Optimization

## Cost Model

The Query Planner selects the execution strategy using cost heuristics based on:
- Collection cardinality $N$.
- Estimated filter selectivity $\sigma \in [0.0, 1.0]$.
- Target $K$ nearest neighbors.
- Index construction type (Flat, HNSW, IVF-PQ).

$$\text{Cost}_{\text{Exact}} = \sigma \cdot N \cdot C_{\text{dist}}$$
$$\text{Cost}_{\text{HNSW}} = efSearch \cdot \log(N) \cdot C_{\text{dist}} \cdot \max(1, \frac{1}{\sigma})$$
