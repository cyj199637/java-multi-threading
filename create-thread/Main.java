public class Main {

    public static void main(String[] args) {
        // 1. 스레드 생성
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 작성된 코드는 새로운 스레드에서 실행됨
                System.out.println("we are now in thread: " + Thread.currentThread().getName());
                System.out.println("current thread priority is " + Thread.currentThread().getPriority());
            }
        });

        thread.setName("worker");

        thread.setPriority(Thread.MAX_PRIORITY);

        System.out.println("we are in thread: " + Thread.currentThread().getName() + " before starting a new thread");

        // 2. 스레드 실행
        // -> JVM이 새 스레드를 생성해 OS에 전달
        thread.start();
        System.out.println("we are in thread: " + Thread.currentThread().getName() + " after starting a new thread");

        try {
            // sleep(): OS에 지정한 시간동안에는 현재 스레드를 스케줄링하지 않도록 지시
            // -> 해당 스레드는 CPU를 사용하지 않게 됨
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}