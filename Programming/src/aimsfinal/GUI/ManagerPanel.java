package aimsfinal.GUI;

import aimsfinal.Controller.OrderController;
import aimsfinal.Controller.ProductController;
import aimsfinal.Model.Order;
import aimsfinal.Model.Product;
import aimsfinal.DAO.BookDAO;
import aimsfinal.DAO.CDDAO;
import aimsfinal.DAO.DVDDAO;
import aimsfinal.DAO.LPDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class ManagerPanel extends JPanel {
    private OrderController orderController;
    private ProductController productController;
    private JTabbedPane tabbedPane;
    private JTable orderTable;
    private DefaultTableModel orderTableModel;
    private JButton viewOrderButton;
    private JButton updateOrderStatusButton;
    
    // Product panels
    private BookPanel bookPanel;
    private CDPanel cdPanel;
    private DVDPanel dvdPanel;
    private LPPanel lpPanel;
    
    // Dashboard labels
    private JLabel bookCountLabel;
    private JLabel cdCountLabel;
    private JLabel dvdCountLabel;
    private JLabel lpCountLabel;

    public ManagerPanel() {
        orderController = new OrderController();
        productController = new ProductController();
        initializeComponents();
        setupLayout();
        setupListeners();
        loadData();
    }

    private void initializeComponents() {
        // Initialize tabbed pane
        tabbedPane = new JTabbedPane();

        // Initialize order table
        String[] orderColumns = {"ID", "Customer", "Date", "Total", "Status"};
        orderTableModel = new DefaultTableModel(orderColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTable = new JTable(orderTableModel);
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Initialize product panels
        bookPanel = new BookPanel();
        cdPanel = new CDPanel();
        dvdPanel = new DVDPanel();
        lpPanel = new LPPanel();

        // Initialize buttons
        viewOrderButton = new JButton("View Order Details");
        updateOrderStatusButton = new JButton("Update Order Status");
        
        // Initialize dashboard labels
        bookCountLabel = new JLabel("Books: 0");
        cdCountLabel = new JLabel("CDs: 0");
        dvdCountLabel = new JLabel("DVDs: 0");
        lpCountLabel = new JLabel("LPs: 0");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Create dashboard panel
        JPanel dashboardPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        dashboardPanel.setBorder(BorderFactory.createTitledBorder("Product Overview"));
        dashboardPanel.add(bookCountLabel);
        dashboardPanel.add(cdCountLabel);
        dashboardPanel.add(dvdCountLabel);
        dashboardPanel.add(lpCountLabel);
        add(dashboardPanel, BorderLayout.NORTH);

        // Create order panel
        JPanel orderPanel = new JPanel(new BorderLayout());
        JPanel orderButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        orderButtonPanel.add(viewOrderButton);
        orderButtonPanel.add(updateOrderStatusButton);
        JScrollPane orderScrollPane = new JScrollPane(orderTable);
        orderPanel.add(orderButtonPanel, BorderLayout.NORTH);
        orderPanel.add(orderScrollPane, BorderLayout.CENTER);

        // Add tabs
        tabbedPane.addTab("Orders", orderPanel);
        tabbedPane.addTab("Books", bookPanel);
        tabbedPane.addTab("CDs", cdPanel);
        tabbedPane.addTab("DVDs", dvdPanel);
        tabbedPane.addTab("LPs", lpPanel);

        // Add tabbed pane to panel
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void setupListeners() {
        viewOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewSelectedOrder();
            }
        });

        updateOrderStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSelectedOrderStatus();
            }
        });
    }

    private void loadData() {
        loadOrders();
        updateProductCounts();
    }
    
    public void updateProductCounts() {
        try {
            int bookCount = BookDAO.getBookCount();
            int cdCount = CDDAO.getCDCount();
            int dvdCount = DVDDAO.getDVDCount();
            int lpCount = LPDAO.getLPCount();
            
            bookCountLabel.setText("Books: " + bookCount);
            cdCountLabel.setText("CDs: " + cdCount);
            dvdCountLabel.setText("DVDs: " + dvdCount);
            lpCountLabel.setText("LPs: " + lpCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadOrders() {
        orderTableModel.setRowCount(0);
        List<Order> orders = orderController.getAllOrders();
        for (Order order : orders) {
            Object[] row = {
                order.getId(),
                order.getUserId(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getStatus()
            };
            orderTableModel.addRow(row);
        }
    }

    private void viewSelectedOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an order to view",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int orderId = (int) orderTableModel.getValueAt(selectedRow, 0);
        Order order = orderController.getOrderById(orderId);
        if (order != null) {
            // TODO: Implement OrderDetailsDialog
            // OrderDetailsDialog dialog = new OrderDetailsDialog((Frame) SwingUtilities.getWindowAncestor(this), order);
            // dialog.setVisible(true);
        }
    }

    private void updateSelectedOrderStatus() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an order to update",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int orderId = (int) orderTableModel.getValueAt(selectedRow, 0);
        String[] options = {"Processing", "Delivering", "Delivered", "Cancelled"};
        String currentStatus = (String) orderTableModel.getValueAt(selectedRow, 4);
        String newStatus = (String) JOptionPane.showInputDialog(this, "Chọn trạng thái mới:",
                "Cập nhật Trạng thái Đơn hàng", JOptionPane.QUESTION_MESSAGE, null, options, currentStatus);

        if (newStatus != null && !newStatus.equals(currentStatus)) {
            try {
                if (orderController.updateOrderStatus(orderId, newStatus)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật trạng thái thành công!");
                    loadOrders(); // Refresh the table
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật trạng thái thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi cơ sở dữ liệu khi cập nhật: " + ex.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Method để refresh dữ liệu
    public void refreshData() {
        loadOrders();
        updateProductCounts();
        // Refresh all product panels
        if (bookPanel != null) bookPanel.reloadData();
        if (cdPanel != null) cdPanel.reloadData();
        if (dvdPanel != null) dvdPanel.reloadData();
        if (lpPanel != null) lpPanel.reloadData();
    }
}