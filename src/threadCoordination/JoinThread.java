package threadCoordination;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 <스레드를 제어하는 이유>
 1. 스레드는 독립적으로 실행되기 때문
 2. 스레드의 실행 순서를 통제할 수 없기 때문

 만약 하나의 스레드(A)가 다른 스레드(B)에 의존하는 경우, B가 작업을 완료했는지 어떻게 확인해야 할까?
 -> 가장 단순한 방법은 A가 주기적으로 B의 상태를 확인하는 것이다.

 그러나 이 방법은 비효율적이다.
 -> B가 빠르게 작업을 완료하려면 계속 CPU를 사용해야 하는데 A가 B의 상태를 확인하기 위해 중간중간에 CPU를 차지하기 때문이다.
 -> B의 작업이 끝나기 전의 확인 작업은 CPU 낭비일 뿐더러, 컨텍스트 스위칭의 비용이 크기 때문에 결과적으로는 B의 작업에 방해가 된다.

 따라서, A는 일단 잠들어 있으면 B가 작업을 완료했을 때 잠들어있던 A를 깨우는 것이 좋다.
 -> Thread.join()
 */
public class JoinThread {

    public static void main(String[] args) throws InterruptedException {
        // 3. 그러나 join() 을 사용하게 되면 한 가지 작업이 안 끝나면 다른 작업이 모두 끝나도 대기해야 하므로 파라미터로 적절한 대기 시간을 넘겨준다.
        List<Long> input = Arrays.asList(0L, 3435L, 35437L, 2341L, 583L, 15L, 4940L, 789L);

        List<FactorialThread> threads = new ArrayList<>();

        for (long n : input) {
            threads.add(new FactorialThread(n));
        }

        // 1. 각 FactorialThread 가 시작하는 코드와 main 스레드가 FactorialThread 의 결과를 확인하는 코드 사이에서 race condition 이 발생
        for (Thread thread : threads) {
            thread.setDaemon(true);
            thread.start();
        }

        // 2. 따라서 FactorialThread 가 작업을 완료할 때까지 main 스레드가 코드를 실행하지 않도록 join() 호출
        for (Thread thread : threads) {
            // join() : 스레드가 종료되어야만 응답을 반환
            // join(long millis, int nanos) : 스레드가 종료되지 않았어도 지정한 시간이 지나면 응답을 반환
            thread.join(2000);
        }

        for (int i = 0; i < input.size(); i++) {
            FactorialThread thread = threads.get(i);
            if (thread.isFinished) {
                System.out.println("Factorial of " + input.get(i) + " is " + thread.getResult());
            } else {
                System.out.println("The calculation for " + input.get(i) + " is still in progress");
            }
        }
    }

    private static class FactorialThread extends Thread {
        private final long input;
        private BigInteger result = BigInteger.ZERO;
        private boolean isFinished = false;

        public FactorialThread(final long input) {
            this.input = input;
        }

        @Override
        public void run() {
            this.result = factorial(this.input);
            this.isFinished =true;
        }

        private BigInteger factorial(final long n) {
            BigInteger tempResult = BigInteger.ONE;

            for (long i = n; i > 0; i--) {
                tempResult = tempResult.multiply(new BigInteger(String.valueOf(i)));
            }

            return tempResult;
        }

        public BigInteger getResult() {
            return result;
        }

        public boolean isFinished() {
            return isFinished;
        }
    }
}
