import java.util.ArrayList;
import java.util.HashMap;

public class OfflineData {
    public static HashMap<String, String> newUser;
    public static ArrayList<HashMap<String, String>> allUser = new ArrayList<>();
    
    public static HashMap<String, String> newProgram;
    public static ArrayList<HashMap<String, String>> allPrograms = new ArrayList<>();
    
    public OfflineData() {
        if((allUser.size() < 1) && (allPrograms.size() < 1)) {
            // ADMIN OFFLINE ACCOUNT
            newUser = new HashMap<>();
            newUser.put("id", "1");
            newUser.put("username", "admin");
            newUser.put("password", "password");
            newUser.put("program", "admin");
            allUser.add(newUser);
            
            // WORKOUT PROGRAMS
            // PUSH PULL LEGS SLPIT 
            newProgram = new HashMap<>();
            newProgram.put("id", "1");
            newProgram.put("title", "Push Pull Legs Split");
            newProgram.put("details", "A 3-6 day workout split.");
            newProgram.put("price", "550");
            allPrograms.add(newProgram);
            
            // Upper Lower Split
            newProgram = new HashMap<>();
            newProgram.put("id", "2");
            newProgram.put("title", "Upper Lower Split");
            newProgram.put("details", "This covers upper & lower body workouts.");
            newProgram.put("price", "550");
            allPrograms.add(newProgram);
            
            // Whole Body Split
            newProgram = new HashMap<>();
            newProgram.put("id", "3");
            newProgram.put("title", "Whole Body Split");
            newProgram.put("details", "A wholebody workout targeting all major muscle groups.");
            newProgram.put("price", "550");
            allPrograms.add(newProgram);
        }
    }
}
