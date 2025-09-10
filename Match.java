// Lucy Beattie (oddconverse) 2025. All use legal. Free Palestine.

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

    // determines home team score based on goals and behinds
    public int getHomeScore() {
        return homeTeamGoals * 6 + homeTeamBehinds;
    }
    // determines away team score based on goals and behinds
    public int getAwayScore() {
        return awayTeamGoals * 6 + awayTeamBehinds;
    }
    public int getWinningScore() {
        //function assumes no draws, draws need to be accounted for later on
        if (homeTeamWin()) {
            return getHomeScore();
        }
        return getAwayScore();
    }
    public int getLosingScore() {
        //function assumes no draws, draws need to be accounted for later on
        if (homeTeamWin()) {
            return getAwayScore();
        }
        return getHomeScore();
    }
    public String getWinningTeamName() {
        if (getHomeScore() > getAwayScore()) {
            return getHomeTeamName();
        }
        else if (getHomeScore() < getAwayScore()) {
            return getAwayTeamName();
        }
        return "draw";
    }
    public String getLosingTeamName() {
        if (getAwayScore() > getHomeScore()) {
            return getHomeTeamName();
        }
        else if (getAwayScore() < getHomeScore()) {
            return getAwayTeamName();
        }
        return "draw";
    }
    // checks for a home team win
    public boolean homeTeamWin() {
        if (getHomeScore() > getAwayScore()) {
            return true;
        }
        return false;
    }
    // checks for a draw
    public boolean wasDrawn() {
        if (getHomeScore() == getAwayScore()) {
            return true;
        }
        return false;
    }
    // finds the margin of the match, returns an absolute value
    // could use this to determine if the winner was the home or away team? remove absolute and use in later programs

    public int getMargin() {
        return Math.abs(getHomeScore() - getAwayScore());
    }

    public int getCombinedScore() {
        return getHomeScore() + getAwayScore();
    }

    public boolean isFinal() {
        if (getRound().contains("Round") || getRound().contains("Week")) {
            return false;
        }
        return true;
    }
    // String returned is dependent on result of match
    // e.g if home team wins it will say "Richmond 6.2 (38) def. Essendon 6.1 (37)"
    // if away deam wins it will say "Sydney 5.5 (35) def. by Hawthorn 7.3 (45)"
    // draw will say "Collingwood 9.14 (68) drew with St Kilda 10.8 (68)"

    public String toString() {
        if (homeTeamWin())
            return String.format("%s: %s %d.%d (%d) def. %s %d.%d (%d)", round, homeTeamName, homeTeamGoals, homeTeamBehinds, getHomeScore(), awayTeamName, awayTeamGoals, awayTeamBehinds, getAwayScore());
        else if (wasDrawn())
            return String.format("%s: %s %d.%d (%d) drew with %s %d.%d (%d)", round, homeTeamName, homeTeamGoals, homeTeamBehinds, getHomeScore(), awayTeamName, awayTeamGoals, awayTeamBehinds, getAwayScore());
        else 
            return String.format("%s: %s %d.%d (%d) def. by %s %d.%d (%d)", round, homeTeamName, homeTeamGoals, homeTeamBehinds, getHomeScore(), awayTeamName, awayTeamGoals, awayTeamBehinds, getAwayScore());
    }

    // getters
    public String getRound() {
        return round;
    }
    public String getHomeTeamName() {
        return homeTeamName;
    }
    public int getHomeTeamGoals() {
        return homeTeamGoals;
    }
    public int getHomeTeamBehinds() {
        return homeTeamBehinds;
    }
    public String getAwayTeamName() {
        return awayTeamName;
    }
    public int getAwayTeamGoals() {
        return awayTeamGoals;
    }
    public int getAwayTeamBehinds() {
        return awayTeamBehinds;
    }
}