public class Quest implements Comparable<Quest> {
    private final long energy;
    private final long reward;

    public Quest(long energy, long reward) {
        this.energy = energy;
        this.reward = reward;
    }

    @Override
    public int compareTo(Quest otherQuest) {
        // we find the largest energy quest; if tied we find the largest gold reward
        if (this == otherQuest) {
            return 0;
        } else if (this.energy > otherQuest.energy) {
            return 1;
        } else if (this.energy == otherQuest.energy && this.reward > otherQuest.reward) {
            return 1;
        } else {
            return -1;
        }
    }

    // we return a new value and not update any of the attributes 
    public long remainingEnergyAfterQuest(long input) {
        return input - this.energy;
    }

    public long updatedRewardAfterQuest(long accumulate) {
        return accumulate + this.reward;
    }
}