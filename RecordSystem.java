import java.io.*;
import java.util.*;


// IMPORTANT: NEVER EVER SORT THE MATSTER MATCHES DATASET. ALWAYS CLONE AND SORT. NO WAY TO RE-SORT INTO CHRONOLOGICAL ORDER
// Lucy Beattie (oddconverse) 2025. All use legal. Free Palestine.

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

    public void findGreatestMargins() {
        int greatestMargin = 0;
        Match matchGreatestMargin = null;
        Match[] matchesClone = matches.clone();
        MarginComparator byMargin = new MarginComparator();
        // uncomment below code to see history of the record
        /* System.out.println("History of Greatest Winning Margin");
        for (int i = 0; i < matchCount; i++) {
            if (matches[i].getMargin() > greatestMargin) {
                matchGreatestMargin = matches[i];
                greatestMargin = matchGreatestMargin.getMargin();
                System.out.println(String.format("%s. %s won by %d points.", matchGreatestMargin, matchGreatestMargin.getWinningTeam(), greatestMargin));
            }
        }*/

        Arrays.sort(matchesClone, 0, matchCount, byMargin);
        for (int i = 0; i < 5; i++) {
            System.out.println(String.format("%s. %s won by %d points.", matchesClone[i], matchesClone[i].getWinningTeam(), matchesClone[i].getMargin()));
        }
    }

    public void findHighestTeamScore() {
        Match[] matchesClone = matches.clone();
        HighSingleTeamScoreComparator byTeamScore = new HighSingleTeamScoreComparator();
        Arrays.sort(matchesClone, 0, matchCount, byTeamScore);
        for (int i = 0; i < 5; i++) {
            System.out.println(matchesClone[i]);
        }
    }

    public void findHighestCombinedScore() {
        Match[] matchesClone = matches.clone();
        HighCombinedScoreComparator byCombinedScore = new HighCombinedScoreComparator();
        Arrays.sort(matchesClone, 0, matchCount, byCombinedScore);
        for (int i = 0; i < 5; i++) {
            System.out.println(String.format("%s. Combined score: %d", matchesClone[i], matchesClone[i].getAwayScore() + matchesClone[i].getHomeScore()));
        }
    }

    public void findHighestLosingScore() {
        Match[] matchesClone = matches.clone();
        HighestLosingScoreComparator byHighestLosingScore = new HighestLosingScoreComparator();
        Arrays.sort(matchesClone, 0, matchCount, byHighestLosingScore);
        for (int i = 0; i < 5; i++) {
            System.out.println(matchesClone[i]);
        }
    }
    // null moved to top of sort, needs fix
    public void findLowestWinningScore() {
        Match[] matchesClone = matches.clone();
        LowestWinningScoreComparator byLowestWinningScore = new LowestWinningScoreComparator();
        Arrays.sort(matchesClone, 0, matchCount, byLowestWinningScore);
        for (int i = 0; i < 5; i++) {
            System.out.println(matchesClone[i]);
        }
    }

    public void findLowestTeamScore() {
        Match[] matchesClone = matches.clone();
        LowestTeamScoreComparator byLowestTeamScore = new LowestTeamScoreComparator();
        Arrays.sort(matchesClone, 0, matchCount, byLowestTeamScore);
        for (int i = 0; i < 5; i++) {
            System.out.println(matchesClone[i]);
        }
    }

    public void findLowestCombinedScore() {
        Match[] matchesClone = matches.clone();
        LowestCombinedScoreComparator byLowestCombinedScore = new LowestCombinedScoreComparator();
        Arrays.sort(matchesClone, 0, matchCount, byLowestCombinedScore);
        for (int i = 0; i < 5; i++) {
            System.out.println(String.format("%s. Combined score: %d", matchesClone[i], matchesClone[i].getHomeScore() + matchesClone[i].getAwayScore()));
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
                currentHomeTeam.incrementWin();
            }
            else if (matches[i].wasDrawn()) {
                currentHomeTeam.incrementDraws();
            }
            else {
                currentHomeTeam.incrementLosses();
            }
            // same function but for away team
            if (!ladder.doesTeamExist(matches[i].getAwayTeamName())) {
                ladder.addTeam(matches[i].getAwayTeamName());
            }
            Team currentAwayTeam = ladder.getTeam(matches[i].getAwayTeamName());
            currentAwayTeam.incrementPercentage(matches[i].getAwayScore(), matches[i].getHomeScore());
            if (matches[i].homeTeamWin()) {
                currentAwayTeam.incrementLosses();
            }
            else if (matches[i].wasDrawn()) {
                currentAwayTeam.incrementDraws();
            }
            else {
                currentAwayTeam.incrementWin();
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

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        }
        catch (Exception e) {
            return false;
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