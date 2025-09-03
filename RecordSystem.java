import java.io.*;
import java.util.Scanner;

public class RecordSystem {
    private Match[] matches;
    private int matchCount = 0;

    public void addMatch(String round, String homeTeamName, int homeTeamGoals, int homeTeamBehinds, String awayTeamName, int awayTeamGoals, int awayTeamBehinds) {
        matches[matchCount] = new Match(round, homeTeamName, homeTeamGoals, homeTeamBehinds, awayTeamName, awayTeamGoals, awayTeamBehinds);
        matchCount++;
    }

    public void saveMatches() throws Exception {
        try {
            File matchFile = new File("data.txt");
            FileWriter matchWriter = new FileWriter(matchFile);
            for (int i = 0; i < matchCount; i++) {
                Match currentMatch = matches[i];
                matchWriter.write(String.format("%s; %s; %d; %d; %s; %d; %d\n", currentMatch.getRound(), currentMatch.getHomeTeamName(), currentMatch.getHomeTeamGoals(), currentMatch.getHomeTeamBehinds(), currentMatch.getAwayTeamName(), currentMatch.getAwayTeamGoals(), currentMatch.getAwayTeamBehinds()));
                
            }
            matchWriter.close();
        }
        catch (Exception e) {

        }
    }

    public void loadMatches() {

    }

    public String findGreatestMargins() {
        return null;
    }
    public static void main(String[] args) {
        
    }
}