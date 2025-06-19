package GUI.dialog.viewDialog;

import Model.CD;
import java.awt.*;

public class ViewCDDialog extends BaseViewDialog {
    private final CD cd;

    public ViewCDDialog(Frame owner, CD cd) {
        super(owner, "Chi tiết CD", cd);
        this.cd = cd;
        completeInitialization(); // Gọi sau khi các biến đã được khởi tạo
    }

    @Override
    protected Object[][] getFields() {
        return new Object[][] {
            {"ID:", String.valueOf(cd.getProductId())},
            {"Tiêu đề:", cd.getTitle()},
            {"Nghệ sĩ:", cd.getArtists()},
            {"Hãng thu:", cd.getRecordLabel()},
            {"Thể loại:", cd.getGenre()},
            {"Ngày phát hành:", cd.getReleaseDate()},
            {"Danh sách bài hát:", cd.getTracklist()},
            {"Kích thước:", cd.getDimensions()},
            {"Cân nặng:", cd.getWeight()},
            {"Giá bán:", String.format("%.2f VNĐ", cd.getPrice())},
            {"Số lượng:", String.valueOf(cd.getQuantity())},
            {"Mô tả:", cd.getDescription()}
        };
    }
}
