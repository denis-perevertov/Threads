package Task3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    static List<Integer> numberList2 = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) throws InterruptedException {

        MyThread thread1 = new MyThread();
        MyThread thread2 = new MyThread();

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println(numberList2.size());
        System.out.println(MyThread.count);
        //System.out.println(MyThread.c);
    }

}

class MyThread extends Thread {

    static AtomicInteger count = new AtomicInteger(0);
    static int c;

    @Override
    public void run() {

        for(int i = 0; i < 100000; i++) {
            Main.numberList2.add(count.getAndIncrement());
            //Main.numberList2.add(c++);
        }
    }
}
