package DAO;

import Model.Product;

import java.sql.*;

public class ProductDAO {
    public static int insertProduct(Product product, Connection conn) throws SQLException {
        String barcode = BarcodeUtil.generateOrValidateBarcode(product.getBarcode(), conn);
        String sqlProduct = "INSERT INTO Products (title, category, value, price, barcode, description, " +
                "quantity, weight, dimensions, warehouse_entry_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING product_id";
        try (PreparedStatement pstmtProduct = conn.prepareStatement(sqlProduct)) {
            pstmtProduct.setString(1, product.getTitle());
            pstmtProduct.setString(2, product.getCategory());
            pstmtProduct.setDouble(3, product.getValue());
            pstmtProduct.setDouble(4, product.getPrice());
            pstmtProduct.setString(5, barcode);
            pstmtProduct.setString(6, product.getDescription());
            pstmtProduct.setInt(7, product.getQuantity());
            pstmtProduct.setObject(8, product.getWeight() == null || product.getWeight().isEmpty() ? null : Double.valueOf(product.getWeight()));
            pstmtProduct.setString(9, product.getDimensions());
            pstmtProduct.setObject(10, product.getWarehouseEntryDate() == null || product.getWarehouseEntryDate().isEmpty() ? null : java.sql.Date.valueOf(product.getWarehouseEntryDate()));
            ResultSet generatedKeys = pstmtProduct.executeQuery();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating product failed, no ID obtained.");
            }
        }
    }

    public static void updateProduct(Product product, Connection conn) throws SQLException {
        String sqlProduct = "UPDATE Products SET title = ?, value = ?, price = ?, barcode = ?, " +
                "description = ?, quantity = ?, weight = ?, dimensions = ?, warehouse_entry_date = ? " +
                "WHERE product_id = ?";
        try (PreparedStatement pstmtProduct = conn.prepareStatement(sqlProduct)) {
            pstmtProduct.setString(1, product.getTitle());
            pstmtProduct.setDouble(2, product.getValue());
            pstmtProduct.setDouble(3, product.getPrice());
            pstmtProduct.setString(4, product.getBarcode());
            pstmtProduct.setString(5, product.getDescription());
            pstmtProduct.setInt(6, product.getQuantity());
            pstmtProduct.setObject(7, product.getWeight() == null || product.getWeight().isEmpty() ? null : new java.math.BigDecimal(product.getWeight()));
            pstmtProduct.setString(8, product.getDimensions());
            pstmtProduct.setObject(9, product.getWarehouseEntryDate() == null || product.getWarehouseEntryDate().isEmpty() ? null : java.sql.Date.valueOf(product.getWarehouseEntryDate()));
            pstmtProduct.setInt(10, product.getProductId());
            pstmtProduct.executeUpdate();
        }
    }
}
