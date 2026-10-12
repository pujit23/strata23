import { create } from "zustand";

export type ActiveTab = "collections" | "query" | "explorer" | "benchmarks";

interface AppState {
  activeTab: ActiveTab;
  selectedCollection: string | null;
  tenantId: string;
  setActiveTab: (tab: ActiveTab) => void;
  setSelectedCollection: (collection: string | null) => void;
  setTenantId: (tenant: string) => void;
}

export const useAppStore = create<AppState>((set) => ({
  activeTab: "collections",
  selectedCollection: null,
  tenantId: "default-tenant",
  setActiveTab: (tab) => set({ activeTab: tab }),
  setSelectedCollection: (collection) => set({ selectedCollection: collection }),
  setTenantId: (tenant) => set({ tenantId: tenant }),
}));
