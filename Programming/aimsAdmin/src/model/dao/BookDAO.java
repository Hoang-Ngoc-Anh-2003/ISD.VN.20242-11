package model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Utils.DatabaseConnection;
import model.entity.Book;

public class BookDAO extends ProductDAO {
    // lay thong tin sach
    public Book getBookInfor (int product_id){
        Book book = null;
        String sql = "SELECT p.product_id, p.title, p.category, p.value, p.price," + 
        "p.barcode, p.description, p.quantity, p.weight, p.dimensions, p.warehouse_entry_date," + 
        "b.authors, b.cover_type, b.publisher, b.publication_date, b.num_pages, b.language, b.genre " + 
        "FROM products p " + 
        "INNER JOIN books b ON p.product_id = b.book_id " + 
        "WHERE p.category = 'book' AND p.product_id = ? ";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, product_id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                book = new Book();
                book.setProductId(rs.getInt("product_id"));
                book.setTitle(rs.getString("title"));
                book.setCategory(rs.getString("category"));
                book.setValue(rs.getDouble("value"));
                book.setPrice(rs.getDouble("price"));
                book.setBarcode(rs.getString("barcode"));
                book.setDescription(rs.getString("description"));
                book.setQuantity(rs.getInt("quantity"));
                book.setWeight(rs.getString("weight"));
                book.setDimensions(rs.getString("dimensions"));
                book.setWarehouseEntryDate(rs.getString("warehouse_entry_date"));
                book.setAuthors(rs.getString("authors"));
                book.setCoverType(rs.getString("cover_type"));
                book.setPublisher(rs.getString("publisher"));
                book.setPublicationDate(rs.getString("publication_date"));
                book.setNumPages(rs.getInt("num_pages"));
                book.setLanguage(rs.getString("language"));
                book.setGenre(rs.getString("genre"));
            }     
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return book;
    }

    // lay danh sach san pham de hien thi tren bang "Panel"
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT p.product_id, p.title, p.category, p.value, p.price," + 
                    "p.barcode, p.description, p.quantity, p.weight, p.dimensions, p.warehouse_entry_date," + 
                    "b.authors, b.cover_type, b.publisher, b.publication_date, b.num_pages, b.language, b.genre " + 
                    "FROM products p " + 
                    "INNER JOIN books b ON p.product_id = b.book_id " + 
                    "WHERE p.category = 'book'";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
    
            while (rs.next()) {
                Book book = new Book(
                    rs.getInt("product_id"),
                    rs.getString("title"),
                    rs.getString("category"),
                    rs.getDouble("value"),
                    rs.getDouble("price"),
                    rs.getString("barcode"),
                    rs.getString("description"),
                    rs.getInt("quantity"),
                    rs.getString("weight"),
                    rs.getString("dimensions"),
                    rs.getString("warehouse_entry_date"),
                    rs.getString("authors"),
                    rs.getString("cover_type"),
                    rs.getString("publisher"),
                    rs.getString("publication_date"),
                    rs.getInt("num_pages"),
                    rs.getString("language"),
                    rs.getString("genre")
                );
                books.add(book);
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    // barcode random
    private String generateUniqueBarcode(Connection conn) throws SQLException {
        String barcode;
        boolean isUnique = false;
        do {
            barcode = String.format("%013d", (long)(Math.random() * 1_000_000_000_0000L));
            String checkSql = "SELECT COUNT(*) FROM Products WHERE barcode = ?";
            try (PreparedStatement stmt = conn.prepareStatement(checkSql)) {
                stmt.setString(1, barcode);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    isUnique = true;
                }
            }
        } while (!isUnique);
        return barcode;
    }    

    public boolean addBook(Book book) {
        String productSQL = "INSERT INTO Products (title, category, value, price, barcode, description, quantity, weight, dimensions, warehouse_entry_date) " +
                            "VALUES (?, 'book', ?, ?, ?, ?, ?, ?, ?, ?) RETURNING product_id";
        String bookSQL = "INSERT INTO Books (book_id, authors, cover_type, publisher, publication_date, num_pages, language, genre) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement productStmt = conn.prepareStatement(productSQL);
             PreparedStatement bookStmt = conn.prepareStatement(bookSQL)) {
    
            // Tạo barcode ngẫu nhiên và đảm bảo không trùng
            String barcode = generateUniqueBarcode(conn);
            book.setBarcode(barcode); // cập nhật lại barcode trong Book
    
            // Thêm vào bảng Products
            productStmt.setString(1, book.getTitle());
            productStmt.setDouble(2, book.getValue());
            productStmt.setDouble(3, book.getPrice());
            productStmt.setString(4, barcode);
            productStmt.setString(5, book.getDescription());
            productStmt.setInt(6, book.getQuantity());
            productStmt.setString(7, book.getWeight());
            productStmt.setString(8, book.getDimensions());
            productStmt.setDate(9, java.sql.Date.valueOf(book.getWarehouseEntryDate()));

    
            ResultSet rs = productStmt.executeQuery();
            if (rs.next()) {
                int productId = rs.getInt(1);
                book.setProductId(productId); // cập nhật lại productId
    
                // Thêm vào bảng Books
                bookStmt.setInt(1, productId);
                bookStmt.setString(2, book.getAuthors());
                bookStmt.setString(3, book.getCoverType());
                bookStmt.setString(4, book.getPublisher());
                bookStmt.setDate(5, java.sql.Date.valueOf(book.getPublicationDate()));
                bookStmt.setInt(6, book.getNumPages());
                bookStmt.setString(7, book.getLanguage());
                bookStmt.setString(8, book.getGenre());
    
                int rowsInserted = bookStmt.executeUpdate();
                return rowsInserted > 0;
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    

    // Cập nhật sách
    public boolean updateBook(Book book) {
        String updateProductSql = "UPDATE Products SET title = ?, value = ?, price = ?, barcode = ?, " +
        "description = ?, quantity = ?, weight = ?, dimensions = ?, warehouse_entry_date = ? " +
        "WHERE product_id = ?";

        String updateBookSql = "UPDATE Books SET authors = ?, cover_type = ?, publisher = ?, " +
        "publication_date = ?, num_pages = ?, language = ?, genre = ? WHERE book_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement productStmt = conn.prepareStatement(updateProductSql);
             PreparedStatement bookStmt = conn.prepareStatement(updateBookSql)) {

            // Cập nhật Products
            productStmt.setString(1, book.getTitle());
            productStmt.setDouble(2, book.getValue());
            productStmt.setDouble(3, book.getPrice());
            productStmt.setString(4, book.getBarcode());
            productStmt.setString(5, book.getDescription());
            productStmt.setInt(6, book.getQuantity());
            productStmt.setString(7, book.getWeight());
            productStmt.setString(8, book.getDimensions());
            productStmt.setDate(9, java.sql.Date.valueOf(book.getWarehouseEntryDate()));
            productStmt.setInt(10, book.getProductId());
            productStmt.executeUpdate();

            // Cập nhật Books
            bookStmt.setString(1, book.getAuthors());
            bookStmt.setString(2, book.getCoverType());
            bookStmt.setString(3, book.getPublisher());
            bookStmt.setDate(4, java.sql.Date.valueOf(book.getPublicationDate()));
            bookStmt.setInt(5, book.getNumPages());
            bookStmt.setString(6, book.getLanguage());
            bookStmt.setString(7, book.getGenre());
            bookStmt.setInt(8, book.getProductId());
            bookStmt.executeUpdate();

            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
            
        }
    }

    public boolean deleteBook(int productId) {
        // Thứ tự xóa quan trọng: xóa từ bảng con trước, sau đó đến bảng cha
        String deleteBookSql = "DELETE FROM Books WHERE book_id = ?";
        String deleteProductSql = "DELETE FROM Products WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement bookStmt = conn.prepareStatement(deleteBookSql);
             PreparedStatement productStmt = conn.prepareStatement(deleteProductSql)) {

            // Xóa từ bảng Books
            bookStmt.setInt(1, productId);
            bookStmt.executeUpdate();

            // Xóa từ bảng Products
            productStmt.setInt(1, productId);
            productStmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Book getBookById(int productId) {
        String sql = "SELECT * FROM Products JOIN Books ON Products.product_id = Books.product_id WHERE Products.product_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToBook(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // so luong sach
    public static int getBookCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Books";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        return new Book(
            rs.getInt("product_id"),
            rs.getString("title"),
            rs.getString("category"),
            rs.getDouble("value"),
            rs.getDouble("price"),
            rs.getString("barcode"),
            rs.getString("description"),
            rs.getInt("quantity"),
            rs.getString("weight"),
            rs.getString("dimensions"),
            rs.getString("warehouse_entry_date"),
            rs.getString("authors"),
            rs.getString("cover_type"),
            rs.getString("publisher"),
            rs.getString("publication_date"),
            rs.getInt("num_pages"),
            rs.getString("language"),
            rs.getString("genre")
        );
    }

    private static BookDAO instance;

    public static BookDAO getInstance() {
        if (instance == null) {
            instance = new BookDAO();
        }
        return instance;
    }

    // Dùng cho test
    public static void setMockInstance(BookDAO mock) {
        instance = mock;
    }
}
