import java.util.*;

// IMPORTANT: NEVER EVER SORT THE MATSTER MATCHES DATASET. ALWAYS CLONE AND SORT. NO WAY TO RE-SORT INTO CHRONOLOGICAL ORDER
// Lucy Beattie (oddconverse) 2025. All use legal. Free Palestine.

public class Ladder {
    private ArrayList<Team> teams;
    private final String[] allParameters = {"pos", "name", "pl", "w", "l", "d", "pts", "pf", "pa", "%", "w%"};

    public Ladder() {
        this.teams = new ArrayList<Team>();
    }
    public void addTeam(String name) {
        Team newTeam = new Team(name);
        teams.add(newTeam);
    }

    public Team getTeam(String name) {
        for (Team team : teams) {
            if (team.getName().equals(name)) {
                return team;
            }
        }
        return null;
    }
    public boolean doesTeamExist(String name) {
        for (Team team : teams) {
            if (team.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
    public void display(Comparator<Team> orderBy, ArrayList<String> parameterList) {
        String teamOutputFormat = "|";

        if (orderBy == null) {
            Collections.sort(teams);
        }
        else {
            Collections.sort(teams, orderBy);
        }
        if (parameterList == null) {
            parameterList = new ArrayList<>(Arrays.asList(allParameters));
        }
        for (String parameter : parameterList) {
            switch (parameter) {
                case "pts":
                case "pos":
                case "pl":
                case "w":
                case "d":
                case "l":
                case "pf":
                case "pa":
                    teamOutputFormat += " %4d |";
                    break;
                case "%":
                case "w%":
                    teamOutputFormat += " %7.2f |";
                    break;
                case "name":
                    teamOutputFormat += " %24s |";
                    break;
            }
        }
        System.out.println(String.format("|%102s", String.format("%-51s|", "Hi bro i hope this works").replace(' ', '-')));
        System.out.println(String.format(teamOutputFormat.replace('d', 's').replace('f', 's'), "Pos", "Name", "Pl", "W", "L", "D", "Pts", "PF", "PA", "%", "W%"));
        System.out.println("|------|--------------------------|------|------|------|------|------|------|------|---------|---------|");
        for (int i = 0; i < teams.size(); i++) {
            System.out.println(String.format("| %4d | %s", i + 1, teams.get(i).ladderDisplay(teamOutputFormat, parameterList)));
            if (i == 7) {
                System.out.println(String.format("|%102s|", "TOP 8 PLAY FINALS").replace(' ', '-'));
            }
        }
        System.out.println(String.format("|%102s|", "  ").replace(' ', '-'));;
        System.out.println();
        System.out.println("Key: Pl = Games Played, W = Wins, L = Losses, D = Draws, Pts = Premiership Points, PF = Points For, PA = Points Against, % = Percentage (PF / PA * 100), W% = Win Percentage (W / Pl * 100)");
        System.out.println();
    }
    public String centreOutput(String input, int length) {
        return null;
    }
}
class SeasonLadderComparator implements Comparator<Team> {
    @Override
    public int compare (Team a, Team b) {
        if (a.getPoints() - b.getPoints() != 0) {
            return Integer.compare(b.getPoints(), a.getPoints());
        }
        return Double.compare(b.getPercentage(), a.getPercentage());
    }
}
class PointsForComparator implements Comparator<Team> {
    @Override
    public int compare (Team a, Team b) {
        return Integer.compare(b.getPointsFor(), a.getPointsFor());
    }
}