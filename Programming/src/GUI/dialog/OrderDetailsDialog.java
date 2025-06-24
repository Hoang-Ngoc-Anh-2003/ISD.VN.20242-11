package aimsfinal.GUI.dialog;

import aimsfinal.Model.Order;
import aimsfinal.Model.OrderItem;
import aimsfinal.Model.Product;
import aimsfinal.Controller.ProductController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrderDetailsDialog extends JDialog {
    private Order order;
    private JTable itemsTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    private JLabel statusLabel;
    private JLabel dateLabel;
    private ProductController productController;

    public OrderDetailsDialog(Frame parent, Order order) {
        super(parent, "Order Details", true);
        this.order = order;
        this.productController = new ProductController();
        initializeComponents();
        setupLayout();
        loadOrderDetails();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initializeComponents() {
        String[] columns = {"Product", "Quantity", "Price", "Total"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        itemsTable = new JTable(tableModel);
        totalLabel = new JLabel();
        statusLabel = new JLabel();
        dateLabel = new JLabel();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Order info panel
        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoPanel.add(new JLabel("Order Date:"));
        infoPanel.add(dateLabel);
        infoPanel.add(new JLabel("Status:"));
        infoPanel.add(statusLabel);
        infoPanel.add(new JLabel("Total Amount:"));
        infoPanel.add(totalLabel);
        add(infoPanel, BorderLayout.NORTH);

        // Items table
        JScrollPane scrollPane = new JScrollPane(itemsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadOrderDetails() {
        // Set order info
        dateLabel.setText(order.getOrderDate().toString());
        statusLabel.setText(order.getStatus());
        totalLabel.setText(String.format("$%.2f", order.getTotalAmount()));

        // Load order items
        tableModel.setRowCount(0);
        for (OrderItem item : order.getOrderItems()) {
            Product product = productController.getProductById(item.getProductId());
            if (product != null) {
                Object[] row = {
                    product.getName(),
                    item.getQuantity(),
                    String.format("$%.2f", item.getPrice()),
                    String.format("$%.2f", item.getPrice() * item.getQuantity())
                };
                tableModel.addRow(row);
            }
        }
    }
} 