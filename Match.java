// Lucy Beattie (oddconverse) 2025. All use legal. Free Palestine.

public class Match {
    private String id;
    private String round;
    private Score homeTeamScore;
    private Score awayTeamScore;

    public Match(String matchID, String round, String homeScoreID, String homeTeamName, int homeTeamGoals, int homeTeamBehinds, String awayScoreID, String awayTeamName, int awayTeamGoals, int awayTeamBehinds) {
        this.id = matchID;
        this.round = round;
        this.homeTeamScore = new Score(homeScoreID, matchID, homeTeamName, homeTeamGoals, homeTeamBehinds);
        this.awayTeamScore = new Score(awayScoreID, matchID, awayTeamName, awayTeamGoals, awayTeamBehinds);
    }

    public Match(String matchID, String round, Score homeTeamScore, Score awayTeamScore) {
        this.id = matchID;
        this.round = round;
        this.homeTeamScore = homeTeamScore;
        this.awayTeamScore = awayTeamScore;
    }

    // determines home team score based on goals and behinds
    public int getHomeScore() {
        return homeTeamScore.getTotalScore();
    }
    // determines away team score based on goals and behinds
    public int getAwayScore() {
        return awayTeamScore.getTotalScore();
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
        return getHomeScore() > getAwayScore();
    }
    // checks for a draw
    public boolean wasDrawn() {
        return getHomeScore() == getAwayScore();
    }

    public boolean hasTeam(String teamName) {
        return getHomeTeamName().equals(teamName) || getAwayTeamName().equals(teamName);
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
        return !(getRound().contains("Round") || getRound().contains("Week"));
    }
    // String returned is dependent on result of match
    // e.g if home team wins it will say "Richmond 6.2 (38) def. Essendon 6.1 (37)"
    // if away deam wins it will say "Sydney 5.5 (35) def. by Hawthorn 7.3 (45)"
    // draw will say "Collingwood 9.14 (68) drew with St Kilda 10.8 (68)"

    public String toString() {
        if (homeTeamWin())
            return String.format("%s: %s %d.%d (%d) def. %s %d.%d (%d)", getRound(), getHomeTeamName(), getHomeTeamGoals(), getHomeTeamBehinds(), getHomeScore(), getAwayTeamName(), getAwayTeamGoals(), getAwayTeamBehinds(), getAwayScore());
        else if (wasDrawn())
            return String.format("%s: %s %d.%d (%d) drew with %s %d.%d (%d)", getRound(), getHomeTeamName(), getHomeTeamGoals(), getHomeTeamBehinds(), getHomeScore(), getAwayTeamName(), getAwayTeamGoals(), getAwayTeamBehinds(), getAwayScore());
        else 
            return String.format("%s: %s %d.%d (%d) def. by %s %d.%d (%d)", getRound(), getHomeTeamName(), getHomeTeamGoals(), getHomeTeamBehinds(), getHomeScore(), getAwayTeamName(), getAwayTeamGoals(), getAwayTeamBehinds(), getAwayScore());
    }
    public String getSeason() {
        int startOfYearIndex = getRound().indexOf(",") + 2;
        return getRound().substring(startOfYearIndex, getRound().length());
    }
    // getters
    public String getRound() {
        return round;
    }
    public String getID() {
        return id;
    }
    public String getHomeTeamScoreID() {
        return homeTeamScore.getID();
    }
    public String getHomeTeamName() {
        return homeTeamScore.getTeam();
    }
    public int getHomeTeamGoals() {
        return homeTeamScore.getGoals();
    }
    public int getHomeTeamBehinds() {
        return homeTeamScore.getBehinds();
    }
    public String getAwayTeamScoreID() {
        return awayTeamScore.getID();
    }
    public String getAwayTeamName() {
        return awayTeamScore.getTeam();
    }
    public int getAwayTeamGoals() {
        return awayTeamScore.getGoals();
    }
    public int getAwayTeamBehinds() {
        return awayTeamScore.getBehinds();
    }
}