package aimsfinal.GUI.panel;

import aimsfinal.Controller.OrderController;
import aimsfinal.Model.Order;
import aimsfinal.GUI.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class OrderHistoryPanel extends JPanel {
    private OrderController orderController;
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private JButton viewDetailButton;
    private JButton cancelOrderButton;

    public OrderHistoryPanel() {
        orderController = new OrderController();
        initializeComponents();
        setupLayout();
        setupListeners();
        loadOrders();
    }

    private void initializeComponents() {
        String[] columns = {"ID", "Date", "Total", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTable = new JTable(tableModel);
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        viewDetailButton = new JButton("View Detail");
        cancelOrderButton = new JButton("Cancel Order");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(orderTable);
        add(scrollPane, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(viewDetailButton);
        buttonPanel.add(cancelOrderButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupListeners() {
        viewDetailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = orderTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(OrderHistoryPanel.this, "Please select an order.");
                    return;
                }
                int orderId = (int) tableModel.getValueAt(selectedRow, 0);
                // TODO: Hiển thị chi tiết đơn hàng
                JOptionPane.showMessageDialog(OrderHistoryPanel.this, "Order detail for ID: " + orderId);
            }
        });
        cancelOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = orderTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(OrderHistoryPanel.this, "Please select an order.");
                    return;
                }
                int orderId = (int) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(OrderHistoryPanel.this, "Are you sure you want to cancel this order?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean ok = orderController.cancelOrder(orderId);
                    if (ok) {
                        JOptionPane.showMessageDialog(OrderHistoryPanel.this, "Order cancelled.");
                        loadOrders();
                    } else {
                        JOptionPane.showMessageDialog(OrderHistoryPanel.this, "Failed to cancel order.");
                    }
                }
            }
        });
    }

    private void loadOrders() {
        tableModel.setRowCount(0);
        List<Order> orders = orderController.getOrdersByUserId(Session.getCurrentUser().getId());
        for (Order order : orders) {
            Object[] row = {
                order.getId(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getStatus()
            };
            tableModel.addRow(row);
        }
    }
}
