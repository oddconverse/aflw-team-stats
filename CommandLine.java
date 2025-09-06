import java.util.Scanner;
import java.util.StringTokenizer;

public class CommandLine {
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
                    if (stk.hasMoreTokens()) {
                        String team = RecordSystemMenu.nameTranslation(stk.nextToken().trim());
                        system.findGreatestMargins(system.getMatchesByTeam(team));
                    }
                    else {
                        system.findGreatestMargins(system.getAllMatches());
                    }
                    command = input.nextLine();
                    break;
                case "exit":
                case "x":
                    System.out.println("Thank you for using the AFLW Team Stats centre.");
                    exit = true;
                default: 
                    System.out.println("Unknown command. Type \"help\" for a list of commands.");
                    command = input.nextLine();
                    break;
            }
        }
        input.close();
    }
}
