import java.io.*;
import java.util.*;

public class MigrateScoreFile {
    public static void main(String[] args) throws Exception {
        File scoreFile = new File("scores.txt");
        FileWriter scoreOutfile = new FileWriter(scoreFile);
        File oldMatchFile = new File("data.txt");
        Scanner matchInfile = new Scanner(oldMatchFile);
        FileWriter matchOutfile = new FileWriter("matches.txt");
        int scoreIDNumber = 1;
        int matchIDNumber = 1;
        while (matchInfile.hasNextLine()) {
            String matchID = String.format("M%6d", matchIDNumber).replace(' ', '0');
            matchIDNumber++;
            String s = matchInfile.nextLine();
            StringTokenizer stk = new StringTokenizer(s, ";");
            String round = stk.nextToken().trim();
            String homeScoreID = String.format("S%6d", scoreIDNumber).replace(' ', '0');
            scoreIDNumber++;
            String homeTeamName = stk.nextToken().trim();
            int homeTeamGoals = Integer.parseInt(stk.nextToken().trim());
            int homeTeamBehinds = Integer.parseInt(stk.nextToken().trim());
            String awayScoreID = String.format("S%6d", scoreIDNumber).replace(' ', '0');
            scoreIDNumber++;
            String awayTeamName = stk.nextToken().trim();
            int awayTeamGoals = Integer.parseInt(stk.nextToken().trim());
            int awayTeamBehinds = Integer.parseInt(stk.nextToken().trim());
            matchOutfile.write(String.format("%s; %s; %s; %s;\n", matchID, round, homeScoreID, awayScoreID));
            scoreOutfile.write(String.format("%s; %s; %s; %d; %d;\n", homeScoreID, matchID, homeTeamName, homeTeamGoals, homeTeamBehinds));
            scoreOutfile.write(String.format("%s; %s; %s; %d; %d;\n", awayScoreID, matchID, awayTeamName, awayTeamGoals, awayTeamBehinds));
        }
        scoreOutfile.close();
        matchInfile.close();
        matchOutfile.close();
    }
}
