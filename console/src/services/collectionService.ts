import { apiClient } from "../lib/api";
import { SearchResultItem } from "../types";

export const collectionService = {
  async listCollections(): Promise<string[]> {
    const res = await apiClient<{ collections: string[] }>("/collections");
    return res.collections;
  },

  async createCollection(data: { name: string; dimension: number; metric: string }): Promise<void> {
    await apiClient("/collections", {
      method: "POST",
      body: JSON.stringify(data),
    });
  },

  async searchPoints(collection: string, vector: number[], topK: number = 10): Promise<SearchResultItem[]> {
    const res = await apiClient<{ results: SearchResultItem[] }>(`/collections/${collection}/search`, {
      method: "POST",
      body: JSON.stringify({ vector, topK }),
    });
    return res.results;
  },
};
