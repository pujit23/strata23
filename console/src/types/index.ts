export type DistanceMetric = "COSINE" | "EUCLIDEAN" | "DOT_PRODUCT";

export interface CollectionSummary {
  name: string;
  dimension: number;
  metric: DistanceMetric;
  pointCount: number;
  segmentCount: number;
  memoryBytes: number;
}

export interface SearchResultItem {
  id: string;
  score: number;
  vector?: number[];
  payload?: Record<string, unknown>;
}

export interface BenchmarkRun {
  id: string;
  datasetName: string;
  totalPoints: number;
  topK: number;
  qps: number;
  p99LatencyMs: number;
  recallAt10: number;
}
