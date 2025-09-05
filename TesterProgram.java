public class TesterProgram {
    public static void main(String[] args) {
        RecordSystem system = new RecordSystem();
        system.loadMatches();
        system.displayHeadToHead("Adelaide", "Melbourne");
    }
}
