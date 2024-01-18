import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class RunSystem {
    public Connection cn;
    public Statement st;
    public PreparedStatement pst;
    public ResultSet rs;
    
    
    public RunSystem() {
        ConnectToDatabase();
        AccountManagement account = new AccountManagement();
        account.pack();
        account.setLocationRelativeTo(null);
        account.setVisible(true);
    }
    
    public static void main(String[] args) {
        RunSystem run = new RunSystem();
    }
    
    public void ConnectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            cn = DriverManager.getConnection("jdbc:mysql://localhost/localgym", "root", "");
            st = cn.createStatement();
            
            JOptionPane.showMessageDialog(null, "Successfully connected to database.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error connecting to database.\nPlease connect to the database first to avoid any errors from occurring.");
        }
    }
}
