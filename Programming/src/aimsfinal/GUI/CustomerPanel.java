package aimsfinal.GUI;
import aimsfinal.GUI.dialog.CartDialog;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerPanel extends JPanel {
    private JTabbedPane tabbedPane;
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private JButton addToCartButton;
    private JButton viewCartButton;
    private JButton viewOrderButton;
    private JButton placeOrderButton;

    public CustomerPanel() {
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents() {
        // Initialize tabbed pane
        tabbedPane = new JTabbedPane();

        // Initialize product table
        String[] productColumns = {"ID", "Name", "Category", "Price", "Stock", "Description"};
        productTableModel = new DefaultTableModel(productColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new JTable(productTableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Initialize buttons
        addToCartButton = new JButton("Add to Cart");
        viewCartButton = new JButton("View Cart");
        viewOrderButton = new JButton("View Order Details");
        placeOrderButton = new JButton("Place Order");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Create product panel
        JPanel productPanel = new JPanel(new BorderLayout());
        JPanel productButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        productButtonPanel.add(addToCartButton);
        productButtonPanel.add(viewCartButton);
        JScrollPane productScrollPane = new JScrollPane(productTable);
        productPanel.add(productButtonPanel, BorderLayout.NORTH);
        productPanel.add(productScrollPane, BorderLayout.CENTER);

        // Add tabs
        tabbedPane.addTab("Products", productPanel);

        // Add tabbed pane to panel
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void setupListeners() {
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSelectedProductToCart();
            }
        });

        viewCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: This should be handled by the parent frame with CheckoutManager
                // For now, show a message that this functionality is not available
                JOptionPane.showMessageDialog(CustomerPanel.this,
                    "Cart functionality is not available in this panel.\nPlease use the main shopping interface.",
                    "Feature Not Available",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });

        viewOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewSelectedOrder();
            }
        });

        placeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPlaceOrderDialog();
            }
        });
    }

    private void addSelectedProductToCart() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a product to add to cart",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int productId = (int) productTableModel.getValueAt(selectedRow, 0);
        String quantityStr = JOptionPane.showInputDialog(this,
            "Enter quantity:",
            "Add to Cart",
            JOptionPane.QUESTION_MESSAGE);
        if (quantityStr != null) {
            try {
                int quantity = Integer.parseInt(quantityStr);
                if (quantity > 0) {
                    // TODO: Implement add to cart functionality
                    // if (cartController.addToCart(productId, quantity)) {
                    //     JOptionPane.showMessageDialog(this,
                    //         "Product added to cart",
                    //         "Success",
                    //         JOptionPane.INFORMATION_MESSAGE);
                    // }
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Please enter a valid quantity",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "Please enter a valid number",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewSelectedOrder() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a product to view order details",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int productId = (int) productTableModel.getValueAt(selectedRow, 0);
        // TODO: Implement view order details functionality
        // OrderDetailsDialog dialog = new OrderDetailsDialog((Frame) SwingUtilities.getWindowAncestor(this), productId);
        // dialog.setVisible(true);
    }

    private void showPlaceOrderDialog() {
        // TODO: Implement PlaceOrderDialog
        // PlaceOrderDialog dialog = new PlaceOrderDialog((Frame) SwingUtilities.getWindowAncestor(this));
        // dialog.setVisible(true);
        // if (dialog.isOrderPlaced()) {
        //     loadProducts();
        // }
    }
}