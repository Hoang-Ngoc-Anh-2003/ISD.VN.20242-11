package Controller;

import DAO.ProductDAO;
import Model.DatabaseConnector;
import Model.Book;
import Model.QueryUtils;
import Model.ProductResultSetUtil;

import java.sql.*;
import java.util.List;

public class BookController extends ProductController {

    public List<Book> getAllBooks() {
        String sql = "SELECT p.product_id, p.title, p.category, p.value, p.price," +
                "p.barcode, p.description, p.quantity, p.weight, p.dimensions, p.warehouse_entry_date," +
                "b.authors, b.cover_type, b.publisher, b.publication_date, b.num_pages, b.language, b.genre " +
                "FROM products p " +
                "INNER JOIN books b ON p.product_id = b.book_id " +
                "WHERE p.category = 'book' " +
                "ORDER BY p.product_id ASC";
        return QueryUtils.executeQueryList(sql, this::createBookFromResultSet);
    }

    public Book getBookInfo(int bookId) {
        Book book = null;
        String sql = "SELECT p.product_id, p.title, p.category, p.value, p.price," +
                "p.barcode, p.description, p.quantity, p.weight, p.dimensions, p.warehouse_entry_date," +
                "b.authors, b.cover_type, b.publisher, b.publication_date, b.num_pages, b.language, b.genre " +
                "FROM products p " +
                "INNER JOIN books b ON p.product_id = b.book_id " +
                "WHERE p.category = 'book' AND p.product_id = ? ";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                book = createBookFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy thông tin sách: " + e.getMessage());
        }

        return book;
    }

    public boolean addBook(Book book) {
        Connection conn = null;
        PreparedStatement pstmtBook = null;
        boolean success = false;

        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false);

            // Sử dụng ProductDAO để thêm vào bảng Products
            int productId = ProductDAO.insertProduct(book, conn);

            // Sau đó thêm vào bảng Books
            String sqlBook = "INSERT INTO Books (book_id, authors, cover_type, publisher, publication_date, " +
                    "num_pages, language, genre) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            pstmtBook = conn.prepareStatement(sqlBook);
            pstmtBook.setInt(1, productId);
            pstmtBook.setString(2, book.getAuthors());
            pstmtBook.setString(3, book.getCoverType());
            pstmtBook.setString(4, book.getPublisher());
            pstmtBook.setObject(5, book.getPublicationDate() == null || book.getPublicationDate().isEmpty() ? null : java.sql.Date.valueOf(book.getPublicationDate()));
            pstmtBook.setInt(6, book.getNumPages());
            pstmtBook.setString(7, book.getLanguage());
            pstmtBook.setString(8, book.getGenre());

            pstmtBook.executeUpdate();
            conn.commit();
            success = true;
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            System.err.println("Lỗi khi thêm sách: " + e.getMessage());
        } finally {
            try {
                if (pstmtBook != null) pstmtBook.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
            }
        }

        return success;
    }

    public boolean updateBook(Book book) {
        Connection conn = null;
        PreparedStatement pstmtBook = null;
        boolean success = false;
        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false);
            ProductDAO.updateProduct(book, conn);
            String sqlBook = "UPDATE Books SET authors = ?, cover_type = ?, publisher = ?, " +
                    "publication_date = ?, num_pages = ?, language = ?, genre = ? " +
                    "WHERE book_id = ?";
            pstmtBook = conn.prepareStatement(sqlBook);
            pstmtBook.setString(1, book.getAuthors());
            pstmtBook.setString(2, book.getCoverType());
            pstmtBook.setString(3, book.getPublisher());
            pstmtBook.setObject(4, book.getPublicationDate() == null || book.getPublicationDate().isEmpty() ? null : java.sql.Date.valueOf(book.getPublicationDate()));
            pstmtBook.setInt(5, book.getNumPages());
            pstmtBook.setString(6, book.getLanguage());
            pstmtBook.setString(7, book.getGenre());
            pstmtBook.setInt(8, book.getProductId());
            pstmtBook.executeUpdate();
            conn.commit();
            success = true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { System.err.println("Lỗi khi rollback: " + ex.getMessage()); }
            }
            System.err.println("Lỗi khi cập nhật sách: " + e.getMessage());
        } finally {
            ControllerUtil.closeQuietly(pstmtBook, conn);
        }
        return success;
    }

    public boolean deleteBook(int bookId) {
        Connection conn = null;
        boolean success = false;
        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false);
            String sqlBook = "DELETE FROM Books WHERE book_id = ?";
            String sqlProduct = "DELETE FROM Products WHERE product_id = ?";
            ControllerUtil.deleteById(conn, sqlBook, sqlProduct, bookId);
            conn.commit();
            success = true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { System.err.println("Lỗi khi rollback: " + ex.getMessage()); }
            }
            System.err.println("Lỗi khi xóa sách: " + e.getMessage());
        } finally {
            ControllerUtil.closeQuietly(conn);
        }
        return success;
    }

    private Book createBookFromResultSet(ResultSet rs) throws SQLException {
        Book book = new Book();
        // Sử dụng phương thức tiện ích dùng chung
        ProductResultSetUtil.fillProductFieldsFromResultSet(book, rs);
        book.setAuthors(rs.getString("authors"));
        book.setCoverType(rs.getString("cover_type"));
        book.setPublisher(rs.getString("publisher"));
        book.setPublicationDate(rs.getString("publication_date"));
        book.setNumPages(rs.getInt("num_pages"));
        book.setLanguage(rs.getString("language"));
        book.setGenre(rs.getString("genre"));
        return book;
    }
}
