package aimsfinal.GUI;

import aimsfinal.Controller.CartController;
import aimsfinal.Controller.CheckoutManager;
import aimsfinal.Controller.ProductController;
import aimsfinal.GUI.dialog.ProductDetailDialog;
import aimsfinal.Model.Product;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CustomerShoppingPanel extends JPanel {
    private ProductController productController;
    private CartController cartController;
    private CheckoutManager checkoutManager;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton viewDetailButton;
    private JButton addToCartButton;
    private JButton viewCartButton;

    public CustomerShoppingPanel(CheckoutManager checkoutManager) {
        this.checkoutManager = checkoutManager;
        this.productController = new ProductController();
        this.cartController = new CartController();
        initializeComponents();
        setupLayout();
        setupListeners();
        loadProducts("");
    }

    private void initializeComponents() {
        String[] columns = {"ID", "Tên sản phẩm", "Loại", "Giá", "Số lượng", "Mô tả"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.setRowHeight(30);
        
        searchField = new JTextField(20);
        searchButton = new JButton("🔍 Tìm kiếm");
        searchButton.setBackground(new Color(52, 152, 219));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        
        viewDetailButton = new JButton("👁️ Xem chi tiết");
        viewDetailButton.setBackground(new Color(46, 204, 113));
        viewDetailButton.setForeground(Color.WHITE);
        viewDetailButton.setFocusPainted(false);
        
        addToCartButton = new JButton("🛒 Thêm vào giỏ");
        addToCartButton.setBackground(new Color(155, 89, 182));
        addToCartButton.setForeground(Color.WHITE);
        addToCartButton.setFocusPainted(false);
        
        viewCartButton = new JButton("📋 Xem giỏ hàng");
        viewCartButton.setBackground(new Color(230, 126, 34));
        viewCartButton.setForeground(Color.WHITE);
        viewCartButton.setFocusPainted(false);
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel tìm kiếm
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm sản phẩm"));
        topPanel.add(new JLabel("Từ khóa:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        add(topPanel, BorderLayout.NORTH);

        // Bảng sản phẩm
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách sản phẩm"));
        add(scrollPane, BorderLayout.CENTER);

        // Panel nút
        JPanel buttonPanel = new JPanel(new BorderLayout(10, 10));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Thao tác"));
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        actionPanel.add(viewDetailButton);
        actionPanel.add(addToCartButton);
        actionPanel.add(viewCartButton);
        
        buttonPanel.add(actionPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupListeners() {
        searchButton.addActionListener(e -> {
            String keyword = searchField.getText();
            loadProducts(keyword);
        });

        viewDetailButton.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(CustomerShoppingPanel.this, "Vui lòng chọn một sản phẩm.");
                return;
            }
            int productId = (int) tableModel.getValueAt(selectedRow, 0);
            Product product = productController.getProductById(productId);
            if (product != null) {
                ProductDetailDialog dialog = new ProductDetailDialog((Frame) SwingUtilities.getWindowAncestor(CustomerShoppingPanel.this), product);
                dialog.setVisible(true);
            }
        });

        addToCartButton.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(CustomerShoppingPanel.this, "Vui lòng chọn một sản phẩm.");
                return;
            }
            int productId = (int) tableModel.getValueAt(selectedRow, 0);
            Product product = productController.getProductById(productId);
            if (product != null) {
                String quantityStr = JOptionPane.showInputDialog(CustomerShoppingPanel.this, "Nhập số lượng:", "1");
                if (quantityStr != null) {
                    try {
                        int quantity = Integer.parseInt(quantityStr);
                        if (quantity > 0) {
                            boolean ok = cartController.addToCart(productId, quantity);
                            if (ok) {
                                JOptionPane.showMessageDialog(CustomerShoppingPanel.this, "Đã thêm vào giỏ hàng!");
                            } else {
                                JOptionPane.showMessageDialog(CustomerShoppingPanel.this, "Không thể thêm vào giỏ hàng.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(CustomerShoppingPanel.this, "Số lượng không hợp lệ.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(CustomerShoppingPanel.this, "Số lượng không hợp lệ.");
                    }
                }
            }
        });

        viewCartButton.addActionListener(e -> {
            // The CheckoutManager now handles the entire flow.
            checkoutManager.startCheckout();
        });
    }

    private void loadProducts(String keyword) {
        tableModel.setRowCount(0);
        List<Product> products = productController.searchProducts(keyword);
        if (products != null) {
            for (Product product : products) {
                if (product != null) {
                    Object[] row = {
                        product.getProductId(),
                        product.getTitle(),
                        product.getCategory(),
                        product.getPrice(),
                        product.getQuantity(),
                        product.getDescription()
                    };
                    tableModel.addRow(row);
                }
            }
        }
    }

    // Method để refresh dữ liệu
    public void refreshData() {
        System.out.println("CustomerShoppingPanel refreshing data...");
        String keyword = searchField.getText();
        loadProducts(keyword); // Tải lại danh sách sản phẩm
        // We might also need to refresh cart view if it's open, but CheckoutManager handles that.
    }

    /**
     * Placeholder method to satisfy the call from the parent frame after payment.
     * The actual cart refresh happens when the CartDialog is opened.
     */
    public void refreshCart() {
        // This method can be expanded if there's a cart summary directly on this panel.
        System.out.println("Cart refresh requested. Actual view is updated when CartDialog opens.");
    }
}