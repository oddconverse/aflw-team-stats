import java.util.ArrayList;
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
            ArrayList<String> words = new ArrayList<String>();
            while (stk.hasMoreTokens()) {
                words.add(stk.nextToken().trim().toLowerCase());
            }
            switch (firstWord.toLowerCase()) {
                case "help" -> {
                    String defaultFormat = "| %s: %s |";
                    System.out.println("|----------------------------------LIST OF COMMANDS-------------------------------------------|");
                    System.out.println(String.format("| %60s |", "Parameters listed between <> arrows. Default values marked with an = after parameter name."));
                    System.out.println(String.format(defaultFormat, "headtohead <team1> <team2>", "Command will output a head to head comparison between two teams."));
                    System.out.println(String.format(defaultFormat, "ladder <firstSeason> <secondSeason> <includefinals=true> <includehomeandaway=true>", "Command will output a ladder of all matches played during and between the given seasons."));
                    command = input.nextLine();
                }
                // HEAD TO HEAD
                case "headtohead", "head2head", "htoh", "h2h" -> {
                    String team1 = null;
                    String team2 = null;
                    boolean displayMatches = false;
                    for (String word : words) {
                        if (system.abbreviationToName(word) != null && team1 == null) {
                            team1 = system.abbreviationToName(word);
                        }
                        else if (system.abbreviationToName(word) != null) {
                            team2 = system.abbreviationToName(word);
                        }
                        else if (word.equals("allresults")) {
                            displayMatches = true;
                        }
                    }
                    system.displayHeadToHead(team1, team2, displayMatches);
                    command = input.nextLine();
                }
                // LADDER
                case "ladder", "l" -> {
                    // TODO: implement per round commands
                    if (words.isEmpty()) {
                        system.createLadder(system.getAllMatches(), null);
                    }
                    else {
                        String firstSeason = null;
                        String secondSeason = null;
                        boolean includeFinals = true;
                        boolean includeHomeAway = true;
                        for (String word : words) {
                            if (system.isSeason(word)) {
                                if (firstSeason == null) {
                                    firstSeason = word;
                                    includeFinals = false;
                                }
                                else {
                                    secondSeason = word;
                                    includeFinals = true;
                                }
                            }
                            // switch should be moved somewhere better
                            switch (word) {
                                case "nofinals", "nf" -> includeFinals = false;
                                case "finals", "f", "nohomeaway", "nha", "nohome+away", "nohome&away", "noh+a", "noh&a" -> {
                                    includeFinals = true;
                                    includeHomeAway = false;
                                }
                                case "includefinals", "incfinals", "if", "incf" -> includeFinals = true;
                                case "homeaway", "ha", "home&away", "h&a", "home+away", "h+a" -> {
                                    includeHomeAway = true;
                                    includeFinals = false;
                                }
                                case "includehomeandaway", "inchomeandaway", "includehomeaway", "inchomeaway", "iha", "incha", "includehome+away", "inchome+away", "ih+a", "inch+a", "includehome&away", "inchome&away", "ih&a", "inch&a" -> includeHomeAway = true;
                            }
                        }
                        if (firstSeason == null) 
                            system.createLadder(system.filterMatchesByFinals(includeFinals, includeHomeAway, system.getAllMatches()), null);
                        else if (secondSeason == null)
                            system.createLadder(system.filterMatchesByFinals(includeFinals, includeHomeAway, system.getMatchesByYear(firstSeason)), null);
                        else 
                            system.createLadder(system.filterMatchesByFinals(includeFinals, includeHomeAway, system.getMatchesByYears(firstSeason, secondSeason)), null);
                    }
                    
                    command = input.nextLine();
                }
                case "minorpremiers", "mp" -> {
                    System.out.println("");
                    command = input.nextLine();
                }
                case "premiers", "p" -> {
                    System.out.println(system.getPremiers());
                    command = input.nextLine();
                }
                // RECORDS
                case "records", "r" -> {
                    // 21/9/25: command now functional
                    String teamName = null;
                    ArrayList<String> recordList = new ArrayList<String>();
                    String firstSeason = null;
                    String secondSeason = null;
                    int resultCount = 5;
                    for (String word : words) {
                        if (system.stringToRecord(word) != null) {
                            recordList.add(system.stringToRecord(word));
                        }
                        else if (system.abbreviationToName(word) != null) {
                            teamName = system.abbreviationToName(word);
                        }
                        else if (system.isSeason(word)) {
                            if (firstSeason == null) {
                                firstSeason = word;
                            }
                            else {
                                secondSeason = word;
                            }
                        }
                        else if (system.isNumeric(word) && Integer.parseInt(word) < 100) {
                            resultCount = Integer.parseInt(word);
                        }
                    }
                    ArrayList<Score> teamScoreSet;
                    ArrayList<Match> teamMatchSet;
                    ArrayList<Match> finalMatchSet;
                    ArrayList<Score> finalScoreSet;
                    if (teamName == null) {
                        teamMatchSet = system.getAllMatches();
                        teamScoreSet = system.getAllScores();
                    }
                    else { 
                        teamScoreSet = system.getAllScoresByTeam(teamName);
                        teamMatchSet = system.getMatchesByTeam(teamName);
                    }
                    if (firstSeason == null) {
                        finalScoreSet = teamScoreSet;
                        finalMatchSet = teamMatchSet;
                    }
                    else if (secondSeason == null) {
                        finalScoreSet = system.getScoresByYear(firstSeason, teamScoreSet);
                        finalMatchSet = system.getMatchesByYear(firstSeason, teamMatchSet);
                    }
                    
                    else {
                        finalScoreSet = system.getScoresByYears(firstSeason, secondSeason, teamScoreSet);
                        finalMatchSet = system.getMatchesByYears(firstSeason, secondSeason, teamMatchSet);
                    }
                    
                    if (recordList.isEmpty()) {
                        // run all record commands
                        System.out.println("Greatest Winning Margin");
                        system.greatestWinningMargin(finalMatchSet, resultCount);
                        System.out.println("Highest Team Score");
                        system.highestTeamScore(finalScoreSet, resultCount);
                        System.out.println("Highest Combined Score");
                        system.highestCombinedScore(finalMatchSet, resultCount);
                        System.out.println("Lowest Team Score");
                        system.lowestTeamScore(finalScoreSet, resultCount);
                        System.out.println("Lowest Combined Score");
                        system.lowestCombinedScore(finalMatchSet, resultCount);
                        System.out.println("Highest Losing Score");
                        system.highestTeamScore(system.getLosingScores(finalScoreSet), resultCount);
                        System.out.println("Lowest Winning Score");
                        system.lowestTeamScore(system.getWinningScores(finalScoreSet), resultCount);
                    }
                    else {
                        for (String record : recordList) {
                            // run record commands based on list entries
                            System.out.println(record);
                            if (record.equals("Greatest Winning Margin"))
                                system.greatestWinningMargin(finalMatchSet, resultCount);
                            else if (record.equals("Highest Team Score")) 
                                system.highestTeamScore(finalScoreSet, resultCount);
                            else if (record.equals("Highest Combined Score")) 
                                system.highestCombinedScore(finalMatchSet, resultCount);
                            else if (record.equals("Lowest Team Score")) 
                                system.lowestTeamScore(finalScoreSet, resultCount);
                            else if (record.equals("Lowest Combined Score"))
                                system.lowestCombinedScore(finalMatchSet, resultCount);
                            else if (record.equals("Highest Losing Score"))
                                system.highestTeamScore(system.getLosingScores(finalScoreSet), resultCount);
                            else if (record.equals("Lowest Winning Score"))
                                system.lowestTeamScore(system.getWinningScores(finalScoreSet), resultCount);
                        }
                    }
                    command = input.nextLine();
                }
                case "exit", "x" -> {
                    System.out.println("Thank you for using the AFLW Team Stats centre.");
                    exit = true;
                }
                     
                default -> {
                    System.out.println("Unknown command. Type \"help\" for a list of commands.");
                    command = input.nextLine();
                }
            }
        }
        input.close();
    }
}
