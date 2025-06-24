package aimsfinal.GUI.dialog;

import aimsfinal.Controller.CheckoutManager;
import aimsfinal.Model.DeliveryInfo;
import aimsfinal.Model.OrderSummary;
import java.awt.*;
import javax.swing.*;

public class PlaceOrderDialog extends JDialog {
    private CheckoutManager checkoutManager;
    private boolean isRush;

    // --- Input fields ---
    private JTextField receiverField;
    private JTextField phoneField;
    private JTextField addressField;
    private JTextField emailField;
    private JComboBox<String> provinceComboBox;
    private JTextField rushHoursField; // For rush order time

    // --- Display fields ---
    private JLabel rushHoursLabel;
    private JTextField subtotalField;
    private JTextField shippingFeeField;
    private JTextField vatField;
    private JTextField totalField;

    private JButton placeOrderButton;
    private JButton cancelButton;

    public PlaceOrderDialog(Frame parent, CheckoutManager manager, boolean isRush) {
        super(parent, "Thông tin Giao hàng & Thanh toán", true);
        this.checkoutManager = manager;
        this.isRush = isRush;
        
        initializeComponents();
        setupLayout();
        setupListeners();
        
        // Initial price calculation
        checkoutManager.updateOrderSummary();
        
        pack();
        setLocationRelativeTo(parent);
    }

    private void initializeComponents() {
        receiverField = new JTextField(25);
        phoneField = new JTextField(25);
        addressField = new JTextField(25);
        emailField = new JTextField(25);
        provinceComboBox = new JComboBox<>(new String[] {"Hà Nội", "Hồ Chí Minh", "Đà Nẵng", "Khác"});
        
        rushHoursLabel = new JLabel("Giao trong vòng (giờ):");
        rushHoursField = new JTextField("24", 5); // Default 24 hours
        
        subtotalField = createDisplayField();
        shippingFeeField = createDisplayField();
        vatField = createDisplayField();
        totalField = createDisplayField();
        totalField.setFont(new Font("Arial", Font.BOLD, 14));

        placeOrderButton = new JButton("Xác nhận & Thanh toán");
        cancelButton = new JButton("Hủy");
    }
    
    private JTextField createDisplayField() {
        JTextField field = new JTextField(15);
        field.setEditable(false);
        field.setFocusable(false);
        field.setBackground(Color.LIGHT_GRAY);
        return field;
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setTitle(isRush ? "Thông tin Giao hàng (Nhanh)" : "Thông tin Giao hàng (Thường)");
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // --- Left side: Delivery Info ---
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Tên người nhận:"), gbc);
        gbc.gridx = 1; formPanel.add(receiverField, gbc);
        gbc.gridy++;
        gbc.gridx = 0; formPanel.add(new JLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1; formPanel.add(phoneField, gbc);
        gbc.gridy++;
        gbc.gridx = 0; formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; formPanel.add(emailField, gbc);
        gbc.gridy++;
        gbc.gridx = 0; formPanel.add(new JLabel("Địa chỉ chi tiết:"), gbc);
        gbc.gridx = 1; formPanel.add(addressField, gbc);
        gbc.gridy++;
        gbc.gridx = 0; formPanel.add(new JLabel("Tỉnh/Thành phố:"), gbc);
        gbc.gridx = 1; formPanel.add(provinceComboBox, gbc);
        
        // --- Right side: Cost Summary ---
        gbc.gridx = 2; gbc.gridy = 0; gbc.insets = new Insets(5, 20, 5, 5);
        formPanel.add(new JLabel("Tiền hàng:"), gbc);
        gbc.gridx = 3; formPanel.add(subtotalField, gbc);
        gbc.gridy++;
        gbc.gridx = 2; formPanel.add(new JLabel("Phí giao hàng:"), gbc);
        gbc.gridx = 3; formPanel.add(shippingFeeField, gbc);
        
        // Add rush hours field if applicable
        if (isRush) {
             gbc.gridy++;
             gbc.gridx = 2; formPanel.add(rushHoursLabel, gbc);
             gbc.gridx = 3; formPanel.add(rushHoursField, gbc);
        }

        gbc.gridy++;
        gbc.gridx = 2; formPanel.add(new JLabel("VAT (10%):"), gbc);
        gbc.gridx = 3; formPanel.add(vatField, gbc);
        gbc.gridy++;
        gbc.gridx = 2; formPanel.add(new JLabel("Tổng thanh toán:"), gbc);
        gbc.gridx = 3; formPanel.add(totalField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(placeOrderButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupListeners() {
        placeOrderButton.addActionListener(e -> placeOrder());
        cancelButton.addActionListener(e -> dispose());

        // Any change in delivery info should trigger a price update
        provinceComboBox.addActionListener(e -> checkoutManager.updateOrderSummary());
        if (isRush) {
            rushHoursField.getDocument().addDocumentListener(new SimpleDocumentListener() {
                @Override
                public void update(javax.swing.event.DocumentEvent e) {
                    checkoutManager.updateOrderSummary();
                }
            });
        }
    }
    
    // --- Getters for manager ---
    public String getSelectedProvince() {
        return (String) provinceComboBox.getSelectedItem();
    }
    
    public int getRushHours() {
        try {
            return Integer.parseInt(rushHoursField.getText());
        } catch (NumberFormatException e) {
            return 24; // Default value if parsing fails
        }
    }

    public boolean isRushOrder() {
        return this.isRush;
    }

    // --- Method for manager to update display ---
    public void updateDisplay(OrderSummary summary) {
        subtotalField.setText(String.format("%,.0f VNĐ", summary.getSubtotal()));
        shippingFeeField.setText(String.format("%,.0f VNĐ", summary.getShippingFee()));
        vatField.setText(String.format("%,.0f VNĐ", summary.getVat()));
        totalField.setText(String.format("%,.0f VNĐ", summary.getTotal()));
    }

    private void placeOrder() {
        // Validation...
        String receiver = receiverField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();
        if (receiver.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin giao hàng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DeliveryInfo deliveryInfo = new DeliveryInfo();
        deliveryInfo.setReceiverName(receiver);
        deliveryInfo.setPhone(phone);
        deliveryInfo.setEmail(email);
        deliveryInfo.setAddress(address);
        deliveryInfo.setProvince(getSelectedProvince());
        deliveryInfo.setShippingMethod(isRush ? "Rush" : "Standard");
        
        checkoutManager.finalizeOrderAndPay(deliveryInfo, isRush, getRushHours());
    }
}