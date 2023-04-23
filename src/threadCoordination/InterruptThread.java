package threadCoordination;

/**
 <스레드를 종료해야 하는 이유>
 1. 스레드는 리소스를 사용한다.
    - 메모리 or 일부 커널 리소스
    - (실행 중이라면) CPU time, CPU 캐시 메모리
    -> 생성한 스레드가 이미 작업을 완료했는데 어플리케이션은 작동 중이라면 사용하지 않는 스레드가 사용하는 리소스를 정리해야 한다.
 2. 스레드가 오작동한다면 이를 중단하기 위해
 3. 어플리케이션 전체를 중단하기 위해
    - 메인 스레드가 종료되어도 하나 이상의 스레드가 실행되고 있다면 애플리케이션은 종료되지 않는다.
    -> 따라서, 어플리케이션을 끝내기 전 모든 스레드를 중단해야 한다.
 */
public class InterruptThread {
    /**
     <Thread.interrupt() 를 사용하는 경우>
     1. 스레드가 InterruptedException을 발생시키는 메소드를 실행하는 경우
     */
    public static void main(String[] args) {
        Thread thread = new Thread(new BlockingTask());

        thread.start();

        thread.interrupt();
    }

    private static class BlockingTask implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(500000);
            } catch (InterruptedException e) {
                System.out.println("Exiting blocking method");
            }
        }
    }
}