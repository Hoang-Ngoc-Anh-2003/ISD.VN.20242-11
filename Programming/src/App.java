package aimsfinal;

import aimsfinal.GUI.CustomerFrame;
import aimsfinal.GUI.ProductManagerFrame;
import aimsfinal.GUI.Session;
import aimsfinal.GUI.dialog.auth.LoginDialog;
import aimsfinal.Util.VnpayCallbackServer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to set Look and Feel", e);
        }
        SwingUtilities.invokeLater(() -> {
            // Show login dialog
            LoginDialog loginDlg = new LoginDialog(null);
            loginDlg.setVisible(true);
            if (loginDlg.isSucceeded()) {
                try {
                    // For testing purposes, we start a server and pass it.
                    // In a real run, MainFrame would handle this.
                    VnpayCallbackServer testServer = new VnpayCallbackServer(8080);
                    testServer.start();
                    
                    if (Session.isManager()) {
                        new ProductManagerFrame().setVisible(true);
                    } else {
                        // Pass the server instance to the CustomerFrame
                        new CustomerFrame(testServer).setVisible(true);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Could not start test server.");
                }
            } else {
                System.exit(0);
            }
        });
    }
}

