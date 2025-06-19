package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class BarcodeUtil {
    /**
     * Sinh barcode mới nếu barcode truyền vào null, rỗng hoặc "0". Nếu barcode đã tồn tại trong DB thì ném exception.
     * @param barcode barcode truyền vào
     * @param conn kết nối DB
     * @return barcode hợp lệ
     * @throws SQLException nếu barcode đã tồn tại
     */
    public static String generateOrValidateBarcode(String barcode, Connection conn) throws SQLException {
        if (barcode == null || barcode.trim().isEmpty() || barcode.trim().equals("0")) {
            barcode = UUID.randomUUID().toString();
        }
        String checkBarcodeSql = "SELECT COUNT(*) FROM Products WHERE barcode = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkBarcodeSql)) {
            checkStmt.setString(1, barcode);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                throw new SQLException("Barcode đã tồn tại. Vui lòng nhập barcode khác.");
            }
        }
        return barcode;
    }
}

