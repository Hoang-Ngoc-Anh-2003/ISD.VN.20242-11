package controller.EditProductControler;

import view.dialog.editDialog.EditCDDialog;
import model.dao.CDDAO;
import model.entity.CD;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class EditCDController {
    private final EditCDDialog dialog;
    private final CDDAO cdDAO;
    private final CD cdToEdit;

    public EditCDController(EditCDDialog dialog, CD cdToEdit) {
        this.dialog = dialog;
        this.cdDAO = CDDAO.getInstance();
        this.cdToEdit = cdToEdit;
        setupListeners();
    }

    private void setupListeners() {
        dialog.addSaveButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCD();
            }
        });
    }

    private void saveCD() {
        try {
            // 1. Lấy dữ liệu từ dialog (View)
            String title = dialog.getTitle();
            String artist = dialog.getArtist();
            String recordLabel = dialog.getRecordLabel();
            String genre = dialog.getGenre();
            String releaseDate = dialog.getReleaseDate();
            String warehouseEntryDate = dialog.getImportDate();
            String quantityStr = dialog.getQuantity();
            String dimensions = dialog.getDimensions();
            String weight = dialog.getWeight();
            String valueStr = dialog.getSellingPrice();
            String priceStr = dialog.getImportPrice();
            String description = dialog.getDescription();
            String trackList = dialog.getTrackList();

        if (title.isEmpty() || artist.isEmpty() || recordLabel.isEmpty() || trackList.isEmpty() || warehouseEntryDate.isEmpty() ||
            quantityStr.isEmpty() || valueStr.isEmpty() || priceStr.isEmpty() || genre.isEmpty()) {
            dialog.showErrorMessage("Vui lòng điền đầy đủ các trường bắt buộc (Tiêu đề, Nghệ sĩ, Hãng thu âm, Danh sách bài hát, Ngày nhập kho, Số lượng, Giá bán, Giá nhập).",  "Thiếu thông tin");
            return ;
        }

        int quantity;
        double value;
        double price;

        try {
            quantity = Integer.parseInt(quantityStr);
            value = Double.parseDouble(valueStr);
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            dialog.showErrorMessage("Vui lòng nhập đúng định dạng số cho Số lượng, Giá bán, Giá nhập.", "Sai định dạng số");
            return ;
        }

        try {
            if(!releaseDate.isEmpty()){
                LocalDate.parse(releaseDate);
            }
            LocalDate.parse(warehouseEntryDate);
        } catch (DateTimeParseException e) {
            dialog.showErrorMessage("Định dạng ngày không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.", "Sai định dạng ngày");
            return ;
        }

            // 3. Cập nhật dữ liệu vào đối tượng CD hiện có
            cdToEdit.setTitle(title);
            cdToEdit.setArtists(artist);
            cdToEdit.setRecordLabel(recordLabel);
            cdToEdit.setGenre(genre);
            cdToEdit.setReleaseDate(releaseDate);
            cdToEdit.setWarehouseEntryDate(warehouseEntryDate);
            cdToEdit.setQuantity(quantity);
            cdToEdit.setDimensions(dimensions);
            cdToEdit.setWeight(weight);
            cdToEdit.setValue(value); 
            cdToEdit.setPrice(price);
            cdToEdit.setDescription(description);
            cdToEdit.setTracklist(trackList);

            // 4. Gọi DAO để lưu vào database 
            boolean success = cdDAO.updateCD(cdToEdit); 
            if (success) {
                dialog.showSuccessMessage("Cập nhật thông tin CD thành công!", "Thành công");
                dialog.dispose(); // Đóng dialog sau khi lưu thành công
            } else {
                dialog.showErrorMessage("Lỗi khi lưu CD vào cơ sở dữ liệu.", "Lỗi");
            }
        } catch (NumberFormatException ex) {
            dialog.showErrorMessage("Vui lòng nhập đúng định dạng số cho các trường số (số lượng bài hát, số lượng, giá...).", "Lỗi định dạng");
        } catch (Exception ex) {
            ex.printStackTrace();
            dialog.showErrorMessage("Đã xảy ra lỗi không mong muốn: " + ex.getMessage(), "Lỗi hệ thống");
        }
    }
}