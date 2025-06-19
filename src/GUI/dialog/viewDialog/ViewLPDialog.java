package GUI.dialog.viewDialog;

import Model.LP;
import java.awt.*;

public class ViewLPDialog extends BaseViewDialog {
    private final LP lp;

    public ViewLPDialog(Frame owner, LP lp) {
        super(owner, "Chi tiết đĩa than", lp);
        this.lp = lp;
        completeInitialization(); // Gọi sau khi các biến đã được khởi tạo
    }

    @Override
    protected Object[][] getFields() {
        return new Object[][] {
            {"ID:", String.valueOf(lp.getProductId())},
            {"Tiêu đề:", lp.getTitle()},
            {"Nghệ sĩ:", lp.getArtists()},
            {"Hãng thu:", lp.getRecordLabel()},
            {"Thể loại:", lp.getGenre()},
            {"Ngày phát hành:", lp.getReleaseDate()},
            {"Danh sách bài hát:", lp.getTracklist()},
            {"Kích thước:", lp.getDimensions()},
            {"Cân nặng:", lp.getWeight()},
            {"Giá bán:", String.format("%.2f VNĐ", lp.getPrice())},
            {"Số lượng:", String.valueOf(lp.getQuantity())},
            {"Mô tả:", lp.getDescription()}
        };
    }
}
