public class Main3 {
    public static void main(String[] args) {
        Thread thread = new MyThread();

        thread.start();
    }

    // Thread 클래스를 상속하여 새로운 스레드 클래스를 생성
    // -> Thread 클래스의 정적 메서드가 아닌 this 키워드로 현재 스레드의 데이터와 메서드에 직접적으로 접근 가능
    private static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("hello from " + this.getName());
        }
    }
}
