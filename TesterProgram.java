public class TesterProgram {
    public static void main(String[] args) {
        RecordSystem system = new RecordSystem();
        system.loadMatches();
        system.displayHeadToHead("Adelaide", "Melbourne");
        MarginComparator margin = new MarginComparator();
        HighSingleTeamScoreComparator highScore = new HighSingleTeamScoreComparator();
        system.findRecord("Collingwood", margin);
        system.findRecord("Collingwood", highScore);
    }
}
