package Test.EditProductTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import controller.EditProductControler.EditLPController;
import model.dao.LPDAO;
import model.entity.LP;
import view.dialog.editDialog.EditLPDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.mockito.Mockito.*;

public class EditLPTest {

    private EditLPDialog mockDialog;
    private LPDAO mockLPDAO;
    private LP mockLPToEdit;
    private EditLPController controller;
    private ArgumentCaptor<ActionListener> actionListenerCaptor;

    @BeforeEach
    void setUp() {
        mockDialog = mock(EditLPDialog.class);
        mockLPDAO = mock(LPDAO.class);
        mockLPToEdit = mock(LP.class);

        try (var mockedStatic = mockStatic(LPDAO.class)) {
            mockedStatic.when(LPDAO::getInstance).thenReturn(mockLPDAO);
            controller = new EditLPController(mockDialog, mockLPToEdit);
        }

        actionListenerCaptor = ArgumentCaptor.forClass(ActionListener.class);
        verify(mockDialog).addSaveButtonListener(actionListenerCaptor.capture());
    }

    private void triggerSaveAction() {
        actionListenerCaptor.getValue().actionPerformed(mock(ActionEvent.class));
    }

    @Test
    void testSaveLP_Success() {
        // 1. Arrange
        when(mockDialog.getTitle()).thenReturn("Test LP Title");
        when(mockDialog.getArtist()).thenReturn("Test Artist");
        when(mockDialog.getRecordLabel()).thenReturn("Test Label");
        when(mockDialog.getGenre()).thenReturn("Rock");
        when(mockDialog.getReleaseDate()).thenReturn("1980-05-20");
        when(mockDialog.getImportDate()).thenReturn("2023-01-10");
        when(mockDialog.getQuantity()).thenReturn("20");
        when(mockDialog.getDimensions()).thenReturn("30x30x0.5");
        when(mockDialog.getWeight()).thenReturn("0.3");
        when(mockDialog.getSellingPrice()).thenReturn("35.00");
        when(mockDialog.getImportPrice()).thenReturn("20.00");
        when(mockDialog.getDescription()).thenReturn("A test LP description.");
        when(mockDialog.getTrackList()).thenReturn("Side A: Track 1, Track 2; Side B: Track 3, Track 4");

        when(mockLPDAO.updateLP(any(LP.class))).thenReturn(true);

        // 2. Act
        triggerSaveAction();

        // 3. Assert
        verify(mockLPToEdit).setTitle("Test LP Title");
        verify(mockLPToEdit).setArtists("Test Artist");
        verify(mockLPToEdit).setRecordLabel("Test Label");
        verify(mockLPToEdit).setGenre("Rock");
        verify(mockLPToEdit).setReleaseDate("1980-05-20");
        verify(mockLPToEdit).setWarehouseEntryDate("2023-01-10");
        verify(mockLPToEdit).setQuantity(20);
        verify(mockLPToEdit).setDimensions("30x30x0.5");
        verify(mockLPToEdit).setWeight("0.3");
        verify(mockLPToEdit).setValue(35.00);
        verify(mockLPToEdit).setPrice(20.00);
        verify(mockLPToEdit).setDescription("A test LP description.");
        verify(mockLPToEdit).setTracklist("Side A: Track 1, Track 2; Side B: Track 3, Track 4");

        verify(mockLPDAO).updateLP(mockLPToEdit);
        verify(mockDialog).showSuccessMessage("Cập nhật thông tin LP thành công!", "Thành công");
        verify(mockDialog).dispose();
    }

    @Test
    void testSaveLP_MissingRequiredFields() {
        // 1. Arrange
        when(mockDialog.getTitle()).thenReturn("Test LP Title");
        when(mockDialog.getArtist()).thenReturn(""); // Missing artist
        when(mockDialog.getRecordLabel()).thenReturn("Test Label");
        when(mockDialog.getGenre()).thenReturn("Rock");
        when(mockDialog.getReleaseDate()).thenReturn("1980-05-20");
        when(mockDialog.getImportDate()).thenReturn("2023-01-10");
        when(mockDialog.getQuantity()).thenReturn("20");
        when(mockDialog.getSellingPrice()).thenReturn("35.00");
        when(mockDialog.getImportPrice()).thenReturn("20.00");
        when(mockDialog.getTrackList()).thenReturn("Track 1");

        // 2. Act
        triggerSaveAction();

        // 3. Assert
        verify(mockDialog).showErrorMessage("Vui lòng điền đầy đủ các trường bắt buộc (Tiêu đề, Nghệ sĩ, Hãng thu âm, Danh sách bài hát, Ngày nhập kho, Số lượng, Giá bán, Giá nhập).", "Thiếu thông tin");
        verify(mockLPDAO, never()).updateLP(any(LP.class));
        verify(mockDialog, never()).dispose();
    }

    @Test
    void testSaveLP_InvalidNumberFormat() {
        // 1. Arrange
        when(mockDialog.getTitle()).thenReturn("Test LP Title");
        when(mockDialog.getArtist()).thenReturn("Test Artist");
        when(mockDialog.getRecordLabel()).thenReturn("Test Label");
        when(mockDialog.getGenre()).thenReturn("Rock");
        when(mockDialog.getReleaseDate()).thenReturn("1980-05-20");
        when(mockDialog.getImportDate()).thenReturn("2023-01-10");
        when(mockDialog.getQuantity()).thenReturn("abc"); // Invalid number
        when(mockDialog.getDimensions()).thenReturn("30x30x0.5");
        when(mockDialog.getWeight()).thenReturn("0.3");
        when(mockDialog.getSellingPrice()).thenReturn("35.00");
        when(mockDialog.getImportPrice()).thenReturn("20.00");
        when(mockDialog.getDescription()).thenReturn("A test LP description.");
        when(mockDialog.getTrackList()).thenReturn("Side A: Track 1, Track 2; Side B: Track 3, Track 4");

        // 2. Act
        triggerSaveAction();

        // 3. Assert
        verify(mockDialog).showErrorMessage("Vui lòng nhập đúng định dạng số cho Số lượng, Giá bán, Giá nhập", "Sai định dạng số");
        verify(mockLPDAO, never()).updateLP(any(LP.class));
        verify(mockDialog, never()).dispose();
    }

    @Test
    void testSaveLP_InvalidDateFormatForImportDate() {
        // 1. Arrange
        when(mockDialog.getTitle()).thenReturn("Test LP Title");
        when(mockDialog.getArtist()).thenReturn("Test Artist");
        when(mockDialog.getRecordLabel()).thenReturn("Test Label");
        when(mockDialog.getGenre()).thenReturn("Rock");
        when(mockDialog.getReleaseDate()).thenReturn("1980-05-20");
        when(mockDialog.getImportDate()).thenReturn("10/01/2023"); // Invalid format
        when(mockDialog.getQuantity()).thenReturn("20");
        when(mockDialog.getDimensions()).thenReturn("30x30x0.5");
        when(mockDialog.getWeight()).thenReturn("0.3");
        when(mockDialog.getSellingPrice()).thenReturn("35.00");
        when(mockDialog.getImportPrice()).thenReturn("20.00");
        when(mockDialog.getDescription()).thenReturn("A test LP description.");
        when(mockDialog.getTrackList()).thenReturn("Side A: Track 1, Track 2; Side B: Track 3, Track 4");

        // 2. Act
        triggerSaveAction();

        // 3. Assert
        verify(mockDialog).showErrorMessage("Định dạng ngày không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.", "Sai định dạng ngày");
        verify(mockLPDAO, never()).updateLP(any(LP.class));
        verify(mockDialog, never()).dispose();
    }

    @Test
    void testSaveLP_InvalidDateFormatForReleaseDate() {
        // 1. Arrange
        when(mockDialog.getTitle()).thenReturn("Test LP Title");
        when(mockDialog.getArtist()).thenReturn("Test Artist");
        when(mockDialog.getRecordLabel()).thenReturn("Test Label");
        when(mockDialog.getGenre()).thenReturn("Rock");
        when(mockDialog.getReleaseDate()).thenReturn("20-05-1980"); // Invalid format
        when(mockDialog.getImportDate()).thenReturn("2023-01-10");
        when(mockDialog.getQuantity()).thenReturn("20");
        when(mockDialog.getDimensions()).thenReturn("30x30x0.5");
        when(mockDialog.getWeight()).thenReturn("0.3");
        when(mockDialog.getSellingPrice()).thenReturn("35.00");
        when(mockDialog.getImportPrice()).thenReturn("20.00");
        when(mockDialog.getDescription()).thenReturn("A test LP description.");
        when(mockDialog.getTrackList()).thenReturn("Side A: Track 1, Track 2; Side B: Track 3, Track 4");

        // 2. Act
        triggerSaveAction();

        // 3. Assert
        verify(mockDialog).showErrorMessage("Định dạng ngày không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.", "Sai định dạng ngày");
        verify(mockLPDAO, never()).updateLP(any(LP.class));
        verify(mockDialog, never()).dispose();
    }

    @Test
    void testSaveLP_ReleaseDateCanBeEmpty() {
        // 1. Arrange
        when(mockDialog.getTitle()).thenReturn("Test LP Title");
        when(mockDialog.getArtist()).thenReturn("Test Artist");
        when(mockDialog.getRecordLabel()).thenReturn("Test Label");
        when(mockDialog.getGenre()).thenReturn("Rock");
        when(mockDialog.getReleaseDate()).thenReturn(""); // Empty release date is allowed
        when(mockDialog.getImportDate()).thenReturn("2023-01-10");
        when(mockDialog.getQuantity()).thenReturn("20");
        when(mockDialog.getDimensions()).thenReturn("30x30x0.5");
        when(mockDialog.getWeight()).thenReturn("0.3");
        when(mockDialog.getSellingPrice()).thenReturn("35.00");
        when(mockDialog.getImportPrice()).thenReturn("20.00");
        when(mockDialog.getDescription()).thenReturn("A test LP description.");
        when(mockDialog.getTrackList()).thenReturn("Side A: Track 1, Track 2; Side B: Track 3, Track 4");

        when(mockLPDAO.updateLP(any(LP.class))).thenReturn(true);

        // 2. Act
        triggerSaveAction();

        // 3. Assert
        verify(mockLPToEdit).setReleaseDate("");
        verify(mockLPDAO).updateLP(mockLPToEdit);
        verify(mockDialog).showSuccessMessage("Cập nhật thông tin LP thành công!", "Thành công");
        verify(mockDialog).dispose();
    }

    @Test
    void testSaveLP_DAOFailure() {
        // 1. Arrange
        when(mockDialog.getTitle()).thenReturn("Test LP Title");
        when(mockDialog.getArtist()).thenReturn("Test Artist");
        when(mockDialog.getRecordLabel()).thenReturn("Test Label");
        when(mockDialog.getGenre()).thenReturn("Rock");
        when(mockDialog.getReleaseDate()).thenReturn("1980-05-20");
        when(mockDialog.getImportDate()).thenReturn("2023-01-10");
        when(mockDialog.getQuantity()).thenReturn("20");
        when(mockDialog.getDimensions()).thenReturn("30x30x0.5");
        when(mockDialog.getWeight()).thenReturn("0.3");
        when(mockDialog.getSellingPrice()).thenReturn("35.00");
        when(mockDialog.getImportPrice()).thenReturn("20.00");
        when(mockDialog.getDescription()).thenReturn("A test LP description.");
        when(mockDialog.getTrackList()).thenReturn("Side A: Track 1, Track 2; Side B: Track 3, Track 4");

        when(mockLPDAO.updateLP(any(LP.class))).thenReturn(false);

        // 2. Act
        triggerSaveAction();

        // 3. Assert
        verify(mockLPDAO).updateLP(mockLPToEdit);
        verify(mockDialog).showErrorMessage("Lỗi khi lưu LP vào cơ sở dữ liệu.", "Lỗi");
        verify(mockDialog, never()).dispose();
    }

    @Test
    void testSaveLP_UnexpectedException() {
        // 1. Arrange
        when(mockDialog.getTitle()).thenThrow(new RuntimeException("Simulated unexpected error"));

        // 2. Act
        triggerSaveAction();

        // 3. Assert
        verify(mockDialog).showErrorMessage(startsWith("Đã xảy ra lỗi không mong muốn: "), eq("Lỗi hệ thống"));
        verify(mockLPDAO, never()).updateLP(any(LP.class));
        verify(mockDialog, never()).dispose();
    }
}