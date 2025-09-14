

// IMPORTANT: NEVER EVER SORT THE MATSTER MATCHES DATASET. ALWAYS CLONE AND SORT. NO WAY TO RE-SORT INTO CHRONOLOGICAL ORDER
// Lucy Beattie (oddconverse) 2025. All use legal. Free Palestine.

public class Team implements Comparable<Team> {
    private String name;
    private int gamesPlayed;
    private int homeWins;
    private int awayWins;
    private int homeLosses;
    private int awayLosses;
    private int homeDraws;
    private int awayDraws;
    private int pointsFor;
    private int pointsAgainst;

    public Team(String name) {
        this.name = name;
        this.gamesPlayed = 0;
        this.homeWins = 0;
        this.awayWins = 0;
        this.homeLosses = 0;
        this.awayLosses = 0;
        this.homeDraws = 0;
        this.awayDraws = 0;
        this.pointsFor = 0;
        this.pointsAgainst = 0;
    }

    @Override 
    public int compareTo(Team t) {
        if (this.getWinPercentage() - t.getWinPercentage() != 0) {
            return Double.compare(t.getWinPercentage(), this.getWinPercentage());
        }
        return Double.compare(t.getPercentage(), this.getPercentage());
    }
    public void incrementHomeWins() {
        this.gamesPlayed++;
        this.homeWins++;
    }
    public void incrementAwayWins() {
        this.gamesPlayed++;
        this.awayWins++;
    }
    public void incrementHomeLosses() {
        this.gamesPlayed++;
        this.homeLosses++;
    }
    public void incrementAwayLosses() {
        this.gamesPlayed++;
        this.awayLosses++;
    }
    public void incrementHomeDraws() {
        this.gamesPlayed++;
        this.homeDraws++;
    }
    public void incrementAwayDraws() {
        this.gamesPlayed++;
        this.awayDraws++;
    }
    
    public void incrementPercentage(int newPointsFor, int newPointsAgainst) {
        this.pointsFor += newPointsFor;
        this.pointsAgainst += newPointsAgainst;
    }

    public double getPercentage() {
        return (double) pointsFor / (double) pointsAgainst * 100;
    }
    
    public double getWinPercentage() {
        return (double) getWins() / (double) gamesPlayed * 100;
    }

    public int getPoints() {
        return getWins() * 4 + getDraws() * 2;
    }
    public String ladderDisplay() {
        return String.format("%24s | %4d | %4d | %4d | %4d | %4d | %4d | %4d | %7.2f | %7.2f |", name, gamesPlayed, getWins(), getLosses(), getDraws(), getPoints(), pointsFor, pointsAgainst, getPercentage(), getWinPercentage());
    }
    public String getName() {
        return name;
    }
    public String toString() {
        return name;
    }
    public int getWins() {
        return homeWins + awayWins;
    }
    public int getLosses() {
        return homeLosses + awayLosses;
    }
    public int getDraws() {
        return homeDraws + awayDraws;
    }
    public int getPointsFor() {
        return pointsFor;
    }
    public int getGamesPlayed() {
        return gamesPlayed;
    } 
    public int getHomeWins() {
        return homeWins;
    }
    public int getAwayWins() {
        return awayWins;
    }
}