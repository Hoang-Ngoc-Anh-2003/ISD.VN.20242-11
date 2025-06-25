package Test.AddProductTest;

import controller.AddProductControler.AddCDController;
import model.dao.CDDAO;
import model.entity.CD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.dialog.addDialog.AddCDDialog;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

public class AddCDTest {
    private AddCDDialog mockDialog;
    private AddCDController controller;
    private CDDAO mockDAO;

    @BeforeEach
    void setUp() {
        mockDialog = mock(AddCDDialog.class);
        mockDAO = mock(CDDAO.class);
        CDDAO.setMockInstance(mockDAO);
        controller = new AddCDController(mockDialog);
    }

    @Test
    void testSuccess() {
        validInputs();
        when(mockDAO.addCD(any(CD.class))).thenReturn(true);

        assertTrue(controller.handleAddCD());
        verify(mockDialog).showSuccessMessage(contains("Thêm CD thành công"));
        verify(mockDialog).closeDialog();
    }

    @Test
    void testMissingField() {
        validInputs();
        when(mockDialog.getTitle()).thenReturn("");

        assertFalse(controller.handleAddCD());
        verify(mockDialog).showWarningMessage(contains("Vui lòng điền đầy đủ"));
    }

    @Test
    void testInvalidNumber() {
        validInputs();
        when(mockDialog.getSellingPrice()).thenReturn("abc");

        assertFalse(controller.handleAddCD());
        verify(mockDialog).showErrorMessage(contains("định dạng số"));
    }

    @Test
    void testInvalidDate() {
        validInputs();
        when(mockDialog.getImportDate()).thenReturn("01-06-2024");

        assertFalse(controller.handleAddCD());
        verify(mockDialog).showErrorMessage(contains("Định dạng ngày"));
    }

    private void validInputs() {
        when(mockDialog.getTitle()).thenReturn("CD Title");
        when(mockDialog.getArtist()).thenReturn("Artist");
        when(mockDialog.getRecordLabel()).thenReturn("Label");
        when(mockDialog.getTrackList()).thenReturn("Track 1, Track 2");
        when(mockDialog.getImportDate()).thenReturn("2024-06-01");
        when(mockDialog.getQuantity()).thenReturn("10");
        when(mockDialog.getSellingPrice()).thenReturn("200000");
        when(mockDialog.getImportPrice()).thenReturn("150000");
        when(mockDialog.getGenre()).thenReturn("Pop");
        when(mockDialog.getReleaseDate()).thenReturn("2024-01-01");
        when(mockDialog.getDimensions()).thenReturn("14x12x1");
        when(mockDialog.getWeight()).thenReturn("100");
        when(mockDialog.getDescription()).thenReturn("CD mô tả");
    }
}
