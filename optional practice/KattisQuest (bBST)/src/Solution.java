import java.util.TreeSet;

public class Solution {
    // every operation must be done in logn time
    TreeSet<Quest> questTree = new TreeSet<Quest>();

    void add(long energy, long value) {
        Quest q = new Quest(energy, value);
        questTree.add(q);
    }

    long query(long remainingEnergy) {
        long totalReward = 0;
        while (!(questTree.isEmpty()) && remainingEnergy > 0) {
            // we find the largest remaining element
            Quest largestPossibleQuest = new Quest(remainingEnergy, Long.MAX_VALUE);
            // using floor lets us find the closest one to the largestPossible 
            // remember that we find the largest energy quest from the current pool of quests which is smaller or equal to X
            Quest toBeRemoved = questTree.floor(largestPossibleQuest);
            // there is a possibility toBeRemoved is a null
            if (toBeRemoved != null) {
                remainingEnergy = toBeRemoved.remainingEnergyAfterQuest(remainingEnergy);
                totalReward = toBeRemoved.updatedRewardAfterQuest(totalReward);
                // we will then remove from the tree after updating the remaining energy and totalReward
                questTree.remove(toBeRemoved);
            } else {
                break;
            }
        }
        return totalReward;
    }
}
