import { describe, it, expect } from "vitest";
import { CreateCollectionSchema, SearchQuerySchema } from "../../src/schemas/collectionSchemas.js";

describe("Collection Schemas Validation", () => {
  it("accepts valid collection configuration", () => {
    const valid = {
      name: "wiki_articles",
      dimension: 768,
      metric: "COSINE",
      hnswConfig: {
        m: 16,
        efConstruction: 100,
        efSearch: 50,
      },
    };

    const result = CreateCollectionSchema.safeParse(valid);
    expect(result.success).toBe(true);
  });

  it("rejects invalid dimensions", () => {
    const invalid = {
      name: "wiki_articles",
      dimension: -10,
      metric: "COSINE",
    };

    const result = CreateCollectionSchema.safeParse(invalid);
    expect(result.success).toBe(false);
  });

  it("validates search query vector", () => {
    const valid = {
      vector: [0.1, 0.2, 0.3],
      topK: 5,
    };

    const result = SearchQuerySchema.safeParse(valid);
    expect(result.success).toBe(true);
  });
});
