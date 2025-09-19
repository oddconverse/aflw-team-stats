import java.io.*;
import java.util.*;

public class MigrateTeamID {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        try {
            File file = new File("data.txt");
            Scanner infile = new Scanner(file);
            FileWriter outfile = new FileWriter("aflwMatches.txt");
            int count = 1;
            while (infile.hasNextLine()) {
                String line = infile.nextLine();
                String id = String.format("M%5d", count).replace(" ", "0");
                outfile.write(String.format("%s; %s\n", id, line));
                count++;
            }
            System.out.println("Migration successful.");
        }
        
        catch (Exception e) {
            System.out.println("Migration failed.");
        }
    }
    
}
