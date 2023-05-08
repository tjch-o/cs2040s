import java.util.Collections;
import java.util.PriorityQueue;

public class MedianFinder {
    private PriorityQueue<Integer> lower;
    private PriorityQueue<Integer> higher;

    public MedianFinder() {
        this.lower = new PriorityQueue<>(Collections.reverseOrder());
        this.higher = new PriorityQueue<>();
    }

    // balance the queues when either one has too many elements
    public void balanceQueues() {
        PriorityQueue<Integer> longerQueue = this.lower.size() > this.higher.size() ? 
        this.lower : this.higher;
        PriorityQueue<Integer> shorterQueue = this.lower.size() > this.higher.size() ? 
        this.higher : this.lower;
        if (longerQueue.size() - shorterQueue.size() > 1) {
            shorterQueue.offer(longerQueue.poll());
        }
    }

    public void insert(int x) {
        if (this.lower.size() == 0 || x <= this.lower.peek()) {
            this.lower.offer(x);
        } else {
            this.higher.offer(x);
        }
        balanceQueues();
    }

    public int getMedian() {
        // remember median is n/2 + 1 element if n is even and (n + 1)/2 if it is odd
        int median;
        if (this.higher.size() >= this.lower.size()) {
            // lowest in the original order in the greater half is removed; this is the median 
            median = this.higher.poll();
        } else {
            // highest in the reverse order in the lower half is removed 
            median = this.lower.poll();
        }
        balanceQueues();
        return median;
    }
}
