package Test.EditProductTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import controller.EditProductControler.EditCDController;
import model.dao.CDDAO;
import model.entity.CD;
import view.dialog.editDialog.EditCDDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.mockito.Mockito.*;

public class EditCDTest {

    private EditCDDialog mockDialog;
    private CDDAO mockCDDAO;
    private CD mockCDToEdit;
    private EditCDController controller;
    private ArgumentCaptor<ActionListener> actionListenerCaptor;

    @BeforeEach
    void setUp() {
        mockDialog = mock(EditCDDialog.class);
        mockCDDAO = mock(CDDAO.class);
        mockCDToEdit = mock(CD.class);

        try (var mockedStatic = mockStatic(CDDAO.class)) {
            mockedStatic.when(CDDAO::getInstance).thenReturn(mockCDDAO);
            controller = new EditCDController(mockDialog, mockCDToEdit);
        }

        actionListenerCaptor = ArgumentCaptor.forClass(ActionListener.class);
        verify(mockDialog).addSaveButtonListener(actionListenerCaptor.capture());
    }

    private void triggerSaveAction() {
        actionListenerCaptor.getValue().actionPerformed(mock(ActionEvent.class));
    }

    @Test
    void testSaveCD_Success() {
        // 1. Arrange
        when(mockDialog.getTitle()).thenReturn("Test CD Title");
        when(mockDialog.getArtist()).thenReturn("Test Artist");
        when(mockDialog.getRecordLabel()).thenReturn("Test Label");
        when(mockDialog.getGenre()).thenReturn("Pop");
        when(mockDialog.getReleaseDate()).thenReturn("2022-03-15");
        when(mockDialog.getImportDate()).thenReturn("2022-04-01");
        when(mockDialog.getQuantity()).thenReturn("50");
        when(mockDialog.getDimensions()).thenReturn("14x12x0.5");
        when(mockDialog.getWeight()).thenReturn("0.1");
        when(mockDialog.getSellingPrice()).thenReturn("19.99");
        when(mockDialog.getImportPrice()).thenReturn("10.00");
        when(mockDialog.getDescription()).thenReturn("A test CD description.");
        when(mockDialog.getTrackList()).thenReturn("Track 1, Track 2, Track 3");

        when(mockCDDAO.updateCD(any(CD.class))).thenReturn(true);

        // 2. Act
        triggerSaveAction();

        // 3. Assert
        verify(mockCDToEdit).setTitle("Test CD Title");
        verify(mockCDToEdit).setArtists("Test Artist");
        verify(mockCDToEdit).setRecordLabel("Test Label");
        verify(mockCDToEdit).setGenre("Pop");
        verify(mockCDToEdit).setReleaseDate("2022-03-15");
        verify(mockCDToEdit).setWarehouseEntryDate("2022-04-01");
        verify(mockCDToEdit).setQuantity(50);
        verify(mockCDToEdit).setDimensions("14x12x0.5");
        verify(mockCDToEdit).setWeight("0.1");
        verify(mockCDToEdit).setValue(19.99);
        verify(mockCDToEdit).setPrice(10.00);
        verify(mockCDToEdit).setDescription("A test CD description.");
        verify(mockCDToEdit).setTracklist("Track 1, Track 2, Track 3");

        verify(mockCDDAO).updateCD(mockCDToEdit);
        verify(mockDialog).showSuccessMessage("Cập nhật thông tin CD thành công!", "Thành công");
        verify(mockDialog).dispose();
    }

    @Test
    void testSaveCD_MissingRequiredFields() {
        // 1. Arrange
        when(mockDialog.getTitle()).thenReturn(""); // Missing title
        when(mockDialog.getArtist()).thenReturn("Test Artist");
        when(mockDialog.getRecordLabel()).thenReturn("Test Label");
        when(mockDialog.getGenre()).thenReturn("Pop");
        when(mockDialog.getReleaseDate()).thenReturn("2022-03-15");
        when(mockDialog.getImportDate()).thenReturn("2022-04-01");
        when(mockDialog.getQuantity()).thenReturn("50");
        when(mockDialog.getSellingPrice()).thenReturn("19.99");
        when(mockDialog.getImportPrice()).thenReturn("10.00");
        when(mockDialog.getTrackList()).thenReturn("Track 1");

        // 2. Act
        triggerSaveAction();

        // 3. Assert
        verify(mockDialog).showErrorMessage("Vui lòng điền đầy đủ các trường bắt buộc (Tiêu đề, Nghệ sĩ, Hãng thu âm, Danh sách bài hát, Ngày nhập kho, Số lượng, Giá bán, Giá nhập).", "Thiếu thông tin");
        verify(mockCDDAO, never()).updateCD(any(CD.class));
        verify(mockDialog, never()).dispose();
    }

    @Test
    void testSaveCD_InvalidNumberFormat() {
        // 1. Arrange
        when(mockDialog.getTitle()).thenReturn("Test CD Title");
        when(mockDialog.getArtist()).thenReturn("Test Artist");
        when(mockDialog.getRecordLabel()).thenReturn("Test Label");
        when(mockDialog.getGenre()).thenReturn("Pop");
        when(mockDialog.getReleaseDate()).thenReturn("2022-03-15");
        when(mockDialog.getImportDate()).thenReturn("2022-04-01");
        when(mockDialog.getQuantity()).thenReturn("abc"); // Invalid number
        when(mockDialog.getDimensions()).thenReturn("14x12x0.5");
        when(mockDialog.getWeight()).thenReturn("0.1");
        when(mockDialog.getSellingPrice()).thenReturn("19.99");
        when(mockDialog.getImportPrice()).thenReturn("10.00");
        when(mockDialog.getDescription()).thenReturn("A test CD description.");
        when(mockDialog.getTrackList()).thenReturn("Track 1, Track 2, Track 3");

        // 2. Act
        triggerSaveAction();

        // 3. Assert
        verify(mockDialog).showErrorMessage("Vui lòng nhập đúng định dạng số cho Số lượng, Giá bán, Giá nhập.", "Sai định dạng số");
        verify(mockCDDAO, never()).updateCD(any(CD.class));
        verify(mockDialog, never()).dispose();
    }

    @Test
    void testSaveCD_InvalidDateFormatForImportDate() {
        // 1. Arrange
        when(mockDialog.getTitle()).thenReturn("Test CD Title");
        when(mockDialog.getArtist()).thenReturn("Test Artist");
        when(mockDialog.getRecordLabel()).thenReturn("Test Label");
        when(mockDialog.getGenre()).thenReturn("Pop");
        when(mockDialog.getReleaseDate()).thenReturn("2022-03-15");
        when(mockDialog.getImportDate()).thenReturn("01/04/2022"); // Invalid format
        when(mockDialog.getQuantity()).thenReturn("50");
        when(mockDialog.getDimensions()).thenReturn("14x12x0.5");
        when(mockDialog.getWeight()).thenReturn("0.1");
        when(mockDialog.getSellingPrice()).thenReturn("19.99");
        when(mockDialog.getImportPrice()).thenReturn("10.00");
        when(mockDialog.getDescription()).thenReturn("A test CD description.");
        when(mockDialog.getTrackList()).thenReturn("Track 1, Track 2, Track 3");

        // 2. Act
        triggerSaveAction();

        // 3. Assert
        verify(mockDialog).showErrorMessage("Định dạng ngày không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.", "Sai định dạng ngày");
        verify(mockCDDAO, never()).updateCD(any(CD.class));
        verify(mockDialog, never()).dispose();
    }

    @Test
    void testSaveCD_InvalidDateFormatForReleaseDate() {
        // 1. Arrange
        when(mockDialog.getTitle()).thenReturn("Test CD Title");
        when(mockDialog.getArtist()).thenReturn("Test Artist");
        when(mockDialog.getRecordLabel()).thenReturn("Test Label");
        when(mockDialog.getGenre()).thenReturn("Pop");
        when(mockDialog.getReleaseDate()).thenReturn("15-03-2022"); // Invalid format
        when(mockDialog.getImportDate()).thenReturn("2022-04-01");
        when(mockDialog.getQuantity()).thenReturn("50");
        when(mockDialog.getDimensions()).thenReturn("14x12x0.5");
        when(mockDialog.getWeight()).thenReturn("0.1");
        when(mockDialog.getSellingPrice()).thenReturn("19.99");
        when(mockDialog.getImportPrice()).thenReturn("10.00");
        when(mockDialog.getDescription()).thenReturn("A test CD description.");
        when(mockDialog.getTrackList()).thenReturn("Track 1, Track 2, Track 3");

        // 2. Act
        triggerSaveAction();

        // 3. Assert
        verify(mockDialog).showErrorMessage("Định dạng ngày không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.", "Sai định dạng ngày");
        verify(mockCDDAO, never()).updateCD(any(CD.class));
        verify(mockDialog, never()).dispose();
    }

    @Test
    void testSaveCD_ReleaseDateCanBeEmpty() {
        // 1. Arrange
        when(mockDialog.getTitle()).thenReturn("Test CD Title");
        when(mockDialog.getArtist()).thenReturn("Test Artist");
        when(mockDialog.getRecordLabel()).thenReturn("Test Label");
        when(mockDialog.getGenre()).thenReturn("Pop");
        when(mockDialog.getReleaseDate()).thenReturn(""); // Empty release date is allowed
        when(mockDialog.getImportDate()).thenReturn("2022-04-01");
        when(mockDialog.getQuantity()).thenReturn("50");
        when(mockDialog.getDimensions()).thenReturn("14x12x0.5");
        when(mockDialog.getWeight()).thenReturn("0.1");
        when(mockDialog.getSellingPrice()).thenReturn("19.99");
        when(mockDialog.getImportPrice()).thenReturn("10.00");
        when(mockDialog.getDescription()).thenReturn("A test CD description.");
        when(mockDialog.getTrackList()).thenReturn("Track 1, Track 2, Track 3");

        when(mockCDDAO.updateCD(any(CD.class))).thenReturn(true);

        // 2. Act
        triggerSaveAction();

        // 3. Assert
        verify(mockCDToEdit).setReleaseDate("");
        verify(mockCDDAO).updateCD(mockCDToEdit);
        verify(mockDialog).showSuccessMessage("Cập nhật thông tin CD thành công!", "Thành công");
        verify(mockDialog).dispose();
    }

    @Test
    void testSaveCD_DAOFailure() {
        // 1. Arrange
        when(mockDialog.getTitle()).thenReturn("Test CD Title");
        when(mockDialog.getArtist()).thenReturn("Test Artist");
        when(mockDialog.getRecordLabel()).thenReturn("Test Label");
        when(mockDialog.getGenre()).thenReturn("Pop");
        when(mockDialog.getReleaseDate()).thenReturn("2022-03-15");
        when(mockDialog.getImportDate()).thenReturn("2022-04-01");
        when(mockDialog.getQuantity()).thenReturn("50");
        when(mockDialog.getDimensions()).thenReturn("14x12x0.5");
        when(mockDialog.getWeight()).thenReturn("0.1");
        when(mockDialog.getSellingPrice()).thenReturn("19.99");
        when(mockDialog.getImportPrice()).thenReturn("10.00");
        when(mockDialog.getDescription()).thenReturn("A test CD description.");
        when(mockDialog.getTrackList()).thenReturn("Track 1, Track 2, Track 3");

        when(mockCDDAO.updateCD(any(CD.class))).thenReturn(false);

        // 2. Act
        triggerSaveAction();

        // 3. Assert
        verify(mockCDDAO).updateCD(mockCDToEdit);
        verify(mockDialog).showErrorMessage("Lỗi khi lưu CD vào cơ sở dữ liệu.", "Lỗi");
        verify(mockDialog, never()).dispose();
    }

    @Test
    void testSaveCD_UnexpectedException() {
        // 1. Arrange
        when(mockDialog.getTitle()).thenThrow(new RuntimeException("Simulated unexpected error"));

        // 2. Act
        triggerSaveAction();

        // 3. Assert
        verify(mockDialog).showErrorMessage(startsWith("Đã xảy ra lỗi không mong muốn: "), eq("Lỗi hệ thống"));
        verify(mockCDDAO, never()).updateCD(any(CD.class));
        verify(mockDialog, never()).dispose();
    }
}