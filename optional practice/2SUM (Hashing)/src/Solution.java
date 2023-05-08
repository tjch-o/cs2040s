import java.util.HashMap;

public class Solution {
    public static int solve(int[] arr, int target) {
        // our hashmap only stores one out of (a, b) where a + b = target at a time
        // however this does not mean a is always inside or b is always inside
        HashMap<Integer, Integer> hashmap = new HashMap<>();
        int count = 0; 
        int currentFreqOfElement = 0;
        int current;
        int other;
        
        for (int i = 0; i < arr.length; i += 1) {
            current = arr[i];
            other = target - current;
            if (!hashmap.containsKey(other)) {
                // we then check if the current key exists in the hashmap
                if (hashmap.containsKey(current)) {
                    currentFreqOfElement = hashmap.get(current) + 1;
                    // we then update the frequency of the element we seen
                    hashmap.replace(current, currentFreqOfElement);
                } else {
                    // we add the key into the hashmap
                    hashmap.put(current, 1);
                }
            } else {
                // we increase because we have spotted a pair: (current, other)
                // since this test case is triggered if other is in the hashmap
                count += 1;
                // we will reduce the frequency of the element to prevent double counting
                currentFreqOfElement = hashmap.get(other) - 1;
                if (currentFreqOfElement > 0) {
                    hashmap.replace(other, currentFreqOfElement);
                } else {
                    // we do not store any elements with 0 frequency
                    hashmap.remove(other);
                }
            }
        }
        return count;
    }
}