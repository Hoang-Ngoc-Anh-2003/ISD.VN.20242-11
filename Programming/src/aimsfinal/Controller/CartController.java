package aimsfinal.Controller;

import aimsfinal.DAO.CartDAO;
import aimsfinal.DAO.ProductDAO;
import aimsfinal.GUI.Session;
import aimsfinal.Model.CartItem;
import aimsfinal.Model.Product;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CartController {
    private CartDAO cartDAO;

    public CartController() {
        cartDAO = new CartDAO();
    }

    public CartController(Connection connection) {
        cartDAO = new CartDAO(connection);
    }

    public boolean addToCart(int productId, int quantity) {
        int userId = Session.getCurrentUser().getId();
        // Lấy số lượng tồn kho
        ProductDAO productDAO = new ProductDAO();
        Product product = productDAO.getById(productId);
        if (product == null) return false;
        int stock = product.getQuantity();
        // Lấy số lượng đã có trong giỏ
        int quantityInCart = 0;
        for (CartItem item : getCartItems()) {
            if (item.getProductId() == productId) {
                quantityInCart = item.getQuantity();
                break;
            }
        }
        if (quantity + quantityInCart > stock) {
            // Vượt quá tồn kho
            return false;
        }
        return cartDAO.addToCart(userId, productId, quantity);
    }

    public List<CartItem> getCartItems() {
        int userId = Session.getCurrentUser().getId();
        return cartDAO.getCartItems(userId);
    }

    public boolean updateItemQuantity(int itemId, int quantity) {
        return cartDAO.updateItemQuantity(itemId, quantity);
    }

    public boolean removeItem(int cartId, int productId) {
        return cartDAO.removeItem(cartId, productId);
    }

    // This method is for the transactional CheckoutManager
    public void clearCart(int userId) throws SQLException {
        cartDAO.clearCart(userId);
    }

    // This method is for other parts of the UI that rely on the session
    public void clearCart() throws SQLException {
        int userId = Session.getCurrentUser().getId();
        cartDAO.clearCart(userId);
    }
}
