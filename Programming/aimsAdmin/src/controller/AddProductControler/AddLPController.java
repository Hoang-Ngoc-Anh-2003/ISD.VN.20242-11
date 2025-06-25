package controller.AddProductControler;

import model.dao.*;
import model.entity.*;
import view.dialog.addDialog.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AddLPController {
    private AddLPDialog addLPDialog;

    public AddLPController(AddLPDialog dialog) {
        this.addLPDialog = dialog;
        setupListeners();
    }

    private void setupListeners() {
        addLPDialog.addSaveButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddLP();
            }
        });

        addLPDialog.addCancelButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addLPDialog.closeDialog();
            }
        });
    }

    public boolean handleAddLP() {
        String title = addLPDialog.getTitleLP();
        String warehouseEntryDate = addLPDialog.getImportDate();
        String dimensions = addLPDialog.getDimensions();
        String weight = addLPDialog.getWeight();
        String description = addLPDialog.getDescription();
        String valueStr = addLPDialog.getSellingPrice();
        String priceStr = addLPDialog.getImportPrice();
        String quantityStr = addLPDialog.getQuantity();
        String artists = addLPDialog.getArtist();
        String recordLabel = addLPDialog.getRecordLabel();
        String tracklist = addLPDialog.getTrackList();
        String genre = addLPDialog.getGenre();
        String releaseDate = addLPDialog.getReleaseDate();

        if (title.isEmpty() || artists.isEmpty() || recordLabel.isEmpty() || tracklist.isEmpty() || warehouseEntryDate.isEmpty() ||
            quantityStr.isEmpty() || valueStr.isEmpty() || priceStr.isEmpty() || genre.isEmpty()) {
            addLPDialog.showWarningMessage("Vui lòng điền đầy đủ các trường bắt buộc (Tiêu đề, Nghệ sĩ, Hãng thu âm, Danh sách bài hát, Ngày nhập kho, Số lượng, Giá bán, Giá nhập).");
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
            addLPDialog.showErrorMessage("Vui lòng nhập đúng định dạng số cho Số lượng, Giá bán, Giá nhập");
            return false;
        }

        try {
            if(!releaseDate.isEmpty()){
                LocalDate.parse(releaseDate);
            }
            LocalDate.parse(warehouseEntryDate);
        } catch (DateTimeParseException e) {
            addLPDialog.showErrorMessage("Định dạng ngày không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.");
            return false;
        }

        try {
            LP LP = new LP(0, title, "LP", value, price, "0", description,
            quantity, weight, dimensions, warehouseEntryDate,artists, recordLabel, tracklist, genre, releaseDate);

            boolean success = LPDAO.getInstance().addLP(LP);
            if (success) {
                addLPDialog.showSuccessMessage("Thêm LP thành công!");
                addLPDialog.closeDialog();
                return true;
            } else {
                addLPDialog.showErrorMessage("Lỗi khi lưu vào cơ sở dữ liệu.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            addLPDialog.showErrorMessage("Đã xảy ra lỗi không mong muốn: " + e.getMessage());
            return false;
        }
    }
}