package aimsfinal.Controller;

import aimsfinal.DAO.RushOrderDAO;
import aimsfinal.Model.RushOrder;
import java.sql.SQLException;
import java.util.List;

public class RushOrderController {
    private RushOrderDAO rushOrderDAO;

    public RushOrderController() {
        this.rushOrderDAO = new RushOrderDAO();
    }

    public boolean createRushOrder(RushOrder rushOrder) {
        if (validateRushOrder(rushOrder)) {
            try {
                return rushOrderDAO.createRushOrder(rushOrder);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public RushOrder getRushOrderById(int id) {
        try {
            return rushOrderDAO.getById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<RushOrder> getAllRushOrders() {
        try {
            return rushOrderDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    public boolean updateRushOrder(RushOrder rushOrder) {
        if (validateRushOrder(rushOrder)) {
            try {
                return rushOrderDAO.update(rushOrder);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean deleteRushOrder(int id) {
        try {
            return rushOrderDAO.delete(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<RushOrder> getRushOrdersByUserId(int userId) {
        try {
            return rushOrderDAO.getByUserId(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    private boolean validateRushOrder(RushOrder rushOrder) {
        if (rushOrder == null) {
            return false;
        }
        if (rushOrder.getUserId() <= 0) {
            return false;
        }
        if (rushOrder.getTotalAmount() <= 0) {
            return false;
        }
        return true;
    }
} 