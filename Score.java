public class Score {
    private String scoreID;
    private String matchID;
    private String teamName;
    private int goals;
    private int behinds;
    public Score(String scoreID, String matchID, String teamName, int goals, int behinds) {
        this.scoreID = scoreID;
        this.matchID = matchID;
        this.teamName = teamName;
        this.goals = goals;
        this.behinds = behinds;
    }
    public String toString() {
        return String.format("%s: %d.%d (%d)", getTeam(), getGoals(), getBehinds(), getTotalScore());
    }
    public int getTotalScore() {
        return goals * 6 + behinds;
    }
    public String getMatchID() {
        return matchID;
    }
    public String getID() {
        return scoreID;
    }
    public String getTeam() {
        return teamName;
    }
    public int getGoals() {
        return goals;
    }
    public int getBehinds() {
        return behinds;
    }
}
public