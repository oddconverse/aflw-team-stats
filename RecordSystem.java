import java.io.*;
import java.util.*;


// IMPORTANT: NEVER EVER SORT THE MATSTER MATCHES DATASET. ALWAYS CLONE AND SORT. NO WAY TO RE-SORT INTO CHRONOLOGICAL ORDER
// Lucy Beattie (oddconverse) 2025. All use legal. Free Palestine.

// TODO
// - team record implementation sorts by margin regardless of winning team
// - if multiple records broken in same game, they will not appear

public class RecordSystem {
    // matches to be stored in array. DO NOT SORT. CLONE AND SORT AFTER
    private ArrayList<Match> matches;
    private ArrayList<Score> scores;

    public static final String[] conferenceATeams2019 = {"Adelaide", "Fremantle", "Melbourne", "North Melbourne", "Western Bulldogs"};
    public static final String[] conferenceATeams2020 = {"North Melbourne", "Greater Western Sydney", "Brisbane", "Gold Coast", "Adelaide", "Geelong", "Richmond"};

    public RecordSystem() {
        this.matches = new ArrayList<>();
        this.scores = new ArrayList<>();
    }
    // add score to array
    public void addScore(Score score) {
        scores.add(score);
    }
    public void addScore(String scoreID, String matchID, String teamName, int goals, int behinds) {
        scores.add(new Score(scoreID, matchID, teamName, goals, behinds));
    }
    // find score in arraylist using id
    public Score getScore(String id) {
        for (Score score : scores) {
            if (score.getID().equals(id)) {
                return score;
            }
        }
        return null;
    }
    // add match to array
    public void addMatch(String matchID, String round, String homeScoreID, String homeTeamName, int homeTeamGoals, int homeTeamBehinds, String awayScoreID, String awayTeamName, int awayTeamGoals, int awayTeamBehinds) {
        matches.add(new Match(matchID, round, homeScoreID, homeTeamName, homeTeamGoals, homeTeamBehinds, awayScoreID, awayTeamName, awayTeamGoals, awayTeamBehinds));
    }
    public void addMatch(String matchID, String round, Score homeScore, Score awayScore) {
        matches.add(new Match(matchID, round, homeScore, awayScore));
    }
    public void addMatch(Match match) {
        matches.add(match);
    }
    // save all data
    public void saveData() {
        saveMatches();
        saveScores();
    }
    // save matches to text file to open later
    @SuppressWarnings("ConvertToTryWithResources")
    public void saveMatches() {
        try {
            File matchFile = new File("matches.txt");
            FileWriter matchWriter = new FileWriter(matchFile);
            for (Match match : matches) {
                matchWriter.write(String.format("%s; %s; %s; %s;\n", match.getID(), match.getRound(), match.getHomeTeamScoreID(), match.getAwayTeamScoreID()));
            }
            matchWriter.close();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
    // save scores to file 
    @SuppressWarnings("ConvertToTryWithResources")
    public void saveScores() {
        try {
            File scoresFile = new File("scores.txt");
            FileWriter scoreWriter = new FileWriter(scoresFile);
            for (Score score : scores) {
                scoreWriter.write(String.format("%s; %s; %s; %d; %d;\n", score.getID(), score.getMatchID(), score.getTeam(), score.getGoals(), score.getBehinds()));
            }
            scoreWriter.close();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
    @SuppressWarnings("ConvertToTryWithResources")
    public void loadMatches() {
        try {
            File scoreFile = new File("scores.txt");
            Scanner scoreInfile = new Scanner(scoreFile);
            while (scoreInfile.hasNextLine()) {
                String s = scoreInfile.nextLine();
                StringTokenizer stk = new StringTokenizer(s, ";");
                String scoreID = stk.nextToken().trim();
                String matchID = stk.nextToken().trim();
                String teamName = stk.nextToken().trim();
                int goals = Integer.parseInt(stk.nextToken().trim());
                int behinds = Integer.parseInt(stk.nextToken().trim());
                addScore(scoreID, matchID, teamName, goals, behinds);
            }
            scoreInfile.close();
            System.out.println("Load successful. Loaded from: " + scoreFile.getAbsolutePath());
        } 
        catch (IOException e) {
            System.out.println("Reading scores failed. Restart now.");
        }

        try {
            File matchFile = new File("matches.txt");
            Scanner matchInfile = new Scanner(matchFile);
            while (matchInfile.hasNextLine()) {
                String s = matchInfile.nextLine();
                StringTokenizer stk = new StringTokenizer(s, ";");
                String matchID = stk.nextToken().trim();
                String round = stk.nextToken().trim();
                Score homeScore = getScore(stk.nextToken().trim());
                Score awayScore = getScore(stk.nextToken().trim());
                addMatch(matchID, round, homeScore, awayScore);
            }
            matchInfile.close();
            System.out.println("Load successful. Loaded from: " + matchFile.getAbsolutePath());
        }
        catch (IOException e) {
            System.out.println(e);
        }

    }

    // RECORDS

    // function displays all records using comparator input
    // TODO: function only displays records where target team wins, must use record input to check if a win or a loss is necessary
    // TODO: tie tracker no longer works
    // TODO: must code a "getDifferential" method to determine how ties should be handled

    public void greatestMargin(ArrayList<Match> matchSet, int resultCount) {
        // tieTracker keeps track of what the record of the previously checked match was, to see if it is tied with the current match to be analysed
        // if it is tied, tieRank will not be updated
        // e.g two matches with the same winning margin should be displayed as:
        //  1: Fremantle 2.2 (14) def. by North Melbourne 18.6 (114). North Melbourne won by 100 points.
        //  1: Adelaide 15.12 (108) def. Carlton 1.2 (8). Adelaide won by 100 points.
        int tieTracker = 0;
        int tieRank = 0;

        // array sorted using byMargin comparator, note 0 - matchCount range is used as everything after matchCount is a null value
        matchSet.sort(new GreatestMarginComparator());
        // for loop conditions allow loop to continue even after i = 5 to get through extra tied 5th place records, ensures that loop ends at some stage

        for (int i = 0; (i < resultCount || tieTracker == matchSet.get(i).getMargin()) && i < matchSet.size(); i++) {
            if (tieTracker != matchSet.get(i).getMargin())
                tieRank = i + 1;
            System.out.println(String.format("%5d: %s. %s won by %d points.", tieRank, matchSet.get(i), matchSet.get(i).getWinningTeamName(), matchSet.get(i).getMargin()));
            tieTracker = matchSet.get(i).getMargin();
        }
    }

    public void highestTeamScore(ArrayList<Score> scoreSet, int resultCount) {
        int tieTracker = 0;
        int tieRank = 0;
        scoreSet.sort(new HighScoreComparator());

        for (int i = 0; (i < resultCount || tieTracker == scoreSet.get(i).getTotalScore()) && i < scoreSet.size(); i++) {
            if (tieTracker != scoreSet.get(i).getTotalScore())
                tieRank = i + 1;
            System.out.println(String.format("%5d: %s", tieRank, getMatch(scoreSet.get(i).getMatchID())));
            tieTracker = scoreSet.get(i).getTotalScore();
        }
    }

    public void highestCombinedScore(ArrayList<Match> matchSet, int resultCount) {
        int tieTracker = 0;
        int tieRank = 0;
        matchSet.sort(new HighCombinedScoreComparator());

        for (int i = 0; (i < resultCount || tieTracker == matchSet.get(i).getCombinedScore()) && i < matchSet.size(); i++) {
            Match match = matchSet.get(i);
            if (tieTracker != matchSet.get(i).getCombinedScore()) {
                tieRank = i + 1;
            }
            System.out.println(String.format("%5d: %s. Combined score: %d.%d (%d)", tieRank, match, match.getHomeTeamGoals() + match.getAwayTeamGoals(), match.getHomeTeamBehinds() + match.getAwayTeamBehinds(), match.getCombinedScore()));
            tieTracker = match.getCombinedScore();
        }
    }
    public void lowestTeamScore(ArrayList<Score> scoreSet, int resultCount) {
        int tieTracker = 0;
        int tieRank = 0;
        scoreSet.sort(new LowScoreComparator());

        for (int i = 0; (i < resultCount || tieTracker == scoreSet.get(i).getTotalScore()) && i < scoreSet.size(); i++) {
            if (tieTracker != scoreSet.get(i).getTotalScore())
                tieRank = i + 1;
            System.out.println(String.format("%5d: %s", tieRank, getMatch(scoreSet.get(i).getMatchID())));
            tieTracker = scoreSet.get(i).getTotalScore();
        }

    }
    public void lowestCombinedScore(ArrayList<Match> matchSet, int resultCount) {
        int tieTracker = 0;
        int tieRank = 0;
        matchSet.sort(new LowestCombinedScoreComparator());
        for (int i = 0; (i < resultCount || tieTracker == matchSet.get(i).getCombinedScore()) && i < matchSet.size(); i++) {
            Match match = matchSet.get(i);
            if (tieTracker != matchSet.get(i).getCombinedScore()) {
                tieRank = i + 1;
            }
            System.out.println(String.format("%5d: %s. Combined score: %d.%d (%d)", tieRank, match, match.getHomeTeamGoals() + match.getAwayTeamGoals(), match.getHomeTeamBehinds() + match.getAwayTeamBehinds(), match.getCombinedScore()));
            tieTracker = match.getCombinedScore();
        }
    }
    
    public void createLadder(ArrayList<Match> matchSet, Comparator<Team> orderBy) {
        
        // variable used to determine whether teams should be shown in conferences or not
        boolean conferenceFlag = true;
        // picks first match of dataset as a tester
        Match firstMatch = matchSet.get(0);
        Match lastMatch = matchSet.get(matchSet.size() - 1);
        boolean singleSeason = false;

        if ((firstMatch.getRound().contains("2019") || firstMatch.getRound().contains("2020")) && !firstMatch.isFinal()) {
            String targetSeason = firstMatch.getSeason();
            for (Match match : matchSet) {
                // checks to see if any match falls outside of a season where conferences were used, or if a match is a final
                if (!match.getRound().contains(targetSeason) || match.isFinal()) {
                    conferenceFlag = false;
                }
            }
        }
        else {
            conferenceFlag = false;
        }
        if (firstMatch.getSeason().equals(lastMatch.getSeason()) && !lastMatch.isFinal()) {
            singleSeason = true;
        }

        if (!conferenceFlag) {
            Ladder ladder = new Ladder();
            for (Match match : matchSet) {
                // instantiate team objects when a new team appears
                if (!ladder.doesTeamExist(match.getHomeTeamName())) {
                    ladder.addTeam(match.getHomeTeamName());
                }
                // adjust stats for home team
                Team currentHomeTeam = ladder.getTeam(match.getHomeTeamName());
                currentHomeTeam.incrementPercentage(match.getHomeScore(), match.getAwayScore());
                if (match.homeTeamWin()) {
                    currentHomeTeam.incrementHomeWins();
                }
                else if (match.wasDrawn()) {
                    currentHomeTeam.incrementHomeDraws();
                }
                else {
                    currentHomeTeam.incrementHomeLosses();
                }
                // same function but for away team
                if (!ladder.doesTeamExist(match.getAwayTeamName())) {
                    ladder.addTeam(match.getAwayTeamName());
                }
                Team currentAwayTeam = ladder.getTeam(match.getAwayTeamName());
                currentAwayTeam.incrementPercentage(match.getAwayScore(), match.getHomeScore());
                if (match.homeTeamWin()) {
                    currentAwayTeam.incrementAwayLosses();
                }
                else if (match.wasDrawn()) {
                    currentAwayTeam.incrementAwayDraws();
                }
                else {
                    currentAwayTeam.incrementAwayWins();
                }
            }
            if (singleSeason && orderBy == null) {
                orderBy = new SeasonLadderComparator();
                ladder.display(orderBy, null);
            }
            else if (orderBy != null) {
                ladder.display(orderBy, null);
            }
            else {
                ladder.display(null, null);
            }

        }
        else {
            Ladder conferenceA = new Ladder();
            Ladder conferenceB = new Ladder();
            for (Match match : matchSet) {
                boolean conferenceAHomeTeamFlag = false;
                boolean conferenceAAwayTeamFlag = false;
                String[] conferenceATeamSet;
                int teamNumber;
                if (match.getRound().contains("2019")) {
                    conferenceATeamSet = conferenceATeams2019.clone();
                    teamNumber = 5;
                }
                else {
                    conferenceATeamSet = conferenceATeams2020.clone();
                    teamNumber = 7;
                }
                // instantiate team objects when a new team appears
                if (!conferenceA.doesTeamExist(match.getHomeTeamName()) && !conferenceB.doesTeamExist(match.getHomeTeamName())) {
                    for (int i = 0; i < teamNumber; i++) {
                        if (match.getHomeTeamName().equals(conferenceATeamSet[i])) {
                            conferenceA.addTeam(match.getHomeTeamName());
                            conferenceAHomeTeamFlag = true;
                        }
                    }
                    if (!conferenceAHomeTeamFlag) {
                        conferenceB.addTeam(match.getHomeTeamName());
                    }
                    
                    
                }
                // adjust stats for home team
                Team currentHomeTeam = null;
                for (int i = 0; i < teamNumber; i++) {
                    if (match.getHomeTeamName().equals(conferenceATeamSet[i])) {
                        currentHomeTeam = conferenceA.getTeam(match.getHomeTeamName());
                        conferenceAHomeTeamFlag = true;
                    }
                }
                if (!conferenceAHomeTeamFlag) {
                    currentHomeTeam = conferenceB.getTeam(match.getHomeTeamName());
                }
                currentHomeTeam.incrementPercentage(match.getHomeScore(), match.getAwayScore());
                if (match.homeTeamWin()) {
                    currentHomeTeam.incrementHomeWins();
                }
                else if (match.wasDrawn()) {
                    currentHomeTeam.incrementHomeDraws();
                }
                else {
                    currentHomeTeam.incrementHomeLosses();
                }
                // same function but for away team
                if (!conferenceA.doesTeamExist(match.getAwayTeamName()) && !conferenceB.doesTeamExist(match.getAwayTeamName())) {
                    for (int i = 0; i < teamNumber; i++) {
                        if (match.getAwayTeamName().equals(conferenceATeamSet[i])) {
                            conferenceA.addTeam(match.getAwayTeamName());
                            conferenceAAwayTeamFlag = true;
                        }
                    }
                    if (!conferenceAAwayTeamFlag) {
                        conferenceB.addTeam(match.getAwayTeamName());
                    }
                    
                    
                }
                Team currentAwayTeam = null;
                for (int i = 0; i < teamNumber; i++) {
                    if (match.getAwayTeamName().equals(conferenceATeamSet[i])) {
                        currentAwayTeam = conferenceA.getTeam(match.getAwayTeamName());
                        conferenceAAwayTeamFlag = true;
                    }
                }
                if (!conferenceAAwayTeamFlag) {
                    currentAwayTeam = conferenceB.getTeam(match.getAwayTeamName());
                }
                currentAwayTeam.incrementPercentage(match.getAwayScore(), match.getHomeScore());
                if (match.homeTeamWin()) {
                    currentAwayTeam.incrementAwayLosses();
                }
                else if (match.wasDrawn()) {
                    currentAwayTeam.incrementAwayDraws();
                }
                else {
                    currentAwayTeam.incrementAwayWins();
                }
            }
            if (orderBy == null) {
                orderBy = new SeasonLadderComparator();
            }

            System.out.println("|---------------------------------------CONFERENCE A---------------------------------------------------|");
            conferenceA.display(orderBy, null);
            System.out.println("|---------------------------------------CONFERENCE B---------------------------------------------------|");
            conferenceB.display(orderBy, null);
        }
    }
    
    public void displayMatchesByRound(String round, String year) {
        ArrayList<Match> results = new ArrayList<Match>();
        for (Match match : matches) {
            if (isNumeric(round) && match.getRound().equals(String.format("Round %s, %s", round, year)) || match.getRound().equals(String.format("Week %s, %s", round, year)) || match.getRound().equals(String.format("%s, %s", round, year))) {
                results.add(match);
                System.out.println(match);
            }
        }
    }

    public void displayMatchesByTeam(String team) {
        for (Match match : matches) {
            if (match.getHomeTeamName().equals(team) || match.getAwayTeamName().equals(team)) {
                System.out.println(match);
            }
        }
    }

    public void displayHeadToHead(String team1str, String team2str, boolean displayResults) {
        ArrayList<Match> results = new ArrayList<Match>();
        for (Match match : matches) {
            if ((match.getHomeTeamName().equals(team1str) || match.getHomeTeamName().equals(team2str)) && (match.getAwayTeamName().equals(team2str) || match.getAwayTeamName().equals(team1str))) {
                results.add(match);
            }
        }
        Team team1 = new Team(team1str);
        String team1GreatestWin = null;
        int team1GreatestWinningMargin = 0;
        Team team2 = new Team(team2str);
        String team2GreatestWin = null;
        int team2GreatestWinningMargin = 0;
        for (Match result : results) {
            if (displayResults) 
                System.out.println(result);
            // trigger if team 1 is winning team
            if (result.getWinningTeamName().equals(team1.getName())){
                // increment if team 1 is home team and team 1 win
                if (result.getHomeTeamName().equals(team1.getName())) {
                    team1.incrementHomeWins();
                    team2.incrementAwayLosses();
                }
                // increment if team 2 is home team and team 1 win
                else if (result.getHomeTeamName().equals(team2.getName())) {
                    team1.incrementAwayWins();
                    team2.incrementHomeLosses();
                }
                team1.incrementPercentage(result.getWinningScore(), result.getLosingScore());
                team2.incrementPercentage(result.getLosingScore(), result.getWinningScore());
                if (result.getMargin() > team1GreatestWinningMargin) {
                    team1GreatestWin = String.format("%dpts, %s", result.getMargin(), result.getRound());
                    team1GreatestWinningMargin = result.getMargin();
                }
            }
            // trigger if team 2 is winning team
            else if (result.getWinningTeamName().equals(team2.getName())) {
                // increment if team 1 is home team and team 2 wins
                if (result.getHomeTeamName().equals(team1.getName())) {
                    team1.incrementHomeLosses();
                    team2.incrementAwayWins();
                }
                // increment if team 1 is home team and team 2 win
                else if (result.getHomeTeamName().equals(team2.getName())) {
                    team1.incrementAwayLosses();
                    team2.incrementHomeWins();
                }              
                team1.incrementPercentage(result.getLosingScore(), result.getWinningScore());
                team2.incrementPercentage(result.getWinningScore(), result.getLosingScore());
                if (result.getMargin() > team2GreatestWinningMargin) {
                    team2GreatestWin = String.format("%dpts, %s", result.getMargin(), result.getRound());
                    team2GreatestWinningMargin = result.getMargin();
                }
            }
            else {
                if (result.getHomeTeamName().equals(team1.getName())) {
                    team1.incrementHomeDraws();
                    team2.incrementAwayDraws();
                }
                // increment if team 1 is home team and team 2 win
                else if (result.getHomeTeamName().equals(team2.getName())) {
                    team1.incrementAwayDraws();
                    team2.incrementHomeDraws();
                }
                team1.incrementPercentage(result.getHomeScore(), result.getHomeScore());
                team2.incrementPercentage(result.getHomeScore(), result.getHomeScore());
            }
        }
        if (team1GreatestWin == null) {
            team1GreatestWin = "N/A";
        }
        if (team2GreatestWin == null) {
            team2GreatestWin = "N/A";
        } 
        System.out.println(String.format("|-------------------------------------------HEAD TO HEAD-------------------------------------------|"));
        System.out.println(String.format("| %-45s Name %45s |", team1.getName(), team2.getName()));
        System.out.println(String.format("| %-41d Games Played %41d |", team1.getGamesPlayed(), team2.getGamesPlayed()));
        System.out.println(String.format("| %-45d Wins %45d |", team1.getWins(), team2.getWins()));
        System.out.println(String.format("| %-44d Draws %45d |", team1.getDraws(), team2.getDraws()));
        System.out.println(String.format("| %-40d Points Scored %41d |", team1.getPointsFor(), team2.getPointsFor()));
        System.out.println(String.format("| %-43d Home Wins %42d |", team1.getHomeWins(), team2.getHomeWins()));
        System.out.println(String.format("| %-43d Away Wins %42d |", team1.getAwayWins(), team2.getAwayWins()));
        System.out.println(String.format("| %-41s Greatest Win %41s |", team1GreatestWin, team2GreatestWin));
        System.out.println(String.format("|--------------------------------------------------------------------------------------------------|"));
    }
    public boolean isNumeric(String str) {
        try {
            Double.valueOf(str);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
    public ArrayList<Match> getMatchesByYears(String startYear, String endYear) {
        return getMatchesByYears(startYear, endYear, getAllMatches());
    }
    public ArrayList<Match> getMatchesByYears(String startYear, String endYear, ArrayList<Match> matchSet) {
        startYear = abbreviationToSeason(startYear);
        endYear = abbreviationToSeason(endYear);
        ArrayList<Match> results = new ArrayList<>();
        boolean seasonFlag = false;
        for (Match match : matchSet) {
            if (match.getRound().contains(startYear)) {
                seasonFlag = true;
            }
            if (match.getRound().contains(endYear)) {
                seasonFlag = false;
            }
            if ((match.getRound().contains(endYear) || seasonFlag)) {
                results.add(match);
            }
        }
        return results;
    }
    
    public ArrayList<Match> getMatchesByYear(String year) {
        return getMatchesByYear(year, getAllMatches());
    }

    public ArrayList<Match> getMatchesByYear(String year, ArrayList<Match> inputArray) {
        year = abbreviationToSeason(year);
        ArrayList<Match> results = new ArrayList<>();
        for (Match match : inputArray) {
            if (match.getRound().contains(year)) {
                results.add(match);
            }
        }
        return results;
    }
    public ArrayList<Match> filterMatchesByFinals(boolean includeFinals, boolean includeHomeAway) {
        return filterMatchesByFinals(includeFinals, includeHomeAway, getAllMatches());
    }
    public ArrayList<Match> filterMatchesByFinals(boolean includeFinals, boolean includeHomeAway, ArrayList<Match> inputArray) {
        ArrayList<Match> results = new ArrayList<>();
        for (Match match : inputArray) {
            // checks if match took place in the year being checked
            // then if both includeFinals and includeHomeAway are true
            // OR if match is a final and includeFinals is true
            // OR if match is a home and away game and includeHomeAway is true
            if ((includeFinals && includeHomeAway) || (match.isFinal() && includeFinals) || (!match.isFinal() && includeHomeAway)) {
                results.add(match);
            }
        }
        return results;
    }

    public ArrayList<Match> getMatchesByTeam(String team) {
        return getMatchesByTeam(team, getAllMatches());
    }
    public ArrayList<Match> getMatchesByTeam(String team, ArrayList<Match> inputArray) {
        if (team == null) {
            return inputArray;
        }
        ArrayList<Match> results = new ArrayList<Match>();
        for (Match match : inputArray) {
            if (match.getHomeTeamName().equals(team) || match.getAwayTeamName().equals(team)) {
                results.add(match);
            }
        }
        return results;
    }
    public ArrayList<Match> getWinningMatchesByTeam(String team) {
        return getWinningMatchesByTeam(team, getAllMatches());
    }
    public ArrayList<Match> getWinningMatchesByTeam(String team, ArrayList<Match> inputArray) {
        ArrayList<Match> results = new ArrayList<>();
        for (Match match : inputArray) {
            if (match.getWinningTeamName().equals(team))
                results.add(match);
        }
        return results;
    }
    public ArrayList<Match> getLosingMatchesByTeam(String team) {
        return getLosingMatchesByTeam(team, getAllMatches());
    }
    public ArrayList<Match> getLosingMatchesByTeam(String team, ArrayList<Match> inputArray) {
        ArrayList<Match> results = new ArrayList<>();
        for (Match match : inputArray) {
            if (match.getLosingTeamName().equals(team))
                results.add(match);
        }
        return results;
    }
    public ArrayList<Score> getAllScoresByTeam(String teamName) {
        return getAllScoresByTeam(teamName, getAllScores());
    }
    public ArrayList<Score> getAllScoresByTeam(String teamName, ArrayList<Score> inputArray) {
        ArrayList<Score> results = new ArrayList<Score>();
        for (Score score : inputArray) {
            if (score.getTeam().equals(teamName)) 
                results.add(score);
        }
        return results;
    }
    public ArrayList<Score> getWinningScoresByTeam(String teamName) {
        return getWinningScoresByTeam(teamName, getAllScores());
    }
    public ArrayList<Score> getWinningScoresByTeam(String teamName, ArrayList<Score> inputArray) {
        ArrayList<Score> results = new ArrayList<Score>();
        for (Score score : inputArray) {
            Match match = getMatch(score.getMatchID());
            if (match.getWinningTeamName().equals(teamName)) {
                results.add(score);
            }
        }
        return results;
    }
    public ArrayList<Score> getLosingScoresByTeam(String teamName) {
        return getLosingScoresByTeam(teamName, getAllScores());
    }
    public ArrayList<Score> getLosingScoresByTeam(String teamName, ArrayList<Score> inputArray) {
        ArrayList<Score> results = new ArrayList<Score>();
        for (Score score : inputArray) {
            Match match = getMatch(score.getMatchID());
            if (match.getLosingTeamName().equals(teamName)) {
                results.add(score);
            }
        }
        return results;
    }
    public ArrayList<Score> getScoresByYear(String year) {
        return getScoresByYear(year, getAllScores());
    }
    public ArrayList<Score> getScoresByYear(String year, ArrayList<Score> inputArray) {
        ArrayList<Score> results = new ArrayList<Score>();
        for (Score score : inputArray) {
            Match match = getMatch(score.getMatchID());
            if (match.getRound().contains(year));
                results.add(score);
        }
        return results;
    }
    public ArrayList<Score> getScoresByYears(String firstYear, String lastYear) {
        return getScoresByYears(firstYear, lastYear, getAllScores());
    }
    public ArrayList<Score> getScoresByYears(String firstYear, String lastYear, ArrayList<Score> inputArray) {
        ArrayList<Score> results = new ArrayList<Score>();
        boolean seasonFlag = false;
        for (Score score : inputArray) {
            Match currentMatch = getMatch(score.getMatchID());
            if (currentMatch.getRound().contains(firstYear)) 
                seasonFlag = true;
            else if (currentMatch.getRound().contains(lastYear)) {
                results.add(score);
                seasonFlag = false;
            }
            else if (seasonFlag) {
                results.add(score);
            }
        }
        return results;
    }
    public ArrayList<Score> getWinningScores() {
        return getWinningScores(getAllScores());
    }
    public ArrayList<Score> getWinningScores(ArrayList<Score> inputArray) {
        ArrayList<Score> results = new ArrayList<Score>();
        for (Score score : inputArray) {
            Match match = getMatch(score.getMatchID());
            if (match.getWinningTeamName().equals(score.getTeam()))
                results.add(score);
        }
        return results;
    }
    public ArrayList<Score> getLosingScores() {
        return getLosingScores(getAllScores());
    }
    public ArrayList<Score> getLosingScores(ArrayList<Score> inputArray) {
        ArrayList<Score> results = new ArrayList<Score>();
        for (Score score : inputArray) {
            Match match = getMatch(score.getMatchID());
            if (match.getLosingTeamName().equals(score.getTeam()))
                results.add(score);
        }
        return results;
    }
    public ArrayList<Score> matchListToScores(ArrayList<Match> matches) {
        ArrayList<Score> results = new ArrayList<Score>();
        for (Match match : matches) {
            results.add(getScore(match.getHomeTeamScoreID()));
            results.add(getScore(match.getAwayTeamScoreID()));
        }
        return results;
    }
    public String getPremiers() {
        ArrayList<Match> grandFinalList = new ArrayList<Match>();
        String returnString = "";
        for (Match match : getAllMatches()) {
            if (match.getRound().contains("Grand Final")) {
                grandFinalList.add(match);
            }
        }
        for (Match match : grandFinalList) {
            // this language is fucked bro i cant believe there isnt a better way to do this other than creating whole new classes
            String teamName = match.getWinningTeamName();
            int teamNameIndex = returnString.indexOf(teamName);
            int endOfTeamLine = returnString.indexOf("\n", teamNameIndex);
            String year = match.getSeason();
            if (returnString.contains(match.getWinningTeamName())) {
                // INSERT NEXT PREMIERSHIP AT CORRECT POINT. FIND INDEX OF END OF LINE AND ADD PREMIERSHIP BEFORE IT
                returnString = returnString.substring(0, endOfTeamLine) + ", " + year + returnString.substring(endOfTeamLine, returnString.length());
            }
            else {
                returnString += String.format("%s: %s\n", teamName, year);
            }
        }
        return returnString;
    }

    public Match getMatch(String id) {
        for (Match match : getAllMatches()) {
            if (match.getID().equals(id)) {
                return match;
            }
        }
        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ArrayList<Match> getAllMatches() {
        return (ArrayList)matches.clone();
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ArrayList<Score> getAllScores() {
        return (ArrayList)scores.clone();
    }

    public String stringToRecord(String str) {
        try {
            return switch (str.toLowerCase()) {
                // greatest winning margin abbreviations
                case "greatest margin", "greatest winning margin", "margin", "gm" -> "Greatest Winning Margin";
                // highest team score abbreviations
                case "high score", "highest score", "hs", "score", "top score", "highscore", "topscore", "ts", "highest team score", "hts", "highteamscore", "high team score", "highestteamscore" -> "Highest Team Score";
                // highest combined score abbreviations
                case "hcs", "highest combined score", "high combined score", "top combined score", "tcs" -> "Highest Combined Score";
                // lowest score abbreviations
                case "ls", "lowest score", "low score", "lowscore", "lowestscore", "lowest team score", "low team score", "lts", "lowestteamscore", "lowteamscore" -> "Lowest Team Score";
                // lowest combined score abbreviations
                case "lowest combined score", "lcs", "low combined score", "lowestcombinedscore", "lowcombinedscore" -> "Lowest Combined Score";
                // highest losing score abbreviations
                case "highest losing score", "hls", "highestlosingscore", "highlosingscore", "high losing score" -> "Highest Losing Score";
                // lowest winning score abbreviations
                case "lowest winning score", "lowestwinningscore", "lws", "lowwinningscore", "low winning score" -> "Lowest Winning Score";
                default -> null;
            };
        }
        catch (Exception e) {
            return null;
        }
    }
    public String abbreviationToTeamName(String abbr) {
        try {
            return switch (abbr.toLowerCase()) {
                // adelaide
                case "a", "ad", "ade", "adel", "adelaide" -> "Adelaide";
                // brisbane
                case "b", "br", "bris", "brisbane" -> "Brisbane";
                // carlton
                case "ca", "carl", "carlton" -> "Carlton";
                // collingwood
                case "co", "coll", "collingwood" -> "Collingwood";
                // essendon
                case "e", "es", "ess", "essendon" -> "Essendon";
                // fremantle
                case "f", "fr", "fre", "freo", "fremantle" -> "Fremantle";
                // geelong
                case "ge", "gee", "geelong" -> "Geelong";
                // gold coast
                case "gc", "gcs", "gold coast" -> "Gold Coast";
                // greater western sydney
                case "gws", "greater western sydney" -> "Greater Western Sydney";
                // hawthorn
                case "h", "ha", "haw", "hawthorn" -> "Hawthorn";
                // melbourne
                case "m", "me", "mel", "melb", "melbourne" -> "Melbourne";
                // north melbourne
                case "nm", "n", "north melbourne", "north", "kangaroos" -> "North Melbourne";
                // port adelaide
                case "p", "pa", "port", "port adelaide" -> "Port Adelaide";
                // richmond
                case "r", "ri", "rich", "richmond" -> "Richmond";
                // st kilda
                case "st", "stk", "st kilda" -> "St Kilda";
                // sydney
                case "ss", "syd", "sydney", "sydney swans" -> "Sydney Swans";
                // west coast eagles
                case "wc", "wce", "west coast", "west coast eagles" -> "West Coast Eagles";
                // western bulldogs
                case "footscray", "wb", "western bulldogs" -> "Western Bulldogs";
                default -> null;
            };
        }
        catch (Exception e) {
            return null;
        }
    }
    public boolean isSeason(String str) {
        return switch (str.toLowerCase()) {
            // all seasons return true
            case "s1", "season1", "2017", "s2", "season2", "2018", "s3", "season3", "2019", "s4", "season4", "2020", "s5", "season5", "2021", "2022", "s6", "s7", "season7", "season6", "s8", "season8", "2023", "s9", "season9", "2024", "s10", "season10", "2025" -> true;
            default -> false;
        };
    }
    public String abbreviationToSeason(String abbr) {
        try {
            return switch (abbr.toLowerCase()) {
                // 2017
                case "s1", "season1" -> "2017";
                // 2018
                case "s2", "season2" -> "2018";
                // 2019
                case "s3", "season3" -> "2019";
                // 2020
                case "s4", "season4" -> "2020";
                // 2021
                case "s5", "season5" -> "2021";
                // 2022 season 6
                case "s6", "season6" -> "Season 6";
                // 2022 season 7
                case "s7", "season7" -> "Season 7";
                // 2023
                case "s8", "season8" -> "2023";
                // 2024
                case "s9", "season9" -> "2024";
                // 2025
                case "s10", "season10" -> "2025";
                default -> abbr;
            };
        }
        catch (Exception e) {
            return null;
        }
    }
}

class GreatestMarginComparator implements Comparator<Match> {
    @Override
    public int compare(Match a, Match b) {
        return Integer.compare(b.getMargin(), a.getMargin());
    }
}
class HighSingleTeamScoreComparator implements Comparator<Match> {
    @Override
    public int compare(Match a, Match b) {
        return Integer.compare(b.getWinningScore(), a.getWinningScore());
    }
}
class HighCombinedScoreComparator implements Comparator<Match> {
    @Override
    public int compare(Match a, Match b) {
        return Integer.compare(b.getAwayScore() + b.getHomeScore(), a.getAwayScore() + a.getHomeScore());
    }
}
class HighestLosingScoreComparator implements Comparator<Match> {
    @Override
    public int compare(Match a, Match b) {
        return Integer.compare(b.getLosingScore(), a.getLosingScore());
    }
}

class LowestWinningScoreComparator implements Comparator<Match> {
    @Override
    public int compare(Match a, Match b) {
        return Integer.compare(a.getWinningScore(), b.getWinningScore());
    }
}

class LowestTeamScoreComparator implements Comparator<Match> {
    @Override
    public int compare(Match a, Match b) {
        return Integer.compare(a.getLosingScore(), b.getLosingScore());
    }
}

class LowestCombinedScoreComparator implements Comparator<Match> {
    @Override
    public int compare (Match a, Match b) {
        return Integer.compare(a.getAwayScore() + a.getHomeScore(), b.getAwayScore() + b.getHomeScore());
    }
}