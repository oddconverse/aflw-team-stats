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
        switch (abbr) {
            case "a":
            case "ad":
            case "ade":
            case "adel":
                return "Adelaide";
            case "b":
            case "br":
            case "bris":
                return "Brisbane";
            case "ca":
            case "carl":
                return "Carlton";
            case "co":
            case "coll":
                return "Collingwood";
            case "e":
            case "es":
            case "ess":
                return "Essendon";
            case "f":
            case "fr":
            case "fre":
            case "freo":
                return "Fremantle";
            case "ge":
            case "gee":
                return "Geelong";
            case "gc":
                return "Gold Coast";
            case "gws":
                return "Greater Western Sydney";
            case "h":
            case "ha":
            case "haw":
                return "Hawthorn";
            case "m":
            case "me":
            case "mel":
            case "melb":
                return "Melbourne";
            case "nm":
            case "n":
                return "North Melbourne";
            case "p":
            case "pa":
                return "Port Adelaide";
            case "r":
            case "ri":
            case "rich":
                return "Richmond";
            case "st":
            case "stk":
                return "St Kilda";
            case "ss":
            case "syd":
                return "Sydney Swans";
            case "wc":
            case "wce":
                return "West Coast Eagles";
            case "wb":
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
                    System.out.println("Greatest winning margins: ");
                    for (Match result : system.findGreatestMargins()) {
                        System.out.println(String.format("%s. %s won by %d points.", result, result.getWinningTeam(), result.getMargin()));
                    }
                    System.out.println("Highest team scores: ");
                    for (Match result : system.findHighestTeamScore()) {
                        System.out.println(result);
                    }
                    System.out.println("Highest combined scores: ");
                    for (Match result : system.findHighestCombinedScore()) {
                        System.out.println(String.format("%s. Combined score: %d", result, result.getAwayScore() + result.getHomeScore()));
                    }
                    System.out.println("Highest losing scores: ");
                    for (Match result : system.findHighestLosingScore()) {
                        System.out.println(result);
                    }
                    System.out.println("Lowest winning scores: ");
                    for (Match result : system.findLowestWinningScore()) {
                        System.out.println(result);
                    }
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
                    system.createLadder();
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
    }
}
