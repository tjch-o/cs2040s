public class Guessing {
    private int low = 0;
    private int high = 1000;
    // middle is essentially my guess
    private int middle;

    public int guess() {
        // always start from the middle; inspired by binary search
        middle = low + (high - low) / 2;
        return middle;
    }

    public void update(int answer) {
        if (answer == 1) {
            // guess is too high; need to change high to be lower
            high = middle - 1;
        } else if (answer == -1) {
            // guess is too low; need to change low to be higher
            low = middle + 1;
        } else {}
    }
}
