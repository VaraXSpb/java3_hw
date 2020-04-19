package hw5;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class MainClass {
    private static final int CARS_COUNT = 4;
    private static CyclicBarrier cb = new CyclicBarrier(CARS_COUNT);
    private static volatile int ctr = 0;
    private static AtomicInteger ctrAtomic = new AtomicInteger(0);
    private static int countOfFinishedCars = 0;
    private static boolean isWin = false;

    public static void main(String[] args) {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        Thread[] threads = new Thread[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10));
        }
        for (int i = 0; i < cars.length; i++) {
            threads[i] = new Thread(cars[i]);
            threads[i].start();
        }
        while (countOfFinishedCars != CARS_COUNT) {
            for (int i = 0; i < threads.length; i++) {
                if (!isWin && !threads[i].isAlive()) {
                    isWin = true;
                    System.out.println(cars[i].getName() + " - WIN!");
                }
                if (!threads[i].isAlive()) {
                    countOfFinishedCars++;
                } else {
                    countOfFinishedCars = 0;
                }
                if (countOfFinishedCars == CARS_COUNT) {
                    System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
                    break;
                }
            }
        }
    }

    public static void waitForCar() {
        try {
            ctrAtomic.incrementAndGet();
            if (ctrAtomic.get() == CARS_COUNT) {
                System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
            }
            cb.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    public static int getCarsCount() {
        return CARS_COUNT;
    }
}
