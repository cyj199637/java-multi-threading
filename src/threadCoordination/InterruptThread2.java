package threadCoordination;

import java.math.BigInteger;

public class InterruptThread2 {
    /**
     <Thread.interrupt() 를 사용하는 경우>
     2. 스레드가 interrupt 신호를 명시적으로 처리하는 경우
     */
    public static void main(String[] args) {
        Thread thread = new Thread(new LongComputationTask(new BigInteger("20000000"), new BigInteger("10000000000000")));

        thread.start();

        // interrupt() 가 동작하지 않음
        // -> 인터럽트는 보냈지만 해당 스레드에 이를 처리할 메서드나 로직이 없기 때문
        thread.interrupt();
    }

    private static class LongComputationTask implements Runnable {
        private final BigInteger base;
        private final BigInteger power;

        public LongComputationTask(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            System.out.println(this.base + "^" + this.power + " = " + pow());
        }

        private BigInteger pow() {
            BigInteger result = BigInteger.ONE;

            // 따라서, 시간이 오래 걸리는 로직을 찾아 현재 스레드의 인터럽트 상태에 따른 분기 처리를 해줘야 한다.
            for (BigInteger i = BigInteger.ZERO; i.compareTo(this.power) != 0; i = i.add(BigInteger.ONE)) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("prematurely interrupted computation");
                    return BigInteger.ZERO;
                }
                result = result.multiply(this.base);
            }

            return result;
        }
    }
}