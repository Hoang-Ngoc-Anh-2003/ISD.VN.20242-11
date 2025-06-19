package GUI.dialog.auth;

import Controller.UserController;
import Model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterDialog extends JDialog {
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JPasswordField pfConfirm;
    private JTextField tfEmail;
    private JTextField tfPhone;
    private boolean succeeded;

    public RegisterDialog(Frame parent) {
        super(parent, "Register", true);
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

        JLabel lbConfirm = new JLabel("Confirm: ");
        cs.gridx = 0; cs.gridy = 2; cs.gridwidth = 1;
        panel.add(lbConfirm, cs);

        pfConfirm = new JPasswordField(20);
        cs.gridx = 1; cs.gridy = 2; cs.gridwidth = 2;
        panel.add(pfConfirm, cs);

        JLabel lbEmail = new JLabel("Email: ");
        cs.gridx = 0; cs.gridy = 3; cs.gridwidth = 1;
        panel.add(lbEmail, cs);

        tfEmail = new JTextField(20);
        cs.gridx = 1; cs.gridy = 3; cs.gridwidth = 2;
        panel.add(tfEmail, cs);

        JLabel lbPhone = new JLabel("Phone: ");
        cs.gridx = 0; cs.gridy = 4; cs.gridwidth = 1;
        panel.add(lbPhone, cs);

        tfPhone = new JTextField(20);
        cs.gridx = 1; cs.gridy = 4; cs.gridwidth = 2;
        panel.add(tfPhone, cs);

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnRegister = new JButton("Register");
        JButton btnCancel = new JButton("Cancel");
        JPanel bp = new JPanel();
        bp.add(btnRegister);
        bp.add(btnCancel);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);

        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = tfUsername.getText().trim();
                String password = new String(pfPassword.getPassword());
                String confirm = new String(pfConfirm.getPassword());
                String email = tfEmail.getText().trim();
                String phone = tfPhone.getText().trim();
                if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterDialog.this,
                        "Please fill in all required fields", "Register", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!password.equals(confirm)) {
                    JOptionPane.showMessageDialog(RegisterDialog.this,
                        "Passwords do not match", "Register", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                UserController userController = new UserController();
                // Check for duplicate username
                if (userController.isUsernameExists(username)) {
                    JOptionPane.showMessageDialog(RegisterDialog.this,
                        "Username already exists", "Register", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                User user = new User(0, username, password, email, phone, "customer");
                if (userController.registerUser(user)) {
                    JOptionPane.showMessageDialog(RegisterDialog.this,
                        "You have successfully registered.",
                        "Register",
                        JOptionPane.INFORMATION_MESSAGE);
                    succeeded = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(RegisterDialog.this,
                        "Failed to register",
                        "Register",
                        JOptionPane.ERROR_MESSAGE);
                    succeeded = false;
                }
            }
        });

        btnCancel.addActionListener(e -> dispose());
    }

    public boolean isSucceeded() { return succeeded; }
}
