package Test.EditProductTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import controller.EditProductControler.EditBookController;
import model.dao.BookDAO;
import model.entity.Book;
import view.dialog.editDialog.EditBookDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.mockito.Mockito.*;

public class EditBookTest {

    private EditBookDialog mockDialog;
    private BookDAO mockBookDAO;
    private Book mockBookToEdit;
    private EditBookController controller;
    private ArgumentCaptor<ActionListener> actionListenerCaptor;

    @BeforeEach
    void setUp() {
        mockDialog = mock(EditBookDialog.class);
        mockBookDAO = mock(BookDAO.class);
        mockBookToEdit = mock(Book.class);

        // Giả định getInstance() trả về mockBookDAO
        try (var mockedStatic = mockStatic(BookDAO.class)) {
            mockedStatic.when(BookDAO::getInstance).thenReturn(mockBookDAO);
            controller = new EditBookController(mockDialog, mockBookToEdit);
        }

        // Bắt action listener được thêm vào dialog
        actionListenerCaptor = ArgumentCaptor.forClass(ActionListener.class);
        verify(mockDialog).addSaveButtonListener(actionListenerCaptor.capture());
    }

    private void triggerSaveAction() {
        actionListenerCaptor.getValue().actionPerformed(mock(ActionEvent.class));
    }

    @Test
    void testSaveBook_Success() {
        // 1. Arrange: Thiết lập dữ liệu hợp lệ và hành vi của mock
        when(mockDialog.getBookTitle()).thenReturn("Test Title");
        when(mockDialog.getAuthor()).thenReturn("Test Author");
        when(mockDialog.getPublisher()).thenReturn("Test Publisher");
        when(mockDialog.getPageCount()).thenReturn("100");
        when(mockDialog.getLanguage()).thenReturn("English");
        when(mockDialog.getGenre()).thenReturn("Fiction");
        when(mockDialog.getPublishDate()).thenReturn("2020-01-01");
        when(mockDialog.getCoverType()).thenReturn("Hardcover");
        when(mockDialog.getImportDate()).thenReturn("2020-02-01");
        when(mockDialog.getQuantity()).thenReturn("10");
        when(mockDialog.getDimensions()).thenReturn("20x15x2");
        when(mockDialog.getWeight()).thenReturn("0.5");
        when(mockDialog.getSellingPrice()).thenReturn("25.0");
        when(mockDialog.getImportPrice()).thenReturn("15.0");
        when(mockDialog.getDescription()).thenReturn("A test book description.");

        when(mockBookDAO.updateBook(any(Book.class))).thenReturn(true);

        // 2. Act: Kích hoạt hành động save
        triggerSaveAction();

        // 3. Assert: Kiểm tra kết quả
        // Kiểm tra xem các setter của Book có được gọi đúng không
        verify(mockBookToEdit).setTitle("Test Title");
        verify(mockBookToEdit).setAuthors("Test Author");
        verify(mockBookToEdit).setPublisher("Test Publisher");
        verify(mockBookToEdit).setNumPages(100);
        verify(mockBookToEdit).setLanguage("English");
        verify(mockBookToEdit).setGenre("Fiction");
        verify(mockBookToEdit).setPublicationDate("2020-01-01");
        verify(mockBookToEdit).setCoverType("Hardcover");
        verify(mockBookToEdit).setWarehouseEntryDate("2020-02-01");
        verify(mockBookToEdit).setQuantity(10);
        verify(mockBookToEdit).setDimensions("20x15x2");
        verify(mockBookToEdit).setWeight("0.5");
        verify(mockBookToEdit).setValue(25.0);
        verify(mockBookToEdit).setPrice(15.0);
        verify(mockBookToEdit).setDescription("A test book description.");

        // Kiểm tra xem DAO có được gọi để cập nhật sách không
        verify(mockBookDAO).updateBook(mockBookToEdit);

        // Kiểm tra xem thông báo thành công có được hiển thị và dialog có được đóng không
        verify(mockDialog).showSuccessMessage("Cập nhật thông tin sách thành công!", "Thành công");
        verify(mockDialog).dispose();
    }

    @Test
    void testSaveBook_MissingRequiredFields() {
        // 1. Arrange: Thiết lập dữ liệu thiếu
        when(mockDialog.getBookTitle()).thenReturn(""); // Missing title
        when(mockDialog.getAuthor()).thenReturn("Test Author");
        when(mockDialog.getPublisher()).thenReturn("Test Publisher");
        when(mockDialog.getPageCount()).thenReturn("100");
        when(mockDialog.getLanguage()).thenReturn("English");
        when(mockDialog.getGenre()).thenReturn("Fiction");
        when(mockDialog.getPublishDate()).thenReturn("2020-01-01");
        when(mockDialog.getCoverType()).thenReturn("Hardcover");
        when(mockDialog.getImportDate()).thenReturn("2020-02-01");
        when(mockDialog.getQuantity()).thenReturn("10");
        when(mockDialog.getDimensions()).thenReturn("20x15x2");
        when(mockDialog.getWeight()).thenReturn("0.5");
        when(mockDialog.getSellingPrice()).thenReturn("25.0");
        when(mockDialog.getImportPrice()).thenReturn("15.0");
        when(mockDialog.getDescription()).thenReturn("A test book description.");

        // 2. Act: Kích hoạt hành động save
        triggerSaveAction();

        // 3. Assert: Kiểm tra kết quả
        verify(mockDialog).showErrorMessage("Vui lòng điền đầy đủ các trường bắt buộc (Tiêu đề, Tác giả, Số trang, Ngày xuất bản, Ngày nhập kho, Số lượng, Giá bán, Giá nhập).", "Thiếu thông tin");
        verify(mockBookDAO, never()).updateBook(any(Book.class)); // Đảm bảo DAO không được gọi
        verify(mockDialog, never()).dispose(); // Đảm bảo dialog không bị đóng
    }

    @Test
    void testSaveBook_InvalidNumberFormat() {
        // 1. Arrange: Thiết lập dữ liệu số không hợp lệ
        when(mockDialog.getBookTitle()).thenReturn("Valid Title");
        when(mockDialog.getAuthor()).thenReturn("Test Author");
        when(mockDialog.getPublisher()).thenReturn("Test Publisher");
        when(mockDialog.getPageCount()).thenReturn("abc"); // Invalid number
        when(mockDialog.getLanguage()).thenReturn("English");
        when(mockDialog.getGenre()).thenReturn("Fiction");
        when(mockDialog.getPublishDate()).thenReturn("2020-01-01");
        when(mockDialog.getCoverType()).thenReturn("Hardcover");
        when(mockDialog.getImportDate()).thenReturn("2020-02-01");
        when(mockDialog.getQuantity()).thenReturn("10");
        when(mockDialog.getDimensions()).thenReturn("20x15x2");
        when(mockDialog.getWeight()).thenReturn("0.5");
        when(mockDialog.getSellingPrice()).thenReturn("25.0");
        when(mockDialog.getImportPrice()).thenReturn("15.0");
        when(mockDialog.getDescription()).thenReturn("A test book description.");

        // 2. Act: Kích hoạt hành động save
        triggerSaveAction();

        // 3. Assert: Kiểm tra kết quả
        verify(mockDialog).showErrorMessage("Vui lòng nhập đúng định dạng số cho Số trang, Số lượng, Giá bán, Giá nhập và Trọng lượng.", "Sai định dạng số");
        verify(mockBookDAO, never()).updateBook(any(Book.class));
        verify(mockDialog, never()).dispose();
    }

    @Test
    void testSaveBook_InvalidDateFormat() {
        // 1. Arrange: Thiết lập dữ liệu ngày không hợp lệ
        when(mockDialog.getBookTitle()).thenReturn("Valid Title");
        when(mockDialog.getAuthor()).thenReturn("Test Author");
        when(mockDialog.getPublisher()).thenReturn("Test Publisher");
        when(mockDialog.getPageCount()).thenReturn("100");
        when(mockDialog.getLanguage()).thenReturn("English");
        when(mockDialog.getGenre()).thenReturn("Fiction");
        when(mockDialog.getPublishDate()).thenReturn("01-01-2020"); // Invalid format
        when(mockDialog.getCoverType()).thenReturn("Hardcover");
        when(mockDialog.getImportDate()).thenReturn("2020-02-01");
        when(mockDialog.getQuantity()).thenReturn("10");
        when(mockDialog.getDimensions()).thenReturn("20x15x2");
        when(mockDialog.getWeight()).thenReturn("0.5");
        when(mockDialog.getSellingPrice()).thenReturn("25.0");
        when(mockDialog.getImportPrice()).thenReturn("15.0");
        when(mockDialog.getDescription()).thenReturn("A test book description.");

        // 2. Act: Kích hoạt hành động save
        triggerSaveAction();

        // 3. Assert: Kiểm tra kết quả
        verify(mockDialog).showErrorMessage("Định dạng ngày không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.", "Sai định dạng ngày");
        verify(mockBookDAO, never()).updateBook(any(Book.class));
        verify(mockDialog, never()).dispose();
    }

    @Test
    void testSaveBook_DAOFailure() {
        // 1. Arrange: Thiết lập dữ liệu hợp lệ nhưng DAO trả về false
        when(mockDialog.getBookTitle()).thenReturn("Test Title");
        when(mockDialog.getAuthor()).thenReturn("Test Author");
        when(mockDialog.getPublisher()).thenReturn("Test Publisher");
        when(mockDialog.getPageCount()).thenReturn("100");
        when(mockDialog.getLanguage()).thenReturn("English");
        when(mockDialog.getGenre()).thenReturn("Fiction");
        when(mockDialog.getPublishDate()).thenReturn("2020-01-01");
        when(mockDialog.getCoverType()).thenReturn("Hardcover");
        when(mockDialog.getImportDate()).thenReturn("2020-02-01");
        when(mockDialog.getQuantity()).thenReturn("10");
        when(mockDialog.getDimensions()).thenReturn("20x15x2");
        when(mockDialog.getWeight()).thenReturn("0.5");
        when(mockDialog.getSellingPrice()).thenReturn("25.0");
        when(mockDialog.getImportPrice()).thenReturn("15.0");
        when(mockDialog.getDescription()).thenReturn("A test book description.");

        when(mockBookDAO.updateBook(any(Book.class))).thenReturn(false); // DAO báo lỗi

        // 2. Act: Kích hoạt hành động save
        triggerSaveAction();

        // 3. Assert: Kiểm tra kết quả
        verify(mockBookDAO).updateBook(mockBookToEdit);
        verify(mockDialog).showErrorMessage("Lỗi khi lưu sách vào cơ sở dữ liệu.", "Lỗi");
        verify(mockDialog, never()).dispose();
    }

    @Test
    void testSaveBook_UnexpectedException() {
        // 1. Arrange: Thiết lập để một ngoại lệ không mong muốn xảy ra
        when(mockDialog.getBookTitle()).thenThrow(new RuntimeException("Simulated unexpected error"));

        // 2. Act: Kích hoạt hành động save
        triggerSaveAction();

        // 3. Assert: Kiểm tra kết quả
        verify(mockDialog).showErrorMessage(startsWith("Đã xảy ra lỗi không mong muốn: "), eq("Lỗi hệ thống"));
        verify(mockBookDAO, never()).updateBook(any(Book.class));
        verify(mockDialog, never()).dispose();
    }
}