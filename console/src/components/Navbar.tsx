import React from "react";
import { useAppStore, ActiveTab } from "../stores/useAppStore";
import { Database, Terminal, Network, BarChart3, Layers } from "lucide-react";

export const Navbar: React.FC = () => {
  const { activeTab, setActiveTab } = useAppStore();

  const navItems: { id: ActiveTab; label: string; icon: React.ReactNode }[] = [
    { id: "collections", label: "Collections", icon: <Database className="w-4 h-4" /> },
    { id: "query", label: "Query Console", icon: <Terminal className="w-4 h-4" /> },
    { id: "explorer", label: "Index Explorer", icon: <Network className="w-4 h-4" /> },
    { id: "benchmarks", label: "Benchmark Lab", icon: <BarChart3 className="w-4 h-4" /> },
  ];

  return (
    <header className="border-b border-slate-800 bg-slate-900/80 backdrop-blur sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
        <div className="flex items-center gap-3">
          <div className="w-9 h-9 rounded-xl bg-gradient-to-tr from-indigo-600 to-cyan-500 flex items-center justify-center shadow-lg shadow-indigo-500/20">
            <Layers className="w-5 h-5 text-white" />
          </div>
          <div>
            <h1 className="text-base font-bold text-white tracking-wide">STRATA</h1>
            <p className="text-[10px] uppercase font-semibold tracking-wider text-indigo-400">Vector Engine v3.0</p>
          </div>
        </div>

        <nav className="flex items-center space-x-1">
          {navItems.map((item) => {
            const isActive = activeTab === item.id;
            return (
              <button
                key={item.id}
                onClick={() => setActiveTab(item.id)}
                className={`flex items-center gap-2 px-3 py-2 rounded-lg text-xs font-medium transition ${
                  isActive
                    ? "bg-indigo-600/15 text-indigo-400 border border-indigo-500/30"
                    : "text-slate-400 hover:text-slate-200 hover:bg-slate-800/60"
                }`}
              >
                {item.icon}
                {item.label}
              </button>
            );
          })}
        </nav>
      </div>
    </header>
  );
};
