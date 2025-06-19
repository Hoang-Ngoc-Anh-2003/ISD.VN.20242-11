package Model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductResultSetUtil {
    public static void fillProductFieldsFromResultSet(Product product, ResultSet rs) throws SQLException {
        product.setProductId(rs.getInt("product_id"));
        product.setTitle(rs.getString("title"));
        product.setCategory(rs.getString("category"));
        product.setValue(rs.getDouble("value"));
        product.setPrice(rs.getDouble("price"));
        product.setBarcode(rs.getString("barcode"));
        product.setDescription(rs.getString("description"));
        product.setQuantity(rs.getInt("quantity"));
        product.setWeight(String.valueOf(rs.getObject("weight")));
        product.setDimensions(rs.getString("dimensions"));
        product.setWarehouseEntryDate(rs.getString("warehouse_entry_date"));
    }
}

