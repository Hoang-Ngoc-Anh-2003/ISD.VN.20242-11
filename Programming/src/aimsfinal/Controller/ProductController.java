package aimsfinal.Controller;

import aimsfinal.DAO.OrderItemDAO;
import aimsfinal.DAO.ProductDAO;
import aimsfinal.Model.OrderItem;
import aimsfinal.Model.Product;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProductController {
    private ProductDAO productDAO;
    private OrderItemDAO orderItemDAO;

    public ProductController() {
        productDAO = new ProductDAO();
        orderItemDAO = new OrderItemDAO();
    }

    public ProductController(Connection connection) {
        productDAO = new ProductDAO(connection);
        orderItemDAO = new OrderItemDAO(connection);
    }

    public List<Product> getAllProducts() {
        return productDAO.getAll();
    }

    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllProducts();
        }
        return productDAO.search(keyword);
    }

    public Product getProductById(int id) {
        return productDAO.getById(id);
    }

    // Additional methods for GUI compatibility
    public boolean addProduct(Product product) {
        try {
            return productDAO.insertProduct(product) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProduct(Product product) {
        try {
            return productDAO.updateProduct(product);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProduct(int productId) {
        try {
            productDAO.deleteProduct(productId);
            return true; // Assume success if no exception
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateStockAfterOrder(int orderId) throws SQLException {
        List<OrderItem> items = orderItemDAO.getOrderItemsByOrderId(orderId);
        if (items == null || items.isEmpty()) {
            // This is a critical issue if an order exists but has no items.
            throw new SQLException("Cannot update stock: No items found for order ID " + orderId);
        }
        
        for (OrderItem item : items) {
            // Let the SQLException from updateStock propagate up to the CheckoutManager
            // to trigger a transaction rollback if anything fails.
            productDAO.updateStock(item.getProductId(), item.getQuantity());
        }
    }
}
