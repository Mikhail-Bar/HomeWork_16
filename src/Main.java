import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    static int Readers = 3 , Writers = 2, min  = 1000, max = 3000;
    String text = "";
    Lock bookLock = new ReentrantLock();

    public void start() {

        for (int i = 0; i < Writers; i++) {
            new Thread(new Writer(i)).start();
        }

        for (int i = 0; i < Readers; i++) {
            new Thread(new Reader(i)).start();
        }
    }
    private class Writer implements Runnable {
        private final int id;

        public Writer(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {

                String museText = getMuse();

                bookLock.lock();
                try {
                    text += museText;
                    System.out.println("Writer " + id + " wrote: " + museText);
                } finally {
                    bookLock.unlock();
                }

                try {
                    Thread.sleep(getRandomWaitTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        private String getMuse() {
            return "Lorem Ipsum is simply dummy text of the printing and typesetting industry.";
        }
        private int getRandomWaitTime() {
            return min + (int) (Math.random() * (max - min));
        }
    }
    private class Reader implements Runnable {
        private final int id;

        public Reader(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                bookLock.lock();
                try {
                    System.out.println("Reader " + id + " is reading the book: " + text);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    bookLock.unlock();
                }
            }
        }
    }

    public static void main(String[] args) {
        new Main().start();
    }
}