import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class AdminPanel extends javax.swing.JFrame {
    boolean databaseConnected = RunSystem.databaseConnected;
    
    public Connection cn;
    public Statement st;
    public PreparedStatement pst;
    public ResultSet rs;
    
    
    public AdminPanel() {
        initComponents();
        
        if(databaseConnected) {
            ConnectToDatabase();
            LoadUserData_database();
            LoadPrograms_database();
        } else {
            LoadUserData_offline();
            LoadPrograms_offline();
        }
        
        jLabel1.setText("ADMIN : ID - " + AccountManagement.id);
    }
    
    public void LoadUserData_offline() {
        username_search.removeAllItems();
        
        for(int i = 0; i < OfflineData.allUser.size(); i++) {
            username_search.addItem(OfflineData.allUser.get(i).get("username"));
        }
        
        DisplayAllUsers_offline();
    }
    
    public void DisplayAllUsers_offline() {
        DefaultTableModel df = (DefaultTableModel) table_all_users.getModel();
        df.setRowCount(0);
        
        for(int i = 0; i < OfflineData.allUser.size(); i++) {
            Vector v = new Vector();
            v.add(OfflineData.allUser.get(i).get("id"));
            v.add(OfflineData.allUser.get(i).get("username"));
            v.add(OfflineData.allUser.get(i).get("program"));
            df.addRow(v);
        }
    }
    
    public void LoadPrograms_offline() {
         program_search.removeAllItems();
        
        for(int i = 0; i < OfflineData.allPrograms.size(); i++) {
            program_search.addItem(OfflineData.allPrograms.get(i).get("id"));
        }
        
        DisplayAllPrograms_offline();
    }
    
    public void DisplayAllPrograms_offline() {
        DefaultTableModel df = (DefaultTableModel) table_all_programs.getModel();
        df.setRowCount(0);
        
        for(int i = 0; i < OfflineData.allPrograms.size(); i++) {
            Vector v = new Vector();
            v.add(OfflineData.allPrograms.get(i).get("id"));
            v.add(OfflineData.allPrograms.get(i).get("title"));
            v.add(OfflineData.allPrograms.get(i).get("details"));
            v.add(OfflineData.allPrograms.get(i).get("price"));
            df.addRow(v);
        }
    }
    
    public void findUser_offline() {
        String username = username_search.getSelectedItem().toString();
        for(int i = 0; i < OfflineData.allUser.size(); i++) {
            if (OfflineData.allUser.get(i).get("username").equals(username)) {
                edttxt_user_program.setText(OfflineData.allUser.get(i).get("program").toString());
            }
        }
    }
    
    public void updateUser_offline() {
        String prog = edttxt_user_program.getText();
        String username = username_search.getSelectedItem().toString();
        
        if(!username_search.getSelectedItem().toString().equals("admin")) {
            for(int i = 0; i < OfflineData.allUser.size(); i++) {
                if (OfflineData.allUser.get(i).get("username").equals(username)) {
                    OfflineData.newUser = new HashMap<>();
                    OfflineData.newUser.put("id", OfflineData.allUser.get(i).get("id"));
                    OfflineData.newUser.put("username", OfflineData.allUser.get(i).get("username"));
                    OfflineData.newUser.put("password", OfflineData.allUser.get(i).get("password"));
                    OfflineData.newUser.put("program", prog);
                    OfflineData.allUser.set(i ,OfflineData.newUser);
                    LoadUserData_offline();
                    JOptionPane.showMessageDialog(null, "Record updated.");
                    break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "You cannot update the admin.");
        }
    }
    
    public void deleteUser_offline() {
        String username = username_search.getSelectedItem().toString();
        
        if(!username_search.getSelectedItem().toString().equals("admin")) {
            for(int i = 0; i < OfflineData.allUser.size(); i++) {
                if (OfflineData.allUser.get(i).get("username").equals(username)) {
                    OfflineData.allUser.remove(i);
                    LoadUserData_offline();
                    JOptionPane.showMessageDialog(null, "Record deleted.");
                    break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "You cannot delete the admin.");
        }
    }
    
    public void findProgram_offline() {
        String id = program_search.getSelectedItem().toString();
        for(int i = 0; i < OfflineData.allPrograms.size(); i++) {
            if (OfflineData.allPrograms.get(i).get("id").equals(id)) {
                edttxt_prog_title.setText(OfflineData.allPrograms.get(i).get("title"));
                edttxt_prog_details.setText(OfflineData.allPrograms.get(i).get("details"));
                edttxt_prog_price.setText(OfflineData.allPrograms.get(i).get("price"));
            }
        }
    }
    
    public void addProgram_offline() {
        String title = edttxt_prog_title.getText();
        String details = edttxt_prog_details.getText();
        String price = edttxt_prog_price.getText();
        
        int newID = 1;
        for(int i = 0; i < OfflineData.allPrograms.size(); i++) {
            newID++;
            if(newID == Integer.valueOf(OfflineData.allPrograms.get(i).get("id"))) {
                newID++;
            }
        }
        OfflineData.newProgram = new HashMap<>();
        OfflineData.newProgram.put("id", Integer.toString(newID));
        OfflineData.newProgram.put("title", title);
        OfflineData.newProgram.put("details", details);
        OfflineData.newProgram.put("price", price);
        OfflineData.allPrograms.add(OfflineData.newProgram);
        
        edttxt_prog_title.setText("");
        edttxt_prog_details.setText("");
        edttxt_prog_price.setText("");
        
        JOptionPane.showMessageDialog(null, "Record added.");
        
        LoadPrograms_offline();
    }
    
    public void updateProgram_offline() {
        try {
            String title = edttxt_prog_title.getText();
            String details = edttxt_prog_details.getText();
            String price = edttxt_prog_price.getText();
            String id = program_search.getSelectedItem().toString();
            for(int i = 0; i < OfflineData.allPrograms.size(); i++) {
                if(OfflineData.allPrograms.get(i).get("id").equals(id)) {
                    OfflineData.newProgram = new HashMap<>();
                    OfflineData.newProgram.put("id", OfflineData.allPrograms.get(i).get("id"));
                    OfflineData.newProgram.put("title", title);
                    OfflineData.newProgram.put("details", details);
                    OfflineData.newProgram.put("price", price);
                    OfflineData.allPrograms.set(i, OfflineData.newProgram);
                    LoadPrograms_offline();

                    edttxt_prog_title.setText("");
                    edttxt_prog_details.setText("");
                    edttxt_prog_price.setText("");

                    JOptionPane.showMessageDialog(null, "Record updated.");
                    break;
                }
            }
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Record failed to update.");
        }
    }
    
    public void deleteProgram_offline() {
        try {
            String id = program_search.getSelectedItem().toString();
            for(int i = 0; i < OfflineData.allPrograms.size(); i++) {
                if (OfflineData.allPrograms.get(i).get("id").equals(id)) {
                    OfflineData.allPrograms.remove(i);
                    LoadPrograms_offline();
                    JOptionPane.showMessageDialog(null, "Record deleted.");
                    break;
                }
            }
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Record failed to delete.");
        }
    }
    
    public void ConnectToDatabase() {
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
    
    public void LoadPrograms_database() {
        program_search.removeAllItems();
        try {
            pst = cn.prepareStatement("SELECT id FROM programs");
            rs = pst.executeQuery();
            while(rs.next()) {
                program_search.addItem(rs.getString(1));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AdminPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        DisplayAllPrograms_database();
    }
    
    public void DisplayAllPrograms_database() {
        int size;
        DefaultTableModel df = (DefaultTableModel) table_all_programs.getModel();
        df.setRowCount(0);
        
        try {
            pst = cn.prepareStatement("SELECT * FROM programs");
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
    
    public void LoadUserData_database() {
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
        DisplayAllUsers_database();
    }
    
    public void DisplayAllUsers_database() {
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
                    v.add(rs.getString("program"));
                }
                df.addRow(v);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void findUser_database() {
        try {
            String username = username_search.getSelectedItem().toString();
            pst = cn.prepareStatement("SELECT * FROM users WHERE username=?");
            pst.setString(1, username);
            rs = pst.executeQuery();
            
            if(rs.next() == true) {
                edttxt_user_program.setText(rs.getString(4));
            } else {
                JOptionPane.showMessageDialog(null, "No record found.");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AdminPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateUser_database() {
        String prog = edttxt_user_program.getText();
        String username = username_search.getSelectedItem().toString();
        
        if(!username_search.getSelectedItem().toString().equals("admin")) {
            try {
                pst = cn.prepareStatement("UPDATE users SET program=? WHERE username=?");
                pst.setString(1, prog);
                pst.setString(2, username);

                int ex = pst.executeUpdate();
                if(ex == 1) {
                    JOptionPane.showMessageDialog(null, "Record updated.");
                    edttxt_user_program.setText("");
                    LoadUserData_database();
                } else {
                    JOptionPane.showMessageDialog(null, "Record failed to update.");
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "You cannot update the admin.");
        }
    }
    
    public void deleteUser_database() {
        String username = username_search.getSelectedItem().toString();
        
        if(!username_search.getSelectedItem().toString().equals("admin")) {
            try {
                pst = cn.prepareStatement("DELETE FROM users WHERE username=?");
                pst.setString(1, username);

                int ex = pst.executeUpdate();
                if(ex == 1) {
                    JOptionPane.showMessageDialog(null, "Record deleted.");
                    edttxt_user_program.setText("");
                    LoadUserData_database();
                } else {
                    JOptionPane.showMessageDialog(null, "Record failed to delete.");
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "You cannot delete the admin.");
        }
    }
    
    public void findProgram_database() {
        try {
            String user_id = program_search.getSelectedItem().toString();
            pst = cn.prepareStatement("SELECT * FROM programs WHERE id=?");
            pst.setString(1, user_id);
            rs = pst.executeQuery();
            
            if(rs.next() == true) {
                edttxt_prog_title.setText(rs.getString(1));
                edttxt_prog_details.setText(rs.getString(2));
                edttxt_prog_price.setText(rs.getString(3));
            } else {
                JOptionPane.showMessageDialog(null, "No record found.");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AdminPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addProgram_database() {
        String title = edttxt_prog_title.getText();
        String details = edttxt_prog_details.getText();
        String price = edttxt_prog_price.getText();
        
        try {
            pst = cn.prepareStatement("INSERT INTO programs (title,details,price)VALUES(?,?,?)");
            pst.setString(1, title);
            pst.setString(2, details);
            pst.setString(3, price);
            
            int ex = pst.executeUpdate();
            if(ex == 1) {
                JOptionPane.showMessageDialog(null, "Record added.");
                edttxt_prog_title.setText("");
                edttxt_prog_details.setText("");
                edttxt_prog_price.setText("");
                LoadPrograms_database();
            } else {
                JOptionPane.showMessageDialog(null, "Record failed to add.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateProgram_database() {
        String title = edttxt_prog_title.getText();
        String details = edttxt_prog_details.getText();
        String price = edttxt_prog_price.getText();
        String id = program_search.getSelectedItem().toString();
        
        try {
            pst = cn.prepareStatement("UPDATE programs SET title=?,details=?,price=? WHERE id=?");
            pst.setString(1, title);
            pst.setString(2, details);
            pst.setString(3, price);
            pst.setString(4, id);
            
            int ex = pst.executeUpdate();
            if(ex == 1) {
                JOptionPane.showMessageDialog(null, "Record updated.");
                edttxt_prog_title.setText("");
                edttxt_prog_details.setText("");
                edttxt_prog_price.setText("");
                LoadPrograms_database();
            } else {
                JOptionPane.showMessageDialog(null, "Record failed to update.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteProgram_database() {
        String id = program_search.getSelectedItem().toString();
        
        try {
            pst = cn.prepareStatement("DELETE FROM programs WHERE id=?");
            pst.setString(1, id);
            
            int ex = pst.executeUpdate();
            if(ex == 1) {
                JOptionPane.showMessageDialog(null, "Record deleted.");
                edttxt_prog_title.setText("");
                edttxt_prog_details.setText("");
                edttxt_prog_price.setText("");
                LoadPrograms_database();
            } else {
                JOptionPane.showMessageDialog(null, "Record failed to delete.");
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
        edttxt_user_program = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        edttxt_prog_title = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        edttxt_prog_details = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        edttxt_prog_price = new javax.swing.JTextField();
        program_search = new javax.swing.JComboBox<>();
        btn_findProgram = new javax.swing.JButton();
        btn_deleteProgram = new javax.swing.JButton();
        btn_updateProgram = new javax.swing.JButton();
        btn_addProgram = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_all_programs = new javax.swing.JTable();
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

        btn_updateUser.setText("Update");
        btn_updateUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateUserActionPerformed(evt);
            }
        });

        jLabel5.setText("Program:");

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
                "ID", "Username", "Program"
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
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btn_updateUser)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_deleteUser))
                            .addComponent(jLabel4))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edttxt_user_program))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(username_search, 0, 144, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_findUser)))
                        .addGap(25, 25, 25))))
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
                    .addComponent(edttxt_user_program, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_updateUser)
                    .addComponent(btn_deleteUser))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Edit Program:");

        jLabel6.setText("Title:");

        jLabel7.setText("Details:");

        jLabel8.setText("Price:");

        btn_findProgram.setText("Search");
        btn_findProgram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_findProgramActionPerformed(evt);
            }
        });

        btn_deleteProgram.setText("Delete");
        btn_deleteProgram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteProgramActionPerformed(evt);
            }
        });

        btn_updateProgram.setText("Update");
        btn_updateProgram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateProgramActionPerformed(evt);
            }
        });

        btn_addProgram.setText("Add");
        btn_addProgram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addProgramActionPerformed(evt);
            }
        });

        table_all_programs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Program", "Details", "Price"
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
        jScrollPane2.setViewportView(table_all_programs);
        if (table_all_programs.getColumnModel().getColumnCount() > 0) {
            table_all_programs.getColumnModel().getColumn(0).setResizable(false);
            table_all_programs.getColumnModel().getColumn(1).setResizable(false);
            table_all_programs.getColumnModel().getColumn(2).setResizable(false);
            table_all_programs.getColumnModel().getColumn(3).setResizable(false);
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
                        .addComponent(edttxt_prog_price))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edttxt_prog_details, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edttxt_prog_title))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btn_addProgram)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_updateProgram)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_deleteProgram))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(program_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_findProgram, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
                    .addComponent(edttxt_prog_title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(edttxt_prog_details, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(edttxt_prog_price, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(program_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_findProgram)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_deleteProgram)
                    .addComponent(btn_updateProgram)
                    .addComponent(btn_addProgram))
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
        if(databaseConnected) {
            updateUser_database();
        } else {
            updateUser_offline();
        }
    }//GEN-LAST:event_btn_updateUserActionPerformed

    private void btn_findUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_findUserActionPerformed
        if(databaseConnected) {
            findUser_database();
        } else {
            findUser_offline();
        }
    }//GEN-LAST:event_btn_findUserActionPerformed

    private void btn_deleteUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteUserActionPerformed
        if(databaseConnected) {
            deleteUser_database();
        } else {
            deleteUser_offline();
        }
    }//GEN-LAST:event_btn_deleteUserActionPerformed

    private void btn_findProgramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_findProgramActionPerformed
        if(databaseConnected) {
            findProgram_database();
        } else {
            findProgram_offline();
        }
    }//GEN-LAST:event_btn_findProgramActionPerformed

    private void btn_deleteProgramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteProgramActionPerformed
        if(databaseConnected) {
            deleteProgram_database();
        } else {
            deleteProgram_offline();
        }
    }//GEN-LAST:event_btn_deleteProgramActionPerformed

    private void btn_updateProgramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateProgramActionPerformed
        if(databaseConnected) {
            updateProgram_database();
        } else {
            updateProgram_offline();
        }
    }//GEN-LAST:event_btn_updateProgramActionPerformed

    private void btn_addProgramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addProgramActionPerformed
        if(databaseConnected) {
            addProgram_database();
        } else {
            addProgram_offline();
        }
    }//GEN-LAST:event_btn_addProgramActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_addProgram;
    private javax.swing.JButton btn_deleteProgram;
    private javax.swing.JButton btn_deleteUser;
    private javax.swing.JButton btn_findProgram;
    private javax.swing.JButton btn_findUser;
    private javax.swing.JButton btn_updateProgram;
    private javax.swing.JButton btn_updateUser;
    private javax.swing.JTextField edttxt_prog_details;
    private javax.swing.JTextField edttxt_prog_price;
    private javax.swing.JTextField edttxt_prog_title;
    private javax.swing.JTextField edttxt_user_program;
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
    private javax.swing.JComboBox<String> program_search;
    private javax.swing.JTable table_all_programs;
    private javax.swing.JTable table_all_users;
    private javax.swing.JComboBox<String> username_search;
    // End of variables declaration//GEN-END:variables
}
