package aimsfinal.GUI;

import aimsfinal.DAO.RushOrderDAO;
import aimsfinal.Model.RushOrder;
import java.awt.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

public class RushOrderPanel extends JPanel {
    private JTextField orderIdField;
    private JTextField customerNameField;
    private JTextField deliveryTimeField;
    private JTextField rushFeeField;
    private JButton btnRushOrder;
    private RushOrderDAO rushOrderDAO;

    public RushOrderPanel() {
        rushOrderDAO = new RushOrderDAO();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }

    private void initializeComponents() {
        orderIdField = new JTextField();
        customerNameField = new JTextField();
        deliveryTimeField = new JTextField();
        rushFeeField = new JTextField();
        btnRushOrder = new JButton("Đặt giao nhanh");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        JLabel title = new JLabel("Đặt hàng giao nhanh (Rush Order)", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        formPanel.add(new JLabel("ID Đơn hàng:"));
        formPanel.add(orderIdField);
        formPanel.add(new JLabel("Tên khách hàng:"));
        formPanel.add(customerNameField);
        formPanel.add(new JLabel("Thời gian giao hàng:"));
        formPanel.add(deliveryTimeField);
        formPanel.add(new JLabel("Phí giao nhanh:"));
        formPanel.add(rushFeeField);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(formPanel, BorderLayout.CENTER);
        centerPanel.setBorder(BorderFactory.createTitledBorder("Thông tin giao nhanh"));
        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnRushOrder);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        btnRushOrder.addActionListener(e -> handleRushOrder());
    }

    private void handleRushOrder() {
        try {
            int orderId = Integer.parseInt(orderIdField.getText().trim());
            String customerName = customerNameField.getText().trim();
            String deliveryTime = deliveryTimeField.getText().trim();
            double rushFee = Double.parseDouble(rushFeeField.getText().trim());

            if (customerName.isEmpty() || deliveryTime.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng điền đầy đủ thông tin!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            RushOrder rushOrder = new RushOrder();
            rushOrder.setOrderId(orderId);
            rushOrder.setCustomerName(customerName);
            
            // Chuyển đổi String thành Timestamp
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date deliveryDate = sdf.parse(deliveryTime);
                rushOrder.setDeliveryTime(new Timestamp(deliveryDate.getTime()));
            } catch (Exception ex) {
                // Nếu không parse được, sử dụng thời gian hiện tại
                rushOrder.setDeliveryTime(new Timestamp(System.currentTimeMillis()));
            }
            
            rushOrder.setRushFee(rushFee);
            rushOrder.setDeliveryDate(new Timestamp(System.currentTimeMillis()));

            try {
                if (rushOrderDAO.createRushOrder(rushOrder)) {
                    JOptionPane.showMessageDialog(this,
                        "Đặt hàng giao nhanh thành công!",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Không thể tạo đơn hàng giao nhanh. Vui lòng thử lại!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Lỗi khi tạo đơn hàng: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập đúng định dạng số cho ID đơn hàng và phí giao nhanh!",
                "Lỗi định dạng",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        orderIdField.setText("");
        customerNameField.setText("");
        deliveryTimeField.setText("");
        rushFeeField.setText("");
    }
} 