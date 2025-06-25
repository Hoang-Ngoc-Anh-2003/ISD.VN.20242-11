package aimsfinal.GUI.dialog;

import aimsfinal.Controller.OrderController;
import aimsfinal.Model.Order;
import aimsfinal.Model.CartItem;
import aimsfinal.Controller.CartController;
import aimsfinal.GUI.Session;
import aimsfinal.GUI.PaymentPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.sql.SQLException;

public class CheckoutDialog extends JDialog {
    private JTextField receiverField;
    private JTextField phoneField;
    private JTextField addressField;
    private JButton placeOrderButton;
    private JButton rushOrderButton;
    private JButton cancelButton;
    private JLabel totalLabel;
    private boolean orderPlaced;
    private double totalAmount;
    private List<CartItem> cartItems;
    private CartController cartController;
    private OrderController orderController;

    public CheckoutDialog(Frame parent) {
        super(parent, "Checkout", true);
        cartController = new CartController();
        orderController = new OrderController();
        cartItems = cartController.getCartItems();
        totalAmount = cartItems.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
        initializeComponents();
        setupLayout();
        setupListeners();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initializeComponents() {
        receiverField = new JTextField(20);
        phoneField = new JTextField(20);
        addressField = new JTextField(30);
        placeOrderButton = new JButton("Place Order");
        rushOrderButton = new JButton("Place Rush Order");
        cancelButton = new JButton("Cancel");
        totalLabel = new JLabel(String.format("Total: $%.2f", totalAmount));
    }

    private void setupLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Receiver Name:"), gbc);
        gbc.gridx = 1;
        add(receiverField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Phone Number:"), gbc);
        gbc.gridx = 1;
        add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        add(addressField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(totalLabel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(placeOrderButton);
        buttonPanel.add(rushOrderButton);
        buttonPanel.add(cancelButton);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(buttonPanel, gbc);
    }

    private void setupListeners() {
        placeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeOrder(false);
            }
        });
        rushOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeOrder(true);
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void placeOrder(boolean isRush) {
        String receiver = receiverField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();
        if (receiver.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // TODO: Gọi OrderController để tạo đơn hàng (và rush order nếu isRush)
        boolean ok = orderController.placeOrder(Session.getCurrentUser().getId(), cartItems, receiver, phone, "", address, "", "", 0.0, isRush);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Order placed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            orderPlaced = true;
            // Clear cart after successful checkout
            try {
                cartController.clearCart();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa giỏ hàng: " + ex.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
            }
            // Mở PaymentPanel
            int orderId = getLastOrderIdForUser(Session.getCurrentUser().getId());
            PaymentPanel paymentPanel = new PaymentPanel(orderId, totalAmount);
            JDialog paymentDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thanh toán", true);
            paymentDialog.setContentPane(paymentPanel);
            paymentDialog.pack();
            paymentDialog.setLocationRelativeTo(this);
            paymentDialog.setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to place order.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isOrderPlaced() {
        return orderPlaced;
    }

    private int getLastOrderIdForUser(int userId) {
        // TODO: Thay bằng truy vấn thực tế từ OrderDAO nếu có
        // Tạm thời giả lập trả về 0
        return 0;
    }
}
