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
    @Test void
    get() throws Exception {
        int port = 1024 + new Random().nextInt(1024);
        try (Server dut = new Server(port)) {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(1))
                    .build();

            HttpResponse<String> response = client.send(
                    HttpRequest.newBuilder()
                            .GET()
                            .uri(URI.create("http://localhost:" + port + "/"))
                            .timeout(Duration.ofSeconds(1))
                            .build(),
                    HttpResponse.BodyHandlers.ofString()
            );
            assertEquals(200, response.statusCode());
            assertEquals("Ok.\n", response.body());
        }
    }

    @Test void
    multipleRequests() throws Exception {
        int port = 1024 + new Random().nextInt(1024);
        try (Server dut = new Server(port)) {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(1))
                    .build();

            HttpResponse<String> response = client.send(
                    HttpRequest.newBuilder()
                            .GET()
                            .uri(URI.create("http://localhost:" + port + "/"))
                            .timeout(Duration.ofSeconds(1))
                            .build(),
                    HttpResponse.BodyHandlers.ofString()
            );
            assertEquals(200, response.statusCode());
            assertEquals("Ok.\n", response.body());

            HttpResponse<String> response2 = client.send(
                    HttpRequest.newBuilder()
                            .GET()
                            .uri(URI.create("http://localhost:" + port + "/"))
                            .timeout(Duration.ofSeconds(1))
                            .build(),
                    HttpResponse.BodyHandlers.ofString()
            );
            assertEquals(200, response2.statusCode());
            assertEquals("Ok.\n", response2.body());
        }
    }
}
