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

/**
 * Panel hiển thị danh sách và quản lý người dùng
 */
public class UserPanel extends JPanel implements ReloadablePanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private UserController userController; // Thay đổi từ UserDAO sang UserController

    /**
     * Khởi tạo UserPanel
     */
    public UserPanel() {
        // Sử dụng UserController thay vì UserDAO
        userController = new UserController();

        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        // Panel chứa nút "Thêm người dùng"
        JPanel header = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAddUser = new JButton("Thêm người dùng");
        header.add(btnAddUser);
        add(header, BorderLayout.NORTH);
        btnAddUser.addActionListener(e -> {
            // Mở RegisterDialog để thêm người dùng mới
            GUI.dialog.auth.RegisterDialog rd = new GUI.dialog.auth.RegisterDialog((Frame)SwingUtilities.getWindowAncestor(UserPanel.this));
            rd.setVisible(true);
            if (rd.isSucceeded()) loadUsers();
        });

        // Thiết lập các cột cho bảng dữ liệu
        String[] cols = {"ID","Username","Email","Phone","Roles","Blocked","Reset Password","Block/Unblock","View","Edit","Delete"};
        tableModel = new DefaultTableModel(cols,0) {
            @Override public boolean isCellEditable(int row,int col) {return false;}
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        // Tải dữ liệu người dùng vào bảng
        loadUsers();

        // Thiết lập hiển thị tùy chỉnh cho cột Roles với HTML đa dòng
        table.getColumn("Roles").setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setVerticalAlignment(SwingConstants.TOP);
                // Đảm bảo nội dung HTML được hiển thị
                label.setText(value != null ? value.toString() : "");
                // Điều chỉnh chiều cao hàng dựa trên nội dung
                int prefHeight = label.getPreferredSize().height;
                if (table.getRowHeight(row) != prefHeight) {
                    table.setRowHeight(row, prefHeight);
                }
                return label;
            }
        });

        // Thiết lập hiển thị nút cho các cột hành động
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
                    if(ok) JOptionPane.showMessageDialog(UserPanel.this,(blocked ? "Đã mở khóa" : "Đã khóa") + " người dùng");
                    loadUsers();
                } else if(c==8){ // View
                    String username = (String)tableModel.getValueAt(r,1);
                    User u = userController.getUserByUsername(username);
                    JOptionPane.showMessageDialog(UserPanel.this,
                        "ID: " + u.getUserId() + "\nUsername: " + u.getUsername() +
                        "\nEmail: " + u.getEmail() + "\nPhone: " + u.getPhone() +
                        "\nRoles: " + String.join(",", u.getRoles()) +
                        "\nBlocked: " + u.isBlocked(),
                        "Chi tiết người dùng", JOptionPane.INFORMATION_MESSAGE);
                } else if(c==9){ // Edit
                    // Mở hộp thoại chỉnh sửa người dùng
                    User u = userController.getUserByUsername(tableModel.getValueAt(r,1).toString());
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

    /**
     * Tải danh sách người dùng từ cơ sở dữ liệu và hiển thị lên bảng
     */
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
                u.isBlocked(),
                null,
                u.isBlocked(),
                null,
                null,
                null
            });
        }
    }

    /**
     * Tải lại dữ liệu người dùng khi có thay đổi
     */
    @Override
    public void reloadData(){
        loadUsers();
    }
}
