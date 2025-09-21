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
        this.matches = new ArrayList<Match>();
        this.scores = new ArrayList<Score>();
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
    public void saveMatches() {
        try {
            File matchFile = new File("matches.txt");
            FileWriter matchWriter = new FileWriter(matchFile);
            for (Match match : matches) {
                matchWriter.write(String.format("%s; %s; %s; %s;\n", match.getID(), match.getRound(), match.getHomeTeamScoreID(), match.getAwayTeamScoreID()));
            }
            matchWriter.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
    // save scores to file 
    public void saveScores() {
        try {
            File scoresFile = new File("scores.txt");
            FileWriter scoreWriter = new FileWriter(scoresFile);
            for (Score score : scores) {
                scoreWriter.write(String.format("%s; %s; %s; %d; %d;\n", score.getID(), score.getMatchID(), score.getTeam(), score.getGoals(), score.getBehinds()));
            }
            scoreWriter.close();
        }
        
        catch (Exception e) {
            System.out.println(e);
        }
    }
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
        catch (Exception e) {
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
        catch (Exception e) {
            System.out.println(e);
        }

    }

    // RECORDS

    // function displays all records using comparator input
    // TODO: function only displays records where target team wins, must use record input to check if a win or a loss is necessary
    // TODO: tie tracker no longer works
    // TODO: must code a "getDifferential" method to determine how ties should be handled

    public void findRecord(String teamName, Comparator<Match> compareBy, int resultCount, String startSeason, String endSeason) {
        ArrayList<Match> matchSet;
        if (startSeason == null) {
            matchSet = getMatchesByTeam(teamName);
        }
        else if (endSeason == null) {
            matchSet = getMatchesByTeam(teamName, getMatchesByYear(startSeason));
        }
        else {
            matchSet = getMatchesByTeam(teamName, getMatchesByYears(startSeason, endSeason));
        }
        
        // tieTracker keeps track of what the record of the previously checked match was, to see if it is tied with the current match to be analysed
        // if it is tied, tieRank will not be updated
        // e.g two matches with the same winning margin should be displayed as:
        //  1: Fremantle 2.2 (14) def. by North Melbourne 18.6 (114). North Melbourne won by 100 points.
        //  1: Adelaide 15.12 (108) def. Carlton 1.2 (8). Adelaide won by 100 points.
        int tieTracker = 0;
        int tieRank = 0;

        // array sorted using byMargin comparator, note 0 - matchCount range is used as everything after matchCount is a null value
        matchSet.sort(compareBy);
        // for loop conditions allow loop to continue even after i = 5 to get through extra tied 5th place records, ensures that loop ends at some stage
        int wrongResults = 0;
        for (int i = 0; (i < resultCount || tieTracker == matchSet.get(i + wrongResults).getMargin()) && i + wrongResults < matchSet.size(); i++) {
            // code below allows records to only be shown in the case where the selected team won the match
            /* if (!matchSet.get(i + wrongResults).getWinningTeamName().equals(teamName) && teamName != null) {
                wrongResults++;
                i--;
                continue;
            }*/
            // same as above but if team loses
            /*if (!matchSet.get(i + wrongResults).getLosingTeamName().equals(teamName) && teamName != null) {
                wrongResults++;
                i--;
                continue;
            }*/
            if (tieTracker != matchSet.get(i + wrongResults).getMargin())
                tieRank = i + 1;
            System.out.println(String.format("%5d: %s. %s won by %d points.", tieRank, matchSet.get(i + wrongResults), matchSet.get(i + wrongResults).getWinningTeamName(), matchSet.get(i + wrongResults).getMargin()));
            tieTracker = matchSet.get(i + wrongResults).getMargin();
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
            Double.parseDouble(str);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    public ArrayList<Match> getMatchesByYears(String startYear, String endYear) {
        startYear = abbreviationToSeason(startYear);
        endYear = abbreviationToSeason(endYear);
        ArrayList<Match> results = new ArrayList<Match>();
        boolean seasonFlag = false;
        for (Match match : matches) {
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
    public ArrayList<Match> getMatchesByYears(String startYear, String endYear, boolean includeFinals, boolean includeHomeAway) {
        startYear = abbreviationToSeason(startYear);
        endYear = abbreviationToSeason(endYear);
        ArrayList<Match> results = new ArrayList<Match>();
        boolean seasonFlag = false;
        for (Match match : matches) {
            if (match.getRound().contains(startYear)) {
                seasonFlag = true;
            }
            if (match.getRound().contains(endYear)) {
                seasonFlag = false;
            }
            // if statement checks if the match is within the desired timeframe
            // then checks if includeFinals and includeHomeAway are both true
            // OR checks if match isnt a final and includeHomeAway is true
            // OR checks if match is a final and includeFinals is true
            
            if ((match.getRound().contains(endYear) || seasonFlag) && ((includeFinals && includeHomeAway) || (!match.isFinal() && includeHomeAway) || (match.isFinal() && includeFinals))) {
                results.add(match);
            }
            /*else if ((match.getRound().contains(endYear) || seasonFlag) && !match.isFinal() && includeHomeAway) {
                results.add(match);
            }
            else if ((match.getRound().contains(endYear) || seasonFlag) && match.isFinal() && includeFinals) {
                results.add(match);
            }*/
        }
    return results;
    }
    public ArrayList<Match> getMatchesByYear(String year) {
        year = abbreviationToSeason(year);
        ArrayList<Match> results = new ArrayList<Match>();
        for (Match match : matches) {
            if (match.getRound().contains(year)) {
                results.add(match);
            }
        }
        return results;
    }

    public ArrayList<Match> getMatchesByYear(String year, ArrayList<Match> inputArray) {
        year = abbreviationToSeason(year);
        ArrayList<Match> results = new ArrayList<Match>();
        for (Match match : inputArray) {
            if (match.getRound().contains(year)) {
                results.add(match);
            }
        }
        return results;
    }
    public ArrayList<Match> getMatchesByYear(String year, boolean includeFinals, boolean includeHomeAway) {
        year = abbreviationToSeason(year);
        ArrayList<Match> results = new ArrayList<Match>();
        for (Match match : matches) {
            // checks if match took place in the year being checked
            // then if both includeFinals and includeHomeAway are true
            // OR if match is a final and includeFinals is true
            // OR if match is a home and away game and includeHomeAway is true
            if (match.getRound().contains(year) && ((includeFinals && includeHomeAway) || (match.isFinal() && includeFinals) || (!match.isFinal() && includeHomeAway))) {
                results.add(match);
            }
        }
        return results;
    }
     

    public ArrayList<Match> getMatchesByTeam(String team) {
        if (team == null) {
            return getAllMatches();
        }
        ArrayList<Match> results = new ArrayList<Match>();
        for (Match match : matches) {
            if (match.getHomeTeamName().equals(team) || match.getAwayTeamName().equals(team)) {
                results.add(match);
            }
        }
        return results;
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
        for (Match match : matches) {
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

    public Comparator<Match> stringToRecordComparator(String str) {
        switch (str.toLowerCase()) {
            // greatest winning margin abbreviations
            case "greatest margin":
            case "greatest winning margin":
            case "margin":
            case "gm":
                return new MarginComparator();
            // highest team score abbreviations
            case "high score":
            case "highest score":
            case "hs":
            case "score":
            case "top score":
            case "highscore":
            case "topscore":
            case "ts":
            case "highest team score":
            case "hts":
            case "highteamscore":
            case "high team score":
            case "highestteamscore":
                return new HighSingleTeamScoreComparator();
            // highest combined score abbreviations
            case "hcs":
            case "highest combined score":
            case "high combined score":
            case "top combined score":
            case "tcs":
                return new HighCombinedScoreComparator();
            // lowest score abbreviations
            case "ls":
            case "lowest score":
            case "low score":
            case "lowscore":
            case "lowestscore":
            case "lowest team score":
            case "low team score":
            case "lts":
            case "lowestteamscore":
            case "lowteamscore":
                return new LowestTeamScoreComparator();
            // lowest combined score abbreviations
            case "lowest combined score":
            case "lcs":
            case "low combined score":
            case "lowestcombinedscore":
            case "lowcombinedscore":
                return new LowestCombinedScoreComparator();
            // highest losing score abbreviations
            case "highest losing score":
            case "hls":
            case "highestlosingscore":
            case "highlosingscore":
            case "high losing score":
                return new HighestLosingScoreComparator();
            // lowest winning score abbreviations
            case "lowest winning score":
            case "lowestwinningscore":
            case "lws":
            case "lowwinningscore":
            case "low winning score":
                return new LowestWinningScoreComparator();

            default:
                return null;

        }
    }

    public String abbreviationToName(String abbr) {
        try {
            switch (abbr.toLowerCase()) {
                case "a":
                case "ad":
                case "ade":
                case "adel":
                case "adelaide":
                    return "Adelaide";
                case "b":
                case "br":
                case "bris":
                case "brisbane":
                    return "Brisbane";
                case "ca":
                case "carl":
                case "carlton":
                    return "Carlton";
                case "co":
                case "coll":
                case "collingwood":
                    return "Collingwood";
                case "e":
                case "es":
                case "ess":
                case "essendon":
                    return "Essendon";
                case "f":
                case "fr":
                case "fre":
                case "freo":
                case "fremantle":
                    return "Fremantle";
                case "ge":
                case "gee":
                case "geelong":
                    return "Geelong";
                case "gc":
                case "gold coast":
                    return "Gold Coast";
                case "gws":
                case "greater western sydney":
                    return "Greater Western Sydney";
                case "h":
                case "ha":
                case "haw":
                case "hawthorn":
                    return "Hawthorn";
                case "m":
                case "me":
                case "mel":
                case "melb":
                case "melbourne":
                    return "Melbourne";
                case "nm":
                case "n":
                case "north melbourne":
                case "north":
                case "kangaroos":
                    return "North Melbourne";
                case "p":
                case "pa":
                case "port":
                case "port adelaide":
                    return "Port Adelaide";
                case "r":
                case "ri":
                case "rich":
                case "richmond":
                    return "Richmond";
                case "st":
                case "stk":
                case "st kilda":
                    return "St Kilda";
                case "ss":
                case "syd":
                case "sydney":
                case "sydney swans":
                    return "Sydney Swans";
                case "wc":
                case "wce":
                case "west coast":
                case "west coast eagles":
                    return "West Coast Eagles";
                case "footscray":
                case "wb":
                case "western bulldogs":
                    return "Western Bulldogs";
                default:
                    return null;
            }
        }
        catch (Exception e) {
            return null;
        }
    }
    public boolean isSeason(String str) {

        switch (str.toLowerCase()) {
            case "s1":
            case "season1":
            case "2017":
            case "s2":
            case "season2":
            case "2018":
            case "s3":
            case "season3":
            case "2019":
            case "s4":
            case "season4":
            case "2020": 
            case "s5":
            case "season5":
            case "2021":
            case "2022":
            case "s6":
            case "s7":
            case "season7":
            case "season6":
            case "s8":
            case "season8":
            case "2023":
            case "s9":
            case "season9":
            case "2024":
            case "s10":
            case "season10":
            case "2025":
                return true;
            default:
                return false;
        }
    }
    public String abbreviationToSeason(String abbr) {
        switch (abbr.toLowerCase()) {
            case "s1":
            case "season1":
                return "2017";
            case "s2":
            case "season2":
                return "2018";
            case "s3":
            case "season3":
                return "2019";
            case "s4":
            case "season4":
                return "2020";
            case "s5":
            case "season5":
                return "2021";
            case "s6":
            case "season6":
                return "Season 6";
            case "s7":
            case "season7":
                return "Season 7";
            case "s8":
            case "season8":
                return "2023";
            case "s9":
            case "season9":
                return "2024";
            case "s10":
            case "season10":
                return "2025";
            default:
                return abbr;
        }
    }
}

class MarginComparator implements Comparator<Match> {
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