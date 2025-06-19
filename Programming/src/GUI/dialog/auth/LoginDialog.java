package GUI.dialog.auth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Controller.UserController;
import GUI.Session;
import Model.User;
import GUI.dialog.auth.RegisterDialog;
import java.util.List;

public class LoginDialog extends JDialog {
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private boolean succeeded;

    public LoginDialog(Frame parent) {
        super(parent, "Login", true);
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
        cs.fill = GridBagConstraints.HORIZONTAL;

        JLabel lbUsername = new JLabel("Username: ");
        cs.gridx = 0; cs.gridy = 0; cs.gridwidth = 1;
        panel.add(lbUsername, cs);

        tfUsername = new JTextField(20);
        cs.gridx = 1; cs.gridy = 0; cs.gridwidth = 2;
        panel.add(tfUsername, cs);

        JLabel lbPassword = new JLabel("Password: ");
        cs.gridx = 0; cs.gridy = 1; cs.gridwidth = 1;
        panel.add(lbPassword, cs);

        pfPassword = new JPasswordField(20);
        cs.gridx = 1; cs.gridy = 1; cs.gridwidth = 2;
        panel.add(pfPassword, cs);

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnLogin = new JButton("Login");
        JButton btnRegister = new JButton("Register");
        JButton btnCancel = new JButton("Cancel");

        JPanel bp = new JPanel();
        bp.add(btnLogin);
        bp.add(btnRegister);
        bp.add(btnCancel);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);

        UserController userController = new UserController();

        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = tfUsername.getText().trim();
                String password = new String(pfPassword.getPassword());
                User user = userController.getUserByUsername(username);
                if (user == null) {
                    JOptionPane.showMessageDialog(LoginDialog.this,
                            "Invalid username or password",
                            "Login",
                            JOptionPane.ERROR_MESSAGE);
                    tfUsername.setText("");
                    pfPassword.setText("");
                    succeeded = false;
                } else if (userController.isUserBlocked(username)) {
                    JOptionPane.showMessageDialog(LoginDialog.this,
                            "Tài khoản đã bị chặn",
                            "Login",
                            JOptionPane.ERROR_MESSAGE);
                    tfUsername.setText("");
                    pfPassword.setText("");
                    succeeded = false;
                } else if (userController.validateUser(username, password)) {
                    // handle multiple roles: prompt if more than one
                    List<String> roles = user.getRoles();
                    String chosenRole;
                    if (roles.size() > 1) {
                        chosenRole = (String) JOptionPane.showInputDialog(
                            LoginDialog.this,
                            "Chọn vai trò:",
                            "Role Selection",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            roles.toArray(new String[0]),
                            roles.get(0)
                        );
                        if (chosenRole == null) {
                            succeeded = false;
                            return;
                        }
                    } else {
                        chosenRole = roles.get(0);
                    }
                    user.setRole(chosenRole);
                    Session.currentUser = user;
                    Session.isAdminFlag = "admin".equalsIgnoreCase(chosenRole);
                    Session.isManagerFlag = "product_manager".equalsIgnoreCase(chosenRole);
                    succeeded = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginDialog.this,
                            "Invalid username or password",
                            "Login",
                            JOptionPane.ERROR_MESSAGE);
                    tfUsername.setText("");
                    pfPassword.setText("");
                    succeeded = false;
                }
            }
        });

        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegisterDialog rd = new RegisterDialog((Frame) getParent());
                rd.setVisible(true);
            }
        });

        btnCancel.addActionListener(e -> {
            succeeded = false;
            dispose();
        });
    }

    public boolean isSucceeded() {
        return succeeded;
    }
}
