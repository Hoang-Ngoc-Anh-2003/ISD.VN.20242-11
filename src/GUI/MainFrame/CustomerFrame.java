package GUI.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.Serial;

import GUI.Panel.BookPanel;
import GUI.Panel.CDPanel;
import GUI.Panel.DVDPanel;
import GUI.Panel.LPPanel;
import GUI.Session;
import GUI.dialog.auth.LoginDialog;

public class CustomerFrame extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;

    // Các màu sắc sử dụng trong dashboard
    private static final Color PRIMARY_COLOR = new Color(63, 81, 181);  // Indigo
    private static final Color ACCENT_COLOR = new Color(255, 64, 129);  // Pink
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light Grey
	// Dark Grey
	private static final Color TAB_SELECTED_COLOR = new Color(255, 255, 255); // White
    private static final Color TEXT_COLOR = new Color(33, 33, 33);     // Dark Grey
	// Indigo

	public CustomerFrame() {
        setTitle("Cửa hàng Media - Trang chủ");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Thiết lập panel chính với màu nền
        JPanel contentPane = new JPanel(new BorderLayout(0, 0));
        contentPane.setBackground(BACKGROUND_COLOR);
        setContentPane(contentPane);

        // Tạo header đẹp mắt
        JPanel header = createHeader();
        contentPane.add(header, BorderLayout.NORTH);

        // Tạo tabbed pane chính với các danh mục sản phẩm
        JTabbedPane tabbedPane = createTabbedPane();
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        // Thêm footer
        JPanel footer = createFooter();
        contentPane.add(footer, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_COLOR);
        header.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Logo và tiêu đề
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("CỬA HÀNG MEDIA");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        titlePanel.add(title);

        // Panel phải chứa nút đăng xuất
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);

        JButton btnLogout = getJButton();

        rightPanel.add(btnLogout);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    private JButton getJButton() {
        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnLogout.setBackground(ACCENT_COLOR);
        btnLogout.setForeground(Color.BLACK);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));

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
        return btnLogout;
    }

    private JTabbedPane createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14));
        tabbedPane.setBackground(BACKGROUND_COLOR);
        tabbedPane.setForeground(TEXT_COLOR);

        // Tùy chỉnh UI cho các tab
        UIManager.put("TabbedPane.selected", TAB_SELECTED_COLOR);
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
        UIManager.put("TabbedPane.tabAreaInsets", new Insets(5, 5, 0, 5));

        // Thêm các panel không có icon
        tabbedPane.addTab("Sách", new BookPanel());
        tabbedPane.addTab("DVD", new DVDPanel());
        tabbedPane.addTab("CD", new CDPanel());
        tabbedPane.addTab("LP", new LPPanel());

        return tabbedPane;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(PRIMARY_COLOR);
        footer.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel footerText = new JLabel("©AIMS Project 20242");
        footerText.setFont(new Font("Arial", Font.PLAIN, 12));
        footerText.setForeground(Color.WHITE);

        footer.add(footerText);

        return footer;
    }
}
