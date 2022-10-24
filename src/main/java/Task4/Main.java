package Task4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {

//        ExecutorService executor = Executors.newCachedThreadPool();
//
//        for(int i = 0; i < 10; i++) {
//            executor.submit(new Racer(latch, sem, barrier));
//        }
//
//        executor.shutdown();

        start_race(5);

        Racer fastest_racer = new Racer();

        long fastest_time = Integer.MAX_VALUE;
        for (Map.Entry<Racer, Long> entry : Racer.times.entrySet()) {
            long racer_time = entry.getValue();
            if(racer_time < fastest_time) {
                fastest_time = racer_time;
                fastest_racer = entry.getKey();
            }
        }

        System.out.println("\n=========================\n");
        System.out.println("Fastest racer is Racer #" + fastest_racer.id);

    }

    public static void start_race(int racer_count) throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(racer_count);
        Semaphore sem = new Semaphore(3);
        CyclicBarrier barrier = new CyclicBarrier(racer_count);
        List<Thread> threadList = new ArrayList<>();

        for(int i = 0; i < racer_count; i++) {
            Thread thread = new Thread(new Racer(latch, sem, barrier));
            thread.start();
            threadList.add(thread);
        }

        for(Thread t : threadList) {
            t.join();
        }
    }

}

class Racer implements Runnable {

    CountDownLatch latch;
    Semaphore sem;
    CyclicBarrier barrier;
    private final static Object lock = new Object();
    static int threadID = 1;
    static int threadOrderID = 1;
    int id;
    long total_time;
    static Map<Racer, Long> times = new HashMap<Racer, Long>();

    Racer(){}

    Racer(CountDownLatch latch, Semaphore sem, CyclicBarrier barrier) {
        this.latch = latch;
        this.sem = sem;
        this.barrier = barrier;
        id = threadID++;
    }

    @Override
    public void run() {

        String name = "Racer #" + id;
        long wait_time_first = Math.round(Math.random()*3000);
        try {
            Thread.sleep(wait_time_first);
            System.out.println(name + " is ready for race, time spent on preparation = " + wait_time_first + " ms");
            latch.countDown();
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name + " has started the race");

        long wait_time_second = Math.round(Math.random()*2000);
        try {
            Thread.sleep(wait_time_second);
            System.out.println(name + " has approached the tunnel, time spent on approach to tunnel = " + wait_time_second + " ms");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        long sys_time = System.currentTimeMillis();
        try {
            sem.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long wait_time_third = System.currentTimeMillis() - sys_time;
        System.out.println(name + " has entered the tunnel, time spent waiting for entrance = " + wait_time_third + " ms");

        long wait_time_fourth = Math.round(Math.random()*4000);
        try {
            Thread.sleep(wait_time_fourth);
            System.out.println(name + " has exited the tunnel, time spent inside the tunnel = " + wait_time_fourth + " ms");
            sem.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long wait_time_fifth = Math.round(Math.random()*1500);
        try {
            Thread.sleep(wait_time_fifth);
            System.out.println(name + " has passed the final stretch in " + wait_time_fifth + " ms");
            total_time = wait_time_second + wait_time_third + wait_time_fourth + wait_time_fifth;
            times.put(this, total_time);
            System.out.println(name + " has finished the race! " +
                    "Total time(w/o preparation): " +
                    total_time + " ms");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            barrier.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
            synchronized (lock) {
                while(id != threadOrderID) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println(" ");
                System.out.println(name.toUpperCase() + " TOTAL TIME: " + total_time + " MS");
                threadOrderID++;
                lock.notifyAll();
            }

    }


}
