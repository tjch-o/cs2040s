import java.util.HashMap;

public class Solution {
    public static int solve(int[] arr) {
        int maxLength = -1;
        // our start here will be unchanged if there is no duplicates spotted
        int start = 0;
        HashMap<Integer, Integer> hashmap = new HashMap<>();

        for (int i = 0; i < arr.length; i += 1) {
            int currentKey = arr[i];
            // if key is not inside the hashmap then we simply add it in and store the last index we saw the key
            if (!hashmap.containsKey(currentKey)) {
                hashmap.put(currentKey, i);
            } else {
                // i - start is the difference in length between 2 duplicates
                maxLength = Math.max(maxLength, i - start);
                // before we update the key we need to use the previous index of the duplicate
                int prevIndex = hashmap.get(currentKey);
                // move on from the previous index hence the +1
                start = start > prevIndex ? start : prevIndex + 1;
                hashmap.replace(currentKey, i);
            }
        }
        return maxLength;
    }
}