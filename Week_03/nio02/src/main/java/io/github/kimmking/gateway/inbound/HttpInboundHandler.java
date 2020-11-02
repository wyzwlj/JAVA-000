package io.github.kimmking.gateway.inbound;

import io.github.kimmking.gateway.filter.CustomHttpRequestFilter;
import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.github.kimmking.gateway.outbound.OutboundHandler;
import io.github.kimmking.gateway.outbound.okhttp.OkhttpOutboundHandler;
import io.github.kimmking.gateway.router.CustomerHttpEndpointRouter;
import io.github.kimmking.gateway.router.HttpEndpointRouter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private final HttpRequestFilter filter;
    private final OutboundHandler handler;

    public HttpInboundHandler(String proxyServer) {
        this.filter = new CustomHttpRequestFilter(null);
        HttpEndpointRouter router = new CustomerHttpEndpointRouter(proxyServer);
        //this.handler = new HttpOutboundHandler(this.proxyServer);
        this.handler = new OkhttpOutboundHandler(router);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
            if (!filter.filter(fullRequest, ctx)) {
                handler.handle(fullRequest, ctx);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
