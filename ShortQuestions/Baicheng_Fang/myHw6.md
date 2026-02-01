## Q2 ##
W/ lock
```java
/*
        private constructor
        static instance class level
        synchronize class level
 */
public final class Singleton {
    private static volatile Singleton instance;
    
    private Singleton() {}
    
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```
W/O lock
```java
/*
        private holder
        private static final instance for thread-safe
        private constructor
 */
public final class Singleton {
    private Singleton() {
        if (Holder.instance != null) {
            throw new IllegalStateException("Singleton already exist");
        }
    }
    
    private static class Holder {
        private static final Singleton instance = new Singleton();
    }
    
    public static Singleton getInstance() {
        return Holder.instance;
    }
}
```

## Q3 ##
1.Extend Thread

```java
import javax.sound.midi.Soundbank;

class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
    }

    public static void main(Sring[] args) {
        MyThread t = new MyThread();
        t.start();
    }
}
```

2.Implement Runnable
```java
class MyRunnable implements Runnable {
    @Override
    public void run(){
        System.out.println(Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        Thread t = new Thread(new MyRunnable());
        t.start();
    }
}
```
3.Implement Callable
```java
class MyCallable implements Callable<String> {
    @Override
    public String call() throws Exception{
        Thread.sleep(1000);
        return Thread.currentThread().getName();
    }
}
```

4.Threa Pool (fixed-size pool)
```java
import java.util.concurrent.*;
class Test {
    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 6; i++) {
            es.submit(new Task("" + i));
        }
        es.shutdown();
    }
}

class Task implements Runnable {
    private final String name;
    
    public Task(String name) {
        this.name = name;
    }
    
    @Override
    public void run() {
        System.out.println("start task " + name);
        try {
           Thread.sleep(1000); 
        } catch (InterruptedException e) {
            
        }
        System.out.println("end task " + name);
    }
}
```
## Q4 ##
Callable can return and throw exception while Runnalbe can not.

## Q5 ##
start() creates a new thread while run() does not.
Under the hood Thread implements Runnable and JVM needs a method to execute when thread starts.
run() defines that task logic.
start() handles thread lifecycle.
Without start(), t.run() is NOT multithreading.

## Q6 ##
Implementing Ruunable is better
1.Decouple task logic from execution, allowing the same task to run in a thread, a thread pool or synchronously in tests.
2.Java allows only one superclass. You can still extend another class after implementing Runnable.
3.ExecutorService accepts Runnable/Callable directly.

## Q7 ##
1.NEW
```java
Thread t = new Thread(() -> System.out.println(Thread.currentThread().getName()));
```
2.RUNNABLE
```java
t.start();
```
JMV merges ready + running into RUNNABLE

3.BLOCKED
```java
synchronized (lock) {
    // ...
        }
```
4.WAITING
```java
obj.wait();
thread.join();
```

5.TIMED WAITING
```java
Thread.sleep(1000);
thread.join(1000);
```

6.TERMINATED

## Q8 ##
```java
class DeadLock {
    private static final Object Lock_A = new Object();
    private static final Object Lock_B = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            synchronized (Lock_A) {
                sleep(1000);
                synchronized (Lock_B) {
                    System.out.println("Acquired A then B");
                }
            }
        }, "t1");
        
        Thread t2 = new Thread(() -> {
            synchronized (Lock_B) {
                sleep(1000);
                synchronized (Lock_A) {
                    System.out.println("Acquired B then A");
                }
            }
            
        }, "t2");
        
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
        System.out.println("I might not be printing at all");
    }
    
    private static void sleep(long ms) {
        try {Thread.sleep(ms);} catch (InterruptedException e) {}
    }
}
```
Resolve by acquiring locks in the same order
```java
private static void AcquireLocks() {
    synchronized (Lock_A) {
        sleep(1000);
        synchronized (Lock_B) {
            System.out.println("Do something");
        }
    }
}
```

## Q9 ##
By shared memory and coordinating access using synchronization, volatile variables, and high-level concurrency utilities like blocking queues and futures.

## Q10 ##
1.Object lock is instance-level lock
```java
synchronized (obj) {...}
```
or
```java
public synchronized void method(){...}
```
each object has its own lock, no blocking across objects

2.class lock
```java
synchronized (ClassName.class) {...}
```
or
```java
public static synchronized void method() {...}
```
synchronizes on the class object and is shared across all instances

## Q11 ##
Thread.join() makes the calling thread block until the target thread terminates
```java
class Main {
    public static void main(String[] args) {
        //...
        t.join();  // here main invokes join, so main is the calling thread and blocked
    }
}
```

## Q12 ##
Indicates OS scheduler that current thread can be paused and let other thread run.
However it does not block, does not release locks and has no guarantees.

## Q13 ##
Thread pool: a collection of pre-created worker threads that execute submitted tasks instead of creating a new thread for each task.
Offers better performance and resource management. Controls the number of concurrent tasks. Reduces the overhead of thread creation and destruction.

1.Fixed Thread Pool

```java
import java.util.concurrent.ExecutorService;

ExecutorService es = Executors.newFixedThreadPool(4);
```
Tasks wait in an unbounded queue LinkedBlockingQueue
if queue grows infinitely would OOM

2.Cached Thread Pool

```java
import java.util.concurrent.ExecutorService;

ExecutorService es = Executors.newCachedThreadPool();
```
creates threads as needed
reuses idle threads
threads die after 60s idle

3.Single Thread Executor

```java
import java.util.concurrent.ExecutorService;

ExecutorService es = Executors.newSingleThreadExecutor();
```
Tasks executed sequentially
Guarantees order

Task Queue stores tasks that are waiting to be executed by worker threads.
For buffering and backpressure

## Q14 ##
```java
import java.util.concurrent.*;
```
Executor: run task  
ExecutorService: provide lifecycle and task-submission APIs  
ThreadPoolExecutor: concrete implementation

## Q15 ##
1.Execute (no return)

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Test {
    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(4);
        es.execute(() -> {
            System.out.println(Thread.currentThread().getName());
        });
    }
}
```
2.Submit
```java
import java.util.concurrent.*;
class Test {
    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 6; i++) {
            es.submit(new Task("" + i));
        }
        es.shutdown();
    }
}
```
## Q16 ##
1.Control the number of concurrent tasks;  
2.Reduce the overhead of thread creation and destruction;  
3.Allow task queuing for pending execution;  
4.Automatically manage thread lifecycles.

## Q17 ##
Both return immediately without blocking.  
shutdown()  
1.stop accepting tasks;  
2.finish queued and running tasks;  
3.threads terminated after tasks complete;

shutdownNow()  
1.stop accepting new tasks;  
2.attempt to interrupt running tasks;  
3.clear task queue  
4.return tasks that havent started

## Q18 ##
Atomic classes provide lock-free, thread-safe operations on single variables using CAS.

CAS under the hood
```java
if (current == expected) 
    current = new
else
    retry
```
Types:  
1.Atomic primitives;  
2.Atomic arrays;     
3.Atomic references;    
4.Field updaters

```java
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Demo {
    private static AtomicInteger cnt = new AtomicInteger(0);

    public static void main(String[] args) {
        System.out.println(cnt.getAndIncrement());
        System.out.println(cnt.get());

        AtomicInteger value = new AtomicInteger(123);
        boolean success = value.compareAndSet(123, 321);
        System.out.println(success);
        System.out.println(value.get());
        
        AtomicIntegerArray arr = new AtomicIntegerArray(3);
        arr.set(0, 100);
        arr.incrementAndGet(0);
        System.out.println(arr.get(0));
    }
}
```

## Q19 ##
CopyOnWriteArrayList  
ConcurrentHashMap  
CopyOnWriteArraySet  
ArrayBlockingQueue  
LinkedBlockingQueue


## Q20 ##
1.Synchronize
```java
/*
Pros
        Java built-in lock
        Simple and safe
        automatic unlock (even on exception)
        lightweight
        reentrant by default
        
Cons
        No timeout
        No fairness
        No try-lock
 */
synchronized (lock) {
    // critical section
        }
```

2.ReentrantLock
```java
/*
        Pros
        trylock() non-blocking
        timeout support
        optional fairness
        interruptible lock acquisition
        multiple condition: condition.await(), condition.signal(), condition.signalAll()
        
        cons
        must unlock manually
 */
Lock lock = new ReentrantLock();
lock.lock();
try {
    // critical section
        } finally {
    lock.unlock();
        }
```

3.ReadWriteLock  
Exclusive write  
Concurrent reads when no writes, readers dont block readers   
Pessimistic Locking, no writing when reading

```java
import java.util.Arrays;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Counter {
    private final ReadWriteLock rwlock = new ReentrantReadWriteLock();
    private final Lock rlock = rwlock.readLock();
    private final Lock wlock = rwlock.writeLock();
    private int[] cnts = new int[10];

    public void inc(int index) {
        wlock.lock();
        try {
            cnts[index]++;
        } finally {
            wlock.unlock();
        }
    }

    public int[] get() {
        rlock.lock();
        try {
            return Arrays.copyOf(cnts, cnts.length);
        } finally {
            rlock.unlock();
        }
    }
}
```

4.StampedLock  
Optimistic Locking  
Write while reading

```java
import java.util.concurrent.locks.StampedLock;

class Demo {
    private final StampedLock stampedlock = new StampedLock();
    
    private int cnt;
    
    public void inc(int val) {
        long stamp = stampedlock.writeLock();
        try {
            cnt++;
        } finally {
            stampedlock.unlockWrite(stamp);
        }
    }
    
    public int get() {
        long stamp = stampedlock.tryOptimisticRead();
        int current = cnt;
        if(!stampedlock.validate(stamp)) {
            stamp = stampedlock.readLock();
            try {
                current = cnt;
            } finally {
                stampedlock.unlockRead(stamp);
            }
        }
    }
}
```

## Q21 ##
Future   
1.Represents the result of an asynchronous computation   
2.Represents a value that will be available later    
3.Blocking API  
4.Cannot chain tasks  
5.Cannot be manually completed

```java
import java.util.concurrent.ExecutorService;

ExecutorService es = Executors.newFixedThreadPool(2);

Future<Integer> future = es.submit(() -> {
    Thread.sleep(1000);
    return 100;
});

Integer ret = future.get();
```

When submitting a task to threadPool we get a Future instance. Later when we call .get(), if the async op is done, we got the result. If its not, .get() is blocked until the job is finished.

We can also use .get(long timeout, Timeunit unit) to prevent infinite blocking

2.CompletableFuture (Implements the Future interface)   
Support chaining operations  
Non-blocking

```java
import java.util.concurrent.CompletableFuture;

CompletableFuture<Integer> cf = 
    CompletableFuture.supplyAsync(() -> 100);

cf.thenAccept(System.out.println);
```

### Create ###
runAsync(Runnable) -> CompletableFuture<Void>  
Use when there is no return value
```java
import java.util.concurrent.CompletableFuture;

CompletableFuture<Void> cf =
    CompletableFuture.runAsync(() -> System.out.println("do sth"));
```

supplyAsync(Supplier<T>) -> CompletableFuture<T>  
Use when there is a result

```java
import java.util.concurrent.CompletableFuture;

CompletableFuture<Integer> cf = 
    CompletableFuture.supplyAsync(() -> 100);
```

### Transform (map) ###
thenApply(fn): T -> U

```java
import java.util.concurrent.CompletableFuture;

CompletableFuture<Integer> cf = 
    CompletableFuture.supplyAsync(() -> 100)
            .thenApply(x -> x * 2)  // may run on the same thread that completed the previous stage
            .thenApplyAsync(x -> x * 2);  // always scheduled async
```

### Consume ###
do something with the result but dont produce a new value
thenAccept(consumer): T -> void  
thenRun(): dont even take the result, just do sth
```java
cf.thenAccept(v -> System.out.println("value: " + v));
cf.thenRun(() -> System.out.println("End"));
```
### Chain (flatMap) ###

```java
import java.util.concurrent.CompletableFuture;

CompletableFuture<Order> order = 
    getUserAsync().thenCompose(u -> getOrderAsync(u));
```

### Combine ###
1.thenCombine(another async task, fn): (T, U) -> V

```java
import java.util.concurrent.CompletableFuture;

CompletableFuture<String> a = CompletableFuture.supplyAsync(() -> "A");
CompletableFuture<String> b = CompletableFuture.supplyAsync(() -> "B");

CompletableFuture<String> both = 
    a.thenCombine(b, (x, y) -> x + y);
```

2.thenAcceptBoth(another async task, consumer): (T, U) -> void
```java
a.thenAcceptboth(b, (x, y) -> System.out.println(x + y));
```

3.allOf()
```java
CompletableFuture.allOf(sumFuture, productFuture).join();
```
### Exception ###
1.exceptionally

```java
import java.util.concurrent.CompletableFuture;

CompletableFuture<Integer> safe = 
    CompletableFuture.supplyAsync(() -> 1/0)
            .exceptionally(ex -> 0); // fallback to default value

```

2.handle

```java
import java.util.concurrent.CompletableFuture;

CompletableFuture<String> msg = 
    CompletableFuture.supplyAsync(() -> "ok")
            .handle((val, ex) -> ex == null ? val : "fallback");
```
