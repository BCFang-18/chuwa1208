import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Q24 {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        try {
            pool.execute(() -> print(1, 10));
            pool.execute(() -> print(11, 20));
            pool.execute(() -> print(21, 30));
        } finally {
            pool.shutdown();
        }
    }

    private static void print(int start, int end) {
        for(int i = start; i <= end; i++) {
            System.out.println(Thread.currentThread().getName() + ": " + i);

        }
    }
}
