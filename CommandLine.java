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
        StringTokenizer stk = new StringTokenizer(command, " ");
        while (!exit) {
            
        }
    }
}
