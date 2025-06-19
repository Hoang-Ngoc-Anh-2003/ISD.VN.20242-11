package GUI.MainFrame;

import javax.swing.*;
import java.awt.*;
import GUI.Panel.UserPanel;
import GUI.Session;
import GUI.dialog.auth.LoginDialog;

public class AdminFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    public AdminFrame() {
        setTitle("Admin Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        // Logout header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.addActionListener(e -> {
            dispose();
            LoginDialog loginDlg = new LoginDialog(null);
            loginDlg.setVisible(true);
            if (loginDlg.isSucceeded()) {
                if (Session.isAdminFlag) new AdminFrame().setVisible(true);
                else if (Session.isManagerFlag) new ProductManagerFrame().setVisible(true);
                else new CustomerFrame().setVisible(true);
            } else {
                System.exit(0);
            }
        });
        header.add(btnLogout);
        contentPane.add(header, BorderLayout.NORTH);

        // Add user management panel
        UserPanel userPanel = new UserPanel();
        contentPane.add(userPanel, BorderLayout.CENTER);

        setVisible(true);
    }
}
