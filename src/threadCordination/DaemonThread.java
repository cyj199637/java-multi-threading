package threadCordination;

import java.math.BigInteger;

/**
 데몬 스레드
 : 백그라운드로 실행되는 스레드
 -> 메인 스레드가 종료되어도 어플리케이션이 종료되지 않는다.

 <데몬 스레드를 사용하는 경우>
 1. 백그라운드 작업을 하는 경우
    ex) 아래 한글, word 같은 문서 편집기의 자동 저장 기능
 2. 작업자 스레드에서 실행되는 코드를 제어할 수 없는 경우
    ex) 외부 라이브러리를 호출하는 코드
 - 두 가지 경우 모두 데몬 스레드가 어플리케이션 종료를 방해하지 않아야 한다.
 */
public class DaemonThread {

    public static void main(String[] args) {
        Thread thread = new Thread(new DaemonThread.LongComputationTask(new BigInteger("20"), new BigInteger("1000")));

        thread.setDaemon(true);
        thread.start();
    }

    private record LongComputationTask(BigInteger base, BigInteger power) implements Runnable {

        @Override
            public void run() {
                System.out.println(this.base + "^" + this.power + " = " + pow());
            }

            private BigInteger pow() {
                BigInteger result = BigInteger.ONE;

                for (BigInteger i = BigInteger.ZERO; i.compareTo(this.power) != 0; i = i.add(BigInteger.ONE)) {
                    result = result.multiply(this.base);
                }

                return result;
            }
        }
}
