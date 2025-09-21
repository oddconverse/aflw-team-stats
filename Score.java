import java.util.*;

public class Score implements Comparable<Score>{
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
    @Override
    public int compareTo(Score s) {
        return Integer.compare(s.getTotalScore(), this.getTotalScore());
    }
    @Override
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
class HighScoreComparator implements Comparator<Score> {
    @Override
    public int compare(Score a, Score b) {
        return Integer.compare(b.getTotalScore(), a.getTotalScore());
    }
}
class LowScoreComparator implements Comparator<Score> {
    @Override
    public int compare(Score a, Score b) {
        return Integer.compare(a.getTotalScore(), b.getTotalScore());
    }
}