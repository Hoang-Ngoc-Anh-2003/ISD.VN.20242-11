import GUI.MainFrame.AdminFrame;
import GUI.MainFrame.CustomerFrame;
import GUI.MainFrame.ProductManagerFrame;
import GUI.Session;
import GUI.dialog.auth.LoginDialog;
import java.util.logging.Logger;
import java.util.logging.Level;

public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to set Look and Feel", e);
        }
        javax.swing.SwingUtilities.invokeLater(() -> {
            // Show login dialog
            LoginDialog loginDlg = new LoginDialog(null);
            loginDlg.setVisible(true);
            if (loginDlg.isSucceeded()) {
                // Launch frame based on role
                if (Session.isAdminFlag) {
                    new AdminFrame().setVisible(true);
                } else if (Session.isManagerFlag) {
                    new ProductManagerFrame().setVisible(true);
                } else {
                    new CustomerFrame().setVisible(true);
                }
            } else {
                System.exit(0);
            }
        });
    }
}

