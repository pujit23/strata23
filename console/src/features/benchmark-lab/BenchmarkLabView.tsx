import React from "react";
import { BarChart3, Play } from "lucide-react";
import { ResponsiveContainer, LineChart, Line, XAxis, YAxis, Tooltip, CartesianGrid } from "recharts";

const SAMPLE_BENCHMARK_DATA = [
  { qps: 500, p99LatencyMs: 1.2, recall: 0.99 },
  { qps: 1500, p99LatencyMs: 2.1, recall: 0.98 },
  { qps: 3000, p99LatencyMs: 4.8, recall: 0.96 },
  { qps: 5000, p99LatencyMs: 8.5, recall: 0.94 },
];

export const BenchmarkLabView: React.FC = () => {
  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between border-b border-slate-800 pb-4">
        <div>
          <h2 className="text-xl font-semibold text-slate-100 flex items-center gap-2">
            <BarChart3 className="w-5 h-5 text-amber-400" />
            Benchmark Lab & Recall Evaluation
          </h2>
          <p className="text-sm text-slate-400">Evaluate QPS vs Recall trade-offs, p99 latency curves, and filter selectivity benchmarks.</p>
        </div>
        <button className="flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-amber-600 hover:bg-amber-500 text-xs font-medium text-white transition">
          <Play className="w-3.5 h-3.5" />
          Run Benchmark Suite
        </button>
      </div>

      <div className="rounded-xl border border-slate-800 bg-slate-900/50 p-5">
        <h3 className="text-sm font-medium text-slate-200 mb-4">Recall @ 10 vs QPS Curve</h3>
        <div className="h-64 w-full">
          <ResponsiveContainer width="100%" height="100%">
            <LineChart data={SAMPLE_BENCHMARK_DATA}>
              <CartesianGrid strokeDasharray="3 3" stroke="#334155" />
              <XAxis dataKey="qps" stroke="#94a3b8" />
              <YAxis domain={[0.9, 1.0]} stroke="#94a3b8" />
              <Tooltip
                contentStyle={{ backgroundColor: "#0f172a", borderColor: "#334155", color: "#f8fafc" }}
              />
              <Line type="monotone" dataKey="recall" stroke="#38bdf8" strokeWidth={2} name="Recall @ 10" />
            </LineChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
};
