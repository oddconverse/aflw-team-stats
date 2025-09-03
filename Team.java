import java.util.*;

public class Team implements Comparable<T> {
    private String name;
    private int gamesPlayed;
    private int wins;
    private int losses;
    private int draws;
    private int pointsFor;
    private int pointsAgainst;
    @Override 
    public int compareTo(Team t) {
        if (this.getPoints() != t.getPoints()) {
            return this.getPoints() - t.getPoints();
        }
        return this.getPercentage() - t.getPercentage();
    }
    public void incrementWin() {
        this.gamesPlayed++;
        this.wins++;
    }
    public void incrementLosses() {
        this.gamesPlayed++;
        this.losses++;
    }
    public void incrementDraws() {
        this.gamesPlayed++;
        this.draws++;
    }

    public int getPoints() {
        return wins * 4 + draws * 2;
    }
}