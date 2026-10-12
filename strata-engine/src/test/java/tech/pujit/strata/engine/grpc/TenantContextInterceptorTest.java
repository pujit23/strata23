package tech.pujit.strata.engine.grpc;

import io.grpc.Context;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class TenantContextInterceptorTest {

    private final TenantContextInterceptor interceptor = new TenantContextInterceptor();

    @Test
    @DisplayName("Should reject gRPC call when x-tenant-id metadata is missing")
    void shouldRejectCallWhenTenantHeaderMissing() {
        MethodDescriptor<String, String> method = createMethod("tech.pujit.strata.v1.PointService/UpsertPoints");
        TestServerCall<String, String> call = new TestServerCall<>(method);
        Metadata headers = new Metadata();

        AtomicBoolean nextInvoked = new AtomicBoolean(false);
        ServerCallHandler<String, String> handler = (c, h) -> {
            nextInvoked.set(true);
            return new ServerCall.Listener<>() {};
        };

        interceptor.interceptCall(call, headers, handler);

        assertTrue(call.closed, "ServerCall should have been closed");
        assertNotNull(call.closedStatus);
        assertEquals(Status.Code.UNAUTHENTICATED, call.closedStatus.getCode());
        assertTrue(call.closedStatus.getDescription().contains("Missing x-tenant-id"));
        assertFalse(nextInvoked.get(), "Handler should not have been invoked");
    }

    @Test
    @DisplayName("Should reject gRPC call when x-tenant-id metadata is blank")
    void shouldRejectCallWhenTenantHeaderIsBlank() {
        MethodDescriptor<String, String> method = createMethod("tech.pujit.strata.v1.PointService/SearchPoints");
        TestServerCall<String, String> call = new TestServerCall<>(method);
        Metadata headers = new Metadata();
        headers.put(TenantContextInterceptor.TENANT_HEADER_KEY, "   ");

        AtomicBoolean nextInvoked = new AtomicBoolean(false);
        ServerCallHandler<String, String> handler = (c, h) -> {
            nextInvoked.set(true);
            return new ServerCall.Listener<>() {};
        };

        interceptor.interceptCall(call, headers, handler);

        assertTrue(call.closed);
        assertNotNull(call.closedStatus);
        assertEquals(Status.Code.UNAUTHENTICATED, call.closedStatus.getCode());
        assertFalse(nextInvoked.get());
    }

    @Test
    @DisplayName("Should attach tenant ID to context and proceed when x-tenant-id is present")
    void shouldProceedWhenTenantHeaderPresent() {
        MethodDescriptor<String, String> method = createMethod("tech.pujit.strata.v1.PointService/SearchPoints");
        TestServerCall<String, String> call = new TestServerCall<>(method);
        Metadata headers = new Metadata();
        headers.put(TenantContextInterceptor.TENANT_HEADER_KEY, "tenant-org-42");

        AtomicReference<String> capturedTenant = new AtomicReference<>();
        ServerCallHandler<String, String> handler = (c, h) -> {
            capturedTenant.set(TenantContextInterceptor.TENANT_CONTEXT_KEY.get());
            return new ServerCall.Listener<>() {};
        };

        interceptor.interceptCall(call, headers, handler);

        assertFalse(call.closed, "ServerCall should not be closed");
        assertEquals("tenant-org-42", capturedTenant.get(), "TenantContext should contain tenant ID");
    }

    @Test
    @DisplayName("Should allow HealthCheck call without x-tenant-id header")
    void shouldAllowHealthCheckWithoutTenantHeader() {
        MethodDescriptor<String, String> method = createMethod("grpc.health.v1.Health/HealthCheck");
        TestServerCall<String, String> call = new TestServerCall<>(method);
        Metadata headers = new Metadata();

        AtomicBoolean nextInvoked = new AtomicBoolean(false);
        ServerCallHandler<String, String> handler = (c, h) -> {
            nextInvoked.set(true);
            return new ServerCall.Listener<>() {};
        };

        interceptor.interceptCall(call, headers, handler);

        assertFalse(call.closed, "ServerCall should not be closed");
        assertTrue(nextInvoked.get(), "Handler should have been invoked for HealthCheck");
    }

    private static MethodDescriptor<String, String> createMethod(String fullMethodName) {
        return MethodDescriptor.<String, String>newBuilder()
            .setType(MethodDescriptor.MethodType.UNARY)
            .setFullMethodName(fullMethodName)
            .setRequestMarshaller(new NoopMarshaller())
            .setResponseMarshaller(new NoopMarshaller())
            .build();
    }

    private static class NoopMarshaller implements MethodDescriptor.Marshaller<String> {
        @Override public InputStream stream(String value) { return null; }
        @Override public String parse(InputStream stream) { return ""; }
    }

    private static class TestServerCall<ReqT, RespT> extends ServerCall<ReqT, RespT> {
        private final MethodDescriptor<ReqT, RespT> descriptor;
        boolean closed = false;
        Status closedStatus;
        Metadata closedTrailers;

        TestServerCall(MethodDescriptor<ReqT, RespT> descriptor) {
            this.descriptor = descriptor;
        }

        @Override
        public void close(Status status, Metadata trailers) {
            this.closed = true;
            this.closedStatus = status;
            this.closedTrailers = trailers;
        }

        @Override public MethodDescriptor<ReqT, RespT> getMethodDescriptor() { return descriptor; }
        @Override public void request(int numMessages) {}
        @Override public void sendHeaders(Metadata headers) {}
        @Override public void sendMessage(RespT message) {}
        @Override public boolean isReady() { return true; }
        @Override public boolean isCancelled() { return false; }
    }
}
