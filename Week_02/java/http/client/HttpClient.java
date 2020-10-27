package http.client;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class HttpClient {
    private String url;

    public HttpClient(String url) {
        this.url = url;
    }

    public void run() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            HttpEntity responseEntity = httpClient.execute(httpGet, httpResponse -> {
                int status = httpResponse.getStatusLine().getStatusCode();
                if (status < 200 || status >= 300) {
                    return null;
                }
                return httpResponse.getEntity();
            });

            System.out.println("Request response: " + responseEntity == null ? "request failed" : EntityUtils.toString(responseEntity));
        }
    }
}
