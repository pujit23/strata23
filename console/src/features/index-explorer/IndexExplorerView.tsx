import React from "react";
import { Eye, Network } from "lucide-react";

export const IndexExplorerView: React.FC = () => {
  return (
    <div className="space-y-6">
      <div className="border-b border-slate-800 pb-4">
        <h2 className="text-xl font-semibold text-slate-100 flex items-center gap-2">
          <Network className="w-5 h-5 text-cyan-400" />
          HNSW & Segment Graph Explorer
        </h2>
        <p className="text-sm text-slate-400">Interactive 3D graph and force-directed topology visualization using three.js and d3-force.</p>
      </div>

      <div className="rounded-xl border border-slate-800 bg-slate-900/40 h-[480px] flex flex-col items-center justify-center text-center p-6">
        <Eye className="w-12 h-12 text-slate-700 mb-3 animate-pulse" />
        <h3 className="text-base font-medium text-slate-200">Index Graph Canvas</h3>
        <p className="text-xs text-slate-500 max-w-sm mt-1">
          Select an active collection and segment to inspect HNSW graph layers, entry points, and edge degree distributions.
        </p>
      </div>
    </div>
  );
};
