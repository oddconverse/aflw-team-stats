import java.util.*;

// IMPORTANT: NEVER EVER SORT THE MATSTER MATCHES DATASET. ALWAYS CLONE AND SORT. NO WAY TO RE-SORT INTO CHRONOLOGICAL ORDER
// Lucy Beattie (oddconverse) 2025. All use legal. Free Palestine.

public class Team implements Comparable<Team> {
    private String name;
    private int gamesPlayed;
    private int wins;
    private int losses;
    private int draws;
    private int pointsFor;
    private int pointsAgainst;

    public Team(String name) {
        this.name = name;
        this.gamesPlayed = 0;
        this.wins = 0;
        this.losses = 0;
        this.draws = 0;
        this.pointsFor = 0;
        this.pointsAgainst = 0;
    }

    @Override 
    public int compareTo(Team t) {
        if (this.getWinPercentage() - t.getWinPercentage() != 0) {
            return Double.compare(t.getWinPercentage(), this.getWinPercentage());
        }
        return (int) (t.getPercentage() - this.getPercentage());
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
    
    public void incrementPercentage(int newPointsFor, int newPointsAgainst) {
        this.pointsFor += newPointsFor;
        this.pointsAgainst += newPointsAgainst;
    }

    public double getPercentage() {
        return (double) pointsFor / (double) pointsAgainst * 100;
    }
    
    public double getWinPercentage() {
        return (double) wins / (double) gamesPlayed * 100;
    }

    public int getPoints() {
        return wins * 4 + draws * 2;
    }
    public String ladderDisplay() {
        return String.format("%24s | %4d | %4d | %4d | %4d | %4d | %4d | %4d | %7.2f | %7.2f |", name, gamesPlayed, wins, losses, draws, getPoints(), pointsFor, pointsAgainst, getPercentage(), getWinPercentage());
    }
    public String getName() {
        return name;
    }
    public String toString() {
        return name;
    }
}