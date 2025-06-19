package GUI.dialog.viewDialog;

import Model.DVD;
import java.awt.*;

public class ViewDVDDialog extends BaseViewDialog {
    private final DVD dvd;

    public ViewDVDDialog(Frame owner, DVD dvd) {
        super(owner, "Chi tiết DVD", dvd);
        this.dvd = dvd;
        completeInitialization(); // Gọi sau khi các biến đã được khởi tạo
    }

    @Override
    protected Object[][] getFields() {
        return new Object[][] {
            {"ID:", String.valueOf(dvd.getProductId())},
            {"Tiêu đề:", dvd.getTitle()},
            {"Đạo diễn:", dvd.getDirector()},
            {"Loại đĩa:", dvd.getDiscType()},
            {"Thời lượng (phút):", String.valueOf(dvd.getRuntime())},
            {"Hãng sản xuất:", dvd.getStudio()},
            {"Ngôn ngữ:", dvd.getLanguage()},
            {"Phụ đề:", dvd.getSubtitles()},
            {"Thể loại:", dvd.getGenre()},
            {"Ngày phát hành:", dvd.getReleaseDate()},
            {"Kích thước:", dvd.getDimensions()},
            {"Cân nặng:", dvd.getWeight()},
            {"Giá bán:", String.format("%.2f VNĐ", dvd.getPrice())},
            {"Số lượng:", String.valueOf(dvd.getQuantity())},
            {"Mô tả:", dvd.getDescription()}
        };
    }
}
