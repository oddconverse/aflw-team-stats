import java.util.Scanner;

// IMPORTANT: NEVER EVER SORT THE MATSTER MATCHES DATASET. ALWAYS CLONE AND SORT. NO WAY TO RE-SORT INTO CHRONOLOGICAL ORDER
// Lucy Beattie (oddconverse) 2025. All use legal. Free Palestine.

public class RecordSystemMenu {
    public static String selectOption(Scanner input) {
        System.out.println("=========================");
        System.out.println(" | AFLW Record Keeping | ");
        System.out.println("=========================");
        System.out.println("Select an option by typing a number (1-7 or X):");
        System.out.println("1. Add new matches");
        System.out.println("2. Save data");
        System.out.println("3. View records");
        System.out.println("4. View matches by round");
        System.out.println("5. Create custom ladder");
        System.out.println("6. View results by team");
        System.out.println("7. View head to head records");
        System.out.println("X. Exit");
        return input.nextLine();
    }

    public static String nameTranslation(String abbr) {
        return switch (abbr.toLowerCase()) {
            case "a", "ad", "ade", "adel", "adelaide" -> "Adelaide";
            case "b", "br", "bris", "brisbane" -> "Brisbane";
            case "ca", "carl", "carlton" -> "Carlton";
            case "co", "coll", "collingwood" -> "Collingwood";
            case "e", "es", "ess", "essendon" -> "Essendon";
            case "f", "fr", "fre", "freo", "fremantle" -> "Fremantle";
            case "ge", "gee", "geelong" -> "Geelong";
            case "gc", "gold coast" -> "Gold Coast";
            case "gws", "greater western sydney" -> "Greater Western Sydney";
            case "h", "ha", "haw", "hawthorn" -> "Hawthorn";
            case "m", "me", "mel", "melb", "melbourne" -> "Melbourne";
            case "nm", "n", "north melbourne", "north", "kangaroos" -> "North Melbourne";
            case "p", "pa", "port", "port adelaide" -> "Port Adelaide";
            case "r", "ri", "rich", "richmond" -> "Richmond";
            case "st", "stk", "st kilda" -> "St Kilda";
            case "ss", "syd", "sydney", "sydney swans" -> "Sydney Swans";
            case "wc", "wce", "west coast", "west coast eagles" -> "West Coast Eagles";
            case "footscray", "wb", "western bulldogs" -> "Western Bulldogs";
            default -> abbr;
        };
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
                        String lastMatchID = system.getAllMatches().get(system.getAllMatches().size() - 1).getID();
                        String matchID = String.format("M%6d", Integer.parseInt(lastMatchID.substring(1, lastMatchID.length())) + 1).replace(' ', '0');
                        String lastScoreID = system.getAllScores().get(system.getAllScores().size() - 1).getID();
                        int scoreIDNumber = Integer.parseInt(lastScoreID.substring(1, lastScoreID.length())) + 1;
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
                        Score homeScore = new Score(String.format("S%6d", scoreIDNumber).replace(' ', '0'), matchID, homeTeamName, homeTeamGoals, homeTeamBehinds);
                        scoreIDNumber++;
                        Score awayScore = new Score(String.format("S%6d", scoreIDNumber).replace(' ', '0'), matchID, awayTeamName, awayTeamGoals, awayTeamBehinds);
                        Match currentMatch = new Match(matchID, roundName, homeScore, awayScore);
                        System.out.println(homeScore);
                        System.out.println(awayScore);
                        system.addScore(homeScore);
                        system.addScore(awayScore);
                        system.addMatch(currentMatch);
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
                    system.saveData();
                    selection = selectOption(input);
                    break;
                case "3":
                    System.out.println("\nGreatest winning margins: ");
                    //system.findGreatestMargins(system.getAllMatches());
                    System.out.println("\nHighest team scores: ");
                    //system.findHighestTeamScore();
                    System.out.println("\nHighest combined scores: ");
                    //system.findHighestCombinedScore();
                    System.out.println("\nHighest losing scores: ");
                    //system.findHighestLosingScore();
                    System.out.println("\nLowest winning scores: ");
                    //system.findLowestWinningScore();
                    System.out.println("\nLowest team scores: ");
                    //system.findLowestTeamScore();
                    System.out.println("\nLowest combined scores: ");
                    //system.findLowestCombinedScore();
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
                    System.out.println("First season: (leave blank for all time ladder)");
                    String firstSeason = system.abbreviationToSeason(input.nextLine());
                    String secondSeason = null;
                    if (!firstSeason.isBlank()) {
                        System.out.println("Second season: (leave blank for single season ladder)");
                        secondSeason = system.abbreviationToSeason(input.nextLine());
                    }
                    System.out.println("Should finals be included in this ladder? (Y)/N");
                    boolean includeFinals = true;
                    if (input.nextLine().equals("N"))
                        includeFinals = false;
                    if (firstSeason.equals("")) {
                        system.createLadder(system.filterMatchesByFinals(includeFinals, true, system.getAllMatches()), null);
                    }
                    else if (secondSeason == null) {
                        system.createLadder(system.filterMatchesByFinals(includeFinals, true, system.getMatchesByYear(firstSeason)), null);
                    }
                    else {
                        system.createLadder(system.filterMatchesByFinals(includeFinals, true, system.getMatchesByYears(firstSeason, secondSeason)), null);
                    }
                    selection = selectOption(input);
                    break;
                case "6":
                    System.out.println("Team name: ");
                    String teamName = nameTranslation(input.nextLine());
                    system.displayMatchesByTeam(teamName);
                    selection = selectOption(input);
                    break;
                case "7":
                    System.out.println("Team 1 name: ");
                    String teamName1 = nameTranslation(input.nextLine());
                    System.out.println("Team 2 name: ");
                    String teamName2 = nameTranslation(input.nextLine());
                    System.out.println("Display all matches? Y/(N)");
                    boolean displayAll = false;
                    if (input.nextLine().toLowerCase().equals("y")) 
                        displayAll = true;
                    system.displayHeadToHead(teamName1, teamName2, displayAll);
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
