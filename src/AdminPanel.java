import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class AdminPanel extends javax.swing.JFrame {
    public Connection cn;
    public Statement st;
    public PreparedStatement pst;
    public ResultSet rs;
    
    
    public AdminPanel() {
        initComponents();
        ConnectToDatabase();
        LoadUserData();
        LoadSubscription();
        
        jLabel1.setText("ADMIN : ID - " + AccountManagement.id);
    }
    
    public void ConnectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            cn = DriverManager.getConnection("jdbc:mysql://localhost/localgym", "root", "");
            st = cn.createStatement();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error connecting to database.\nPlease connect to the database first to avoid any errors from occurring.");
        }
    }
    
    public void LoadSubscription() {
        subscription_search.removeAllItems();
        try {
            pst = cn.prepareStatement("SELECT id FROM subscriptions");
            rs = pst.executeQuery();
            while(rs.next()) {
                subscription_search.addItem(rs.getString(1));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AdminPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        DisplayAllSubscriptions();
    }
    
    public void DisplayAllSubscriptions() {
        int size;
        DefaultTableModel df = (DefaultTableModel) table_all_subs.getModel();
        df.setRowCount(0);
        
        try {
            pst = cn.prepareStatement("SELECT * FROM subscriptions");
            rs = pst.executeQuery();
            ResultSetMetaData rss = rs.getMetaData();
            size = rss.getColumnCount();
            
            while(rs.next()) {
                Vector v = new Vector();
                
                for(int i = 1; i <= size; i++) {
                    v.add(rs.getString("id"));
                    v.add(rs.getString("title"));
                    v.add(rs.getString("details"));
                    v.add(rs.getString("price"));
                }
                df.addRow(v);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void LoadUserData() {
        username_search.removeAllItems();
        try {
            pst = cn.prepareStatement("SELECT username FROM users");
            rs = pst.executeQuery();
            while(rs.next()) {
                username_search.addItem(rs.getString(1));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AdminPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        DisplayAllUsers();
    }
    
    public void DisplayAllUsers() {
        int size;
        DefaultTableModel df = (DefaultTableModel) table_all_users.getModel();
        df.setRowCount(0);
        
        try {
            pst = cn.prepareStatement("SELECT * FROM users");
            rs = pst.executeQuery();
            ResultSetMetaData rss = rs.getMetaData();
            size = rss.getColumnCount();
            
            while(rs.next()) {
                Vector v = new Vector();
                
                for(int i = 1; i <= size; i++) {
                    v.add(rs.getString("id"));
                    v.add(rs.getString("username"));
                    v.add(rs.getString("subscription"));
                }
                df.addRow(v);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        username_search = new javax.swing.JComboBox<>();
        btn_updateUser = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        btn_findUser = new javax.swing.JButton();
        btn_deleteUser = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_all_users = new javax.swing.JTable();
        edttxt_user_subscription = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        edttxt_sub_title = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        edttxt_sub_details = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        edttxt_sub_price = new javax.swing.JTextField();
        subscription_search = new javax.swing.JComboBox<>();
        btn_findSubscription = new javax.swing.JButton();
        btn_deleteSubscription = new javax.swing.JButton();
        btn_updateSubscription = new javax.swing.JButton();
        btn_addSubscription = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_all_subs = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setPreferredSize(new java.awt.Dimension(700, 450));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Admin");

        jButton1.setText("SIGN OUT");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Edit Members:");

        jLabel3.setText("Username:");

        username_search.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "User 1", "User 2", "User 3", "User 4" }));

        btn_updateUser.setText("Update");
        btn_updateUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateUserActionPerformed(evt);
            }
        });

        jLabel5.setText("Subscription:");

        btn_findUser.setText("Search");
        btn_findUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_findUserActionPerformed(evt);
            }
        });

        btn_deleteUser.setText("Delete");
        btn_deleteUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteUserActionPerformed(evt);
            }
        });

        table_all_users.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Username", "Subscription"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(table_all_users);
        if (table_all_users.getColumnModel().getColumnCount() > 0) {
            table_all_users.getColumnModel().getColumn(0).setResizable(false);
            table_all_users.getColumnModel().getColumn(1).setResizable(false);
            table_all_users.getColumnModel().getColumn(2).setResizable(false);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btn_updateUser)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_deleteUser))
                            .addComponent(jLabel4))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edttxt_user_subscription))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(username_search, 0, 144, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_findUser)))
                        .addGap(19, 19, 19)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(username_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(btn_findUser))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(edttxt_user_subscription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_updateUser)
                    .addComponent(btn_deleteUser))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Edit Subscription:");

        jLabel6.setText("Title:");

        jLabel7.setText("Details:");

        jLabel8.setText("Price:");

        subscription_search.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sub 1", "Sub 2", "Sub 3", "Sub 4" }));

        btn_findSubscription.setText("Search");
        btn_findSubscription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_findSubscriptionActionPerformed(evt);
            }
        });

        btn_deleteSubscription.setText("Delete");
        btn_deleteSubscription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteSubscriptionActionPerformed(evt);
            }
        });

        btn_updateSubscription.setText("Update");
        btn_updateSubscription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateSubscriptionActionPerformed(evt);
            }
        });

        btn_addSubscription.setText("Add");
        btn_addSubscription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addSubscriptionActionPerformed(evt);
            }
        });

        table_all_subs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Subscription", "Details", "Price"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(table_all_subs);
        if (table_all_subs.getColumnModel().getColumnCount() > 0) {
            table_all_subs.getColumnModel().getColumn(0).setResizable(false);
            table_all_subs.getColumnModel().getColumn(1).setResizable(false);
            table_all_subs.getColumnModel().getColumn(2).setResizable(false);
            table_all_subs.getColumnModel().getColumn(3).setResizable(false);
        }

        jLabel9.setText("ID:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel2)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edttxt_sub_price))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edttxt_sub_details, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edttxt_sub_title))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btn_addSubscription)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_updateSubscription)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_deleteSubscription))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(subscription_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_findSubscription, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(edttxt_sub_title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(edttxt_sub_details, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(edttxt_sub_price, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(subscription_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_findSubscription)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_deleteSubscription)
                    .addComponent(btn_updateSubscription)
                    .addComponent(btn_addSubscription))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(57, 57, 57))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        RunSystem run = new RunSystem();
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btn_updateUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateUserActionPerformed
        String sub = edttxt_user_subscription.getText();
        String username = username_search.getSelectedItem().toString();
        
        if(!username_search.getSelectedItem().toString().equals("admin")) {
            try {
                pst = cn.prepareStatement("UPDATE users SET subscription=? WHERE username=?");
                pst.setString(1, sub);
                pst.setString(2, username);

                int ex = pst.executeUpdate();
                if(ex == 1) {
                    JOptionPane.showMessageDialog(null, "Record updated.");
                    edttxt_user_subscription.setText("");
                    LoadUserData();
                } else {
                    JOptionPane.showMessageDialog(null, "Record failed to update.");
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "You cannot update the admin.");
        }
    }//GEN-LAST:event_btn_updateUserActionPerformed

    private void btn_findUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_findUserActionPerformed
        try {
            String username = username_search.getSelectedItem().toString();
            pst = cn.prepareStatement("SELECT * FROM users WHERE username=?");
            pst.setString(1, username);
            rs = pst.executeQuery();
            
            if(rs.next() == true) {
                edttxt_user_subscription.setText(rs.getString(4));
            } else {
                JOptionPane.showMessageDialog(null, "No record found.");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AdminPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_findUserActionPerformed

    private void btn_deleteUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteUserActionPerformed
        String username = username_search.getSelectedItem().toString();
        
        if(!username_search.getSelectedItem().toString().equals("admin")) {
            try {
                pst = cn.prepareStatement("DELETE FROM users WHERE username=?");
                pst.setString(1, username);

                int ex = pst.executeUpdate();
                if(ex == 1) {
                    JOptionPane.showMessageDialog(null, "Record deleted.");
                    edttxt_user_subscription.setText("");
                    LoadUserData();
                } else {
                    JOptionPane.showMessageDialog(null, "Record failed to delete.");
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "You cannot delete the admin.");
        }
    }//GEN-LAST:event_btn_deleteUserActionPerformed

    private void btn_findSubscriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_findSubscriptionActionPerformed
        try {
            String user_id = subscription_search.getSelectedItem().toString();
            pst = cn.prepareStatement("SELECT * FROM subscriptions WHERE id=?");
            pst.setString(1, user_id);
            rs = pst.executeQuery();
            
            if(rs.next() == true) {
                edttxt_sub_title.setText(rs.getString(1));
                edttxt_sub_details.setText(rs.getString(2));
                edttxt_sub_price.setText(rs.getString(3));
            } else {
                JOptionPane.showMessageDialog(null, "No record found.");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AdminPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_findSubscriptionActionPerformed

    private void btn_deleteSubscriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteSubscriptionActionPerformed
        String user_id = subscription_search.getSelectedItem().toString();
        
        try {
            pst = cn.prepareStatement("DELETE FROM subscriptions WHERE id=?");
            pst.setString(1, user_id);
            
            int ex = pst.executeUpdate();
            if(ex == 1) {
                JOptionPane.showMessageDialog(null, "Record deleted.");
                edttxt_sub_title.setText("");
                edttxt_sub_details.setText("");
                edttxt_sub_price.setText("");
                LoadSubscription();
            } else {
                JOptionPane.showMessageDialog(null, "Record failed to delete.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_deleteSubscriptionActionPerformed

    private void btn_updateSubscriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateSubscriptionActionPerformed
        String title = edttxt_sub_title.getText();
        String details = edttxt_sub_details.getText();
        String price = edttxt_sub_price.getText();
        String id = subscription_search.getSelectedItem().toString();
        
        try {
            pst = cn.prepareStatement("UPDATE subscriptions SET title=?,details=?,price=? WHERE id=?");
            pst.setString(1, title);
            pst.setString(2, details);
            pst.setString(3, price);
            pst.setString(4, id);
            
            int ex = pst.executeUpdate();
            if(ex == 1) {
                JOptionPane.showMessageDialog(null, "Record updated.");
                edttxt_sub_title.setText("");
                edttxt_sub_details.setText("");
                edttxt_sub_price.setText("");
                LoadSubscription();
            } else {
                JOptionPane.showMessageDialog(null, "Record failed to update.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_updateSubscriptionActionPerformed

    private void btn_addSubscriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addSubscriptionActionPerformed
        String title = edttxt_sub_title.getText();
        String details = edttxt_sub_details.getText();
        String price = edttxt_sub_price.getText();
        
        try {
            pst = cn.prepareStatement("INSERT INTO subscriptions (title,details,price)VALUES(?,?,?)");
            pst.setString(1, title);
            pst.setString(2, details);
            pst.setString(3, price);
            
            int ex = pst.executeUpdate();
            if(ex == 1) {
                JOptionPane.showMessageDialog(null, "Record added.");
                edttxt_sub_title.setText("");
                edttxt_sub_details.setText("");
                edttxt_sub_price.setText("");
                LoadSubscription();
            } else {
                JOptionPane.showMessageDialog(null, "Record failed to add.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_addSubscriptionActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_addSubscription;
    private javax.swing.JButton btn_deleteSubscription;
    private javax.swing.JButton btn_deleteUser;
    private javax.swing.JButton btn_findSubscription;
    private javax.swing.JButton btn_findUser;
    private javax.swing.JButton btn_updateSubscription;
    private javax.swing.JButton btn_updateUser;
    private javax.swing.JTextField edttxt_sub_details;
    private javax.swing.JTextField edttxt_sub_price;
    private javax.swing.JTextField edttxt_sub_title;
    private javax.swing.JTextField edttxt_user_subscription;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox<String> subscription_search;
    private javax.swing.JTable table_all_subs;
    private javax.swing.JTable table_all_users;
    private javax.swing.JComboBox<String> username_search;
    // End of variables declaration//GEN-END:variables
}
