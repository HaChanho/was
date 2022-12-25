import org.example.Server;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTest {
    @Test void
    get() throws Exception {
        int port = 1024 + new Random().nextInt(1024);
        try (Server dut = new Server(port)) {
            HttpClient client = HttpClient.newBuilder()
                    .build();
            HttpResponse<String> response = client.send(
                    HttpRequest.newBuilder()
                            .GET()
                            .uri(URI.create("http://localhost:" + port + "/"))
                            .build(),
                    HttpResponse.BodyHandlers.ofString()
            );
            assertEquals(200, response.statusCode());
            assertEquals("Ok.\n", response.body());
        }
    }
}
