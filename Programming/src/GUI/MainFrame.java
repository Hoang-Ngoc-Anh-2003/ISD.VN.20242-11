package aimsfinal.GUI;

import aimsfinal.GUI.dialog.auth.LoginDialog;
import aimsfinal.Model.User;
import aimsfinal.Util.VnpayCallbackServer;
import java.io.IOException;
import javax.swing.*;

public class MainFrame extends JFrame {

    private static VnpayCallbackServer callbackServer;

    public MainFrame() {
        // This frame is now just a dispatcher, it's not visible itself.
        // It shows the login dialog, and based on the result, opens the correct frame.
        LoginDialog loginDialog = new LoginDialog(this);
        loginDialog.setVisible(true);

        if (loginDialog.isSucceeded()) {
            User loggedInUser = Session.getCurrentUser();
            if (loggedInUser != null) {
                if (Session.isManager()) {
                    new ProductManagerFrame().setVisible(true);
                } else if (Session.isCustomer()) {
                    // Pass the running server to the CustomerFrame
                    new CustomerFrame(callbackServer).setVisible(true);
                } else {
                     JOptionPane.showMessageDialog(null, "User role is not recognized.", "Login Error", JOptionPane.ERROR_MESSAGE);
                     System.exit(1);
                }
            }
        } else {
            // User cancelled login
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        // Start the VNPay callback server ONCE at the beginning.
        try {
            callbackServer = new VnpayCallbackServer(8080);
            callbackServer.start();
        } catch (IOException e) {
            System.err.println("Failed to start VNPay callback server: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Could not start payment server. Exiting.", "Fatal Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        
        // Schedule the GUI to be created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // The MainFrame constructor will handle the login and dispatching.
            // We don't need to keep a reference to it or set it visible.
            new MainFrame();
        });
    }
}