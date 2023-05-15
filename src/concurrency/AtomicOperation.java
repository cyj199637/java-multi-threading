package concurrency;

import java.util.Random;

public class AtomicOperation {

    public static void main(String[] args) {
        Metrics metrics = new Metrics();

        BusinessLogic businessLogic1 = new BusinessLogic(metrics);
        BusinessLogic businessLogic2 = new BusinessLogic(metrics);
        MetricsPrinter metricsPrinter = new MetricsPrinter(metrics);

        businessLogic1.start();
        businessLogic2.start();
        metricsPrinter.start();
    }

    public static class MetricsPrinter extends Thread {
        private Metrics metrics;

        public MetricsPrinter(Metrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {
            while(true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }

                System.out.println("Current average is " + metrics.getAverage());
            }
        }
    }

    public static class BusinessLogic extends Thread {
        private Metrics metrics;
        private final Random random = new Random();

        public BusinessLogic(Metrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {
            while(true) {
                long start = System.currentTimeMillis();

                try {
                    Thread.sleep(random.nextInt(10));
                } catch (InterruptedException e) {
                    System.out.println(e);
                }

                long end = System.currentTimeMillis();

                metrics.addSample(end - start);
            }
        }
    }

    public static class Metrics {
        private long count = 0;         // 공유 변수
        private volatile double average = 0.0;   // 공유 변수

        public synchronized void addSample(long sample) {
            double currentSum = average * count;
            count++;
            average = (currentSum + sample) / count;
        }

        // 일반적인 primitive type / reference type 변수에 대한 할당 연산은 원자적이지만 average 변수는 double 타입이므로 원자성이 보장되지 않는다.
        // -> average 변수에 volatile 선언
        public double getAverage() {
            return average;
        }
    }
}
