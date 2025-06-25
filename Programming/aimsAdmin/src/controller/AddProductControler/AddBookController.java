package controller.AddProductControler;

import model.dao.*;
import model.entity.*;
import view.dialog.addDialog.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AddBookController {
    private AddBookDialog addBookDialog;

    public AddBookController(AddBookDialog dialog) {
        this.addBookDialog = dialog;
        setupListeners();
    }

    private void setupListeners() {
        addBookDialog.addSaveButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddBook();
            }
        });

        addBookDialog.addCancelButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBookDialog.closeDialog();
            }
        });
    }


    public boolean handleAddBook() {
        String title = addBookDialog.getTitle();
        String authors = addBookDialog.getAuthor();
        String publisher = addBookDialog.getPublisher();
        String numPagesStr = addBookDialog.getPageCount();
        String language = addBookDialog.getLanguage();
        String genre = addBookDialog.getGenre();
        String publicationDate = addBookDialog.getPublishDate();
        String coverType = addBookDialog.getCoverType();
        String warehouseEntryDate = addBookDialog.getImportDate();
        String quantityStr = addBookDialog.getQuantity();
        String dimensions = addBookDialog.getDimensions();
        String weight = addBookDialog.getWeight();
        String valueStr = addBookDialog.getSellingPrice();
        String priceStr = addBookDialog.getImportPrice();
        String description = addBookDialog.getDescription();

        if (title.isEmpty() || authors.isEmpty() || publisher.isEmpty() || publicationDate.isEmpty() ||
            coverType.isEmpty() || warehouseEntryDate.isEmpty() || valueStr.isEmpty() || priceStr.isEmpty() || quantityStr.isEmpty()) {
            addBookDialog.showWarningMessage("Vui lòng điền đầy đủ các trường bắt buộc (Tiêu đề, Tác giả, Số trang, Ngày xuất bản, Ngày nhập kho, Số lượng, Giá bán, Giá nhập).");
            return false;
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
            addBookDialog.showErrorMessage("Vui lòng nhập đúng định dạng số cho Số trang, Số lượng, Giá bán, Giá nhập và Trọng lượng.");
            return false;
        }

        try {
            LocalDate.parse(publicationDate);
            LocalDate.parse(warehouseEntryDate);
        } catch (DateTimeParseException e) {
            addBookDialog.showErrorMessage("Định dạng ngày không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.");
            return false;
        }


        try {
            Book book = new Book(0, title, "book", value, price, "0", description,
                quantity, weight, dimensions, warehouseEntryDate,authors,
                coverType, publisher, publicationDate, numPages,
                language, genre);

            boolean success = BookDAO.getInstance().addBook(book);
            if (success) {
                addBookDialog.showSuccessMessage("Thêm sách thành công!");
                addBookDialog.closeDialog();
                return true;
            } else {
                addBookDialog.showErrorMessage("Lỗi khi lưu vào cơ sở dữ liệu.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            addBookDialog.showErrorMessage("Đã xảy ra lỗi không mong muốn: " + e.getMessage());
            return false;
        }
    }

}
