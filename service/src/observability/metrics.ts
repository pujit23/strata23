import client from "prom-client";

export const register = new client.Registry();

client.collectDefaultMetrics({ register, prefix: "strata_service_" });

export const httpRequestDuration = new client.Histogram({
  name: "strata_http_request_duration_seconds",
  help: "Duration of HTTP requests in seconds",
  labelNames: ["method", "route", "status_code"],
  buckets: [0.001, 0.005, 0.015, 0.05, 0.1, 0.2, 0.5, 1, 2],
  registers: [register],
});

export const engineGrpcDuration = new client.Histogram({
  name: "strata_engine_grpc_duration_seconds",
  help: "Duration of gRPC calls to the Java vector engine",
  labelNames: ["rpc_method", "status"],
  buckets: [0.0005, 0.001, 0.005, 0.01, 0.025, 0.05, 0.1, 0.5],
  registers: [register],
});
