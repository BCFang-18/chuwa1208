import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class Q25_2 {
    private static final String PRODUCTS_URL  = "https://jsonplaceholder.typicode.com/posts";
    private static final String REVIEWS_URL   = "https://jsonplaceholder.typicode.com/comments";
    private static final String INVENTORY_URL = "https://jsonplaceholder.typicode.com/todos";

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    record StoreData(String productsJson, String reviewsJson, String inventoryJson) {}

    public static void main(String[] args) {
        CompletableFuture<String> productsFuture  = fetchJson(PRODUCTS_URL);
        CompletableFuture<String> reviewsFuture   = fetchJson(REVIEWS_URL);
        CompletableFuture<String> inventoryFuture = fetchJson(INVENTORY_URL);

        CompletableFuture<StoreData> merged =
                CompletableFuture.allOf(productsFuture, reviewsFuture, inventoryFuture)
                        .thenApply(v -> new StoreData(
                                productsFuture.join(),
                                reviewsFuture.join(),
                                inventoryFuture.join()
                        ));

        merged.thenAccept(data -> {
            int productCount  = countObjects(data.productsJson());
            int reviewCount   = countObjects(data.reviewsJson());
            int inventoryCount= countObjects(data.inventoryJson());

            System.out.println("=== Merged Store Data ===");
            System.out.println("Products count (approx):  " + productCount);
            System.out.println("Reviews count (approx):   " + reviewCount);
            System.out.println("Inventory count (approx): " + inventoryCount);

            System.out.println("\nProducts JSON snippet:  " + snippet(data.productsJson()));
            System.out.println("Reviews JSON snippet:   " + snippet(data.reviewsJson()));
            System.out.println("Inventory JSON snippet: " + snippet(data.inventoryJson()));
        }).join();
    }

    private static CompletableFuture<String> fetchJson(String url) {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .GET()
                .build();

        return CLIENT.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                .thenApply(resp -> {
                    int status = resp.statusCode();
                    if (status < 200 || status >= 300) {
                        throw new RuntimeException("HTTP " + status + " from " + url);
                    }
                    return resp.body();
                })
                .exceptionally(e -> {
                    throw new RuntimeException("Failed fetching " + url + ": " + e.getMessage(), e);
                });
    }

    private static int countObjects(String json) {
        int count = 0;
        for (int i = 0; i < json.length(); i++) {
            if (json.charAt(i) == '{') count++;
        }
        return count;
    }

    private static String snippet(String s) {
        int n = Math.min(120, s.length());
        return s.substring(0, n).replaceAll("\\s+", " ") + (s.length() > n ? "..." : "");
    }
}
