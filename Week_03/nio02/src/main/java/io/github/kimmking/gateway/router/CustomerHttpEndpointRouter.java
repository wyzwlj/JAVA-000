package io.github.kimmking.gateway.router;

public class CustomerHttpEndpointRouter implements HttpEndpointRouter {

    private String proxyServer;

    public CustomerHttpEndpointRouter(String proxyServer) {
        this.proxyServer = proxyServer;
    }

    public String route(String requestUri) {
        if (requestUri.startsWith("/test")) {
            return proxyServer;
        }
        return "http://localhost:8081"+requestUri;
    }

}
