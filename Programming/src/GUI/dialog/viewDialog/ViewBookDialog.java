package GUI.dialog.viewDialog;

import Model.Book;
import java.awt.*;

public class ViewBookDialog extends BaseViewDialog {
    private final Book book;

    public ViewBookDialog(Frame owner, Book book) {
        super(owner, "Chi tiết sách", book);
        this.book = book;
        completeInitialization(); // Gọi sau khi các biến đã được khởi tạo
    }

    @Override
    protected Object[][] getFields() {
        return new Object[][] {
            {"ID:", String.valueOf(book.getProductId())},
            {"Tên sách:", book.getTitle()},
            {"Tác giả:", book.getAuthors()},
            {"Nhà xuất bản:", book.getPublisher()},
            {"Số trang:", String.valueOf(book.getNumPages())},
            {"Ngôn ngữ:", book.getLanguage()},
            {"Thể loại:", book.getGenre()},
            {"Ngày xuất bản:", book.getPublicationDate()},
            {"Bìa sách:", book.getCoverType()},
            {"Kích thước:", book.getDimensions()},
            {"Cân nặng:", book.getWeight()},
            {"Giá bán:", String.format("%.2f VNĐ", book.getPrice())},
            {"Số lượng:", String.valueOf(book.getQuantity())},
            {"Mô tả:", book.getDescription()}
        };
    }
}
