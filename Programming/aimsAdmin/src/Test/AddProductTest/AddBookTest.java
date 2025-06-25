package Test.AddProductTest;

import controller.AddProductControler.AddBookController;
import model.dao.BookDAO;
import model.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.dialog.addDialog.AddBookDialog;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

public class AddBookTest {
    private AddBookDialog mockDialog;
    private AddBookController controller;
    private BookDAO mockDAO;

    @BeforeEach
    void setUp() {
        mockDialog = mock(AddBookDialog.class);
        mockDAO = mock(BookDAO.class);
        BookDAO.setMockInstance(mockDAO);
        controller = new AddBookController(mockDialog);
    }

    @Test
    void testSuccess() {
        validInputs();
        when(mockDAO.addBook(any(Book.class))).thenReturn(true);

        assertTrue(controller.handleAddBook());
        verify(mockDialog).showSuccessMessage(contains("Thêm sách thành công"));
        verify(mockDialog).closeDialog();
    }

    @Test
    void testMissingRequiredField() {
        validInputs();
        when(mockDialog.getTitle()).thenReturn(""); // thiếu title

        assertFalse(controller.handleAddBook());
        verify(mockDialog).showWarningMessage(contains("Vui lòng điền đầy đủ"));
    }

    @Test
    void testInvalidNumberFormat() {
        validInputs();
        when(mockDialog.getPageCount()).thenReturn("abc");

        assertFalse(controller.handleAddBook());
        verify(mockDialog).showErrorMessage(contains("định dạng số"));
    }

    @Test
    void testInvalidDateFormat() {
        validInputs();
        when(mockDialog.getPublishDate()).thenReturn("31/12/2024");

        assertFalse(controller.handleAddBook());
        verify(mockDialog).showErrorMessage(contains("Định dạng ngày"));
    }

    private void validInputs() {
        when(mockDialog.getTitle()).thenReturn("Book Title");
        when(mockDialog.getAuthor()).thenReturn("Author");
        when(mockDialog.getPublisher()).thenReturn("NXB");
        when(mockDialog.getPageCount()).thenReturn("300");
        when(mockDialog.getLanguage()).thenReturn("Vietnamese");
        when(mockDialog.getGenre()).thenReturn("Fiction");
        when(mockDialog.getPublishDate()).thenReturn("2024-06-01");
        when(mockDialog.getCoverType()).thenReturn("Bìa cứng");
        when(mockDialog.getImportDate()).thenReturn("2024-06-10");
        when(mockDialog.getQuantity()).thenReturn("10");
        when(mockDialog.getDimensions()).thenReturn("20x30x2");
        when(mockDialog.getWeight()).thenReturn("500");
        when(mockDialog.getSellingPrice()).thenReturn("150000");
        when(mockDialog.getImportPrice()).thenReturn("100000");
        when(mockDialog.getDescription()).thenReturn("Mô tả sách");
    }
}
