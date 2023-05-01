package dataSharingBetweenThreads;

/**
 결과가 매번 다르게 나오는 이유
 1. InventoryCounter 객체를 여러 스레드가 공유하고 있기 때문
 2. increase() 와 decrease() 에서 사용되는 items++, items-- 연산이 원자적으로 일어나지 않기 때문
 */
public class RaceCondition {

    public static void main(String[] args) throws InterruptedException {
        InventoryCounter inventoryCounter = new InventoryCounter();
        IncrementingThread incrementingThread = new IncrementingThread(inventoryCounter);
        DecrementingThread decrementingThread = new DecrementingThread(inventoryCounter);

        incrementingThread.start();
        decrementingThread.start();

        incrementingThread.join();
        decrementingThread.join();

        System.out.println("We currently have " + inventoryCounter.getItems() + " items");
    }

    public static class IncrementingThread extends Thread {
        private InventoryCounter inventoryCounter;

        public IncrementingThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                inventoryCounter.increase();
            }
        }
    }

    public static class DecrementingThread extends Thread {
        private InventoryCounter inventoryCounter;

        public DecrementingThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                inventoryCounter.decrease();
            }
        }
    }

    private static class InventoryCounter {
        private int items = 0;

        public void increase() {
            items++;
        }

        public void decrease() {
            items--;
        }

        public int getItems() {
            return items;
        }
    }
}
