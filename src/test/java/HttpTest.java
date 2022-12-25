import org.example.Server;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTest {

    @FunctionalInterface
    public interface Fixture {
        void run(HttpClient client, URI baseUrl) throws Exception;
    }

    void fixture(Fixture f) throws Exception {
        int port = 1024 + new Random().nextInt(1024);
        try (Server dut = new Server(port)) {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(1))
                    .build();
            f.run(client, URI.create("http://localhost:" + port + "/"));
        }
    }

    HttpResponse<String> request(HttpClient client, URI baseUrl) throws Exception {
        return client.send(
                HttpRequest.newBuilder()
                        .GET()
                        .uri(baseUrl)
                        .timeout(Duration.ofSeconds(1))
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );
    }

    @Test void
    get() throws Exception {
        fixture((client, baseUrl) -> {
            HttpResponse<String> response = request(client, baseUrl);
            assertEquals(200, response.statusCode());
            assertEquals("Ok.\n", response.body());
        });
    }

    @Test void
    multipleRequests() throws Exception {
        fixture((client, baseUrl) -> {
            HttpResponse<String> response = request(client, baseUrl);
            assertEquals(200, response.statusCode());
            assertEquals("Ok.\n", response.body());

            HttpResponse<String> response2 = request(client, baseUrl);
            assertEquals(200, response2.statusCode());
            assertEquals("Ok.\n", response2.body());
        });
    }
}
