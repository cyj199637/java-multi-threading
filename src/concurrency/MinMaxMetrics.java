package concurrency;

public class MinMaxMetrics {

    private volatile long min = Long.MIN_VALUE;
    private volatile long max = Long.MAX_VALUE;

    public MinMaxMetrics() {
    }

    public void addSample(long newSample) {
        synchronized (this) {
            this.min = Math.min(this.min, newSample);
            this.max = Math.max(this.max, newSample);
        }
    }

    public long getMin() {
        return this.min;
    }

    public long getMax() {
        return this.max;
    }
}

