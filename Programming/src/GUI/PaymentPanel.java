package aimsfinal.GUI;

import aimsfinal.Controller.PaymentController;
import aimsfinal.Model.Payment;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import aimsfinal.Util.VnpayUtil;
import java.awt.Desktop;
import java.net.URI;

public class PaymentPanel extends JPanel {
    private JTextField orderIdField;
    private JTextField amountField;
    private JComboBox<String> methodComboBox;
    private JButton payButton;
    private PaymentController paymentController;

    public PaymentPanel() {
        paymentController = new PaymentController();
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    public PaymentPanel(int orderId, double amount) {
        this();
        orderIdField.setText(String.valueOf(orderId));
        amountField.setText(String.valueOf(amount));
        orderIdField.setEditable(false);
        amountField.setEditable(false);
    }

    private void initializeComponents() {
        orderIdField = new JTextField(20);
        amountField = new JTextField(20);
        methodComboBox = new JComboBox<>(new String[]{"Credit Card", "VNPay", "Momo"});
        payButton = new JButton("Thanh toán");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        inputPanel.add(new JLabel("Mã đơn hàng:"));
        inputPanel.add(orderIdField);
        inputPanel.add(new JLabel("Số tiền:"));
        inputPanel.add(amountField);
        inputPanel.add(new JLabel("Phương thức:"));
        inputPanel.add(methodComboBox);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(payButton);
        
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupListeners() {
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handlePayment();
            }
        });
    }

    private void handlePayment() {
        try {
            int orderId = Integer.parseInt(orderIdField.getText());
            double amount = Double.parseDouble(amountField.getText());
            String method = (String) methodComboBox.getSelectedItem();
            if ("VNPay".equalsIgnoreCase(method)) {
                // Sử dụng thông tin mặc định từ file mẫu
                String vnp_TmnCode = "SSA2UKJS";
                String vnp_HashSecret = "3QNH2I3BZ37R6Q6B6CYX8Q6YL3HFXAKW";
                String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
                String vnp_ReturnUrl = "http://localhost:8080/vnpay_return";
                int userId = aimsfinal.GUI.Session.getCurrentUser().getId();
                String orderInfo = "Thanh toan don hang " + orderId + " user " + userId;
                int vnpAmount = (int)(amount * 100); // VNPay yêu cầu số tiền x100
                String txnRef = String.valueOf(orderId); // Set vnp_TxnRef to orderId
                String payUrl = VnpayUtil.createPaymentUrl(vnp_TmnCode, vnp_HashSecret, vnp_Url, vnp_ReturnUrl, vnpAmount, orderInfo, txnRef);
                // Mở trình duyệt
                Desktop.getDesktop().browse(new URI(payUrl));
                JOptionPane.showMessageDialog(this, "Đang chuyển hướng sang VNPay để thanh toán!");
            } else {
                JOptionPane.showMessageDialog(this, "Tính năng sẽ phát triển sau!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi chuyển hướng sang VNPay: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        orderIdField.setText("");
        amountField.setText("");
        methodComboBox.setSelectedIndex(0);
    }
}
