/**
 * Contains static routines for solving the problem of balancing m jobs on p
 * processors
 * with the constraint that each processor can only perform consecutive jobs.
 */
public class LoadBalancing {
    /**
     * Checks if it is possible to assign the specified jobs to the specified number
     * of processors such that no
     * processor's load is higher than the specified query load.
     *
     * @param jobSizes  the sizes of the jobs to be performed
     * @param queryLoad the maximum load allowed for any processor
     * @param p         the number of processors
     * @return true iff it is possible to assign the jobs to p processors so that no
     *         processor has more than queryLoad load.
     */
    public static boolean isFeasibleLoad(int[] jobSizes, int queryLoad, int p) {
        // edge cases
        if (jobSizes.length == 0) {
            /* the pdf says can consider this invalid even though I feel in
            reality we should return true */
            return false;
        } else if (p == 0) {
            // array not empty but no processors to process jobs
            return false;
        } else {
            // we only have p processors so its 0 to p-1
            int howManyLoadsLeft = p - 1;
            int eachLoad = queryLoad;
            // basically each job
            int jobAtHand;

            for (int i = 0; i < jobSizes.length; i += 1) {
                jobAtHand = jobSizes[i];
                // no load available is going to handle this
                if (queryLoad < jobAtHand) {
                    return false;
                } else if (eachLoad < jobAtHand) {
                    // we use a new load so we decrease by 1
                    howManyLoadsLeft -= 1;
                    // and we reset the load before we handle the current job
                    // if not it will be ignored
                    eachLoad = queryLoad - jobAtHand;
                } else {
                    // we can just use the current load
                    eachLoad -= jobAtHand;
                }
            }

            // if at the end of the loop howManyLoadsLeft is negative we return false
            if (howManyLoadsLeft < 0) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * Returns the minimum achievable load given the specified jobs and number of
     * processors.
     *
     * @param jobSizes the sizes of the jobs to be performed
     * @param p        the number of processors
     * @return the maximum load for a job assignment that minimizes the maximum load
     */
    public static int findLoad(int[] jobSizes, int p) {
        // handling invalid inputs
        if (jobSizes.length == 0) {
            return -1;
        } else if  (p <= 0) {
            return -1;
        } else {
            // make use of isFeasibleLoad here when they gave us both array and p
            int low = 0;
            int high = 0;
            int middle;

            // we need to know the total size of all the jobs
            for (int j = 0; j < jobSizes.length; j += 1) {
                high += jobSizes[j];
            }

            // we want to find the minimum maximum load that is feasible
            /* the IsFeasibleLoad here can be taken as a black box function 
            that magically handles the size of job at individual indexes */ 
            // similar to the tutorial allocation question in the lecture
            while (low < high) {
                middle = low + (high - low) / 2;
                // if its feasible we go down to check if it can be even lower
                if (isFeasibleLoad(jobSizes, middle, p)) {
                    /* in this case middle is inclusive because we dont know if
                    the middle is the minimum already */
                    high = middle;
                } else {
                    /* we have to go higher because we know that the current 
                    weight is not feasible */ 
                    low = middle + 1;
                }
            }
            return low;
        }  
    }

    // These are some arbitrary testcases.
    public static int[][] testCases = {
            { 1, 3, 5, 7, 9, 11, 10, 8, 6, 4 },
            { 67, 65, 43, 42, 23, 17, 9, 100 },
            { 4, 100, 80, 15, 20, 25, 30 },
            { 2, 3, 4, 5, 6, 7, 8, 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83 },
            { 7 }
    };

    /**
     * Some simple tests for the findLoad routine.
     */
    public static void main(String[] args) {
        for (int p = 1; p < 30; p++) {
            System.out.println("Processors: " + p);
            for (int[] testCase : testCases) {
                System.out.println(findLoad(testCase, p));
            }
        }
    }
}
