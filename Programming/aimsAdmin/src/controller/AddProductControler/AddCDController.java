package controller.AddProductControler;

import model.dao.*;
import model.entity.*;
import view.dialog.addDialog.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AddCDController {
    private AddCDDialog addCDDialog;

    public AddCDController(AddCDDialog dialog) {
        this.addCDDialog = dialog;
        setupListeners();
    }

    private void setupListeners() {
        addCDDialog.addSaveButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddCD();
            }
        });

        addCDDialog.addCancelButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCDDialog.closeDialog();
            }
        });
    }

    public boolean handleAddCD() {
        String title = addCDDialog.getTitle();
        String warehouseEntryDate = addCDDialog.getImportDate();
        String dimensions = addCDDialog.getDimensions();
        String weight = addCDDialog.getWeight();
        String description = addCDDialog.getDescription();
        String valueStr = addCDDialog.getSellingPrice();
        String priceStr = addCDDialog.getImportPrice();
        String quantityStr = addCDDialog.getQuantity();
        String artists = addCDDialog.getArtist();
        String recordLabel = addCDDialog.getRecordLabel();
        String tracklist = addCDDialog.getTrackList();
        String genre = addCDDialog.getGenre();
        String releaseDate = addCDDialog.getReleaseDate();

        if (title.isEmpty() || artists.isEmpty() || recordLabel.isEmpty() || tracklist.isEmpty() || warehouseEntryDate.isEmpty() ||
            quantityStr.isEmpty() || valueStr.isEmpty() || priceStr.isEmpty() || genre.isEmpty()) {
            addCDDialog.showWarningMessage("Vui lòng điền đầy đủ các trường bắt buộc (Tiêu đề, Nghệ sĩ, Hãng thu âm, Danh sách bài hát, Ngày nhập kho, Số lượng, Giá bán, Giá nhập).");
            return false;
        }

        int quantity;
        double value;
        double price;

        try {
            quantity = Integer.parseInt(quantityStr);
            value = Double.parseDouble(valueStr);
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            addCDDialog.showErrorMessage("Vui lòng nhập đúng định dạng số cho Số lượng, Giá bán, Giá nhập.");
            return false;
        }

        try {
            if(!releaseDate.isEmpty()){
                LocalDate.parse(releaseDate);
            }
            LocalDate.parse(warehouseEntryDate);
        } catch (DateTimeParseException e) {
            addCDDialog.showErrorMessage("Định dạng ngày không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.");
            return false;
        }

        try {
            CD cd = new CD(0, title, "CD", value, price, title, description, 
                            quantity, weight, dimensions, warehouseEntryDate, artists, artists, 
                            tracklist, genre, releaseDate);

            boolean success = CDDAO.getInstance().addCD(cd);
            if (success) {
                addCDDialog.showSuccessMessage("Thêm CD thành công!");
                addCDDialog.closeDialog();
                return true;
            } else {
                addCDDialog.showErrorMessage("Lỗi khi lưu vào cơ sở dữ liệu.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            addCDDialog.showErrorMessage("Đã xảy ra lỗi không mong muốn: " + e.getMessage());
            return false;
        }
    }
}