package Test.AddProductTest;

import controller.AddProductControler.AddLPController;
import model.dao.LPDAO;
import model.entity.LP;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.dialog.addDialog.AddLPDialog;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

public class AddLPTest {
    private AddLPDialog mockDialog;
    private AddLPController controller;
    private LPDAO mockDAO;

    @BeforeEach
    void setUp() {
        mockDialog = mock(AddLPDialog.class);
        mockDAO = mock(LPDAO.class);
        LPDAO.setMockInstance(mockDAO);
        controller = new AddLPController(mockDialog);
    }

    @Test
    void testSuccess() {
        validInputs();
        when(mockDAO.addLP(any(LP.class))).thenReturn(true);

        assertTrue(controller.handleAddLP());
        verify(mockDialog).showSuccessMessage(contains("Thêm LP thành công"));
        verify(mockDialog).closeDialog();
    }

    @Test
    void testMissingField() {
        validInputs();
        when(mockDialog.getTitleLP()).thenReturn("");

        assertFalse(controller.handleAddLP());
        verify(mockDialog).showWarningMessage(contains("Vui lòng điền đầy đủ"));
    }

    @Test
    void testInvalidNumber() {
        validInputs();
        when(mockDialog.getImportPrice()).thenReturn("abc");

        assertFalse(controller.handleAddLP());
        verify(mockDialog).showErrorMessage(contains("định dạng số"));
    }

    @Test
    void testInvalidDate() {
        validInputs();
        when(mockDialog.getReleaseDate()).thenReturn("31/02/2024");

        assertFalse(controller.handleAddLP());
        verify(mockDialog).showErrorMessage(contains("Định dạng ngày"));
    }

    private void validInputs() {
        when(mockDialog.getTitleLP()).thenReturn("LP Title");
        when(mockDialog.getArtist()).thenReturn("Artist");
        when(mockDialog.getRecordLabel()).thenReturn("Label");
        when(mockDialog.getTrackList()).thenReturn("Track A, Track B");
        when(mockDialog.getImportDate()).thenReturn("2024-06-01");
        when(mockDialog.getQuantity()).thenReturn("5");
        when(mockDialog.getSellingPrice()).thenReturn("300000");
        when(mockDialog.getImportPrice()).thenReturn("200000");
        when(mockDialog.getGenre()).thenReturn("Rock");
        when(mockDialog.getReleaseDate()).thenReturn("2024-01-01");
        when(mockDialog.getDimensions()).thenReturn("30x30x1");
        when(mockDialog.getWeight()).thenReturn("400");
        when(mockDialog.getDescription()).thenReturn("LP mô tả");
    }
}
