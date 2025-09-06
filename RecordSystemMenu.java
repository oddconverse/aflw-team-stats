import java.util.Scanner;

// IMPORTANT: NEVER EVER SORT THE MATSTER MATCHES DATASET. ALWAYS CLONE AND SORT. NO WAY TO RE-SORT INTO CHRONOLOGICAL ORDER
// Lucy Beattie (oddconverse) 2025. All use legal. Free Palestine.

public class RecordSystemMenu {
    public static String selectOption(Scanner input) {
        System.out.println("=========================");
        System.out.println(" | AFLW Record Keeping | ");
        System.out.println("=========================");
        System.out.println("Select an option by typing a number (1-6 or X):");
        System.out.println("1. Add new matches");
        System.out.println("2. Save data");
        System.out.println("3. View records");
        System.out.println("4. View matches by round");
        System.out.println("5. View all time ladder");
        System.out.println("6. View results by team");
        System.out.println("X. Exit");
        return input.nextLine();
    }

    public static String nameTranslation(String abbr) {
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
                return abbr;
        }
    }
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        RecordSystem system = new RecordSystem();
        boolean exit = false;
        system.loadMatches();

        String selection = selectOption(input);
        while (!exit) {
            switch (selection) {
                case "1":
                    boolean matchExitFlag = false;
                    System.out.println("Round name: ");
                    String roundName = input.nextLine();
                    while (!matchExitFlag) {
                        System.out.println("Home team name: ");
                        String homeTeamName = nameTranslation(input.nextLine());
                        System.out.println("Home team goals: ");
                        int homeTeamGoals = Integer.parseInt(input.nextLine());
                        System.out.println("Home team behinds: ");
                        int homeTeamBehinds = Integer.parseInt(input.nextLine());
                        System.out.println("Away team name: ");
                        String awayTeamName = nameTranslation(input.nextLine());
                        System.out.println("Away team goals: ");
                        int awayTeamGoals = Integer.parseInt(input.nextLine());
                        System.out.println("Away team behinds: ");
                        int awayTeamBehinds = Integer.parseInt(input.nextLine());
                        Match currentMatch = new Match(roundName, homeTeamName, homeTeamGoals, homeTeamBehinds, awayTeamName, awayTeamGoals, awayTeamBehinds);
                        system.addMatch(roundName, homeTeamName, homeTeamGoals, homeTeamBehinds, awayTeamName, awayTeamGoals, awayTeamBehinds);
                        System.out.println("Match added successfully. " + currentMatch);
                        System.out.println("Add another match from same round? (y)/n");
                        String matchSelection = input.nextLine();
                        switch (matchSelection) {
                            case "N":
                            case "n":
                                matchExitFlag = true;
                        }
                    }
                    selection = selectOption(input);
                    break;
                    
                case "2":
                    system.saveMatches();
                    selection = selectOption(input);
                    break;
                case "3":
                    System.out.println("\nGreatest winning margins: ");
                    system.findGreatestMargins();
                    System.out.println("\nHighest team scores: ");
                    system.findHighestTeamScore();
                    System.out.println("\nHighest combined scores: ");
                    system.findHighestCombinedScore();
                    System.out.println("\nHighest losing scores: ");
                    system.findHighestLosingScore();
                    System.out.println("\nLowest winning scores: ");
                    system.findLowestWinningScore();
                    System.out.println("\nLowest team scores: ");
                    system.findLowestTeamScore();
                    System.out.println("\nLowest combined scores: ");
                    system.findLowestCombinedScore();
                    selection = selectOption(input);
                    break;
                case "4":
                    System.out.println("Round number: ");
                    String roundNumber = input.nextLine();
                    System.out.println("Year of matches: ");
                    String year = input.nextLine();
                    system.displayMatchesByRound(roundNumber, year);
                    selection = selectOption(input);
                    break;
                case "5":
                    system.createLadder(system.getAllMatches());
                    selection = selectOption(input);
                    break;
                case "6":
                    System.out.println("Team name: ");
                    String teamName = input.nextLine();
                    system.displayMatchesByTeam(teamName);
                    selection = selectOption(input);
                    break;
                case "x":
                case "X":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Select an option from 1-7 or X.");
                    selection = selectOption(input);
                    break;
            }
        }
        input.close();
    }
}
