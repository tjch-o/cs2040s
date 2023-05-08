class InversionCounter {
    public static long countSwapsHelper(int[] arr, int[] sortedArr, int start, int end) {
        // should be an equal sign because if start = end then there is only one element left and no swaps / sorting is needed 
        if (start >= end) {
            return 0;
        } else {
            long numberOfSwaps = 0;
            int middle = start + (end - start) / 2;
    
            numberOfSwaps += countSwapsHelper(arr, sortedArr, start, middle);
            numberOfSwaps += countSwapsHelper(arr, sortedArr, middle + 1, end);
            // after that we assume we have 2 sorted portions of the big array so we do one final check
            numberOfSwaps += mergeAndCountHelper(arr, sortedArr, start, middle, middle + 1, end);
            return numberOfSwaps;

        }
    }

    public static long countSwaps(int[] arr) {
        int lengthArr = arr.length;
        return countSwapsHelper(arr, new int[lengthArr], 0, lengthArr - 1);
    }

    public static long mergeAndCountHelper(int[] arr, int[] sortedArr, int left1, int right1, int left2, int right2) {
        long count = 0;
        // this left pointer does not necessarily start from 0 when we copy from the array
        int start = left1;
        int current = left1;
        
        // making sure the left1 and left2 do not exceed the ends of their array
        while (left1 <= right1 && left2 <= right2) {
            if (arr[left1] <= arr[left2]) {
                sortedArr[current] = arr[left1];
                left1 += 1;
            } else {
                sortedArr[current] = arr[left2];
                /* imagine left = [1, 3, 5] and right = [2, 4, 6]; when we choose to add 2 into the 
                new array we know the number of inversions is 2. but difference of right1 - left1 
                is 1 since left1 is at 3 and right1 is at 5 so we should + 1 */ 
                count += (right1 - left1) + 1;
                left2 += 1;
            }
            current += 1;
        }

        // one of the arrays have remaining elements not copied
        while (left1 <= right1) {
            sortedArr[current] = arr[left1];
            left1 += 1;
            current += 1;
        }

        while (left2 <= right2) {
            sortedArr[current] = arr[left2];
            left2 += 1;
            current += 1;
        }

        // finally we copy into the original array and return count 
        for (int i = start; i <= right2; i += 1) {
            arr[i] = sortedArr[i];
        }
        return count;
    }   

    /**
     * Given an input array so that arr[left1] to arr[right1] is sorted and arr[left2] to arr[right2] is sorted
     * (also left2 = right1 + 1), merges the two so that arr[left1] to arr[right2] is sorted, and returns the
     * minimum amount of adjacent swaps needed to do so.
     */
    public static long mergeAndCount(int[] arr, int left1, int right1, int left2, int right2) {
        int lengthArr = arr.length;
        // we want to return a count but also have a sorted array hence the extra parameter
        return mergeAndCountHelper(arr, new int[lengthArr], left1, right1, left2, right2);
    }
}
