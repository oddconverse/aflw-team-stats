import java.util.Comparator;
import java.util.Scanner;
import java.util.StringTokenizer;

public class CommandLine {
    private static final Comparator[] allComparators = {new HighCombinedScoreComparator(), new MarginComparator(), new HighSingleTeamScoreComparator(), new HighestLosingScoreComparator(), new LowestWinningScoreComparator(), new LowestCombinedScoreComparator(), new LowestTeamScoreComparator()};
    private static final int allComparatorCount = 7;
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        RecordSystem system = new RecordSystem();
        boolean exit = false;
        system.loadMatches();
        System.out.println("Welcome to the AFLW Team Stats centre.");
        System.out.println("Enter a command, or type \"help\" for a list of commands.");
        String command = input.nextLine();
        while (!exit) {
            StringTokenizer stk = new StringTokenizer(command);
            String firstWord = stk.nextToken().trim();
            String[] words = new String[50];
            int wordCount = 0;
            while (stk.hasMoreTokens()) {
                words[wordCount] = stk.nextToken().trim().toLowerCase();
                wordCount++;
            }
            switch (firstWord.toLowerCase()) {
                case "help":
                    command = input.nextLine();
                    break;
                case "headtohead":
                case "head2head":
                case "htoh":
                case "h2h":
                    command = input.nextLine();
                    break;
                case "ladder":
                case "l":
                // TODO: implement per round commands
                    if (wordCount == 0) {
                        system.createLadder(system.getAllMatches());
                    }
                    else {
                        String firstSeason = null;
                        String secondSeason = null;
                        boolean includeFinals = true;
                        boolean includeHomeAway = true;
                        for (int i = 0; i < wordCount; i++) {
                            if (system.isSeason(words[i])) {
                                if (firstSeason == null) {
                                    firstSeason = words[i];
                                    includeFinals = false;
                                }
                                else {
                                    secondSeason = words[i];
                                    includeFinals = true;
                                }
                            }
                            // switch should be moved somewhere better
                            switch (words[i]) {
                                case "nofinals":
                                case "nf":
                                    includeFinals = false;
                                    break;
                                case "finals":
                                case "f":
                                    includeFinals = true;
                                    includeHomeAway = false;
                                    break;
                                case "includefinals":
                                case "incfinals":
                                case "if":
                                case "incf":
                                    includeFinals = true;
                                    break;
                                case "nohomeaway":
                                case "nha":
                                case "nohome+away":
                                case "nohome&away":
                                case "noh+a":
                                case "noh&a":
                                    includeHomeAway = false;
                                    break;
                                case "homeaway":
                                case "ha":
                                case "home&away":
                                case "h&a":
                                case "home+away":
                                case "h+a":
                                    includeHomeAway = true;
                                    includeFinals = false;
                                    break;
                                case "includehomeandaway":
                                case "inchomeandaway":
                                case "includehomeaway":
                                case "inchomeaway":
                                case "iha":
                                case "incha":
                                case "includehome+away":
                                case "inchome+away":
                                case "ih+a":
                                case "inch+a":
                                case "includehome&away":
                                case "inchome&away":
                                case "ih&a":
                                case "inch&a":
                                    includeHomeAway = true;
                                    break;
                            }
                        }
                        if (firstSeason == null) {
                            system.createLadder(system.getMatchesByYears("s1", "s10", includeFinals, includeHomeAway));
                        }
                        else if (secondSeason == null)
                            system.createLadder(system.getMatchesByYear(firstSeason, includeFinals, includeHomeAway));
                        else {
                            system.createLadder(system.getMatchesByYears(firstSeason, secondSeason, includeFinals, includeHomeAway));
                        }
                    }
                    
                    command = input.nextLine();
                    break;
                case "records":
                case "r":
                // TODO:
                // want to rework
                // check if command contains a team, or a season, or multiple seasons
                // if so, run commands to get team data, or season data, or multiple season data
                // if not, run command on all matches

                // 7/9/25: command now functional without season implementation
                    String teamName = null;
                    Comparator[] comparators = new Comparator[10];
                    int comparatorCount = 0;
                    String firstSeason = null;
                    String secondSeason = null;
                    int resultCount = 5;
                    for (int i = 0; i < wordCount; i++) {
                        if (system.stringToRecordComparator(words[i]) != null) {
                            comparators[comparatorCount] = (Comparator) system.stringToRecordComparator(words[i]);
                            comparatorCount++;
                        }
                        else if (system.abbreviationToName(words[i]) != null) {
                            teamName = system.abbreviationToName(words[i]);
                        }
                        
                        else if (system.isSeason(words[i])) {
                            if (firstSeason == null) {
                                firstSeason = words[i];
                            }
                            else {
                                secondSeason = words[i];
                            }
                        }
                        else if (system.isNumeric(words[i]) && Integer.parseInt(words[i]) < 100) {
                            resultCount = Integer.parseInt(words[i]);
                        }
                    }
                    if (comparators[0] == null) {
                        comparators = allComparators;
                        comparatorCount = allComparatorCount;
                    }
                    for (int i = 0; i < comparatorCount; i++) {
                        switch (comparators[i].getClass().getName()) {
                            case "MarginComparator":
                                System.out.println("Greatest Winning Margins");
                                break;
                            case "HighSingleTeamScoreComparator":
                                System.out.println("Highest Scores");
                                break;
                            case "HighCombinedScoreComparator":
                                System.out.println("Highest Combined Scores");
                                break;
                            case "HighestLosingScoreComparator":
                                System.out.println("Highest Losing Scores");
                                break;
                            case "LowestWinningScoreComparator":
                                System.out.println("Lowest Winning Scores");
                                break;
                            case "LowestTeamScoreComparator":
                                System.out.println("Lowest Scores");
                                break;
                            case "LowestCombinedScoreComparator":
                                System.out.println("Lowest Combined Scores");
                                break;

                        }
                        system.findRecord(teamName, comparators[i], resultCount, firstSeason, secondSeason);
                    }
                    command = input.nextLine();
                    break;
                case "exit":
                case "x":
                    System.out.println("Thank you for using the AFLW Team Stats centre.");
                    exit = true;
                    break;
                default: 
                    System.out.println("Unknown command. Type \"help\" for a list of commands.");
                    command = input.nextLine();
                    break;
            }
        }
        input.close();
    }
}
