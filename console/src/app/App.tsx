import React from "react";
import { Navbar } from "../components/Navbar";
import { useAppStore } from "../stores/useAppStore";
import { CollectionsView } from "../features/collections/CollectionsView";
import { QueryConsoleView } from "../features/query-console/QueryConsoleView";
import { IndexExplorerView } from "../features/index-explorer/IndexExplorerView";
import { BenchmarkLabView } from "../features/benchmark-lab/BenchmarkLabView";

export const App: React.FC = () => {
  const { activeTab } = useAppStore();

  return (
    <div className="min-h-screen flex flex-col bg-slate-950 text-slate-100">
      <Navbar />
      <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {activeTab === "collections" && <CollectionsView />}
        {activeTab === "query" && <QueryConsoleView />}
        {activeTab === "explorer" && <IndexExplorerView />}
        {activeTab === "benchmarks" && <BenchmarkLabView />}
      </main>
      <footer className="border-t border-slate-900 py-4 text-center text-xs text-slate-500">
        STRATA v3.0 — Single-Node Filter-Aware Vector Database
      </footer>
    </div>
  );
};
