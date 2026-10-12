import { z } from "zod";

export const DistanceMetricEnum = z.enum(["COSINE", "EUCLIDEAN", "DOT_PRODUCT"]);

export const CreateCollectionSchema = z.object({
  name: z.string().min(1).max(64).regex(/^[a-zA-Z0-9_-]+$/),
  dimension: z.number().int().min(1).max(65536),
  metric: DistanceMetricEnum,
  hnswConfig: z
    .object({
      m: z.number().int().min(4).max(128).default(16),
      efConstruction: z.number().int().min(8).max(1024).default(100),
      efSearch: z.number().int().min(4).max(1024).default(50),
    })
    .optional(),
});

export const SearchQuerySchema = z.object({
  vector: z.array(z.number()).min(1),
  topK: z.number().int().min(1).max(1000).default(10),
  filter: z.record(z.unknown()).optional(),
  efSearch: z.number().int().min(1).max(2048).optional(),
});

export const UpsertPointsSchema = z.object({
  points: z.array(
    z.object({
      id: z.string().min(1),
      vector: z.array(z.number()).min(1),
      payload: z.record(z.unknown()).optional(),
    })
  ).min(1),
});

export type CreateCollectionInput = z.infer<typeof CreateCollectionSchema>;
export type SearchQueryInput = z.infer<typeof SearchQuerySchema>;
export type UpsertPointsInput = z.infer<typeof UpsertPointsSchema>;
