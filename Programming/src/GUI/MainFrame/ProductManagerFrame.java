package GUI.MainFrame;

import javax.swing.*;
import java.awt.*;
import Controller.*;
import GUI.Panel.*;
import GUI.Session;
import GUI.dialog.auth.LoginDialog;

public class ProductManagerFrame extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JLabel lblTitle;
    private int bookCount, dvdCount, cdCount, lpCount;

    public ProductManagerFrame() {
        // load counts
        BookController bookController = new BookController();
        DVDController dvdController = new DVDController();
        CDController cdController = new CDController();
        LPController lpController = new LPController();
        bookCount = bookController.countProducts("Books");
        dvdCount = dvdController.countProducts("DVDs");
        cdCount = cdController.countProducts("CDs");
        lpCount = lpController.countProducts("LPs");

        setTitle("Product Manager Dashboard");
        setSize(1200,700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setBackground(new Color(22,33,53));

        JButton btnHome = createSidebarButton("\uD83C\uDFE0 Trang chủ", true);
        JLabel lblProdMgmt = new JLabel("Quản lý sản phẩm");
        lblProdMgmt.setForeground(Color.WHITE);
        lblProdMgmt.setBorder(BorderFactory.createEmptyBorder(10,20,10,10));
        JButton btnBooks = createSidebarButton("Danh sách Book", false);
        JButton btnDVDs = createSidebarButton("Danh sách DVD", false);
        JButton btnCDs = createSidebarButton("Danh sách CD", false);
        JButton btnLPs = createSidebarButton("Danh sách LP", false);

        sidebar.add(btnHome);
        sidebar.add(lblProdMgmt);
        sidebar.add(btnBooks);
        sidebar.add(btnDVDs);
        sidebar.add(btnCDs);
        sidebar.add(btnLPs);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(950,70));
        JPanel headerLeft = new JPanel(new GridBagLayout());
        headerLeft.setPreferredSize(new Dimension(250,70));
        headerLeft.setBackground(new Color(22,33,53));
        JLabel lblApp = new JLabel("AIMS", SwingConstants.CENTER);
        lblApp.setForeground(Color.WHITE);
        lblApp.setFont(new Font("Arial",Font.BOLD,20));
        headerLeft.add(lblApp);
        JPanel headerCenter = new JPanel(new FlowLayout(FlowLayout.LEFT,20,20));
        headerCenter.setBackground(new Color(0,123,255));
        lblTitle = new JLabel("Trang chủ");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial",Font.BOLD,20));
        headerCenter.add(lblTitle);
        header.add(headerLeft,BorderLayout.WEST);
        header.add(headerCenter,BorderLayout.CENTER);
        // Logout button
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
        header.add(btnLogout, BorderLayout.EAST);

        // Main panel
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(new Color(240,242,245));

        // Home with counts
        JPanel homePanel = new JPanel(new GridLayout(2,2,20,20));
        homePanel.setBackground(new Color(240,242,245));
        homePanel.add(createCategoryButton("Book", bookCount));
        homePanel.add(createCategoryButton("DVD", dvdCount));
        homePanel.add(createCategoryButton("CD", cdCount));
        homePanel.add(createCategoryButton("LP", lpCount));

        // product panels
        BookPanel bookPanel = new BookPanel();
        DVDPanel dvdPanel = new DVDPanel();
        CDPanel cdPanel = new CDPanel();
        LPPanel lpPanel = new LPPanel();

        mainPanel.add(homePanel, "Home");
        mainPanel.add(bookPanel, "Books");
        mainPanel.add(dvdPanel, "DVDs");
        mainPanel.add(cdPanel, "CDs");
        mainPanel.add(lpPanel, "LPs");

        // actions
        btnHome.addActionListener(e -> switchPanel("Home","Trang chủ"));
        btnBooks.addActionListener(e -> switchPanel("Books","Danh sách Book"));
        btnDVDs.addActionListener(e -> switchPanel("DVDs","Danh sách DVD"));
        btnCDs.addActionListener(e -> switchPanel("CDs","Danh sách CD"));
        btnLPs.addActionListener(e -> switchPanel("LPs","Danh sách LP"));
        // category buttons also navigate
        btnHome.addActionListener(e -> switchPanel("Home","Trang chủ"));

        contentPane.add(sidebar,BorderLayout.WEST);
        contentPane.add(header,BorderLayout.NORTH);
        contentPane.add(mainPanel,BorderLayout.CENTER);

        // default page
        if (Session.isManagerFlag || Session.isAdminFlag) {
            switchPanel("Books","Danh sách Book");
        } else {
            switchPanel("Home","Trang chủ");
        }
        setVisible(true);
    }

    private JButton createSidebarButton(String name, boolean hasIcon) {
        JButton btn = new JButton(name);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(250,40));
        btn.setBackground(new Color(33,43,54));
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        if (!hasIcon) {
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setBorder(BorderFactory.createEmptyBorder(5,20,5,5));
        }
        return btn;
    }

    private JButton createCategoryButton(String cat, int count) {
        JButton btn = new JButton("<html><center>"+cat+"<br>"+count+"</center></html>");
        btn.setFont(new Font("Arial",Font.BOLD,16));
        btn.setPreferredSize(new Dimension(180,120));
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,1));
        btn.addActionListener(e -> switchPanel(cat + (cat.equals("Book")?"s":"s"),"Danh sách " + cat));
        return btn;
    }

    private void switchPanel(String name, String title) {
        cardLayout.show(mainPanel,name);
        lblTitle.setText(title);
    }
}
