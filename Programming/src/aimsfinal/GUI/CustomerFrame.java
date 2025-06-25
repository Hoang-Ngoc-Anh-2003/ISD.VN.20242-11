package aimsfinal.GUI;

import aimsfinal.Controller.CheckoutManager;
import aimsfinal.GUI.dialog.auth.LoginDialog;
import aimsfinal.Model.User;
import aimsfinal.Util.VnpayCallbackServer;
import java.awt.*;
import javax.swing.*;

public class CustomerFrame extends JFrame {
    private CustomerShoppingPanel shoppingPanel;
    private JLabel lblUser;
    private User currentUser;
    private CheckoutManager checkoutManager;

    public CustomerFrame(VnpayCallbackServer server) {
        this.currentUser = Session.getCurrentUser();
        
        // The CheckoutManager now handles all logic related to checkout and payment.
        this.checkoutManager = new CheckoutManager(this, this.currentUser);

        if (server != null) {
            // Register the single manager to listen for payment status updates.
            server.addListener(this.checkoutManager);
        }

        setTitle("AIMS - Customer Shopping");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(1200, 60));
        JLabel lblApp = new JLabel("AIMS", SwingConstants.LEFT);
        lblApp.setFont(new Font("Arial", Font.BOLD, 22));
        lblApp.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        header.add(lblApp, BorderLayout.WEST);

        String userName = (currentUser != null) ? currentUser.getFullName() : "";
        lblUser = new JLabel("Xin chào, " + userName, SwingConstants.CENTER);
        lblUser.setFont(new Font("Arial", Font.PLAIN, 16));
        header.add(lblUser, BorderLayout.CENTER);

        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.addActionListener(e -> {
            Session.logout();
            dispose();
            LoginDialog loginDlg = new LoginDialog(null);
            loginDlg.setVisible(true);
            if (loginDlg.isSucceeded()) {
                User loggedIn = Session.getCurrentUser();
                if (loggedIn != null && Session.isManager()) {
                    new ProductManagerFrame().setVisible(true);
                } else {
                    // Pass the server instance to the new frame if needed
                    new CustomerFrame(server).setVisible(true);
                }
            } else {
                System.exit(0);
            }
        });
        header.add(btnLogout, BorderLayout.EAST);

        // Main content
        // Pass the checkout manager to the shopping panel
        shoppingPanel = new CustomerShoppingPanel(checkoutManager);

        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(shoppingPanel, BorderLayout.CENTER);
    }
    
    /**
     * This method is called by the CheckoutManager after a successful transaction
     * to ensure the main UI reflects the changes in the database (e.g., updated stock).
     */
    public void refreshAllData() {
        System.out.println("CustomerFrame received command to refresh all data.");
        if (shoppingPanel != null) {
            shoppingPanel.refreshData();
        }
    }
}
