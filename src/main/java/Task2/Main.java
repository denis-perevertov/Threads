package Task2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class Main {

    public static List<String> namesList = Arrays.asList("name1", "name2", "name3", "name4", "name5");

    public static void main(String[] args) {

        MyThread thread = new MyThread();
        MyThread daemon_thread = new MyThread();

        daemon_thread.setDaemon(true);

        thread.start();
        daemon_thread.start();


    }

}

class MyThread extends Thread implements Runnable, Callable<String> {

    Scanner scanner = new Scanner(System.in);
    static boolean b = false;
    int index;

    @Override
    public void run() {

        while(this.isDaemon()) {
            while(b) {
                System.out.print(".");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        System.out.print("Input index: ");
        index = scanner.nextInt();
        b = true;
        try {
            System.out.println("\n" + call());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String call() throws Exception {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Main.namesList.get(index);
    }
}

