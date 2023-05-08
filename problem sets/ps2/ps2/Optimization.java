/**
 * The Optimization class contains a static routine to find the maximum in an
 * array that changes direction at most once.
 */
public class Optimization {
    /**
     * A set of test cases.
     */
    static int[][] testCases = {
            { 1, 3, 5, 7, 9, 11, 10, 8, 6, 4 },
            { 67, 65, 43, 42, 23, 17, 9, 100 },
            { 4, -100, -80, 15, 20, 25, 30 },
            { 2, 3, 4, 5, 6, 7, 8, 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83 }
    };

    /**
     * A helper function for searchMax that takes in an array of at least
     * length 2 and compares the first element and the last element; the larger
     * of the 2 elements will be returned.
     *
     * @param arr an array of integers >= length 2.
     * @return the higher of the 2: first and last element
     */
    public static int compareFirstAndLast(int[] arr) {
        int lenArr = arr.length;
        if (arr[0] > arr[lenArr - 1]) {
            return arr[0];
        } else {
            return arr[lenArr - 1];
        }
    }

    /**
     * Returns the maximum item in the specified array of integers which
     * changes direction at most once.
     *
     * @param dataArray an array of integers which changes direction at most once.
     * @return the maximum item in data Array
     */
    public static int searchMax(int[] dataArray) {
        int lengthArr = dataArray.length;
        if (lengthArr == 0) {
            // an empty array can be considered invalid input
            return 0;
        } else if (lengthArr == 1) {
            // there is only one element in the array so we can just return it
            return dataArray[0];
        } else if (lengthArr == 2 || dataArray[0] > dataArray[1]) {
            // in the case of 2 elements in the array we return the higher one
            /* the other if case is when the numbers in the array is a
            decreasing trend from left to right or it is actually a V shape */
            // if first element > last element it is a decreasing trend
            /* else it is a V shape graph with a turning point in between 
            possibly the last element is higher than the first */
            return compareFirstAndLast(dataArray);
        } else {
            /* if we reach this case the numbers in array represents an 
            inverted v shape graph with a max or an increasing trend */
            int begin = 0;
            int end = lengthArr - 1;

            while (begin < end) {
                int middle = begin + (end - begin) / 2;
                // inside the while loop we also need to do comparisons
                /* this step determines if its an increasing trend or inverted
                v shape graph; without this cannot pass edge case from 
                coursemology as it will raise error */
                // both begin = end - 1 or end = begin + 1 should work
                if (begin == end - 1) {
                    // in the case where there are 2 elements left to compare
                    int[] compare = { dataArray[begin], dataArray[end] };
                    return compareFirstAndLast(compare);
                }
                /* best case; middle is max in the array since there is only
                one change in direction */
                if (dataArray[middle] > dataArray[middle - 1] && 
                dataArray[middle] > dataArray[middle + 1]) {
                    return dataArray[middle];
                } else {
                    /* the max is not in the centre so we compare with the left
                    side of the array */
                    if (dataArray[middle] < dataArray[middle - 1]) {
                        end = middle - 1;
                    } else {
                        // we compare with the right side of the array
                        begin = middle + 1;
                    }
                }
            }
            return dataArray[begin];
        }
    }

    /**
     * A routine to test the searchMax routine.
     */
    public static void main(String[] args) {
        for (int[] testCase : testCases) {
            System.out.println(searchMax(testCase));
        }
    }
}
