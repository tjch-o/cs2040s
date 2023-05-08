import java.util.Arrays;

class WiFi {
    public static double computeDistance(int[] houses, int numOfAccessPoints) {
        Arrays.sort(houses);
        int numOfHouses = houses.length;

        double start = 0;
        double end = houses[numOfHouses - 1] - houses[0];
        double middle;

        // we do a binary search and test what is the min distance we can go
        while (start < end) {
            middle = start + (end - start) / 2;
            if (coverable(houses, numOfAccessPoints, middle)) {
                end = middle;
            } else {
                // 0.5 instead of 1 as we want it to be within 0.5 of nearest distance
                start = middle + 0.5;
            }
        }
        return start;
    }

    public static boolean coverable(int[] houses, int numOfAccessPoints, double distance) {
        // we make our lives easier by sorting the array first
        Arrays.sort(houses);

        int numOfHouses = houses.length;
        // we need one point for the first house on the street
        int accessPointsNeeded = 1;
        int referencePoint = 0;

        for (int i = 0; i < numOfHouses; i += 1) {
            /* if houses are {1, 3} and radius is 1 then the max we can go to 
            accommodate both houses is by it being at the midpoint of both */
            if (houses[i] - houses[referencePoint] <= 2 * distance) {
                continue;
            } else {
                accessPointsNeeded += 1;
                referencePoint = i;
            }
        }
        return accessPointsNeeded <= numOfAccessPoints;
    }

    public static void main(String[] args) {
        int[] houses = {1, 3, 10};
        int numAccessPoints = 2;
        System.out.println(coverable(houses, numAccessPoints, 1));
    }
}
