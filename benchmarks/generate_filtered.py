"""
Synthetic vector and structured metadata generator for filter-aware evaluation.
"""

import json
from pathlib import Path
import numpy as np
from datasets import generate_synthetic_vectors


def generate_filtered_corpus(
    output_path: Path,
    num_points: int = 10000,
    dimension: int = 128,
    num_categories: int = 10,
    seed: int = 42
) -> None:
    vectors = generate_synthetic_vectors(num_points, dimension, seed)
    rng = np.random.default_rng(seed)

    records = []
    for i in range(num_points):
        records.append({
            "id": f"pt-{i:07d}",
            "vector": vectors[i].tolist(),
            "payload": {
                "category_id": int(rng.integers(0, num_categories)),
                "price": float(rng.uniform(10.0, 1000.0)),
                "in_stock": bool(rng.choice([True, False], p=[0.7, 0.3])),
                "rating": float(rng.uniform(1.0, 5.0)),
            }
        })

    output_path.parent.mkdir(parents=True, exist_ok=True)
    with open(output_path, "w", encoding="utf-8") as f:
        for r in records:
            f.write(json.dumps(r) + "\n")


if __name__ == "__main__":
    generate_filtered_corpus(Path("benchmarks/data/filtered_10k.jsonl"))
    print("Generated benchmarks/data/filtered_10k.jsonl")
