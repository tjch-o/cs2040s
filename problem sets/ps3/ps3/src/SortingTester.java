import java.util.Random;

public class SortingTester {
    public static boolean checkSort(ISort sorter, int size) {
        KeyValuePair[] testArray = new KeyValuePair[size];
        int currentKey;
        KeyValuePair prev;
        KeyValuePair current;

        for (int i = 0; i < size; i += 1) {
            // we create a list of pairs that are randomly numbered by key
            currentKey = (int) (Math.random() * size);
            testArray[i] = new KeyValuePair(currentKey, i);
        }

        sorter.sort(testArray);

        // we now check if the array is properly sorted based on key
        for (int j = 1; j < size; j += 1) {
            prev = testArray[j - 1];
            current = testArray[j];
            if (current.compareTo(prev) < 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isStable(ISort sorter, int size) {
        KeyValuePair[] testArray = new KeyValuePair[size];
        int currentKey;
        KeyValuePair prev;
        KeyValuePair current;
        int currentValue;
        int previousValue;

        /* to test stability we need duplicates in the array so some elements
        have same key but different values; we use Pigeonhole Principle here by 
        dividing the range of numbers we can randomly choose from as the key by 
        half so that we can gaurantee duplicates in the array for comparison 
        later on */
        int halfOfArraySize = size / 2;
        for (int i = 0; i < size; i += 1) {
            currentKey = (int) (Math.random() * halfOfArraySize);
            testArray[i] = new KeyValuePair(currentKey, i);
        }

        sorter.sort(testArray);

        // we are still doing side by side comparisons here in this sorted array
        // but our focus is on elements side by side which are the same key
        for (int j = 1; j < size; j += 1) {
            prev = testArray[j - 1];
            current = testArray[j];
            if (current.compareTo(prev) == 0) {
                currentValue = current.getValue();
                previousValue = prev.getValue();
                // values of KeyValuePair objects are distinct
                if (currentValue < previousValue) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 
     * @param n size of array to run
     * @return an array of KeyValuePair where the key is sorted in descending order
     */
    public static KeyValuePair[] worstCaseArray(int n) {
        KeyValuePair[] result = new KeyValuePair[n];
        for (int i = 0; i < n; i += 1) {
            result[i] = new KeyValuePair(n - i - 1, n - i - 1);
        }
        return result;
    }

    /**
     * 
     * @param n size of array to run
     * @return an array of KeyValuePair where the key is sorted in ascending order
     */
    public static KeyValuePair[] bestCaseArray(int n) {
        KeyValuePair[] result = new KeyValuePair[n];
        for (int i = 0; i < n; i += 1) {
            result[i] = new KeyValuePair(i, i);
        }
        return result;
    }

    public static KeyValuePair[] nearlySortedArray(int n) {
        KeyValuePair[] result = new KeyValuePair[n];
        for (int i = 0; i < n - 1; i += 1) {
            result[i] = new KeyValuePair(i, i);
        }
        // array is nearly sorted except the last element
        result[n - 1] = new KeyValuePair(-1, -1);
        return result;
    }

    /**
     * Checks if the sorter object can consistently produce correct sorts and
     * determine which of the sorter objects is Dr Evil; this is because the
     * boolean returned from checkSort could change
     * 
     * @param sorter             a sorter object
     * @param size               the size of the array we are checking
     * @param numberOfIterations how many time we want to run checkSort
     * @return a boolean value of whether the particular sort works correctly over
     *         many iterations
     */
    public static boolean checkParticularSort(ISort sorter, int size,
            int numberOfIterations) {
        for (int i = 0; i < numberOfIterations; i += 1) {
            if (checkSort(sorter, size) == false) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        ISort sortA = new SorterA();
        ISort sortB = new SorterB();
        ISort sortC = new SorterC();
        ISort sortD = new SorterD();
        ISort sortE = new SorterE();
        ISort sortF = new SorterF();

        // test all sorts if they are stable
        System.out.println(isStable(sortA, 100));
        System.out.println(isStable(sortB, 100));
        System.out.println(isStable(sortC, 100));
        System.out.println(isStable(sortD, 100));
        System.out.println(isStable(sortE, 100));
        System.out.println(isStable(sortF, 100));

        // test if any of the sorts don't work correctly over given number of iterations
        System.out.println(checkParticularSort(sortA, 4000, 5));
        System.out.println(checkParticularSort(sortB, 4000, 5));
        System.out.println(checkParticularSort(sortC, 4000, 5));
        System.out.println(checkParticularSort(sortD, 4000, 5));
        System.out.println(checkParticularSort(sortE, 4000, 5));
        System.out.println(checkParticularSort(sortF, 4000, 5));

        // System.out.println(sortA.sort(bestCaseArray(10)));
        // System.out.println(sortA.sort(bestCaseArray(20)));
        // System.out.println(sortA.sort(bestCaseArray(40)));
        // System.out.println(sortA.sort(bestCaseArray(80)));
        // System.out.println(sortA.sort(bestCaseArray(100)));
        // System.out.println(sortA.sort(bestCaseArray(160)));
        // System.out.println(sortA.sort(bestCaseArray(320)));
        // System.out.println(sortA.sort(bestCaseArray(500)));
        // System.out.println(sortA.sort(bestCaseArray(640)));
        // System.out.println(sortA.sort(bestCaseArray(800)));
        // System.out.println(sortA.sort(bestCaseArray(900)));
        // System.out.println(sortA.sort(bestCaseArray(1000)));

        // System.out.println(sortA.sort(worstCaseArray(10)));
        // System.out.println(sortA.sort(worstCaseArray(20)));
        // System.out.println(sortA.sort(worstCaseArray(40)));
        // System.out.println(sortA.sort(worstCaseArray(80)));
        // System.out.println(sortA.sort(worstCaseArray(100)));
        // System.out.println(sortA.sort(worstCaseArray(160)));
        // System.out.println(sortA.sort(worstCaseArray(320)));
        // System.out.println(sortA.sort(worstCaseArray(500)));
        // System.out.println(sortA.sort(worstCaseArray(640)));
        // System.out.println(sortA.sort(worstCaseArray(800)));
        // System.out.println(sortA.sort(worstCaseArray(900)));
        // System.out.println(sortA.sort(worstCaseArray(1000)));

        // System.out.println(sortC.sort(bestCaseArray(10)));
        // System.out.println(sortC.sort(bestCaseArray(20)));
        // System.out.println(sortC.sort(bestCaseArray(40)));
        // System.out.println(sortC.sort(bestCaseArray(80)));
        // System.out.println(sortC.sort(bestCaseArray(100)));
        // System.out.println(sortC.sort(bestCaseArray(160)));
        // System.out.println(sortC.sort(bestCaseArray(320)));
        // System.out.println(sortC.sort(bestCaseArray(500)));
        // System.out.println(sortC.sort(bestCaseArray(640)));
        // System.out.println(sortC.sort(bestCaseArray(800)));
        // System.out.println(sortC.sort(bestCaseArray(900)));
        // System.out.println(sortC.sort(bestCaseArray(1000)));

        // System.out.println(sortC.sort(worstCaseArray(10)));
        // System.out.println(sortC.sort(worstCaseArray(20)));
        // System.out.println(sortC.sort(worstCaseArray(40)));
        // System.out.println(sortC.sort(worstCaseArray(80)));
        // System.out.println(sortC.sort(worstCaseArray(100)));
        // System.out.println(sortC.sort(worstCaseArray(160)));
        // System.out.println(sortC.sort(worstCaseArray(320)));
        // System.out.println(sortC.sort(worstCaseArray(500)));
        // System.out.println(sortC.sort(worstCaseArray(640)));
        // System.out.println(sortC.sort(worstCaseArray(800)));
        // System.out.println(sortC.sort(worstCaseArray(900)));
        // System.out.println(sortC.sort(worstCaseArray(1000)));

        // System.out.println(sortD.sort(bestCaseArray(10)));
        // System.out.println(sortD.sort(bestCaseArray(20)));
        // System.out.println(sortD.sort(bestCaseArray(40)));
        // System.out.println(sortD.sort(bestCaseArray(80)));
        // System.out.println(sortD.sort(bestCaseArray(100)));
        // System.out.println(sortD.sort(bestCaseArray(160)));
        // System.out.println(sortD.sort(bestCaseArray(320)));
        // System.out.println(sortD.sort(bestCaseArray(500)));
        // System.out.println(sortD.sort(bestCaseArray(640)));
        // System.out.println(sortD.sort(bestCaseArray(800)));
        // System.out.println(sortD.sort(bestCaseArray(900)));
        // System.out.println(sortD.sort(bestCaseArray(1000)));
        // System.out.println(sortD.sort(bestCaseArray(1250)));
        // System.out.println(sortD.sort(bestCaseArray(1500)));
        // System.out.println(sortD.sort(bestCaseArray(2000)));
        // System.out.println(sortD.sort(bestCaseArray(2500)));

        // System.out.println(sortD.sort(worstCaseArray(10)));
        // System.out.println(sortD.sort(worstCaseArray(20)));
        // System.out.println(sortD.sort(worstCaseArray(40)));
        // System.out.println(sortD.sort(worstCaseArray(80)));
        // System.out.println(sortD.sort(worstCaseArray(100)));
        // System.out.println(sortD.sort(worstCaseArray(160)));
        // System.out.println(sortD.sort(worstCaseArray(320)));
        // System.out.println(sortD.sort(worstCaseArray(500)));
        // System.out.println(sortD.sort(worstCaseArray(640)));
        // System.out.println(sortD.sort(worstCaseArray(800)));
        // System.out.println(sortD.sort(worstCaseArray(900)));
        // System.out.println(sortD.sort(worstCaseArray(1000)));
        // System.out.println(sortD.sort(worstCaseArray(1250)));
        // System.out.println(sortD.sort(worstCaseArray(1500)));
        // System.out.println(sortD.sort(worstCaseArray(2000)));
        // System.out.println(sortD.sort(worstCaseArray(2500)));

        // System.out.println(sortE.sort(bestCaseArray(10)));
        // System.out.println(sortE.sort(bestCaseArray(20)));
        // System.out.println(sortE.sort(bestCaseArray(40)));
        // System.out.println(sortE.sort(bestCaseArray(80)));
        // System.out.println(sortE.sort(bestCaseArray(100)));
        // System.out.println(sortE.sort(bestCaseArray(160)));
        // System.out.println(sortE.sort(bestCaseArray(320)));
        // System.out.println(sortE.sort(bestCaseArray(500)));
        // System.out.println(sortE.sort(bestCaseArray(640)));
        // System.out.println(sortE.sort(bestCaseArray(800)));
        // System.out.println(sortE.sort(bestCaseArray(900)));
        // System.out.println(sortE.sort(bestCaseArray(1000)));
        // System.out.println(sortE.sort(bestCaseArray(1250)));
        // System.out.println(sortE.sort(bestCaseArray(1500)));
        // System.out.println(sortE.sort(bestCaseArray(2000)));
        // System.out.println(sortE.sort(bestCaseArray(2500)));

        // System.out.println(sortE.sort(worstCaseArray(10)));
        // System.out.println(sortE.sort(worstCaseArray(20)));
        // System.out.println(sortE.sort(worstCaseArray(40)));
        // System.out.println(sortE.sort(worstCaseArray(80)));
        // System.out.println(sortE.sort(worstCaseArray(100)));
        // System.out.println(sortE.sort(worstCaseArray(160)));
        // System.out.println(sortE.sort(worstCaseArray(320)));
        // System.out.println(sortE.sort(worstCaseArray(500)));
        // System.out.println(sortE.sort(worstCaseArray(640)));
        // System.out.println(sortE.sort(worstCaseArray(800)));
        // System.out.println(sortE.sort(worstCaseArray(900)));
        // System.out.println(sortE.sort(worstCaseArray(1000)));
        // System.out.println(sortE.sort(worstCaseArray(1250)));
        // System.out.println(sortE.sort(worstCaseArray(1500)));
        // System.out.println(sortE.sort(worstCaseArray(2000)));
        // System.out.println(sortE.sort(worstCaseArray(2500)));

        // System.out.println(sortF.sort(bestCaseArray(10)));
        // System.out.println(sortF.sort(bestCaseArray(20)));
        // System.out.println(sortF.sort(bestCaseArray(40)));
        // System.out.println(sortF.sort(bestCaseArray(80)));
        // System.out.println(sortF.sort(bestCaseArray(100)));
        // System.out.println(sortF.sort(bestCaseArray(160)));
        // System.out.println(sortF.sort(bestCaseArray(320)));
        // System.out.println(sortF.sort(bestCaseArray(500)));
        // System.out.println(sortF.sort(bestCaseArray(640)));
        // System.out.println(sortF.sort(bestCaseArray(800)));
        // System.out.println(sortF.sort(bestCaseArray(900)));
        // System.out.println(sortF.sort(bestCaseArray(1000)));

        // System.out.println(sortF.sort(worstCaseArray(10)));
        // System.out.println(sortF.sort(worstCaseArray(20)));
        // System.out.println(sortF.sort(worstCaseArray(40)));
        // System.out.println(sortF.sort(worstCaseArray(80)));
        // System.out.println(sortF.sort(worstCaseArray(100)));
        // System.out.println(sortF.sort(worstCaseArray(160)));
        // System.out.println(sortF.sort(worstCaseArray(320)));
        // System.out.println(sortF.sort(worstCaseArray(500)));
        // System.out.println(sortF.sort(worstCaseArray(640)));
        // System.out.println(sortF.sort(worstCaseArray(800)));
        // System.out.println(sortF.sort(worstCaseArray(900)));
        // System.out.println(sortF.sort(worstCaseArray(1000)));

        // comparing C and F to determine who is insertion sort who is bubble sort
        // System.out.println(sortC.sort(nearlySortedArray(10)));
        // System.out.println(sortC.sort(nearlySortedArray(20)));
        // System.out.println(sortC.sort(nearlySortedArray(40)));
        // System.out.println(sortC.sort(nearlySortedArray(80)));
        // System.out.println(sortC.sort(nearlySortedArray(100)));
        // System.out.println(sortC.sort(nearlySortedArray(160)));
        // System.out.println(sortC.sort(nearlySortedArray(320)));
        // System.out.println(sortC.sort(nearlySortedArray(500)));
        // System.out.println(sortC.sort(nearlySortedArray(640)));
        // System.out.println(sortC.sort(nearlySortedArray(800)));
        // System.out.println(sortC.sort(nearlySortedArray(900)));
        // System.out.println(sortC.sort(nearlySortedArray(1000)));

        // System.out.println(sortF.sort(nearlySortedArray(10)));
        // System.out.println(sortF.sort(nearlySortedArray(20)));
        // System.out.println(sortF.sort(nearlySortedArray(40)));
        // System.out.println(sortF.sort(nearlySortedArray(80)));
        // System.out.println(sortF.sort(nearlySortedArray(100)));
        // System.out.println(sortF.sort(nearlySortedArray(160)));
        // System.out.println(sortF.sort(nearlySortedArray(320)));
        // System.out.println(sortF.sort(nearlySortedArray(500)));
        // System.out.println(sortF.sort(nearlySortedArray(640)));
        // System.out.println(sortF.sort(nearlySortedArray(800)));
        // System.out.println(sortF.sort(nearlySortedArray(900)));
        // System.out.println(sortF.sort(nearlySortedArray(1000)));
    }
}
