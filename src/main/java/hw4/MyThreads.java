package hw4;

public class MyThreads {
    private static volatile char c = 'A';

    public static void main(String[] args) {
        final Object monitor = new Object();

        class PrintLetter implements Runnable {
            char letter;
            char nextLetter;

            public PrintLetter(char letter, char nextLetter) {
                this.letter = letter;
                this.nextLetter = nextLetter;
            }

            @Override
            public void run() {
                synchronized (monitor) {
                    for (int i = 0; i < 5; i++) {
                        while (c != letter) {
                            try {
                                monitor.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        System.out.print(letter);
                        c = nextLetter;
                        monitor.notifyAll();
                    }
                }
            }
        }
        new Thread(new PrintLetter('A','B')).start();
        new Thread(new PrintLetter('B','C')).start();
        new Thread(new PrintLetter('C','A')).start();
    }
}
