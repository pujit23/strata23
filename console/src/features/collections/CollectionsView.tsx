import React from "react";
import { useQuery } from "@tanstack/react-query";
import { collectionService } from "../../services/collectionService";
import { Database, Plus, RefreshCw } from "lucide-react";

export const CollectionsView: React.FC = () => {
  const { data: collections = [], isLoading, refetch } = useQuery({
    queryKey: ["collections"],
    queryFn: () => collectionService.listCollections().catch(() => []),
  });

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between border-b border-slate-800 pb-4">
        <div>
          <h2 className="text-xl font-semibold text-slate-100 flex items-center gap-2">
            <Database className="w-5 h-5 text-indigo-400" />
            Vector Collections
          </h2>
          <p className="text-sm text-slate-400">Manage vector collections, dimensions, metrics, and index configurations.</p>
        </div>
        <div className="flex items-center gap-3">
          <button
            onClick={() => refetch()}
            className="flex items-center gap-1.5 px-3 py-1.5 rounded-lg border border-slate-750 bg-slate-800 hover:bg-slate-700 text-xs font-medium text-slate-200 transition"
          >
            <RefreshCw className="w-3.5 h-3.5" />
            Refresh
          </button>
          <button className="flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-indigo-600 hover:bg-indigo-500 text-xs font-medium text-white transition shadow-sm">
            <Plus className="w-3.5 h-3.5" />
            New Collection
          </button>
        </div>
      </div>

      {isLoading ? (
        <div className="text-sm text-slate-400 py-8 text-center">Loading collections...</div>
      ) : collections.length === 0 ? (
        <div className="rounded-xl border border-dashed border-slate-800 p-12 text-center">
          <Database className="w-10 h-10 text-slate-600 mx-auto mb-3" />
          <h3 className="text-base font-medium text-slate-200">No collections found</h3>
          <p className="text-sm text-slate-400 max-w-sm mx-auto mt-1">
            Create your first vector collection to begin upserting points and executing filter-aware similarity searches.
          </p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {collections.map((name) => (
            <div key={name} className="p-4 rounded-xl border border-slate-800 bg-slate-900/50 hover:border-slate-700 transition">
              <h4 className="font-medium text-slate-100">{name}</h4>
              <p className="text-xs text-slate-400 mt-1">Vector Index: HNSW / Flat</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
