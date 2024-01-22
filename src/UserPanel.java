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
                txt_programPrice.setText("₱" + OfflineData.allPrograms.get(i).get("price"));
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
                txt_programPrice.setText("₱" + rs.getString(3));
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
                            JOptionPane.showMessageDialog(null, "You are now enrolled to\n\nProgram:\n" + OfflineData.allPrograms.get(i).get("title") + "\n\nPrice: ₱" + OfflineData.allPrograms.get(i).get("price") + "\n\nThank you.");
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
                    JOptionPane.showMessageDialog(null, "You are now enrolled to\n\nProgram:\n" + rs.getString(1) + "\n\nPrice: ₱" + rs.getString(3) + "\n\nThank you.");
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
        jPanel7 = new javax.swing.JPanel();
        btn_signout = new javax.swing.JButton();
        txt_username = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txt_userProgram = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        program_search = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        btn_enroll = new javax.swing.JButton();
        txt_programPrice = new javax.swing.JLabel();
        txt_programDetails = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setPreferredSize(new java.awt.Dimension(700, 450));

        jPanel7.setBackground(new java.awt.Color(51, 51, 51));

        btn_signout.setBackground(new java.awt.Color(32, 28, 28));
        btn_signout.setForeground(new java.awt.Color(255, 255, 255));
        btn_signout.setText("SIGN OUT");
        btn_signout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_signoutActionPerformed(evt);
            }
        });

        txt_username.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txt_username.setText("@username");

        jLabel1.setText("GYM or GIN Fitness");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(btn_signout)
                        .addGap(9, 9, 9))
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txt_username)
                        .addComponent(jLabel1)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txt_username)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 228, Short.MAX_VALUE)
                .addComponent(btn_signout)
                .addGap(56, 56, 56))
        );

        txt_userProgram.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txt_userProgram.setText("null");

        jLabel6.setText("Details:");

        jPanel6.setBackground(new java.awt.Color(86, 86, 86));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 559, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Enroll");

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

        program_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                program_searchActionPerformed(evt);
            }
        });

        jLabel4.setText("Program:");

        btn_enroll.setText("ENROLL");
        btn_enroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_enrollActionPerformed(evt);
            }
        });

        txt_programPrice.setText("₱0");

        txt_programDetails.setText("null");

        jLabel2.setText("Your program:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txt_userProgram))
                        .addContainerGap())
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(jLabel3)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(55, 55, 55)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(txt_programDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txt_programPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addContainerGap())
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(program_search, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_enroll)
                        .addGap(24, 24, 24))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 6, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(37, 37, 37)
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
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(program_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_enroll))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_programPrice)
                .addGap(31, 31, 31)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_programDetails)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 363, Short.MAX_VALUE)
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
    private javax.swing.JPanel jPanel7;
    private javax.swing.JComboBox<String> program_search;
    private javax.swing.JLabel txt_programDetails;
    private javax.swing.JLabel txt_programPrice;
    private javax.swing.JLabel txt_userProgram;
    private javax.swing.JLabel txt_username;
    // End of variables declaration//GEN-END:variables
}
