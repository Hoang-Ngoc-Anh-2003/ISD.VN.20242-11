package aimsfinal.GUI.dialog;

import aimsfinal.Model.Product;
import java.awt.*;
import javax.swing.*;

public class ProductDetailDialog extends JDialog {
    private Product product;

    public ProductDetailDialog(Frame parent, Product product) {
        super(parent, "Chi tiết sản phẩm", true);
        this.product = product;
        
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        // Components sẽ được tạo trong setupLayout
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Thông tin sản phẩm
        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        contentPanel.add(new JLabel(String.valueOf(product.getProductId())), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        contentPanel.add(new JLabel("Tên:"), gbc);
        gbc.gridx = 1;
        contentPanel.add(new JLabel(product.getTitle()), gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        contentPanel.add(new JLabel("Loại:"), gbc);
        gbc.gridx = 1;
        contentPanel.add(new JLabel(product.getCategory()), gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        contentPanel.add(new JLabel("Giá:"), gbc);
        gbc.gridx = 1;
        contentPanel.add(new JLabel(String.valueOf(product.getPrice())), gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        contentPanel.add(new JLabel("Số lượng:"), gbc);
        gbc.gridx = 1;
        contentPanel.add(new JLabel(String.valueOf(product.getQuantity())), gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        contentPanel.add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1;
        contentPanel.add(new JLabel(product.getDescription()), gbc);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Nút đóng
        JButton closeButton = new JButton("Đóng");
        closeButton.addActionListener(e -> dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}