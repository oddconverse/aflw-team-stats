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
                words[wordCount] = stk.nextToken().trim();
                wordCount++;
            }
            switch (firstWord.toLowerCase()) {
                case "help":
                case "ladder":
                case "l":
                    for (int i = 0; i < wordCount; i++) {

                    }
                    
                    system.createLadder(system.getAllMatches());
                    
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
                            System.out.println(teamName);
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
