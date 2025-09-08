import java.util.Comparator;
import java.util.Scanner;
import java.util.StringTokenizer;

public class CommandLine {
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
            switch (firstWord.toLowerCase()) {
                case "help":
                case "ladder":
                case "l":
                    if (stk.hasMoreTokens()) {
                        String secondWord = stk.nextToken().trim();
                        // all time ladder no longer works
                        system.createLadder(system.getMatchesByYear(secondWord));
                    }
                    else {
                        system.createLadder(system.getAllMatches());
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
                    String[] words = new String[50];
                    int wordCount = 0;
                    while (stk.hasMoreTokens()) {
                        words[wordCount] = stk.nextToken().trim();
                        wordCount++;
                    }
                    String teamName = null;
                    Comparator[] comparators = new Comparator[10];
                    int comparatorCount = 0;
                    String firstSeason = null;
                    String secondSeason = null;
                    for (int i = 0; i < wordCount; i++) {
                        if (system.stringToRecordComparator(words[i]) != null) {
                            comparators[comparatorCount] = (Comparator) system.stringToRecordComparator(words[i]);
                            comparatorCount++;
                        }
                        else if (system.abbreviationToName(words[i]) != null) {
                            teamName = system.abbreviationToName(words[i]);
                        }
                        // future season implementation
                        else if (system.isSeason(words[i])) {
                            if (firstSeason == null) {
                                firstSeason = words[i];
                            }
                            else {
                                secondSeason = words[i];
                            }
                        }
                    }
                    for (int i = 0; i < comparatorCount; i++) {
                        system.findRecord(teamName, comparators[i], firstSeason);
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
