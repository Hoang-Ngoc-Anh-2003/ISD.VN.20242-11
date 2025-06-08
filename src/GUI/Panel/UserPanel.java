package GUI.Panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Comparator;
import DAO.UserDAO;
import Model.User;
import Interface.ReloadablePanel;
import GUI.dialog.editDialog.EditUserDialog;

public class UserPanel extends JPanel implements ReloadablePanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private UserDAO userDAO;

    public UserPanel() {
        userDAO = new UserDAO();
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        // Header with Add User button
        JPanel header = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAddUser = new JButton("Thêm người dùng");
        header.add(btnAddUser);
        add(header, BorderLayout.NORTH);
        btnAddUser.addActionListener(e -> {
            // open RegisterDialog for new customer
            GUI.dialog.auth.RegisterDialog rd = new GUI.dialog.auth.RegisterDialog((Frame)SwingUtilities.getWindowAncestor(UserPanel.this));
            rd.setVisible(true);
            if (rd.isSucceeded()) loadUsers();
        });

        String[] cols = {"ID","Username","Email","Phone","Roles","Blocked","Reset Password","Block/Unblock","View","Edit","Delete"};
        tableModel = new DefaultTableModel(cols,0) {
            @Override public boolean isCellEditable(int row,int col) {return false;}
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        loadUsers();
        // Custom renderer to display Roles column with HTML multiline and adjust row height
        table.getColumn("Roles").setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setVerticalAlignment(SwingConstants.TOP);
                // Ensure HTML content is rendered
                label.setText(value != null ? value.toString() : "");
                // Adjust row height based on content height
                int prefHeight = label.getPreferredSize().height;
                if (table.getRowHeight(row) != prefHeight) {
                    table.setRowHeight(row, prefHeight);
                }
                return label;
            }
        });

        // renderer for Reset, Block/Unblock, View, Edit, Delete columns
        table.getColumn("Reset Password").setCellRenderer((tbl,val,sel,focus,r,c) -> {
            JButton btn = new JButton("Reset");
            btn.setBackground(new Color(0, 123, 255)); // blue
            btn.setForeground(Color.WHITE);
            btn.setOpaque(true);
            btn.setBorderPainted(false);
            return btn;
        });
        table.getColumn("Block/Unblock").setCellRenderer((tbl,val,sel,focus,r,c) -> {
            boolean blocked = (Boolean) val;
            JButton btn = new JButton(blocked ? "Unblock" : "Block");
            // green for Unblock, red for Block
            btn.setBackground(blocked ? new Color(40, 167, 69) : new Color(220, 53, 69));
            btn.setForeground(Color.WHITE);
            btn.setOpaque(true);
            btn.setBorderPainted(false);
            return btn;
        });
        table.getColumn("View").setCellRenderer((tbl,val,sel,focus,r,c) -> {
            JButton btn = new JButton("Chi tiết");
            btn.setBackground(new Color(23, 162, 184)); // teal
            btn.setForeground(Color.WHITE);
            btn.setOpaque(true); btn.setBorderPainted(false);
            return btn;
        });
        table.getColumn("Edit").setCellRenderer((tbl,val,sel,focus,r,c) -> {
            JButton btn = new JButton("Sửa");
            btn.setBackground(new Color(0, 123, 255));
            btn.setForeground(Color.WHITE);
            btn.setOpaque(true); btn.setBorderPainted(false);
            return btn;
        });
        table.getColumn("Delete").setCellRenderer((tbl,val,sel,focus,r,c) -> {
            JButton btn = new JButton("Xóa");
            btn.setBackground(new Color(220, 53, 69));
            btn.setForeground(Color.WHITE);
            btn.setOpaque(true); btn.setBorderPainted(false);
            return btn;
        });

        table.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                int c = table.columnAtPoint(e.getPoint());
                int r = table.rowAtPoint(e.getPoint());
                if(r<0) return;
                int userId = (int)tableModel.getValueAt(r,0);
                if(c==6){ // Reset
                    boolean ok = userDAO.resetPassword(userId, "123456");
                    if(ok) JOptionPane.showMessageDialog(UserPanel.this,"Password reset to 123456");
                } else if(c==7){ // Block/Unblock
                    boolean blocked = (Boolean)tableModel.getValueAt(r,5);
                    boolean ok = userDAO.setBlocked(userId, !blocked);
                    if(ok) JOptionPane.showMessageDialog(UserPanel.this,(blocked?"Unblocked":"Blocked")+" user");
                    loadUsers();
                } else if(c==8){ // View
                    User u = userDAO.getUserByUsername((String)tableModel.getValueAt(r,1));
                    JOptionPane.showMessageDialog(UserPanel.this,
                        "ID: " + u.getUserId() + "\nUsername: " + u.getUsername() +
                        "\nEmail: " + u.getEmail() + "\nPhone: " + u.getPhone() +
                        "\nRoles: " + String.join(",", u.getRoles()) +
                        "\nBlocked: " + u.isBlocked(),
                        "Chi tiết người dùng", JOptionPane.INFORMATION_MESSAGE);
                } else if(c==9){ // Edit
                    // Open edit user dialog
                    User u = userDAO.getUserByUsername(tableModel.getValueAt(r,1).toString());
                    EditUserDialog edt = new EditUserDialog((Frame) SwingUtilities.getWindowAncestor(UserPanel.this), u);
                    edt.setVisible(true);
                    if (edt.isSaved()) {
                        loadUsers();
                    }
                } else if(c==10){ // Delete
                    int confirm = JOptionPane.showConfirmDialog(UserPanel.this,
                        "Delete user " + userId + "?","Confirm",JOptionPane.YES_NO_OPTION);
                    if(confirm==JOptionPane.YES_OPTION){
                        if(userDAO.deleteUser(userId)) loadUsers();
                    }
                }
            }
        });
    }

    public void loadUsers(){
        List<User> users = userDAO.getAllUsers();
        // sort by role priority: admin first, then product_manager, then customer
        users.sort(Comparator.comparingInt(u -> {
            if (u.getRoles().contains("admin")) return 1;
            if (u.getRoles().contains("product_manager")) return 2;
            if (u.getRoles().contains("customer")) return 3;
            return 4;
        }));
        tableModel.setRowCount(0);
        for (User u : users) {
            // render roles each on its own line
            String rolesHtml = "<html>" + String.join("<br>", u.getRoles()) + "</html>";
            tableModel.addRow(new Object[]{
                u.getUserId(),
                u.getUsername(),
                u.getEmail(),
                u.getPhone(),
                rolesHtml,
                u.isBlocked(),
                null,
                u.isBlocked(),
                null,
                null,
                null
            });
        }
    }

    @Override
    public void reloadData(){ loadUsers(); }
}
