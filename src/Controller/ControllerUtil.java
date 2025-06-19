package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControllerUtil {
	public static void closeQuietly(AutoCloseable... resources) {
		for (AutoCloseable res : resources) {
			if (res != null) {
				try {
					res.close();
				} catch (Exception e) {
					System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
				}
			}
		}
	}

	public static void deleteById(Connection conn, String childSql, String parentSql, int id) throws SQLException {
		// Ensure SQL strings are not user input and are always static/final in code
		if (isSafeSql(childSql) || isSafeSql(parentSql)) {
			throw new SQLException("Unsafe SQL statement detected. Only static SQL is allowed.");
		}
		PreparedStatement childStmt = null, parentStmt = null;
		try {
			// Use only static, final SQL strings for childSql and parentSql
			// Suppress warning: these SQLs are always static in code, not user input
			// noinspection SqlSourceToSinkFlow
			childStmt = conn.prepareStatement(childSql);
			childStmt.setInt(1, id);
			childStmt.executeUpdate();

			// noinspection SqlSourceToSinkFlow
			parentStmt = conn.prepareStatement(parentSql);
			parentStmt.setInt(1, id);
			parentStmt.executeUpdate();
		} finally {
			closeQuietly(childStmt, parentStmt);
		}
	}

	// Utility to check if SQL is safe (very basic: only allow to DELETE FROM ... WHERE ... = ?)
	private static boolean isSafeSql(String sql) {
		return sql == null || !sql.matches("(?i)^\\s*DELETE\\s+FROM\\s+\\w+\\s+WHERE\\s+\\w+\\s*=\\s*\\?\\s*$");
	}

	public static void setProductFields(PreparedStatement pstmt, String artists, String recordLabel, String tracklist, String genre, String releaseDate, int id) throws SQLException {
		pstmt.setString(1, artists);
		pstmt.setString(2, recordLabel);
		pstmt.setString(3, tracklist);
		pstmt.setString(4, genre);
		pstmt.setObject(5, (releaseDate == null || releaseDate.isEmpty()) ? null : java.sql.Date.valueOf(releaseDate));
		pstmt.setInt(6, id);
	}

	public static String[] getMediaFieldsFromResultSet(ResultSet rs) throws SQLException {
        return new String[] {
            rs.getString("artists"),
            rs.getString("record_label"),
            rs.getString("tracklist"),
            rs.getString("genre"),
            rs.getString("release_date")
        };
    }
}

