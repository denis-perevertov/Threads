package Task1;

public class Main {

    public static char ch = 65;

    private static void createAndStartThreads(int count) {
        Object monitor = new Object();
        for (int i = 0; i < count; i++) {
            new Thread(new Printer((char) (65 + i), monitor)).start();
        }
    }

    public static void main(String[] args) {
        createAndStartThreads(3);
    }

    
}
class Printer implements Runnable {

    private char ch;
    private Object monitor;
    private static int runnerCount;
    private static int count = 1;

    public Printer(char ch, Object monitor) {
        this.ch = ch;
        this.monitor = monitor;
        runnerCount++;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (monitor) {
                while (ch != Main.ch) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print(Main.ch++);
                if (Main.ch == (65 + runnerCount)) {
                    if(count == 5) break;
                    else {
                        Main.ch = 65;
                        count++;
                    }
                }
                monitor.notifyAll();
            }
        }
    }

}






