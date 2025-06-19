package GUI.dialog.viewDialog;

import Model.Product;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public abstract class BaseViewDialog extends JDialog {
    protected final Product product;
    private boolean initialized = false;

    // Các màu sắc được sử dụng trong dialog
    protected static final Color HEADER_COLOR = new Color(70, 130, 180); // Steel Blue
    protected static final Color LABEL_COLOR = new Color(47, 79, 79);    // Dark Slate Gray
    protected static final Color VALUE_COLOR = new Color(25, 25, 112);   // Midnight Blue
    protected static final Color BACKGROUND_COLOR = new Color(240, 248, 255); // Alice Blue
    protected static final Color BUTTON_COLOR = new Color(65, 105, 225); // Royal Blue
    protected static final Color BUTTON_TEXT_COLOR = Color.BLACK;

    public BaseViewDialog(Frame owner, String title, Product product) {
        super(owner, title, true);
        this.product = product;
        // Xác định vị trí dialog ở giữa frame chính
        setLocationRelativeTo(owner);
    }

    // Phương thức này sẽ được gọi bởi lớp con sau khi constructor của lớp con đã hoàn tất
    protected void completeInitialization() {
        if (!initialized) {
            initUI();
            pack();
            // Sau khi pack, cần đặt vị trí dialog ở giữa frame chủ một lần nữa để đảm bảo
            setLocationRelativeTo(getOwner());
            initialized = true;
        }
    }

    private void initUI() {
        // Thiết lập giao diện tổng thể
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Panel chính chứa các thông tin sản phẩm
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Panel tiêu đề với màu nền khác
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(HEADER_COLOR);
        JLabel headerLabel = new JLabel("Thông tin chi tiết sản phẩm");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // Panel thông tin với viền và tiêu đề
        JPanel infoPanel = createGridPanel();
        infoPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(HEADER_COLOR, 2),
            "Thông tin sản phẩm",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            HEADER_COLOR
        ));

        // Thêm các cặp nhãn-giá trị
        Object[][] fields = getFields();
        addLabelValueRows(infoPanel, fields);

        // Nút đóng với màu sắc tùy chỉnh
        JButton btnClose = new JButton("Đóng");
        btnClose.setBackground(BUTTON_COLOR);
        btnClose.setForeground(BUTTON_TEXT_COLOR);
        btnClose.setFont(new Font("Arial", Font.BOLD, 14));
        btnClose.setFocusPainted(false);
        btnClose.addActionListener(e -> dispose());

        // Panel chân trang chứa nút đóng
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(BACKGROUND_COLOR);
        footerPanel.add(btnClose);

        // Kết hợp các thành phần
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);

        contentPanel.add(mainPanel, BorderLayout.CENTER);
        contentPanel.add(footerPanel, BorderLayout.SOUTH);

        // Thiết lập kích thước cửa sổ
        setContentPane(contentPanel);
        setSize(700, 600);
        setResizable(true);
    }

    // Tạo panel với layout lưới và màu nền
    protected static JPanel createGridPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 15, 10));
        panel.setBackground(BACKGROUND_COLOR);
        return panel;
    }

    // Thêm một cặp nhãn-giá trị với định dạng và màu sắc
    protected static void addLabelValueRow(JPanel panel, String label, String value) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Arial", Font.BOLD, 14));
        labelComponent.setForeground(LABEL_COLOR);
        labelComponent.setBorder(new EmptyBorder(5, 10, 5, 5));

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Arial", Font.PLAIN, 14));
        valueComponent.setForeground(VALUE_COLOR);
        valueComponent.setBorder(new EmptyBorder(5, 5, 5, 10));

        panel.add(labelComponent);
        panel.add(valueComponent);
    }

    protected static void addLabelValueRows(JPanel panel, Object[][] fields) {
        for (Object[] field : fields) {
            addLabelValueRow(panel, (String) field[0], (String) field[1]);
        }
    }

    // Lớp con sẽ triển khai phương thức này để cung cấp dữ liệu cụ thể
    protected abstract Object[][] getFields();
}
