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
            
            // SAMPLE PROGRAM (DEFAULT)
            newProgram = new HashMap<>();
            newProgram.put("id", "1");
            newProgram.put("title", "14 Days Plan");
            newProgram.put("details", "Achieve Your Fitness Goals in 14 Days – Join Now.");
            newProgram.put("price", "699");
            allPrograms.add(newProgram);
        }
    }
}
