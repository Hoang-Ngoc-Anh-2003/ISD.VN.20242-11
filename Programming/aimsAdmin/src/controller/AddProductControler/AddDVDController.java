package controller.AddProductControler;

import model.dao.*;
import model.entity.*;
import view.dialog.addDialog.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AddDVDController {
    private AddDVDDialog addDVDDialog;

    public AddDVDController(AddDVDDialog dialog) {
        this.addDVDDialog = dialog;
        setupListeners();
    }

    private void setupListeners() {
        addDVDDialog.addSaveButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddDVD();
            }
        });

        addDVDDialog.addCancelButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDVDDialog.closeDialog();
            }
        });
    }

    public boolean handleAddDVD() {
        String title = addDVDDialog.getTitle();
        String warehouseEntryDate = addDVDDialog.getWarehouseEntryDate();
        String dimensions = addDVDDialog.getDimensions(); 
        String weight = addDVDDialog.getWeight(); 
        String description = addDVDDialog.getDescription();
        String valueStr = addDVDDialog.getValue();
        String priceStr = addDVDDialog.getPrice();
        String quantityStr = addDVDDialog.getQuantity();
        String discType = addDVDDialog.getDiscType();
        String director = addDVDDialog.getDirector();
        String runtimeStr = addDVDDialog.getRuntime();
        String studio = addDVDDialog.getStudio();
        String language = addDVDDialog.getLangue();
        String subtitles = addDVDDialog.getSubtitles();
        String releaseDate = addDVDDialog.getreleaseDate();
        String genre = addDVDDialog.getGenre();

        if (title.isEmpty() || director.isEmpty() || valueStr.isEmpty() || priceStr.isEmpty() ||
            discType.isEmpty() || warehouseEntryDate.isEmpty() || quantityStr.isEmpty() ||
            runtimeStr.isEmpty() || studio.isEmpty() || language.isEmpty() || subtitles.isEmpty()) {
            addDVDDialog.showWarningMessage("Vui lòng điền đầy đủ các trường bắt buộc (Tiêu đề, Đạo diễn, Thời lượng, Hãng sản xuất, Ngày nhập kho, Số lượng, Giá bán, Giá nhập, Ngôn ngữ, Phụ đề, Loại đĩa).");
            return false;
        }

        double value;
        double price;
        int quantity;
        int runtime;

        try {
            value = Double.parseDouble(valueStr);
            quantity = Integer.parseInt(quantityStr);
            price = Double.parseDouble(priceStr);
            runtime = Integer.parseInt(runtimeStr);
        } catch (NumberFormatException e) {
            addDVDDialog.showErrorMessage("Vui lòng nhập đúng định dạng số cho Thời lượng, Số lượng, Giá bán, Giá nhập.");
            return false;
        }

        try {
            if(!releaseDate.isEmpty()){
                LocalDate.parse(releaseDate);
            }
            LocalDate.parse(warehouseEntryDate);
        } catch (DateTimeParseException e) {
            addDVDDialog.showErrorMessage("Định dạng ngày không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.");
            return false;
        }

        try {
            DVD DVD = new DVD(0,title,"DVD",value, price, "0", description,
            quantity, weight, dimensions, warehouseEntryDate,discType,
            director, runtime, studio, language, subtitles, releaseDate, genre);

            boolean success = DVDDAO.getInstance().addDVD(DVD);
            if (success) {
                addDVDDialog.showSuccessMessage("Thêm DVD thành công!");
                addDVDDialog.closeDialog();
                return true;
            } else {
                addDVDDialog.showErrorMessage("Lỗi khi lưu vào cơ sở dữ liệu.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            addDVDDialog.showErrorMessage("Đã xảy ra lỗi không mong muốn: " + e.getMessage());
            return false;
        }
    }

    
}