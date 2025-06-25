package controller.EditProductControler;

import view.dialog.editDialog.EditBookDialog;
import model.dao.BookDAO;
import model.entity.Book;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class EditBookController {
    private final EditBookDialog dialog;
    private final BookDAO bookDAO;
    private final Book bookToEdit;

    public EditBookController(EditBookDialog dialog, Book bookToEdit) {
        this.dialog = dialog;
        this.bookDAO = BookDAO.getInstance();
        this.bookToEdit = bookToEdit;
        setupListeners();
    }

    private void setupListeners() {
        dialog.addSaveButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveBook();
            }
        });
    }

    private void saveBook() {
        try {
            // 1. Lấy dữ liệu từ dialog (View)
            String title = dialog.getBookTitle();
            String authors = dialog.getAuthor();
            String publisher = dialog.getPublisher();
            String numPagesStr = dialog.getPageCount();
            String language = dialog.getLanguage();
            String genre = dialog.getGenre();
            String publicationDate = dialog.getPublishDate();
            String coverType = dialog.getCoverType();
            String warehouseEntryDate = dialog.getImportDate();
            String quantityStr = dialog.getQuantity();
            String dimensions = dialog.getDimensions();
            String weight = dialog.getWeight();
            String valueStr = dialog.getSellingPrice();
            String priceStr = dialog.getImportPrice();
            String description = dialog.getDescription();

            // 2. Validation dữ liệu (có thể cải thiện bằng lớp Validator riêng)
            if (title.isEmpty() || authors.isEmpty() || publisher.isEmpty() || publicationDate.isEmpty() ||
                coverType.isEmpty() || warehouseEntryDate.isEmpty() || valueStr.isEmpty() || priceStr.isEmpty() || quantityStr.isEmpty()) {
                dialog.showErrorMessage("Vui lòng điền đầy đủ các trường bắt buộc (Tiêu đề, Tác giả, Số trang, Ngày xuất bản, Ngày nhập kho, Số lượng, Giá bán, Giá nhập).", "Thiếu thông tin");
                return;
            }
            
            int numPages;
            int quantity;
            double value;
            double price;

            try {
                numPages = Integer.parseInt(numPagesStr);
                quantity = Integer.parseInt(quantityStr);
                value = Double.parseDouble(valueStr);
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                dialog.showErrorMessage("Vui lòng nhập đúng định dạng số cho Số trang, Số lượng, Giá bán, Giá nhập và Trọng lượng.", "Sai định dạng số");
                return ;
            }

            try {
                LocalDate.parse(publicationDate);
                LocalDate.parse(warehouseEntryDate);
            } catch (DateTimeParseException e) {
                dialog.showErrorMessage("Định dạng ngày không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.", "Sai định dạng ngày");
                return ;
            }

            // 3. Cập nhật dữ liệu vào đối tượng Book hiện có
            bookToEdit.setTitle(title);
            bookToEdit.setAuthors(authors);
            bookToEdit.setPublisher(publisher);
            bookToEdit.setNumPages(numPages);
            bookToEdit.setLanguage(language);
            bookToEdit.setGenre(genre);
            bookToEdit.setPublicationDate(publicationDate);
            bookToEdit.setCoverType(coverType);
            bookToEdit.setWarehouseEntryDate(warehouseEntryDate);
            bookToEdit.setQuantity(quantity);
            bookToEdit.setDimensions(dimensions);
            bookToEdit.setWeight(weight);
            bookToEdit.setValue(value);
            bookToEdit.setPrice(price);
            bookToEdit.setDescription(description);

            // 4. Gọi DAO để lưu vào database (tương tác với Model)
            boolean success = bookDAO.updateBook(bookToEdit);
            if (success) {
                dialog.showSuccessMessage("Cập nhật thông tin sách thành công!", "Thành công");
                dialog.dispose(); // Đóng dialog sau khi lưu thành công
            } else {
                dialog.showErrorMessage("Lỗi khi lưu sách vào cơ sở dữ liệu.", "Lỗi");
            }
        } catch (NumberFormatException ex) {
            dialog.showErrorMessage("Vui lòng nhập đúng định dạng số cho các trường số (số trang, số lượng, giá...).", "Lỗi định dạng");
        } catch (Exception ex) {
            ex.printStackTrace();
            dialog.showErrorMessage("Đã xảy ra lỗi không mong muốn: " + ex.getMessage(), "Lỗi hệ thống");
        }
    }
}