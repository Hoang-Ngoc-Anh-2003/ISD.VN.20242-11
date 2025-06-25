package controller.EditProductControler;

import view.dialog.editDialog.EditDVDDialog;
import model.dao.DVDDAO;
import model.entity.DVD;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class EditDVDController {
    private final EditDVDDialog dialog;
    private final DVDDAO dvdDAO;
    private final DVD dvdToEdit; 

    public EditDVDController(EditDVDDialog dialog, DVD dvdToEdit) {
        this.dialog = dialog;
        this.dvdDAO = DVDDAO.getInstance(); 
        this.dvdToEdit = dvdToEdit;
        setupListeners();
    }

    private void setupListeners() {
        dialog.addSaveButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDVD();
            }
        });
    }

    private void saveDVD() {
        try {
            // 1. Lấy dữ liệu từ dialog (View)
            String title = dialog.getTitle();
            String director = dialog.getDirector();
            String language = dialog.getLangue(); 
            String subtitles = dialog.getSubtitles();
            String genre = dialog.getGenre();
            String studio = dialog.getStudio(); 
            String releaseDate = dialog.getreleaseDate(); 
            String runtimeStr = dialog.getRuntime(); 
            String discType = dialog.getDiscType();
            String warehouseEntryDate = dialog.getWarehouseEntryDate();
            String quantityStr = dialog.getQuantity();
            String dimensions = dialog.getDimensions();
            String weight = dialog.getWeight();
            String valueStr = dialog.getValue(); 
            String priceStr = dialog.getPrice();
            String description = dialog.getDescription();

            // 2. Validation dữ liệu
            if (title.isEmpty() || director.isEmpty() || valueStr.isEmpty() || priceStr.isEmpty() ||
                discType.isEmpty() || warehouseEntryDate.isEmpty() || quantityStr.isEmpty() ||
                runtimeStr.isEmpty() || studio.isEmpty() || language.isEmpty() || subtitles.isEmpty()) {
                dialog.showErrorMessage("Vui lòng điền đầy đủ các trường bắt buộc (Tiêu đề, Đạo diễn, Thời lượng, Hãng sản xuất, Ngày nhập kho, Số lượng, Giá bán, Giá nhập, Ngôn ngữ, Phụ đề, Loại đĩa).", "Thiếu thông tin");
                return;
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
                dialog.showErrorMessage("Vui lòng nhập đúng định dạng số cho Thời lượng, Số lượng, Giá bán, Giá nhập.", "Sai định dạng số");
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

            // 3. Cập nhật dữ liệu vào đối tượng DVD hiện có
            dvdToEdit.setTitle(title);
            dvdToEdit.setDirector(director);
            dvdToEdit.setLanguage(language);
            dvdToEdit.setSubtitles(subtitles);
            dvdToEdit.setGenre(genre);
            dvdToEdit.setStudio(studio);
            dvdToEdit.setReleaseDate(releaseDate);
            dvdToEdit.setRuntime(runtime);
            dvdToEdit.setDiscType(discType);
            dvdToEdit.setWarehouseEntryDate(warehouseEntryDate);
            dvdToEdit.setQuantity(quantity);
            dvdToEdit.setDimensions(dimensions);
            dvdToEdit.setWeight(weight);
            dvdToEdit.setValue(value);
            dvdToEdit.setPrice(price);
            dvdToEdit.setDescription(description);

            // 4. Gọi DAO để lưu vào database (tương tác với Model)
            boolean success = dvdDAO.updateDVD(dvdToEdit); 
            if (success) {
                dialog.showSuccessMessage("Cập nhật thông tin DVD thành công!", "Thành công");
                dialog.dispose(); // Đóng dialog sau khi lưu thành công
            } else {
                dialog.showErrorMessage("Lỗi khi lưu DVD vào cơ sở dữ liệu.", "Lỗi");
            }
        } catch (NumberFormatException ex) {
            dialog.showErrorMessage("Vui lòng nhập đúng định dạng số cho các trường số (thời lượng, số lượng, giá...).", "Lỗi định dạng");
        } catch (Exception ex) {
            ex.printStackTrace();
            dialog.showErrorMessage("Đã xảy ra lỗi không mong muốn: " + ex.getMessage(), "Lỗi hệ thống");
        }
    }
}