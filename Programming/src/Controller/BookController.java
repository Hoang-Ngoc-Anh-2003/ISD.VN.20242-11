package aimsfinal.Controller;

import aimsfinal.DAO.BookDAO;
import aimsfinal.Model.Book;

import java.util.List;

public class BookController {
    private BookDAO bookDAO;

    public BookController() {
        bookDAO = new BookDAO();
    }

    // public boolean insertBook(Book book) {
    //     return bookDAO.insertBook(book);
    // }

    // public boolean updateBook(Book book) {
    //     return bookDAO.updateBook(book);
    // }

    // public boolean deleteBook(int productId) {
    //     return bookDAO.deleteBook(productId);
    // }

    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }
}