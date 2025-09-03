import java.lang.Math;

public class Match {
    private String round;
    private String homeTeamName;
    private int homeTeamGoals;
    private int homeTeamBehinds;
    private String awayTeamName;
    private int awayTeamGoals;
    private int awayTeamBehinds;

    public Match(String round, String homeTeamName, int homeTeamGoals, int homeTeamBehinds, String awayTeamName, int awayTeamGoals, int awayTeamBehinds) {
        this.round = round;
        this.homeTeamName = homeTeamName;
        this.homeTeamGoals = homeTeamGoals;
        this.homeTeamBehinds = homeTeamBehinds;
        this.awayTeamName = awayTeamName;
        this.awayTeamGoals = awayTeamGoals;
        this.awayTeamBehinds = awayTeamBehinds;
    }

    public int getHomeScore() {
        return homeTeamGoals * 6 + homeTeamBehinds;
    }

    public int getAwayScore() {
        return awayTeamGoals * 6 + awayTeamBehinds;
    }

    public boolean homeTeamWin() {
        if (getHomeScore() > getAwayScore()) {
            return true;
        }
        return false;
    }

    public boolean wasDrawn() {
        if (getHomeScore() == getAwayScore()) {
            return true;
        }
        return false;
    }

    public int getMargin() {
        return Math.abs(getHomeScore() - getAwayScore());
    }
    public String toString() {
        if (homeTeamWin())
            return String.format("%s: %s %d.%d (%d) def. %s %d.%d (%d)", round, homeTeamName, homeTeamGoals, homeTeamBehinds, getHomeScore(), awayTeamName, awayTeamGoals, awayTeamBehinds, getAwayScore());
        else if (wasDrawn())
            return String.format("%s: %s %d.%d (%d) drew with %s %d.%d (%d)", round, homeTeamName, homeTeamGoals, homeTeamBehinds, getHomeScore(), awayTeamName, awayTeamGoals, awayTeamBehinds, getAwayScore());
        else 
            return String.format("%s: %s %d.%d (%d) def. by %s %d.%d (%d)", round, homeTeamName, homeTeamGoals, homeTeamBehinds, getHomeScore(), awayTeamName, awayTeamGoals, awayTeamBehinds, getAwayScore());
    }
}