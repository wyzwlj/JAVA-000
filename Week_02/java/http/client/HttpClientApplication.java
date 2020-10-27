package http.client;


public class HttpClientApplication {

    public static void main(String[] args) {
        try {
            new HttpClient("http://localhost:8808/test").run();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
