import java.awt.Image;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.util.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class AccountManagement extends javax.swing.JFrame {
    public static int id;
    
    boolean databaseConnected = RunSystem.databaseConnected;
    
    private Connection cn;
    private Statement st;
    private PreparedStatement pst;
    private ResultSet rs;
    
    
    public AccountManagement() {
        initComponents();
        frameDesign("");
        fixImage();
        if(databaseConnected) {
            ConnectToDatabase();
        }
    }
    
    private void frameDesign(String title) {
        setTitle(title);
        ImageIcon icon = new ImageIcon(getClass().getResource("/icons/Gym_Logo.png"));
        setIconImage(icon.getImage());
    }
    
    private void fixImage() {
        Icon i = systemIcon.getIcon();
        ImageIcon icon = (ImageIcon) i;
        Image image = icon.getImage().getScaledInstance(systemIcon.getWidth(), systemIcon.getHeight(), Image.SCALE_SMOOTH);
        systemIcon.setIcon(new ImageIcon(image));
    }
    
    private void loginUser_temporary(String user, String pass) {
        boolean userExist = false;
        
        if(!(user.isEmpty() || pass.isEmpty())) {
            for(int i = 0; i < OfflineData.allUser.size(); i++) {
                String name = OfflineData.allUser.get(i).get("username");
                String pwd = OfflineData.allUser.get(i).get("password");
                String prog = OfflineData.allUser.get(i).get("program");
                
                if(user.equals(name) && pass.equals(pwd)) {
                    setUserID(name, pwd);
                    userExist = true;
                    
                    if(prog.equals("admin")) {
                        AdminPanel admin = new AdminPanel();
                        admin.pack();
                        admin.setLocationRelativeTo(null);
                        admin.setVisible(true);
                    } else {
                        UserPanel userPanel = new UserPanel();
                        userPanel.pack();
                        userPanel.setLocationRelativeTo(null);
                        userPanel.setVisible(true);
                    }
                    dispose();
                    break;
                } else if(user.equals(name) && !pass.equals(pwd)) {
                    userExist = true;
                    JOptionPane.showMessageDialog(null, "The password you entered is incorrect.");
                    break;
                }
            }
            
            if(!userExist) {
                JOptionPane.showMessageDialog(null, "Account not registered.");
            }
            
        } else {
            JOptionPane.showMessageDialog(null, "The username and password cannot be left empty.");
        }
    }
    
    private void registerUser_temporary(String user, String pass, String prog) {
        boolean userExist = false;
        
        if(!(user.isEmpty() || pass.isEmpty())) {
            for(int i = 0; i < OfflineData.allUser.size(); i++) {
                String name = OfflineData.allUser.get(i).get("username");
                String pwd = OfflineData.allUser.get(i).get("password");
                
                if(user.equals(name)) {
                    userExist = true;
                }
            }
            
            if(userExist) {
                JOptionPane.showMessageDialog(null, "Username already registered.");
            } else {
                int newID = 1;
                
                for(int i = 0; i < OfflineData.allUser.size(); i++) {
                    newID++;
                    if(newID == Integer.valueOf(OfflineData.allUser.get(i).get("id"))) {
                        newID++;
                    }
                }
                
                OfflineData offline = new OfflineData();
                offline.newUser = new HashMap<>();
                offline.newUser.put("id", Integer.toString(newID));
                offline.newUser.put("username", user);
                offline.newUser.put("password", pass);
                offline.newUser.put("program", prog);
                offline.allUser.add(offline.newUser);
                
                JOptionPane.showMessageDialog(null, "Account successfully registered.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "The username and password cannot be left empty.");
        }
    }
    
    private void loginUser_database(String user, String pass) {
        boolean userExist = false;
        
        if(!(user.isEmpty() || pass.isEmpty())) {
            try {
                pst = cn.prepareStatement("SELECT * FROM users");
                rs = pst.executeQuery();
                
                while(rs.next()) {
                    String name = rs.getString("username");
                    String pwd = rs.getString("password");
                    String prog = rs.getString("program");
                    
                    if(user.equals(name) && pass.equals(pwd)) {
                        setUserID(name, pwd);
                        userExist = true;
                        
                        if(prog.equals("admin")) {
                            AdminPanel admin = new AdminPanel();
                            admin.pack();
                            admin.setLocationRelativeTo(null);
                            admin.setVisible(true);
                        } else {
                            UserPanel userPanel = new UserPanel();
                            userPanel.pack();
                            userPanel.setLocationRelativeTo(null);
                            userPanel.setVisible(true);
                        }
                        dispose();
                        break;
                    }else if(user.equals(name) && !pass.equals(pwd)) {
                        userExist = true;
                        JOptionPane.showMessageDialog(null, "The password you entered is incorrect.");
                        break;
                    }
                }
                
                if(!userExist) {
                    JOptionPane.showMessageDialog(null, "Account not registered.");
                }
            } catch (SQLException ex) {
                Logger.getLogger(AccountManagement.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "The username and password cannot be left empty.");
        }
    }
    
    private void registerUser_database(String user, String pass, String prog) {
        boolean userExist = false;
        
        if(!(user.isEmpty() || pass.isEmpty())){
            try {
                pst = cn.prepareStatement("SELECT * FROM users");
                rs = pst.executeQuery();
                
                while(rs.next()) {
                    String name = rs.getString("username");
                    String pwd = rs.getString("password");
                    
                    if(user.equals(name)) {
                        userExist = true;
                    }
                }
                
                if(userExist) {
                    JOptionPane.showMessageDialog(null, "Username already registered.");
                } else {
                    try {
                        pst = cn.prepareStatement("INSERT INTO users (username,password,program)VALUES(?,?,?)");
                        pst.setString(1, user);
                        pst.setString(2, pass);
                        pst.setString(3, prog);
                        
                        int ex = pst.executeUpdate();
                        if(ex == 1) {
                            JOptionPane.showMessageDialog(null, "Account successfully registered.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Account failed to register.");
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(AccountManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(AccountManagement.class.getName()).log(Level.SEVERE, null, ex);
            }   
        } else {
            JOptionPane.showMessageDialog(null, "The username and password cannot be left empty.");
        }
    }
    
    private void setUserID(String username, String password) {
        if(databaseConnected) {
            try {
                pst = cn.prepareStatement("SELECT * FROM users");
                rs = pst.executeQuery();

                while(rs.next()) {
                    String name = rs.getString("username");
                    String pwd = rs.getString("password");

                    if (username.equals(name) && password.equals(pwd)) {
                        id = Integer.valueOf(rs.getString("id"));
                        break;
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error saving ID.");
            }
        } else {
            for(int i = 0; i < OfflineData.allUser.size(); i++) {
                String name = OfflineData.allUser.get(i).get("username");
                String pwd = OfflineData.allUser.get(i).get("password");
                
                if (username.equals(name) && password.equals(pwd)) {
                    id = Integer.valueOf(OfflineData.allUser.get(i).get("id"));
                    break;
                }
            }
        }
    }
    
    private void ConnectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            cn = DriverManager.getConnection("jdbc:mysql://localhost/localgym", "root", "");
            st = cn.createStatement();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error connecting to database.\nPlease connect to the database first to avoid any errors from occurring.");
            new RunSystem();
            dispose();
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        edttxt_username = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        edttxt_password = new javax.swing.JPasswordField();
        btn_login = new javax.swing.JButton();
        btn_register = new javax.swing.JButton();
        btn_exit = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        systemIcon = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("GYM or GIN");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Username");

        edttxt_username.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Password");

        edttxt_password.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        btn_login.setText("LOGIN");
        btn_login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_loginActionPerformed(evt);
            }
        });

        btn_register.setText("REGISTER");
        btn_register.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_registerActionPerformed(evt);
            }
        });

        btn_exit.setText("EXIT");
        btn_exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_exitActionPerformed(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Developed by Group 3");

        systemIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        systemIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Gym_Logo.png"))); // NOI18N
        systemIcon.setPreferredSize(new java.awt.Dimension(200, 200));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btn_exit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(btn_register, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn_login, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(edttxt_username)
                            .addComponent(edttxt_password, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(systemIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(edttxt_username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(edttxt_password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_login, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_register, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btn_exit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(systemIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_exitActionPerformed
        dispose();
    }//GEN-LAST:event_btn_exitActionPerformed

    private void btn_loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_loginActionPerformed
        String user = edttxt_username.getText();
        String pass = new String(edttxt_password.getPassword());
        
        if(databaseConnected) {
            loginUser_database(user, pass);
        } else {
            loginUser_temporary(user, pass);
        }
    }//GEN-LAST:event_btn_loginActionPerformed

    private void btn_registerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_registerActionPerformed
        String user = edttxt_username.getText();
        String pass = new String(edttxt_password.getPassword());
        String prog = "Not enrolled";
        
        if(databaseConnected) {
            registerUser_database(user, pass, prog);
        } else {
            registerUser_temporary(user, pass, prog);
        }
    }//GEN-LAST:event_btn_registerActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_exit;
    private javax.swing.JButton btn_login;
    private javax.swing.JButton btn_register;
    private javax.swing.JPasswordField edttxt_password;
    private javax.swing.JTextField edttxt_username;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel systemIcon;
    // End of variables declaration//GEN-END:variables
}
