import java.io.*;
import java.util.*;


// IMPORTANT: NEVER EVER SORT THE MATSTER MATCHES DATASET. ALWAYS CLONE AND SORT. NO WAY TO RE-SORT INTO CHRONOLOGICAL ORDER
// Lucy Beattie (oddconverse) 2025. All use legal. Free Palestine.

// TODO
// - lowest winning/highest losing score records do not account for draws
// - if multiple records broken in same game, they will not appear

public class RecordSystem {
    // matches to be stored in array. DO NOT SORT. CLONE AND SORT AFTER
    private Match[] matches;
    private int matchCount;

    public RecordSystem() {
        this.matches = new Match[1000];
        this.matchCount = 0;
    }
    // add match to array
    public void addMatch(String round, String homeTeamName, int homeTeamGoals, int homeTeamBehinds, String awayTeamName, int awayTeamGoals, int awayTeamBehinds) {
        matches[matchCount] = new Match(round, homeTeamName, homeTeamGoals, homeTeamBehinds, awayTeamName, awayTeamGoals, awayTeamBehinds);
        matchCount++;
    }
    // save matches to text file to open later
    public void saveMatches() {
        try {
            File matchFile = new File("data.txt");
            FileWriter matchWriter = new FileWriter(matchFile);
            for (int i = 0; i < matchCount; i++) {
                Match currentMatch = matches[i];
                matchWriter.write(String.format("%s; %s; %d; %d; %s; %d; %d\n", currentMatch.getRound(), currentMatch.getHomeTeamName(), currentMatch.getHomeTeamGoals(), currentMatch.getHomeTeamBehinds(), currentMatch.getAwayTeamName(), currentMatch.getAwayTeamGoals(), currentMatch.getAwayTeamBehinds()));
            }
            matchWriter.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void loadMatches() {
        try {
            File matchFile = new File("data.txt");
            Scanner matchInfile = new Scanner(matchFile);
            while (matchInfile.hasNextLine()) {
                String s = matchInfile.nextLine();
                StringTokenizer stk = new StringTokenizer(s, ";");
                String round = stk.nextToken().trim();
                String homeTeamName = stk.nextToken().trim();
                int homeTeamGoals = Integer.parseInt(stk.nextToken().trim());
                int homeTeamBehinds = Integer.parseInt(stk.nextToken().trim());
                String awayTeamName = stk.nextToken().trim();
                int awayTeamGoals = Integer.parseInt(stk.nextToken().trim());
                int awayTeamBehinds = Integer.parseInt(stk.nextToken().trim());
                addMatch(round, homeTeamName, homeTeamGoals, homeTeamBehinds, awayTeamName, awayTeamGoals, awayTeamBehinds);
            }
            matchInfile.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }

    }

    // RECORDS

    // returns 5 greatest winning margins in history
    public void findGreatestMargins() {
        // clone match array as to not sort original array
        Match[] matchesClone = matches.clone();

        //create comparator to sort by margin (comparator code at bottom of document)
        MarginComparator byMargin = new MarginComparator();

        // marginTieTracker keeps track of what the winning margin of the previous match was, to see if it is tied with the current match to be analysed
        // if it is tied, tieRank will not be updated
        // e.g two matches with the same winning margin should be displayed as:
        //  1: Fremantle 2.2 (14) def. by North Melbourne 18.6 (114). North Melbourne won by 100 points.
        //  1: Adelaide 15.12 (108) def. Carlton 1.2 (8). Adelaide won by 100 points.
        int marginTieTracker = 0;
        int tieRank = 0;

        // uncomment below code to see history of the record
        /*int greatestMargin = 0;
        Match matchGreatestMargin = null;
        System.out.println("History of Greatest Winning Margin");
        for (int i = 0; i < matchCount; i++) {
            if (matches[i].getMargin() > greatestMargin) {
                matchGreatestMargin = matches[i];
                greatestMargin = matchGreatestMargin.getMargin();
                System.out.println(String.format("%s. %s won by %d points.", matchGreatestMargin, matchGreatestMargin.getWinningTeamName(), greatestMargin));
            }
        }*/

        // array sorted using byMargin comparator, note 0 - matchCount range is used as everything after matchCount is a null value
        Arrays.sort(matchesClone, 0, matchCount, byMargin);
        // for loop conditions allow loop to continue even after i = 5 to get through extra tied 5th place records
        for (int i = 0; i < 5 || marginTieTracker == matchesClone[i].getMargin(); i++) {
            if (marginTieTracker != matchesClone[i].getMargin())
                tieRank = i + 1;
            System.out.println(String.format("%5d: %s. %s won by %d points.", tieRank, matchesClone[i], matchesClone[i].getWinningTeamName(), matchesClone[i].getMargin()));
            marginTieTracker = matchesClone[i].getMargin();
        }
    }

    // all of the following records work on same principals as the above record, use above comments for reference

    public void findHighestTeamScore() {
        Match[] matchesClone = matches.clone();
        int highestTeamScore = 0;
        int tieRank = 0;
        HighSingleTeamScoreComparator byTeamScore = new HighSingleTeamScoreComparator();
        Arrays.sort(matchesClone, 0, matchCount, byTeamScore);
        for (int i = 0; i < 5 || highestTeamScore == matchesClone[i].getWinningScore(); i++) {
            if (highestTeamScore != matchesClone[i].getWinningScore())
                tieRank = i + 1;
            System.out.println(String.format("%5d: %s", tieRank, matchesClone[i]));
            highestTeamScore = matchesClone[i].getWinningScore();
        }
    }

    public void findHighestCombinedScore() {
        Match[] matchesClone = matches.clone();
        int highestCombinedScore = 0;
        int tieRank = 0;
        HighCombinedScoreComparator byCombinedScore = new HighCombinedScoreComparator();
        Arrays.sort(matchesClone, 0, matchCount, byCombinedScore);
        for (int i = 0; i < 5 || highestCombinedScore == matchesClone[i].getCombinedScore(); i++) {
            if (highestCombinedScore != matchesClone[i].getCombinedScore())
                tieRank = i + 1;
            System.out.println(String.format("%5d: %s. Combined score: %d", tieRank, matchesClone[i], matchesClone[i].getAwayScore() + matchesClone[i].getHomeScore()));
            highestCombinedScore = matchesClone[i].getCombinedScore();
        }
    }

    public void findHighestLosingScore() {
        Match[] matchesClone = matches.clone();
        int highestLosingScore = 0;
        int tieRank = 0;
        // see findLowestWinningScore() for explanation
        int skips = 0;
        HighestLosingScoreComparator byHighestLosingScore = new HighestLosingScoreComparator();
        Arrays.sort(matchesClone, 0, matchCount, byHighestLosingScore);
        for (int i = 0; i < 5 || matchesClone[i + skips].getLosingScore() == highestLosingScore; i++) {
            if (matchesClone[i + skips].wasDrawn()) {
                skips++;
                i--;
                continue;
            }
            if (highestLosingScore != matchesClone[i + skips].getLosingScore())
                tieRank = i + 1;
            System.out.println(String.format("%5d: %s", tieRank, matchesClone[i + skips]));
            highestLosingScore = matchesClone[i + skips].getLosingScore();
        }
    }

    public void findLowestWinningScore() {
        Match[] matchesClone = matches.clone();
        int lowestWinningScore = 0;
        int tieRank = 0;
        // skips accounts for draws and skips them
        int skips = 0;
        LowestWinningScoreComparator byLowestWinningScore = new LowestWinningScoreComparator();
        Arrays.sort(matchesClone, 0, matchCount, byLowestWinningScore);
        for (int i = 0; i < 5 || lowestWinningScore == matchesClone[i + skips].getWinningScore(); i++) {
            // if a match is drawn, skips is incremented and loop completed again (so final loop is reached)
            if (matchesClone[i + skips].wasDrawn()) {
                skips++;
                i--;
                continue; 
            }
            if (lowestWinningScore != matchesClone[i + skips].getWinningScore())
                tieRank = i + 1;
            System.out.println(String.format("%5d: %s", tieRank, matchesClone[i + skips]));
            lowestWinningScore = matchesClone[i + skips].getWinningScore();
        }
    }

    public void findLowestTeamScore() {
        Match[] matchesClone = matches.clone();
        int lowestTeamScore = 0;
        int tieRank = 0;
        LowestTeamScoreComparator byLowestTeamScore = new LowestTeamScoreComparator();
        Arrays.sort(matchesClone, 0, matchCount, byLowestTeamScore);
        for (int i = 0; i < 5 || lowestTeamScore == matchesClone[i].getLosingScore(); i++) {
            if (lowestTeamScore != matchesClone[i].getLosingScore())
                tieRank = i + 1;
            System.out.println(String.format("%5d: %s", tieRank, matchesClone[i]));
            lowestTeamScore = matchesClone[i].getLosingScore();
        }
    }

    public void findLowestCombinedScore() {
        Match[] matchesClone = matches.clone();
        int lowestCombinedScore = 0;
        int tieRank = 0;
        LowestCombinedScoreComparator byLowestCombinedScore = new LowestCombinedScoreComparator();
        Arrays.sort(matchesClone, 0, matchCount, byLowestCombinedScore);
        for (int i = 0; i < 5 || lowestCombinedScore == matchesClone[i].getCombinedScore(); i++) {
            if (lowestCombinedScore != matchesClone[i].getCombinedScore())
                tieRank = i + 1;
            System.out.println(String.format("%5d: %s. Combined score: %d", tieRank, matchesClone[i], matchesClone[i].getCombinedScore()));
            lowestCombinedScore = matchesClone[i].getAwayScore() + matchesClone[i].getHomeScore();
        }
    }
    public void createLadder() {
        Ladder ladder = new Ladder();
        for (int i = 0; i < matchCount; i++) {
            // instantiate team objects when a new team appears
            if (!ladder.doesTeamExist(matches[i].getHomeTeamName())) {
                ladder.addTeam(matches[i].getHomeTeamName());
            }
            // adjust stats for home team
            Team currentHomeTeam = ladder.getTeam(matches[i].getHomeTeamName());
            currentHomeTeam.incrementPercentage(matches[i].getHomeScore(), matches[i].getAwayScore());
            if (matches[i].homeTeamWin()) {
                currentHomeTeam.incrementHomeWins();
            }
            else if (matches[i].wasDrawn()) {
                currentHomeTeam.incrementHomeDraws();
            }
            else {
                currentHomeTeam.incrementHomeLosses();
            }
            // same function but for away team
            if (!ladder.doesTeamExist(matches[i].getAwayTeamName())) {
                ladder.addTeam(matches[i].getAwayTeamName());
            }
            Team currentAwayTeam = ladder.getTeam(matches[i].getAwayTeamName());
            currentAwayTeam.incrementPercentage(matches[i].getAwayScore(), matches[i].getHomeScore());
            if (matches[i].homeTeamWin()) {
                currentAwayTeam.incrementAwayLosses();
            }
            else if (matches[i].wasDrawn()) {
                currentAwayTeam.incrementAwayDraws();
            }
            else {
                currentAwayTeam.incrementAwayWins();
            }
        }
        ladder.display();
    }
    
    public void displayMatchesByRound(String round, String year) {
        Match[] results = new Match[15];
        int resultsCount = 0;
        for (int i = 0; i < matchCount; i++) {
            if (isNumeric(round) && matches[i].getRound().equals(String.format("Round %s, %s", round, year)) || matches[i].getRound().equals(String.format("Week %s, %s", round, year))) {
                results[resultsCount] = matches[i];
                resultsCount++;
            }
            else if (matches[i].getRound().equals(String.format("%s, %s", round, year))) {
                results[resultsCount] = matches[i];
                resultsCount++;
            }
        }
        for (int i = 0; i < resultsCount; i++) {
            System.out.println(results[i]);
        }
    }

    public void displayMatchesByTeam(String team) {
        Match[] results = new Match[100];
        int resultsCount = 0;
        for (int i = 0; i < matchCount; i++) {
            if (matches[i].getHomeTeamName().equals(team) || matches[i].getAwayTeamName().equals(team)) {
                results[resultsCount] = matches[i];
                resultsCount++;
            }

        }
        for (int i = 0; i < resultsCount; i++) {
            System.out.println(results[i]);
        }
    }

    public void displayHeadToHead(String team1str, String team2str) {
        // TODO: It works but its so hard to read. Just make it less shit. Also add more home/away functions and stuff. Margin works.
        Match[] results = new Match[20];
        int resultCount = 0;
        for (int i = 0; i < matchCount; i++) {
            if ((matches[i].getHomeTeamName().equals(team1str) || matches[i].getHomeTeamName().equals(team2str)) && (matches[i].getAwayTeamName().equals(team2str) || matches[i].getAwayTeamName().equals(team1str))) {
                results[resultCount] = matches[i];
                resultCount++;
            }
        }
        Team team1 = new Team(team1str);
        Match team1GreatestWin = null;
        int team1GreatestWinningMargin = 0;
        Team team2 = new Team(team2str);
        Match team2GreatestWin = null;
        int team2GreatestWinningMargin = 0;
        for (int i = 0; i < resultCount; i++) {
            System.out.println(results[i]);
            // trigger if team 1 is winning team
            if (results[i].getWinningTeamName().equals(team1.getName())){
                // increment if team 1 is home team and team 1 win
                if (results[i].getHomeTeamName().equals(team1.getName())) {
                    team1.incrementHomeWins();
                    team2.incrementAwayLosses();
                }
                // increment if team 2 is home team and team 1 win
                else if (results[i].getHomeTeamName().equals(team2.getName())) {
                    team1.incrementAwayWins();
                    team2.incrementHomeLosses();
                }
                team1.incrementPercentage(results[i].getWinningScore(), results[i].getLosingScore());
                team2.incrementPercentage(results[i].getLosingScore(), results[i].getWinningScore());
                if (results[i].getMargin() > team1GreatestWinningMargin) {
                    team1GreatestWin = results[i];
                    team1GreatestWinningMargin = results[i].getMargin();
                }
            }
            // trigger if team 2 is winning team
            else if (results[i].getWinningTeamName().equals(team2.getName())) {
                // increment if team 1 is home team and team 2 wins
                if (results[i].getHomeTeamName().equals(team1.getName())) {
                    team1.incrementHomeLosses();
                    team2.incrementAwayWins();
                }
                // increment if team 1 is home team and team 2 win
                else if (results[i].getHomeTeamName().equals(team2.getName())) {
                    team1.incrementAwayLosses();
                    team2.incrementHomeWins();
                }              
                team1.incrementPercentage(results[i].getLosingScore(), results[i].getWinningScore());
                team2.incrementPercentage(results[i].getWinningScore(), results[i].getLosingScore());
                if (results[i].getMargin() > team2GreatestWinningMargin) {
                    team2GreatestWin = results[i];
                    team2GreatestWinningMargin = results[i].getMargin();
                }
            }
            else {
                if (results[i].getHomeTeamName().equals(team1.getName())) {
                    team1.incrementHomeDraws();
                    team2.incrementAwayDraws();
                }
                // increment if team 1 is home team and team 2 win
                else if (results[i].getHomeTeamName().equals(team2.getName())) {
                    team1.incrementAwayDraws();
                    team2.incrementHomeDraws();
                }
                team1.incrementPercentage(results[i].getHomeScore(), results[i].getHomeScore());
                team2.incrementPercentage(results[i].getHomeScore(), results[i].getHomeScore());
            }
        }
        System.out.println(String.format("|----------------------------HEAD TO HEAD----------------------------|"));
        System.out.println(String.format("| %-30s Name %30s |", team1.getName(), team2.getName()));
        System.out.println(String.format("| %-26d Games Played %26d |", team1.getGamesPlayed(), team2.getGamesPlayed()));
        System.out.println(String.format("| %-30d Wins %30d |", team1.getWins(), team2.getWins()));
        System.out.println(String.format("| %-29d Draws %30d |", team1.getDraws(), team2.getDraws()));
        System.out.println(String.format("| %-25d Points Scored %26d |", team1.getPointsFor(), team2.getPointsFor()));
        System.out.println(String.format("| %-28d Home Wins %27d |", team1.getHomeWins(), team2.getHomeWins()));
        System.out.println(String.format("| %-28d Away Wins %27d |", team1.getAwayWins(), team2.getAwayWins()));
        System.out.println(String.format("| %-26s Greatest Win %26s |", String.format("%dpts, %s", team1GreatestWinningMargin, team1GreatestWin.getRound()), String.format("%dpts, %s", team2GreatestWinningMargin, team2GreatestWin.getRound())));
        System.out.println(String.format("|--------------------------------------------------------------------|"));
    }
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    public Match[] getMatches(String startYear, String endYear) {
        return null;
    }
    public Match[] getMatches(String year) {
        return null;
    }
    public Match[] getMatches(String startYear, String endYear, String startRound) {
        return null;
    }
    public Match[] getMatches(String startYear, String endYear, String startRound, String endRound) {
        return null;
    }
    public Match[] getMatchesByTeam(String team) {
        return null;
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