public class Main {
    private volatile char expectedLetter = 'A';
    private Object monitor = new Object();
    public static void main(String[] args) {
        /*
        1. Создать три потока, каждый из которых выводит
         определенную букву (A, B и C) 5 раз (порядок – ABСABСABС).
          Используйте wait/notify/notifyAll.
         */
        Main main = new Main();

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                main.printA();
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                main.printB();
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                main.printC();
            }
        }).start();
    }

    public void printA(){
        synchronized (monitor){
            while (expectedLetter != 'A'){
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.print(expectedLetter);
            expectedLetter = 'B';
            monitor.notifyAll();
        }
    }

    public void printB(){
        synchronized (monitor){
            while (expectedLetter != 'B'){
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.print(expectedLetter);
            expectedLetter = 'C';
            monitor.notifyAll();
        }
    }

    public void printC(){
        synchronized (monitor){
            while (expectedLetter != 'C'){
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.print(expectedLetter);
            expectedLetter = 'A';
            monitor.notifyAll();
        }
    }
}
