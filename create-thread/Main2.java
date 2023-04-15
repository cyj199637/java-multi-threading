import java.lang.Thread.UncaughtExceptionHandler;

public class Main2 {

    public static void main(String[] args) {

        Thread thread = new Thread(() -> {
            throw new RuntimeException("intentional exception");
        });

        thread.setName("misbehaving thread");

        // setUncaughtExceptionHandler() : 스레드 전체에 적용되는 uncaught exception 핸들러 지정
        thread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("a critical error happened in thread"
                    + t.getName() + " the error is " + e.getMessage());
            }
        });
        thread.start();
    }
}
