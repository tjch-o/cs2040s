public class Team implements Comparable<Team> {
    private int id;
    private int score;
    private int totalPenalty;

    public Team(int id, int score, int penalty) {
        this.id = id;
        this.score = score;
        this.totalPenalty = penalty;
    }

    public void updateScore() {
        this.score += 1;
    }

    public void updatePenalty(long penalty) {
        this.totalPenalty += penalty;
    }

    @Override
    public int compareTo(Team otherTeam) {
        if (this == otherTeam) {
            return 0;
        } else if (this.score > otherTeam.score) {
            return -1;
        } else if (this.score == otherTeam.score && this.totalPenalty < otherTeam.totalPenalty) {
            return -1;
        } else if (this.score == otherTeam.score && this.totalPenalty == otherTeam.totalPenalty && 
        this.id < otherTeam.id) {
            // without this will fail private test case
            return -1;
        } else {
            return 1;
        }
    }
}