import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class RunSystem {
    public static boolean databaseConnected;
    
    private Connection cn;
    private Statement st;
    private PreparedStatement pst;
    private ResultSet rs;
    
    
    public RunSystem() {
        ConnectToDatabase();
        AccountManagement account = new AccountManagement();
        account.pack();
        account.setLocationRelativeTo(null);
        account.setVisible(true);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel( new FlatMacDarkLaf());
        } catch(Exception e) {
            
        }
        
        new RunSystem();
    }
    
    private void ConnectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            cn = DriverManager.getConnection("jdbc:mysql://localhost/localgym", "root", "");
            st = cn.createStatement();
            
            JOptionPane.showMessageDialog(null, "Successfully connected to database.");
            databaseConnected = true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error connecting to database.\nPlease connect to the database first to avoid any errors from occurring.");
            JOptionPane.showMessageDialog(null, "Offline mode turned on.\nData save will be temporary.");
            new OfflineData();
            databaseConnected = false;
        }
    }
}
