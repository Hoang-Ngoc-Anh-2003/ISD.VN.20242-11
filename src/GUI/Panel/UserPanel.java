package GUI.Panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Comparator;
import Controller.UserController;
import Model.User;
import Interface.ReloadablePanel;
import GUI.dialog.editDialog.EditUserDialog;

// Panel hiển thị danh sách và quản lý người dùng
public class UserPanel extends JPanel implements ReloadablePanel {
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final UserController userController;

    public UserPanel() {
        userController = new UserController();

        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JPanel header = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAddUser = new JButton("Thêm người dùng");
        header.add(btnAddUser);
        add(header, BorderLayout.NORTH);
        btnAddUser.addActionListener(e -> {
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
        table.getColumn("Roles").setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setVerticalAlignment(SwingConstants.TOP);
                label.setText(value != null ? value.toString() : "");
                int prefHeight = label.getPreferredSize().height;
                if (table.getRowHeight(row) != prefHeight) {
                    table.setRowHeight(row, prefHeight);
                }
                return label;
            }
        });

        table.getColumn("Reset Password").setCellRenderer((tbl,val,sel,focus,r,c) -> {
            JButton btn = new JButton("Reset");
            btn.setBackground(new Color(0, 123, 255)); // blue
            btn.setForeground(Color.WHITE);
            btn.setOpaque(true);
            btn.setBorderPainted(false);
            return btn;
        });
        table.getColumn("Block/Unblock").setCellRenderer((tbl,val,sel,focus,r,c) -> {
            boolean blocked = (Boolean) tbl.getValueAt(r, 5);
            JButton btn = new JButton(blocked ? "Unblock" : "Block");
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

        // Xử lý sự kiện khi nhấp vào các nút trong bảng
        table.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                int c = table.columnAtPoint(e.getPoint());
                int r = table.rowAtPoint(e.getPoint());
                if(r<0) return;
                int userId = (int)tableModel.getValueAt(r,0);
                if(c==6){ // Reset Password
                    boolean ok = userController.resetPassword(userId, "123456");
                    if(ok) JOptionPane.showMessageDialog(UserPanel.this,"Đã đặt lại mật khẩu thành 123456");
                } else if(c==7){ // Block/Unblock
                    boolean blocked = (Boolean)tableModel.getValueAt(r,5);
                    boolean ok = userController.setBlocked(userId, !blocked);
                    if(ok) {
                        JOptionPane.showMessageDialog(UserPanel.this,(blocked ? "Đã mở khóa" : "Đã khóa") + " người dùng");
                        loadUsers();
                    } else {
                        JOptionPane.showMessageDialog(UserPanel.this, "Lỗi khi cập nhật trạng thái người dùng", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else if(c==8){ // View
                    String username = (String)tableModel.getValueAt(r,1);
                    User u = userController.findUserByUsername(username);
                    JOptionPane.showMessageDialog(UserPanel.this,
                        "ID: " + u.getUserId() + "\nUsername: " + u.getUsername() +
                        "\nEmail: " + u.getEmail() + "\nPhone: " + u.getPhone() +
                        "\nRoles: " + String.join(",", u.getRoles()) +
                        "\nBlocked: " + u.isBlocked(),
                        "Chi tiết người dùng", JOptionPane.INFORMATION_MESSAGE);
                } else if(c==9){ // Edit
                    User u = userController.findUserByUsername(tableModel.getValueAt(r,1).toString());
                    EditUserDialog edt = new EditUserDialog((Frame) SwingUtilities.getWindowAncestor(UserPanel.this), u);
                    edt.setVisible(true);
                    if (edt.isSaved()) {
                        loadUsers();
                    }
                } else if(c==10){ // Delete
                    int confirm = JOptionPane.showConfirmDialog(UserPanel.this,
                        "Xóa người dùng " + userId + "?","Xác nhận",JOptionPane.YES_NO_OPTION);
                    if(confirm==JOptionPane.YES_OPTION){
                        if(userController.deleteUser(userId)) loadUsers();
                    }
                }
            }
        });
    }

    public void loadUsers(){
        List<User> users = userController.getAllUsers();
        // Sắp xếp theo ưu tiên vai trò: admin trước, sau đó product_manager, cuối cùng là customer
        users.sort(Comparator.comparingInt(u -> {
            if (u.getRoles().contains("admin")) return 1;
            if (u.getRoles().contains("product_manager")) return 2;
            if (u.getRoles().contains("customer")) return 3;
            return 4;
        }));
        tableModel.setRowCount(0);
        for (User u : users) {
            // Hiển thị vai trò mỗi vai trò trên một dòng
            String rolesHtml = "<html>" + String.join("<br>", u.getRoles()) + "</html>";
            tableModel.addRow(new Object[]{
                u.getUserId(),
                u.getUsername(),
                u.getEmail(),
                u.getPhone(),
                rolesHtml,
                u.isBlocked(),  // Cột 5: Blocked status (data)
                null,           // Cột 6: Reset Password button
                null,           // Cột 7: Block/Unblock button (không cần data, chỉ cần renderer)
                null,           // Cột 8: View button
                null,           // Cột 9: Edit button
                null            // Cột 10: Delete button
            });
        }
    }

    @Override
    public void reloadData(){
        loadUsers();
    }
}
