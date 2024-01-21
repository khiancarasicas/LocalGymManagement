import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class UserPanel extends javax.swing.JFrame {
    boolean databaseConnected = RunSystem.databaseConnected;
    
    private final int userID = AccountManagement.id;
    
    private Connection cn;
    private Statement st;
    private PreparedStatement pst;
    private ResultSet rs;
    
    public UserPanel() {
        initComponents();
        if(databaseConnected) {
            ConnectToDatabase();
            loadUserData_database();
            loadPrograms_database();
        } else {
            loadUserData_offline();
            LoadPrograms_offline();
        }
    }
    
    private void loadUserData_offline() {
        for(int i = 0; i < OfflineData.allUser.size(); i++) {
            String id = OfflineData.allUser.get(i).get("id");
            if(Integer.toString(userID).equals(id)) {
                txt_username.setText("@" + OfflineData.allUser.get(i).get("username"));
                txt_userProgram.setText(OfflineData.allUser.get(i).get("program"));
                break;
            }
        }
    }
    
    private void LoadPrograms_offline() {
        program_search.removeAllItems();
        
        for(int i = 0; i < OfflineData.allPrograms.size(); i++) {
            program_search.addItem(OfflineData.allPrograms.get(i).get("title"));
        }
    }
    
    private void findProgram_offline() {
        try {
        String prog_title = program_search.getSelectedItem().toString();
        for(int i = 0; i < OfflineData.allPrograms.size(); i++) {
            if (OfflineData.allPrograms.get(i).get("title").equals(prog_title)) {
                txt_programDetails.setText(OfflineData.allPrograms.get(i).get("details"));
                txt_programPrice.setText("$" + OfflineData.allPrograms.get(i).get("price"));
            }
        }
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured.");
        }
    }
    
    private void loadUserData_database() {
        try {
            pst = cn.prepareStatement("SELECT * FROM users");
            rs = pst.executeQuery();
            
            while(rs.next()) {
                String name = rs.getString("username");
                String id = rs.getString("id");
                String prog = rs.getString("program");
                
                if(Integer.toString(userID).equals(id)) {
                    txt_username.setText("@" + name);
                    txt_userProgram.setText(prog);
                    break;
                }
            }
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error login user.");
            new RunSystem();
            dispose();
        }
    }
    
    private void loadPrograms_database() {
        program_search.removeAllItems();
        try {
            pst = cn.prepareStatement("SELECT title FROM programs");
            rs = pst.executeQuery();
            while(rs.next()) {
                program_search.addItem(rs.getString(1));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AdminPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void findProgram_database() {
        try {
            String prog_title = program_search.getSelectedItem().toString();
            pst = cn.prepareStatement("SELECT * FROM programs WHERE title=?");
            pst.setString(1, prog_title);
            rs = pst.executeQuery();
            
            if(rs.next() == true) {
                txt_programDetails.setText(rs.getString(2));
                txt_programPrice.setText("$" + rs.getString(3));
            } else {
                JOptionPane.showMessageDialog(null, "An error occured.");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AdminPanel.class.getName()).log(Level.SEVERE, null, ex);
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
    
    private void enroll_offline() {
        try {
            String prog_title = program_search.getSelectedItem().toString();
            
            for(int i = 0; i < OfflineData.allPrograms.size(); i++) {
                if (OfflineData.allPrograms.get(i).get("title").equals(prog_title)) {
                    for(int j = 0; j < OfflineData.allUser.size(); j++) {
                        if (Integer.toString(userID).equals(OfflineData.allUser.get(j).get("id"))) {
                            OfflineData.newUser = new HashMap<>();
                            OfflineData.newUser.put("id", OfflineData.allUser.get(j).get("id"));
                            OfflineData.newUser.put("username", OfflineData.allUser.get(j).get("username"));
                            OfflineData.newUser.put("password", OfflineData.allUser.get(j).get("password"));
                            OfflineData.newUser.put("program", prog_title);
                            OfflineData.allUser.set(j ,OfflineData.newUser);
                            JOptionPane.showMessageDialog(null, "You are now enrolled to\n\nProgram:\n" + OfflineData.allPrograms.get(i).get("title") + "\n\nPrice: $" + OfflineData.allPrograms.get(i).get("price") + "\n\nThank you.");
                            loadUserData_offline();
                            break;
                        }
                    }
                }
            }
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured.");
        }
    }
    
    private void enroll_database() {
        String prog_title = program_search.getSelectedItem().toString();
        try {
            pst = cn.prepareStatement("UPDATE users SET program=? WHERE id=?");
            pst.setString(1, prog_title);
            pst.setString(2, Integer.toString(userID));
            
            int ex = pst.executeUpdate();
            if(ex == 1) {
                pst = cn.prepareStatement("SELECT * FROM programs WHERE title=?");
                pst.setString(1, prog_title);
                rs = pst.executeQuery();
            
                if(rs.next() == true) {
                    JOptionPane.showMessageDialog(null, "You are now enrolled to\n\nProgram:\n" + rs.getString(1) + "\n\nPrice: $" + rs.getString(3) + "\n\nThank you.");
                    loadUserData_database();
                } else {
                    JOptionPane.showMessageDialog(null, "An error occured.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Failed to enroll.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_username = new javax.swing.JLabel();
        btn_signout = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        program_search = new javax.swing.JComboBox<>();
        btn_enroll = new javax.swing.JButton();
        txt_programPrice = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txt_programDetails = new javax.swing.JLabel();
        txt_userProgram = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setPreferredSize(new java.awt.Dimension(700, 450));

        jLabel1.setText("Welcome to GYM or GIN Fitness");

        txt_username.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txt_username.setText("@username");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_username)
                    .addComponent(jLabel1))
                .addGap(242, 242, 242))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(txt_username)
                .addGap(13, 13, 13))
        );

        btn_signout.setText("SIGN OUT");
        btn_signout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_signoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 137, Short.MAX_VALUE)
                .addComponent(btn_signout)
                .addGap(32, 32, 32))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(btn_signout)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(86, 86, 86));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jLabel2.setText("Your program:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Enroll");

        jPanel6.setBackground(new java.awt.Color(86, 86, 86));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jLabel4.setText("Program:");

        program_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                program_searchActionPerformed(evt);
            }
        });

        btn_enroll.setText("ENROLL");
        btn_enroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_enrollActionPerformed(evt);
            }
        });

        txt_programPrice.setText("$0");

        jLabel6.setText("Details:");

        txt_programDetails.setText("null");

        txt_userProgram.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txt_userProgram.setText("null");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txt_userProgram)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_programPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(program_search, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(txt_programDetails, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_enroll)
                        .addGap(32, 32, 32))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(0, 0, 0)
                .addComponent(txt_userProgram)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(program_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_enroll))
                .addGap(0, 0, 0)
                .addComponent(txt_programPrice)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_programDetails)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_signoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_signoutActionPerformed
        new RunSystem();
        dispose();
    }//GEN-LAST:event_btn_signoutActionPerformed

    private void btn_enrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_enrollActionPerformed
        if(databaseConnected) {
            enroll_database();
        } else {
            enroll_offline();
        }
    }//GEN-LAST:event_btn_enrollActionPerformed

    private void program_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_program_searchActionPerformed
        if(databaseConnected) {
            findProgram_database();
        } else {
            findProgram_offline();
        }
    }//GEN-LAST:event_program_searchActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_enroll;
    private javax.swing.JButton btn_signout;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JComboBox<String> program_search;
    private javax.swing.JLabel txt_programDetails;
    private javax.swing.JLabel txt_programPrice;
    private javax.swing.JLabel txt_userProgram;
    private javax.swing.JLabel txt_username;
    // End of variables declaration//GEN-END:variables
}
