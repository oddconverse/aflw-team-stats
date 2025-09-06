import java.io.*;
import java.util.*;


// IMPORTANT: NEVER EVER SORT THE MATSTER MATCHES DATASET. ALWAYS CLONE AND SORT. NO WAY TO RE-SORT INTO CHRONOLOGICAL ORDER
// Lucy Beattie (oddconverse) 2025. All use legal. Free Palestine.

// TODO
// - lowest winning/highest losing score records do not account for draws
// - if multiple records broken in same game, they will not appear

public class RecordSystem {
    // matches to be stored in array. DO NOT SORT. CLONE AND SORT AFTER
    private ArrayList<Match> matches;
    private int matchCount;

    public RecordSystem() {
        this.matches = new ArrayList<Match>();
        this.matchCount = 0;
    }
    // add match to array
    public void addMatch(String round, String homeTeamName, int homeTeamGoals, int homeTeamBehinds, String awayTeamName, int awayTeamGoals, int awayTeamBehinds) {
        matches.add(new Match(round, homeTeamName, homeTeamGoals, homeTeamBehinds, awayTeamName, awayTeamGoals, awayTeamBehinds));
        matchCount++;
    }
    // save matches to text file to open later
    public void saveMatches() {
        try {
            File matchFile = new File("data.txt");
            FileWriter matchWriter = new FileWriter(matchFile);
            for (Match match : matches) {
                Match currentMatch = match;
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
        ArrayList<Match> matchesClone = (ArrayList)matches.clone();

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
        matchesClone.sort(byMargin);
        // for loop conditions allow loop to continue even after i = 5 to get through extra tied 5th place records
        for (int i = 0; i < 5 || marginTieTracker == matchesClone.get(i + tieRank).getMargin(); i++) {
            if (marginTieTracker != matchesClone.get(i).getMargin())
                tieRank = i + 1;
            System.out.println(String.format("%5d: %s. %s won by %d points.", tieRank, matchesClone.get(i), matchesClone.get(i).getWinningTeamName(), matchesClone.get(i).getMargin()));
            marginTieTracker = matchesClone.get(i).getMargin();
        }
    }

    // all of the following records work on same principals as the above record, use above comments for reference

    public void findHighestTeamScore() {
        ArrayList<Match> matchesClone = (ArrayList)matches.clone();
        int highestTeamScore = 0;
        int tieRank = 0;
        HighSingleTeamScoreComparator byTeamScore = new HighSingleTeamScoreComparator();
        matchesClone.sort(byTeamScore);
        for (int i = 0; i < 5 || highestTeamScore == matchesClone.get(i).getWinningScore(); i++) {
            if (highestTeamScore != matchesClone.get(i).getWinningScore())
                tieRank = i + 1;
            System.out.println(String.format("%5d: %s", tieRank, matchesClone.get(i)));
            highestTeamScore = matchesClone.get(i).getWinningScore();
        }
    }

    public void findHighestCombinedScore() {
        ArrayList<Match> matchesClone = (ArrayList)matches.clone();
        int highestCombinedScore = 0;
        int tieRank = 0;
        HighCombinedScoreComparator byCombinedScore = new HighCombinedScoreComparator();
        matchesClone.sort(byCombinedScore);
        for (int i = 0; i < 5 || highestCombinedScore == matchesClone.get(i).getCombinedScore(); i++) {
            if (highestCombinedScore != matchesClone.get(i).getCombinedScore())
                tieRank = i + 1;
            System.out.println(String.format("%5d: %s. Combined score: %d", tieRank, matchesClone.get(i), matchesClone.get(i).getAwayScore() + matchesClone.get(i).getHomeScore()));
            highestCombinedScore = matchesClone.get(i).getCombinedScore();
        }
    }

    public void findHighestLosingScore() {
        ArrayList<Match> matchesClone = (ArrayList)matches.clone();

        int highestLosingScore = 0;
        int tieRank = 0;
        // see findLowestWinningScore() for explanation
        int skips = 0;
        HighestLosingScoreComparator byHighestLosingScore = new HighestLosingScoreComparator();
        matchesClone.sort(byHighestLosingScore);
        for (int i = 0; i < 5 || matchesClone.get(i + skips).getLosingScore() == highestLosingScore; i++) {
            if (matchesClone.get(i + skips).wasDrawn()) {
                skips++;
                i--;
                continue;
            }
            if (highestLosingScore != matchesClone.get(i + skips).getLosingScore())
                tieRank = i + 1;
            System.out.println(String.format("%5d: %s", tieRank, matchesClone.get(i + skips)));
            highestLosingScore = matchesClone.get(i + skips).getLosingScore();
        }
    }

    public void findLowestWinningScore() {
        ArrayList<Match> matchesClone = (ArrayList)matches.clone();

        int lowestWinningScore = 0;
        int tieRank = 0;
        // skips accounts for draws and skips them
        int skips = 0;
        LowestWinningScoreComparator byLowestWinningScore = new LowestWinningScoreComparator();
        matchesClone.sort(byLowestWinningScore);
        for (int i = 0; i < 5 || lowestWinningScore == matchesClone.get(i + skips).getWinningScore(); i++) {
            // if a match is drawn, skips is incremented and loop completed again (so final loop is reached)
            if (matchesClone.get(i + skips).wasDrawn()) {
                skips++;
                i--;
                continue; 
            }
            if (lowestWinningScore != matchesClone.get(i + skips).getWinningScore())
                tieRank = i + 1;
            System.out.println(String.format("%5d: %s", tieRank, matchesClone.get(i + skips)));
            lowestWinningScore = matchesClone.get(i + skips).getWinningScore();
        }
    }

    public void findLowestTeamScore() {
        ArrayList<Match> matchesClone = (ArrayList)matches.clone();

        int lowestTeamScore = 0;
        int tieRank = 0;
        LowestTeamScoreComparator byLowestTeamScore = new LowestTeamScoreComparator();
        matchesClone.sort(byLowestTeamScore);
        for (int i = 0; i < 5 || lowestTeamScore == matchesClone.get(i).getLosingScore(); i++) {
            if (lowestTeamScore != matchesClone.get(i).getLosingScore())
                tieRank = i + 1;
            System.out.println(String.format("%5d: %s", tieRank, matchesClone.get(i)));
            lowestTeamScore = matchesClone.get(i).getLosingScore();
        }
    }

    public void findLowestCombinedScore() {
        ArrayList<Match> matchesClone = (ArrayList)matches.clone();

        int lowestCombinedScore = 0;
        int tieRank = 0;
        LowestCombinedScoreComparator byLowestCombinedScore = new LowestCombinedScoreComparator();
        matchesClone.sort(byLowestCombinedScore);
        for (int i = 0; i < 5 || lowestCombinedScore == matchesClone.get(i).getCombinedScore(); i++) {
            if (lowestCombinedScore != matchesClone.get(i).getCombinedScore())
                tieRank = i + 1;
            System.out.println(String.format("%5d: %s. Combined score: %d", tieRank, matchesClone.get(i), matchesClone.get(i).getCombinedScore()));
            lowestCombinedScore = matchesClone.get(i).getAwayScore() + matchesClone.get(i).getHomeScore();
        }
    }
    public void createLadder(ArrayList<Match> matchSet) {
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
        ladder.display();
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
        ArrayList<Match> results = new ArrayList<Match>();
        for (Match match : matches) {
            if (match.getHomeTeamName().equals(team) || match.getAwayTeamName().equals(team)) {
                results.add(match);
                System.out.println(match);
            }
        }
    }

    public void displayHeadToHead(String team1str, String team2str) {
        // TODO: It works but its so hard to read. Just make it less shit. Also add more home/away functions and stuff. Margin works.
        ArrayList<Match> results = new ArrayList<Match>();
        for (Match match : matches) {
            if ((match.getHomeTeamName().equals(team1str) || match.getHomeTeamName().equals(team2str)) && (match.getAwayTeamName().equals(team2str) || match.getAwayTeamName().equals(team1str))) {
                results.add(match);
            }
        }
        Team team1 = new Team(team1str);
        Match team1GreatestWin = null;
        int team1GreatestWinningMargin = 0;
        Team team2 = new Team(team2str);
        Match team2GreatestWin = null;
        int team2GreatestWinningMargin = 0;
        for (Match result : results) {
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
                    team1GreatestWin = result;
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
                    team2GreatestWin = result;
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
    public ArrayList<Match> getMatchesByYear(String startYear, String endYear) {
        return null;
    }
    public ArrayList<Match> getMatchesByYear(String year) {
        ArrayList<Match> results = new ArrayList<Match>();
        for (Match match : matches) {
            if (match.getRound().contains(year)) {
                results.add(match);
            }
        }
        return results;
    }
    public ArrayList<Match> getMatches(String startYear, String endYear, String startRound) {
        return null;
    }
    public ArrayList<Match> getMatches(String startYear, String endYear, String startRound, String endRound) {
        return null;
    }
    public ArrayList<Match> getMatchesByTeam(String team) {
        return null;
    }
    public ArrayList<Match> getAllMatches() {
        return matches;
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