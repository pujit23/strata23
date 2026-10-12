"""
Benchmark dataset generation and ingestion utilities for STRATA.
"""

import numpy as np


def generate_synthetic_vectors(num_points: int, dimension: int, seed: int = 42) -> np.ndarray:
    """
    Generate normalized unit-sphere synthetic float32 vectors.
    """
    rng = np.random.default_rng(seed)
    raw = rng.standard_normal((num_points, dimension), dtype=np.float32)
    norms = np.linalg.norm(raw, axis=1, keepdims=True)
    norms[norms == 0] = 1.0
    return raw / norms
