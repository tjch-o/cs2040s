import java.util.TreeSet;

public class Solution {
    // we use tree simply because we want to do updating in logn time
    TreeSet<Team> scoreTree = new TreeSet<>();
    Team[] teams;

    public Solution(int numTeams) {
        this.teams = new Team[numTeams];
        for (int i = 0; i < numTeams; i += 1) {
            // we initialise all the teams 
            this.teams[i] = new Team(i + 1, 0, 0);
        }
    }

    public int update(int team, long newPenalty){
        // remember we only care about the rank of team 1
        Team currentTeam = this.teams[team - 1]; 
        scoreTree.remove(currentTeam);
        currentTeam.updateScore();
        currentTeam.updatePenalty(newPenalty);
        if (team == 1) {
            // we need to loop through everything in the tree to update the rank
            // we will remove all the elements that are now less than the updated score
            while (!scoreTree.isEmpty()) {
                Team highest = scoreTree.last();
                if (currentTeam.compareTo(highest) < 0) {
                    scoreTree.remove(highest);
                } else {
                    break;
                }
            }
        } else {
            if (this.teams[0].compareTo(currentTeam) > 0) {
                scoreTree.add(currentTeam);
            }
        }
        // we only store teams greater than ourselves in the scoreTree
        return scoreTree.size() + 1;
    }
}