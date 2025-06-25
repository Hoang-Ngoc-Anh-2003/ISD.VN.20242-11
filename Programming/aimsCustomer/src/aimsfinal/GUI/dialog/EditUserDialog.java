package aimsfinal.GUI.dialog;

import aimsfinal.Controller.UserController;
import aimsfinal.Model.User;

import javax.swing.*;
import java.awt.*;

public class EditUserDialog extends JDialog {
    private JTextField usernameField;
    private JTextField emailField;
    private JTextField fullNameField;
    private JTextField phoneField;
    private JTextField addressField;
    private JButton saveButton;
    private JButton cancelButton;
    private User user;
    private UserController userController;
    private boolean updated;

    public EditUserDialog(Frame parent, User user) {
        super(parent, "Edit User", true);
        this.user = user;
        this.userController = new UserController();
        initializeComponents();
        setupLayout();
        setupListeners();
        loadUserData();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initializeComponents() {
        usernameField = new JTextField(20);
        emailField = new JTextField(20);
        fullNameField = new JTextField(20);
        phoneField = new JTextField(20);
        addressField = new JTextField(30);
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(fullNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        formPanel.add(addressField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupListeners() {
        saveButton.addActionListener(e -> saveUser());
        cancelButton.addActionListener(e -> dispose());
    }

    private void loadUserData() {
        usernameField.setText(user.getUsername());
        emailField.setText(user.getEmail());
        fullNameField.setText(user.getFullName());
        phoneField.setText(user.getPhone());
        addressField.setText(user.getAddress());
    }

    private void saveUser() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();

        if (username.isEmpty() || email.isEmpty() || fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all required fields.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        user.setUsername(username);
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setAddress(address);

        if (userController.updateUser(user)) {
            JOptionPane.showMessageDialog(this,
                "User updated successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            updated = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to update user.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isUpdated() {
        return updated;
    }
}