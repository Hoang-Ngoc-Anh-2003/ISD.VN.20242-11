package GUI.MainFrame;

import javax.swing.*;
import java.awt.*;
import GUI.Panel.BookPanel;
import GUI.Panel.CDPanel;
import GUI.Panel.DVDPanel;
import GUI.Panel.LPPanel;
import GUI.Session;
import GUI.dialog.auth.LoginDialog;
import GUI.MainFrame.AdminFrame;
import GUI.MainFrame.ProductManagerFrame;

public class CustomerFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    public CustomerFrame() {
        setTitle("Customer Dashboard");
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

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Books", new BookPanel());
        tabbedPane.addTab("DVDs", new DVDPanel());
        tabbedPane.addTab("CDs", new CDPanel());
        tabbedPane.addTab("LPs", new LPPanel());

        contentPane.add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);
    }
}
