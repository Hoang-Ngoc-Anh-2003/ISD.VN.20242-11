package GUI.dialog.editDialog;

import Controller.UserController;
import Model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class EditUserDialog extends JDialog {
    private final JTextField tfUsername;
    private final JTextField tfEmail;
    private final JTextField tfPhone;
    private final JCheckBox cbBlocked;
    private final JList<String> listRoles;
    private boolean saved;

    public EditUserDialog(Frame parent, User user) {
        super(parent, "Sửa người dùng", true);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5,5,5,5);

        // Username
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Username:"), gbc);
        tfUsername = new JTextField(user.getUsername(), 20);
        gbc.gridx = 1; panel.add(tfUsername, gbc);
        // Email
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Email:"), gbc);
        tfEmail = new JTextField(user.getEmail(), 20);
        gbc.gridx = 1; panel.add(tfEmail, gbc);
        // Phone
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Phone:"), gbc);
        tfPhone = new JTextField(user.getPhone(), 20);
        gbc.gridx = 1; panel.add(tfPhone, gbc);
        // Blocked
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Blocked:"), gbc);
        cbBlocked = new JCheckBox(); cbBlocked.setSelected(user.isBlocked());
        gbc.gridx = 1; panel.add(cbBlocked, gbc);
        // Roles selection
        UserController userController = new UserController();
        List<String> roles = userController.getAllRoles();
        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Roles:"), gbc);
        listRoles = new JList<>(new DefaultListModel<>());
        DefaultListModel<String> model = (DefaultListModel<String>) listRoles.getModel();
        for (String r : roles) model.addElement(r);
        listRoles.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // pre-select existing user roles
        java.util.List<String> userRoles = user.getRoles();
        java.util.List<Integer> idx = new java.util.ArrayList<>();
        for (int i = 0; i < model.size(); i++) {
            if (userRoles.contains(model.get(i))) idx.add(i);
        }
        int[] sel = idx.stream().mapToInt(Integer::intValue).toArray();
        listRoles.setSelectedIndices(sel);
        gbc.gridx = 1; panel.add(new JScrollPane(listRoles), gbc);

        add(panel, BorderLayout.CENTER);

        // Buttons
        JPanel btns = new JPanel();
        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");
        btns.add(btnSave); btns.add(btnCancel);
        add(btns, BorderLayout.SOUTH);

        btnSave.addActionListener((ActionEvent e) -> {
            user.setUsername(tfUsername.getText().trim());
            user.setEmail(tfEmail.getText().trim());
            user.setPhone(tfPhone.getText().trim());
            user.setBlocked(cbBlocked.isSelected());
            user.setRoles(listRoles.getSelectedValuesList());
            if (new UserController().updateUser(user)) {
                saved = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật người dùng", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnCancel.addActionListener(e -> dispose());

        pack(); setLocationRelativeTo(parent);
    }

    public boolean isSaved() { return saved; }
}
