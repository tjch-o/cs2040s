class Sorter {
    public static boolean isGreaterThan(String str1, String str2) {
        // they only compare the first 2 letters
        for (int i = 0; i < 2; i += 1) {
            if (str1.charAt(i) < str2.charAt(i)) {
                return false;
            } else if (str1.charAt(i) > str2.charAt(i)) {
                return true;
            } else {}
        }
        // if we reach here then we don't need to change anything we make the sort stable
        return false;
    }

    public static void sortStrings(String[] arr) {
        // we will do an insertion sort since it is stable and in place
        int numOfElements = arr.length;
        String currentStr;
        int j;

        for (int i = 1; i < numOfElements; i += 1) {
            currentStr = arr[i];
            j = i - 1;
            while (j >= 0 && isGreaterThan(arr[j], currentStr)) {
                // we shift everything to the right
                arr[j + 1] = arr[j];
                j -= 1;
            }
            // we insert it here since we decreased j until it cannot go any lower
            arr[j + 1] = currentStr;
        }
    }
}