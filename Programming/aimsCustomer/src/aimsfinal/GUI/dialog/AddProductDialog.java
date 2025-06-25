package aimsfinal.GUI.dialog;

import aimsfinal.Controller.ProductController;
import aimsfinal.Model.Book;
import aimsfinal.Model.Product;
import java.awt.*;
import javax.swing.*;

public class AddProductDialog extends JDialog {
    private JTextField nameField;
    private JTextField priceField;
    private JTextField stockField;
    private JComboBox<String> categoryComboBox;
    private JTextArea descriptionArea;
    private JButton saveButton;
    private JButton cancelButton;
    private Product product;
    private ProductController productController;

    public AddProductDialog(Frame parent) {
        super(parent, "Add Product", true);
        productController = new ProductController();
        initializeComponents();
        setupLayout();
        setupListeners();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initializeComponents() {
        nameField = new JTextField(20);
        priceField = new JTextField(20);
        stockField = new JTextField(20);
        String[] categories = {"Book", "CD", "DVD", "LP"};
        categoryComboBox = new JComboBox<>(categories);
        descriptionArea = new JTextArea(5, 20);
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        formPanel.add(priceField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Stock:"), gbc);
        gbc.gridx = 1;
        formPanel.add(stockField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        formPanel.add(categoryComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(descriptionArea), gbc);

        add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupListeners() {
        saveButton.addActionListener(e -> saveProduct());
        cancelButton.addActionListener(e -> dispose());
    }

    private void saveProduct() {
        try {
            String name = nameField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int stock = Integer.parseInt(stockField.getText().trim());
            String category = (String) categoryComboBox.getSelectedItem();
            String description = descriptionArea.getText().trim();

            if (name.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please fill in all fields.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            product = new Book();
            product.setName(name);
            product.setPrice(price);
            product.setStock(stock);
            product.setCategory(category);
            product.setDescription(description);

            if (productController.addProduct(product)) {
                JOptionPane.showMessageDialog(this,
                    "Product added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to add product.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Please enter valid numbers for price and stock.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public Product getProduct() {
        return product;
    }
}