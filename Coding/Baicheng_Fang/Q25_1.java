import java.util.concurrent.CompletableFuture;

public class Q25_1 {
    public static void main(String[] args) {
        int a = 2;
        int b = 3;

        CompletableFuture<Integer> sumFuture =
                CompletableFuture.supplyAsync(() -> a + b)
                .thenApply(sum -> {
                    System.out.println("sum: " + sum);
                    return sum;
                });


        CompletableFuture<Void> productFuture =
                CompletableFuture.supplyAsync(() -> a * b)
                        .thenAccept(product -> System.out.println("product: " + product));

        CompletableFuture.allOf(sumFuture, productFuture).join();
    }
}
