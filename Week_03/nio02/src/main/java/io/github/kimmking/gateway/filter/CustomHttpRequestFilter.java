package io.github.kimmking.gateway.filter;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import java.util.Arrays;
import java.util.List;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class CustomHttpRequestFilter implements HttpRequestFilter {
    private static final List<String> FILTER_URIS = Arrays.asList("/test1", "/test2");
    private static byte[] BAD_REQUEST_RESPONSE_BODY_BYTES = "bad request".getBytes();

    private HttpRequestFilter nextFilter;

    public CustomHttpRequestFilter(HttpRequestFilter nextFilter) {
        this.nextFilter = nextFilter;
    }

    @Override
    public boolean filter(FullHttpRequest inbound, ChannelHandlerContext ctx) {
        String uri = inbound.uri();
        if (FILTER_URIS.stream().anyMatch(filterUri -> filterUri.equals(uri))) {
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST, Unpooled.wrappedBuffer(BAD_REQUEST_RESPONSE_BODY_BYTES));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", BAD_REQUEST_RESPONSE_BODY_BYTES.length);
            ctx.write(response);
            return true;
        }
        return nextFilter == null ? false : nextFilter.filter(inbound, ctx);
    }
}
