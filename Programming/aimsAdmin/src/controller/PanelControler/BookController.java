package controller.PanelControler;

import model.dao.BookDAO;
import model.entity.Book;
import view.dialog.addDialog.*;
import view.dialog.editDialog.*;
import view.Panel.*;
import view.component.ButtonAction.EditButtonAction;
import view.component.ButtonUI.EditButtonRenderer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import controller.AddProductControler.*;
import controller.EditProductControler.*;
import Interface.*;

import java.util.List;

public class BookController {
    private final BookDAO bookDAO;
    private final BookPanel bookPanel;

    public BookController(BookPanel bookPanel) {
        this.bookDAO = new BookDAO();
        this.bookPanel = bookPanel;
        setupEventListeners();
        loadBooks();
    }

    private void setupEventListeners() {
        bookPanel.addAddButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(bookPanel);
                openAddBookDialog(parentFrame);
            }
        });

        // Thiết lập Renderer và Editor cho cột "Hành động"
        bookPanel.setActionColumnRendererAndEditor(
            new EditButtonRenderer(), // Renderer chỉ để hiển thị
            new EditButtonAction(bookPanel.getTable(), new ProductActionListener() { // Editor xử lý click
                @Override
                public void onEdit(int productId, int row) {
                    openEditBookDialog(productId); // Gọi phương thức chỉnh sửa của controller này
                }

                @Override
                public void onDelete(int productId, int row) {
                    deleteBook(productId, row);
                }
            })
        );
    }

    public void loadBooks() {
        List<Book> books = bookDAO.getAllBooks();
        bookPanel.updateBookTable(books);
    }

    public void openAddBookDialog(JFrame parentFrame) {
        AddBookDialog dialog = new AddBookDialog(parentFrame, "Thêm Sách", true);
        AddBookController addBookController = new AddBookController(dialog);
        dialog.setVisible(true);
        loadBooks();
    }

    public void openEditBookDialog(int bookId) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(bookPanel);
        Book book = bookDAO.getBookInfor(bookId);
        EditBookDialog editBookDialog = new EditBookDialog(parentFrame, "Chỉnh sửa Sách", true, book);
        EditBookController editBookController = new EditBookController(editBookDialog, book);
        editBookDialog.setVisible(true);
        loadBooks();
    }

        // --- Thêm chức năng xóa sách ---
    private void deleteBook(int bookId, int row) {
        int confirm = JOptionPane.showConfirmDialog(bookPanel,
                "Bạn có chắc chắn muốn xóa sách này không?", "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = bookDAO.deleteBook(bookId); // Gọi phương thức deleteBook từ DAO
            if (success) {
                JOptionPane.showMessageDialog(bookPanel, "Xóa sách thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadBooks(); // Tải lại bảng sau khi xóa
            } else {
                JOptionPane.showMessageDialog(bookPanel, "Có lỗi xảy ra khi xóa sách.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
}
