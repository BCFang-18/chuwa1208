import java.security.spec.RSAOtherPrimeInfo;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Q23_synchronize {
    private static final Object lock = new Object();
    private static int cnt = 1;

    public static void main(String[] args) throws InterruptedException {
        Thread odd = new Thread(() -> print(true), "odd");
        Thread even = new Thread(() -> print(false), "even");

        odd.start();
        even.start();
        odd.join();
        even.join();
    }

    private static void print(boolean isOdd) {
        while(true) {
            synchronized (lock) {
                try {
                while(cnt <= 10 && (cnt % 2 == 1) != isOdd) {

                        lock.wait();
                    }} catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }


                if (cnt > 10) {
                    lock.notifyAll();
                    return;
                }

                System.out.println(cnt++);
                lock.notifyAll();
            }
        }
    }
}

public class Q23 {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();
    private static int cnt = 1;

    public static void main(String[] args) throws InterruptedException {
        Thread odd = new Thread(() -> print(true), "odd");
        Thread even = new Thread(() -> print(false), "even");

        odd.start();
        even.start();

        odd.join();
        even.join();

    }
    private static void print(boolean isOdd) {
        while(true) {
            lock.lock();
            try {
                while(cnt <= 10 && (cnt % 2 == 1) != isOdd) {
                    condition.await();
                }

                if (cnt > 10) {
                    condition.signalAll();
                    return;
                }

                System.out.println(cnt++);
                condition.signalAll();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } finally {
                lock.unlock();
            }
        }
    }
}