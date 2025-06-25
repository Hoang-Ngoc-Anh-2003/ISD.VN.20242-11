package Test.EditProductTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import controller.EditProductControler.EditDVDController;
import model.dao.DVDDAO;
import model.entity.DVD;
import view.dialog.editDialog.EditDVDDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.mockito.Mockito.*;

public class EditDVDTest {

    private EditDVDDialog mockDialog;
    private DVDDAO mockDVDDAO;
    private DVD mockDVDToEdit;
    private EditDVDController controller;
    private ArgumentCaptor<ActionListener> actionListenerCaptor;

    @BeforeEach
    void setUp() {
        mockDialog = mock(EditDVDDialog.class);
        mockDVDDAO = mock(DVDDAO.class);
        mockDVDToEdit = mock(DVD.class);

        try (var mockedStatic = mockStatic(DVDDAO.class)) {
            mockedStatic.when(DVDDAO::getInstance).thenReturn(mockDVDDAO);
            controller = new EditDVDController(mockDialog, mockDVDToEdit);
        }

        actionListenerCaptor = ArgumentCaptor.forClass(ActionListener.class);
        verify(mockDialog).addSaveButtonListener(actionListenerCaptor.capture());
    }

    private void triggerSaveAction() {
        actionListenerCaptor.getValue().actionPerformed(mock(ActionEvent.class));
    }

    @Test
    void testSaveDVD_Success() {
        // 1. Arrange
        when(mockDialog.getTitle()).thenReturn("Test DVD Title");
        when(mockDialog.getDirector()).thenReturn("Test Director");
        when(mockDialog.getLangue()).thenReturn("Vietnamese");
        when(mockDialog.getSubtitles()).thenReturn("English, French");
        when(mockDialog.getGenre()).thenReturn("Action");
        when(mockDialog.getStudio()).thenReturn("Test Studio");
        when(mockDialog.getreleaseDate()).thenReturn("2021-07-20");
        when(mockDialog.getRuntime()).thenReturn("120");
        when(mockDialog.getDiscType()).thenReturn("Blu-ray");
        when(mockDialog.getWarehouseEntryDate()).thenReturn("2021-08-01");
        when(mockDialog.getQuantity()).thenReturn("30");
        when(mockDialog.getDimensions()).thenReturn("19x14x1.5");
        when(mockDialog.getWeight()).thenReturn("0.2");
        when(mockDialog.getValue()).thenReturn("29.99");
        when(mockDialog.getPrice()).thenReturn("18.00");
        when(mockDialog.getDescription()).thenReturn("A test DVD description.");

        when(mockDVDDAO.updateDVD(any(DVD.class))).thenReturn(true);

        // 2. Act
        triggerSaveAction();

        // 3. Assert
        verify(mockDVDToEdit).setTitle("Test DVD Title");
        verify(mockDVDToEdit).setDirector("Test Director");
        verify(mockDVDToEdit).setLanguage("Vietnamese");
        verify(mockDVDToEdit).setSubtitles("English, French");
        verify(mockDVDToEdit).setGenre("Action");
        verify(mockDVDToEdit).setStudio("Test Studio");
        verify(mockDVDToEdit).setReleaseDate("2021-07-20");
        verify(mockDVDToEdit).setRuntime(120);
        verify(mockDVDToEdit).setDiscType("Blu-ray");
        verify(mockDVDToEdit).setWarehouseEntryDate("2021-08-01");
        verify(mockDVDToEdit).setQuantity(30);
        verify(mockDVDToEdit).setDimensions("19x14x1.5");
        verify(mockDVDToEdit).setWeight("0.2");
        verify(mockDVDToEdit).setValue(29.99);
        verify(mockDVDToEdit).setPrice(18.00);
        verify(mockDVDToEdit).setDescription("A test DVD description.");

        verify(mockDVDDAO).updateDVD(mockDVDToEdit);
        verify(mockDialog).showSuccessMessage("Cập nhật thông tin DVD thành công!", "Thành công");
        verify(mockDialog).dispose();
    }

    @Test
    void testSaveDVD_MissingRequiredFields() {
        // 1. Arrange
        when(mockDialog.getTitle()).thenReturn(""); // Missing title
        when(mockDialog.getDirector()).thenReturn("Test Director");
        when(mockDialog.getLangue()).thenReturn("Vietnamese");
        when(mockDialog.getSubtitles()).thenReturn("English, French");
        when(mockDialog.getGenre()).thenReturn("Action");
        when(mockDialog.getStudio()).thenReturn("Test Studio");
        when(mockDialog.getreleaseDate()).thenReturn("2021-07-20");
        when(mockDialog.getRuntime()).thenReturn("120");
        when(mockDialog.getDiscType()).thenReturn("Blu-ray");
        when(mockDialog.getWarehouseEntryDate()).thenReturn("2021-08-01");
        when(mockDialog.getQuantity()).thenReturn("30");
        when(mockDialog.getValue()).thenReturn("29.99");
        when(mockDialog.getPrice()).thenReturn("18.00");

        // 2. Act
        triggerSaveAction();

        // 3. Assert
        verify(mockDialog).showErrorMessage("Vui lòng điền đầy đủ các trường bắt buộc (Tiêu đề, Đạo diễn, Thời lượng, Hãng sản xuất, Ngày nhập kho, Số lượng, Giá bán, Giá nhập, Ngôn ngữ, Phụ đề, Loại đĩa).", "Thiếu thông tin");
        verify(mockDVDDAO, never()).updateDVD(any(DVD.class));
        verify(mockDialog, never()).dispose();
    }

    @Test
    void testSaveDVD_InvalidNumberFormat() {
        // 1. Arrange
        when(mockDialog.getTitle()).thenReturn("Test DVD Title");
        when(mockDialog.getDirector()).thenReturn("Test Director");
        when(mockDialog.getLangue()).thenReturn("Vietnamese");
        when(mockDialog.getSubtitles()).thenReturn("English, French");
        when(mockDialog.getGenre()).thenReturn("Action");
        when(mockDialog.getStudio()).thenReturn("Test Studio");
        when(mockDialog.getreleaseDate()).thenReturn("2021-07-20");
        when(mockDialog.getRuntime()).thenReturn("abc"); // Invalid number
        when(mockDialog.getDiscType()).thenReturn("Blu-ray");
        when(mockDialog.getWarehouseEntryDate()).thenReturn("2021-08-01");
        when(mockDialog.getQuantity()).thenReturn("30");
        when(mockDialog.getDimensions()).thenReturn("19x14x1.5");
        when(mockDialog.getWeight()).thenReturn("0.2");
        when(mockDialog.getValue()).thenReturn("29.99");
        when(mockDialog.getPrice()).thenReturn("18.00");
        when(mockDialog.getDescription()).thenReturn("A test DVD description.");

        // 2. Act
        triggerSaveAction();

        // 3. Assert
        verify(mockDialog).showErrorMessage("Vui lòng nhập đúng định dạng số cho Thời lượng, Số lượng, Giá bán, Giá nhập.", "Sai định dạng số");
        verify(mockDVDDAO, never()).updateDVD(any(DVD.class));
        verify(mockDialog, never()).dispose();
    }

    @Test
    void testSaveDVD_InvalidDateFormatForWarehouseEntryDate() {
        // 1. Arrange
        when(mockDialog.getTitle()).thenReturn("Test DVD Title");
        when(mockDialog.getDirector()).thenReturn("Test Director");
        when(mockDialog.getLangue()).thenReturn("Vietnamese");
        when(mockDialog.getSubtitles()).thenReturn("English, French");
        when(mockDialog.getGenre()).thenReturn("Action");
        when(mockDialog.getStudio()).thenReturn("Test Studio");
        when(mockDialog.getreleaseDate()).thenReturn("2021-07-20");
        when(mockDialog.getRuntime()).thenReturn("120");
        when(mockDialog.getDiscType()).thenReturn("Blu-ray");
        when(mockDialog.getWarehouseEntryDate()).thenReturn("01/08/2021"); // Invalid format
        when(mockDialog.getQuantity()).thenReturn("30");
        when(mockDialog.getDimensions()).thenReturn("19x14x1.5");
        when(mockDialog.getWeight()).thenReturn("0.2");
        when(mockDialog.getValue()).thenReturn("29.99");
        when(mockDialog.getPrice()).thenReturn("18.00");
        when(mockDialog.getDescription()).thenReturn("A test DVD description.");

        // 2. Act
        triggerSaveAction();

        // 3. Assert
        verify(mockDialog).showErrorMessage("Định dạng ngày không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.", "Sai định dạng ngày");
        verify(mockDVDDAO, never()).updateDVD(any(DVD.class));
        verify(mockDialog, never()).dispose();
    }

    @Test
    void testSaveDVD_InvalidDateFormatForReleaseDate() {
        // 1. Arrange
        when(mockDialog.getTitle()).thenReturn("Test DVD Title");
        when(mockDialog.getDirector()).thenReturn("Test Director");
        when(mockDialog.getLangue()).thenReturn("Vietnamese");
        when(mockDialog.getSubtitles()).thenReturn("English, French");
        when(mockDialog.getGenre()).thenReturn("Action");
        when(mockDialog.getStudio()).thenReturn("Test Studio");
        when(mockDialog.getreleaseDate()).thenReturn("20-07-2021"); // Invalid format
        when(mockDialog.getRuntime()).thenReturn("120");
        when(mockDialog.getDiscType()).thenReturn("Blu-ray");
        when(mockDialog.getWarehouseEntryDate()).thenReturn("2021-08-01");
        when(mockDialog.getQuantity()).thenReturn("30");
        when(mockDialog.getDimensions()).thenReturn("19x14x1.5");
        when(mockDialog.getWeight()).thenReturn("0.2");
        when(mockDialog.getValue()).thenReturn("29.99");
        when(mockDialog.getPrice()).thenReturn("18.00");
        when(mockDialog.getDescription()).thenReturn("A test DVD description.");

        // 2. Act
        triggerSaveAction();

        // 3. Assert
        verify(mockDialog).showErrorMessage("Định dạng ngày không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.", "Sai định dạng ngày");
        verify(mockDVDDAO, never()).updateDVD(any(DVD.class));
        verify(mockDialog, never()).dispose();
    }

    @Test
    void testSaveDVD_ReleaseDateCanBeEmpty() {
        // 1. Arrange
        when(mockDialog.getTitle()).thenReturn("Test DVD Title");
        when(mockDialog.getDirector()).thenReturn("Test Director");
        when(mockDialog.getLangue()).thenReturn("Vietnamese");
        when(mockDialog.getSubtitles()).thenReturn("English, French");
        when(mockDialog.getGenre()).thenReturn("Action");
        when(mockDialog.getStudio()).thenReturn("Test Studio");
        when(mockDialog.getreleaseDate()).thenReturn(""); // Empty release date is allowed
        when(mockDialog.getRuntime()).thenReturn("120");
        when(mockDialog.getDiscType()).thenReturn("Blu-ray");
        when(mockDialog.getWarehouseEntryDate()).thenReturn("2021-08-01");
        when(mockDialog.getQuantity()).thenReturn("30");
        when(mockDialog.getDimensions()).thenReturn("19x14x1.5");
        when(mockDialog.getWeight()).thenReturn("0.2");
        when(mockDialog.getValue()).thenReturn("29.99");
        when(mockDialog.getPrice()).thenReturn("18.00");
        when(mockDialog.getDescription()).thenReturn("A test DVD description.");

        when(mockDVDDAO.updateDVD(any(DVD.class))).thenReturn(true);

        // 2. Act
        triggerSaveAction();

        // 3. Assert
        verify(mockDVDToEdit).setReleaseDate("");
        verify(mockDVDDAO).updateDVD(mockDVDToEdit);
        verify(mockDialog).showSuccessMessage("Cập nhật thông tin DVD thành công!", "Thành công");
        verify(mockDialog).dispose();
    }

    @Test
    void testSaveDVD_DAOFailure() {
        // 1. Arrange
        when(mockDialog.getTitle()).thenReturn("Test DVD Title");
        when(mockDialog.getDirector()).thenReturn("Test Director");
        when(mockDialog.getLangue()).thenReturn("Vietnamese");
        when(mockDialog.getSubtitles()).thenReturn("English, French");
        when(mockDialog.getGenre()).thenReturn("Action");
        when(mockDialog.getStudio()).thenReturn("Test Studio");
        when(mockDialog.getreleaseDate()).thenReturn("2021-07-20");
        when(mockDialog.getRuntime()).thenReturn("120");
        when(mockDialog.getDiscType()).thenReturn("Blu-ray");
        when(mockDialog.getWarehouseEntryDate()).thenReturn("2021-08-01");
        when(mockDialog.getQuantity()).thenReturn("30");
        when(mockDialog.getDimensions()).thenReturn("19x14x1.5");
        when(mockDialog.getWeight()).thenReturn("0.2");
        when(mockDialog.getValue()).thenReturn("29.99");
        when(mockDialog.getPrice()).thenReturn("18.00");
        when(mockDialog.getDescription()).thenReturn("A test DVD description.");

        when(mockDVDDAO.updateDVD(any(DVD.class))).thenReturn(false);

        // 2. Act
        triggerSaveAction();

        // 3. Assert
        verify(mockDVDDAO).updateDVD(mockDVDToEdit);
        verify(mockDialog).showErrorMessage("Lỗi khi lưu DVD vào cơ sở dữ liệu.", "Lỗi");
        verify(mockDialog, never()).dispose();
    }

    @Test
    void testSaveDVD_UnexpectedException() {
        // 1. Arrange
        when(mockDialog.getTitle()).thenThrow(new RuntimeException("Simulated unexpected error"));

        // 2. Act
        triggerSaveAction();

        // 3. Assert
        verify(mockDialog).showErrorMessage(startsWith("Đã xảy ra lỗi không mong muốn: "), eq("Lỗi hệ thống"));
        verify(mockDVDDAO, never()).updateDVD(any(DVD.class));
        verify(mockDialog, never()).dispose();
    }
}