import { render, screen } from "@testing-library/react";
import { describe, it, expect } from "vitest";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { App } from "../src/app/App";

describe("Console App Root Component", () => {
  it("renders navbar with STRATA branding and default collections tab", () => {
    const queryClient = new QueryClient();

    render(
      <QueryClientProvider client={queryClient}>
        <App />
      </QueryClientProvider>
    );

    expect(screen.getByText("STRATA")).toBeInTheDocument();
    expect(screen.getByText("Vector Engine v3.0")).toBeInTheDocument();
    expect(screen.getByText("Vector Collections")).toBeInTheDocument();
  });
});
