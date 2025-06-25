package Test.AddProductTest;

import controller.AddProductControler.AddDVDController;
import model.dao.DVDDAO;
import model.entity.DVD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.dialog.addDialog.AddDVDDialog;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

public class AddDVDTest {
    private AddDVDDialog mockDialog;
    private AddDVDController controller;
    private DVDDAO mockDAO;

    @BeforeEach
    void setUp() {
        mockDialog = mock(AddDVDDialog.class);
        mockDAO = mock(DVDDAO.class);
        DVDDAO.setMockInstance(mockDAO);
        controller = new AddDVDController(mockDialog);
    }

    @Test
    void testSuccess() {
        validInputs();
        when(mockDAO.addDVD(any(DVD.class))).thenReturn(true);

        assertTrue(controller.handleAddDVD());
        verify(mockDialog).showSuccessMessage(contains("Thêm DVD thành công"));
        verify(mockDialog).closeDialog();
    }

    @Test
    void testMissingField() {
        validInputs();
        when(mockDialog.getTitle()).thenReturn("");

        assertFalse(controller.handleAddDVD());
        verify(mockDialog).showWarningMessage(contains("Vui lòng điền đầy đủ"));
    }

    @Test
    void testInvalidNumber() {
        validInputs();
        when(mockDialog.getRuntime()).thenReturn("abc");

        assertFalse(controller.handleAddDVD());
        verify(mockDialog).showErrorMessage(contains("định dạng số"));
    }

    @Test
    void testInvalidDate() {
        validInputs();
        when(mockDialog.getWarehouseEntryDate()).thenReturn("01/01/2024");

        assertFalse(controller.handleAddDVD());
        verify(mockDialog).showErrorMessage(contains("Định dạng ngày"));
    }

    private void validInputs() {
        when(mockDialog.getTitle()).thenReturn("DVD Title");
        when(mockDialog.getDirector()).thenReturn("Director");
        when(mockDialog.getValue()).thenReturn("200000");
        when(mockDialog.getPrice()).thenReturn("150000");
        when(mockDialog.getDiscType()).thenReturn("Blu-ray");
        when(mockDialog.getWarehouseEntryDate()).thenReturn("2024-06-01");
        when(mockDialog.getQuantity()).thenReturn("10");
        when(mockDialog.getRuntime()).thenReturn("120");
        when(mockDialog.getStudio()).thenReturn("Studio");
        when(mockDialog.getLangue()).thenReturn("English");
        when(mockDialog.getSubtitles()).thenReturn("Vietnamese");
        when(mockDialog.getreleaseDate()).thenReturn("2024-01-01");
        when(mockDialog.getGenre()).thenReturn("Action");
        when(mockDialog.getDimensions()).thenReturn("14x19x2");
        when(mockDialog.getWeight()).thenReturn("200");
        when(mockDialog.getDescription()).thenReturn("DVD mô tả");
    }
}
