import java.util.*;

// IMPORTANT: NEVER EVER SORT THE MATSTER MATCHES DATASET. ALWAYS CLONE AND SORT. NO WAY TO RE-SORT INTO CHRONOLOGICAL ORDER
// Lucy Beattie (oddconverse) 2025. All use legal. Free Palestine.

public class Ladder {
    private Team[] teams;
    private int teamCount;

    public Ladder() {
        this.teams = new Team[20];
        this.teamCount = 0;
    }
    public void addTeam(String name) {
        Team newTeam = new Team(name);
        teams[teamCount] = newTeam;
        teamCount++;
    }

    public Team getTeam(String name) {
        for (int i = 0; i < teamCount; i++) {
            if (teams[i].getName().equals(name)) {
                return teams[i];
            }
        }
        return null;
    }
    public boolean doesTeamExist(String name) {
        for (int i = 0; i < teamCount; i++) {
            if (teams[i].getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
    public void display() {
        System.out.println();
        System.out.println("|------------------------------------------------------------------------------------------------------|");
        System.out.println(String.format("| %4s | %24s | %4s | %4s | %4s | %4s | %4s | %4s | %4s | %7s | %7s |", "Pos", "Name", "Pl", "W", "L", "D", "Pts", "PF", "PA", "%", "W%"));
        System.out.println("|------|--------------------------|------|------|------|------|------|------|------|---------|---------|");
        Arrays.sort(teams, 0, teamCount);
        for (int i = 0; i < teamCount; i++) {
            System.out.println(String.format("| %4d | %s", i + 1, teams[i].ladderDisplay()));
        }
        System.out.println("|------------------------------------------------------------------------------------------------------|");
        System.out.println();
        System.out.println("Key: Pl = Games Played, W = Wins, L = Losses, D = Draws, Pts = Premiership Points, PF = Points For, PA = Points Against, % = Percentage (PF / PA * 100), W% = Win Percentage (W / Pl * 100)");
        System.out.println();
    }
}