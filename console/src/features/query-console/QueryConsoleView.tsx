import React, { useState } from "react";
import { Search, Terminal } from "lucide-react";

export const QueryConsoleView: React.FC = () => {
  const [collection, setCollection] = useState("documents");
  const [topK, setTopK] = useState(10);
  const [filterJson, setFilterJson] = useState('{\n  "category": "systems",\n  "year": { "$gte": 2024 }\n}');

  return (
    <div className="space-y-6">
      <div className="border-b border-slate-800 pb-4">
        <h2 className="text-xl font-semibold text-slate-100 flex items-center gap-2">
          <Terminal className="w-5 h-5 text-emerald-400" />
          Filter-Aware Query Console
        </h2>
        <p className="text-sm text-slate-400">Execute nearest neighbor vector search with arbitrary boolean metadata predicates.</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="space-y-4 rounded-xl border border-slate-800 bg-slate-900/50 p-5">
          <h3 className="text-sm font-medium text-slate-200">Query Parameters</h3>

          <div>
            <label className="block text-xs font-medium text-slate-400 mb-1">Target Collection</label>
            <input
              type="text"
              value={collection}
              onChange={(e) => setCollection(e.target.value)}
              className="w-full px-3 py-2 rounded-lg border border-slate-800 bg-slate-950 text-sm text-slate-200 focus:outline-none focus:border-indigo-500"
            />
          </div>

          <div>
            <label className="block text-xs font-medium text-slate-400 mb-1">Top K Results</label>
            <input
              type="number"
              value={topK}
              onChange={(e) => setTopK(Number(e.target.value))}
              className="w-full px-3 py-2 rounded-lg border border-slate-800 bg-slate-950 text-sm text-slate-200 focus:outline-none focus:border-indigo-500"
            />
          </div>

          <div>
            <label className="block text-xs font-medium text-slate-400 mb-1">Metadata Filter (JSON)</label>
            <textarea
              rows={4}
              value={filterJson}
              onChange={(e) => setFilterJson(e.target.value)}
              className="w-full font-mono text-xs px-3 py-2 rounded-lg border border-slate-800 bg-slate-950 text-slate-200 focus:outline-none focus:border-indigo-500"
            />
          </div>

          <button className="w-full flex items-center justify-center gap-2 px-4 py-2 rounded-lg bg-emerald-600 hover:bg-emerald-500 text-sm font-medium text-white transition">
            <Search className="w-4 h-4" />
            Execute Vector Search
          </button>
        </div>

        <div className="rounded-xl border border-slate-800 bg-slate-900/50 p-5 flex flex-col justify-center items-center text-center">
          <Terminal className="w-8 h-8 text-slate-700 mb-2" />
          <h4 className="text-sm font-medium text-slate-300">Execution Plan & Results</h4>
          <p className="text-xs text-slate-500 max-w-xs mt-1">
            Query plans, estimated selectivity, strategy chosen, and ranked candidates will appear here.
          </p>
        </div>
      </div>
    </div>
  );
};
