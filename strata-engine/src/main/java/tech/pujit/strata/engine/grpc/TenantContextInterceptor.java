package tech.pujit.strata.engine.grpc;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Enforces tenant identity extraction from trusted gRPC metadata.
 * Architectural Contract: Tenant identity is NOT passed in protobuf message bodies;
 * it is verified by the service layer and conveyed over the gRPC boundary via metadata.
 */
public class TenantContextInterceptor implements ServerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(TenantContextInterceptor.class);

    public static final Metadata.Key<String> TENANT_HEADER_KEY =
        Metadata.Key.of("x-strata-tenant-id", Metadata.ASCII_STRING_MARSHALLER);

    public static final Context.Key<String> TENANT_CONTEXT_KEY =
        Context.key("strata.tenant.id");

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
        ServerCall<ReqT, RespT> call,
        Metadata headers,
        ServerCallHandler<ReqT, RespT> next
    ) {
        String methodName = call.getMethodDescriptor().getFullMethodName();

        // Allow health check without tenant header
        if (methodName.endsWith("HealthCheck")) {
            return next.startCall(call, headers);
        }

        String tenantId = headers.get(TENANT_HEADER_KEY);
        if (tenantId == null || tenantId.isBlank()) {
            log.warn("Rejected gRPC call {} without required x-strata-tenant-id metadata", methodName);
            call.close(Status.UNAUTHENTICATED.withDescription("Missing x-strata-tenant-id header"), new Metadata());
            return new ServerCall.Listener<>() {};
        }

        Context context = Context.current().withValue(TENANT_CONTEXT_KEY, tenantId);
        return Contexts.interceptCall(context, call, headers, next);
    }
}
