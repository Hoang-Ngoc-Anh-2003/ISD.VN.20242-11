package GUI.dialog.addDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import GUI.component.ButtonUI.*;
import Model.*;
import Controller.CDController;

import javax.swing.border.TitledBorder;

public class AddCDDialog extends JDialog {

    private final JTextField titleTextField;
    private final JTextField artistTextField;
    private final JTextField recordLabelTextField;
    private final JTextField genreTextField;
    private final JFormattedTextField releaseDateTextField; // Sử dụng JFormattedTextField cho định dạng ngày
    private final JTextArea trackListTextArea;
    private final JFormattedTextField importDateTextField; // Sử dụng JFormattedTextField cho định dạng ngày
    private final JTextField quantityTextField;
    private final JTextField dimensionsTextField;
    private final JTextField weightTextField;
    private final JTextField sellingPriceTextField;
    private final JTextField importPriceTextField;
    private final JTextArea descriptionTextArea;
    private static final Insets labelMargin = new Insets(10, 10, 10, 15);
    private static final Insets fieldMargin = new Insets(10, 0, 10, 15);
	private boolean isSaveClicked = false;

    public AddCDDialog(JFrame parent, String title, boolean modal) {
        super(parent, title, modal);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(contentPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(5, 5, 5, 5);

        // --- Thông tin CD ---
        JPanel cdInfoPanel = new JPanel(new GridBagLayout());
        cdInfoPanel.setBorder(new TitledBorder("Thông tin chung"));
        GridBagConstraints gbcCD = new GridBagConstraints();
        gbcCD.fill = GridBagConstraints.HORIZONTAL;
        gbcCD.weightx = 0.5;
        gbcCD.insets = new Insets(5, 5, 5, 5);

        // Hàng 1: Tiêu đề - Hãng thu
        gbcCD.gridx = 0;
        gbcCD.gridy = 0;
        gbcCD.insets = labelMargin;
        cdInfoPanel.add(new JLabel("Tiêu đề :"), gbcCD);

        titleTextField = new JTextField(20);
        titleTextField.setText("CD mới");
        gbcCD.gridx = 1;
        gbcCD.gridy = 0;
        gbcCD.insets = fieldMargin;
        cdInfoPanel.add(titleTextField, gbcCD);

        gbcCD.gridx = 2;
        gbcCD.gridy = 0;
        gbcCD.insets = labelMargin;
        cdInfoPanel.add(new JLabel("Hãng thu :"), gbcCD);

        recordLabelTextField = new JTextField(20);
        recordLabelTextField.setText("Hãng thu mới");
        gbcCD.gridx = 3;
        gbcCD.gridy = 0;
        gbcCD.insets = fieldMargin;
        cdInfoPanel.add(recordLabelTextField, gbcCD);

        // Hàng 2: Nghệ sĩ - Thể loại
        gbcCD.gridx = 0;
        gbcCD.gridy = 1;
        gbcCD.insets = labelMargin;
        cdInfoPanel.add(new JLabel("Nghệ sĩ :"), gbcCD);

        artistTextField = new JTextField(20);
        artistTextField.setText("Nghệ sĩ mới");
        gbcCD.gridx = 1;
        gbcCD.gridy = 1;
        gbcCD.insets = fieldMargin;
        cdInfoPanel.add(artistTextField, gbcCD);

        gbcCD.gridx = 2;
        gbcCD.gridy = 1;
        gbcCD.insets = labelMargin;
        cdInfoPanel.add(new JLabel("Thể loại :"), gbcCD);

        genreTextField = new JTextField(20);
        genreTextField.setText("Pop");
        gbcCD.gridx = 3;
        gbcCD.gridy = 1;
        gbcCD.insets = fieldMargin;
        cdInfoPanel.add(genreTextField, gbcCD);

        // Hàng 3: Ngày phát hành - Danh sách bài hát
        gbcCD.gridx = 0;
        gbcCD.gridy = 2;
        gbcCD.insets = labelMargin;
        gbcCD.anchor = GridBagConstraints.NORTHWEST; // Neo label lên trên
        cdInfoPanel.add(new JLabel("Ngày phát hành :"), gbcCD);

        releaseDateTextField = new JFormattedTextField();
        releaseDateTextField.setText("2025-01-01");
        releaseDateTextField.setColumns(20);
        gbcCD.gridx = 1;
        gbcCD.gridy = 2;
        gbcCD.insets = fieldMargin;
        gbcCD.anchor = GridBagConstraints.NORTHWEST; // Neo textField lên trên
        cdInfoPanel.add(releaseDateTextField, gbcCD);
        gbcCD.anchor = GridBagConstraints.WEST; // Trả lại anchor mặc định

        gbcCD.gridx = 2;
        gbcCD.gridy = 2;
        gbcCD.insets = labelMargin;
        gbcCD.anchor = GridBagConstraints.NORTHWEST; // Neo label lên trên
        cdInfoPanel.add(new JLabel("Danh sách bài hát :"), gbcCD);

        trackListTextArea = new JTextArea(3, 20);
        trackListTextArea.setText("Bài hát 1, Bài hát 2");
        gbcCD.gridx = 3;
        gbcCD.gridy = 2;
        gbcCD.gridwidth = GridBagConstraints.REMAINDER; // Chiếm toàn bộ không gian còn lại bên phải
        gbcCD.gridheight = 2; // Cho phép JTextArea có chiều cao 2 hàng (hoặc hơn nếu cần)
        gbcCD.insets = fieldMargin;
        gbcCD.fill = GridBagConstraints.BOTH; // Lấp đầy cả chiều ngang và dọc
        cdInfoPanel.add(new JScrollPane(trackListTextArea), gbcCD);
        gbcCD.gridwidth = 1; // Trả lại gridwidth mặc định
        gbcCD.gridheight = 1; // Trả lại gridheight mặc định
        gbcCD.fill = GridBagConstraints.HORIZONTAL; // Trả lại fill mặc định

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH; // Fill cả chiều ngang và dọc cho panel thông tin CD
        gbc.weighty = 0.0; // Reset weighty
        contentPanel.add(cdInfoPanel, gbc);
        gbc.gridwidth = 1; // Reset gridwidth
        gbc.fill = GridBagConstraints.HORIZONTAL; // Trả lại fill mặc định

        // --- Thông tin nhập kho ---
        JPanel importInfoPanel = new JPanel(new GridBagLayout());
        importInfoPanel.setBorder(new TitledBorder("Thông tin nhập kho"));
        GridBagConstraints gbcImport = new GridBagConstraints();
        gbcImport.fill = GridBagConstraints.HORIZONTAL;
        gbcImport.weightx = 0.5;
        gbcImport.insets = new Insets(5, 5, 5, 5);

        // Hàng 1: Ngày nhập - Số lượng
        gbcImport.gridx = 0;
        gbcImport.gridy = 0;
        gbcImport.insets = labelMargin;
        importInfoPanel.add(new JLabel("Ngày nhập :"), gbcImport);

        importDateTextField = new JFormattedTextField();
        importDateTextField.setText("2025-01-01");
        importDateTextField.setColumns(20);
        gbcImport.gridx = 1;
        gbcImport.gridy = 0;
        gbcImport.insets = fieldMargin;
        importInfoPanel.add(importDateTextField, gbcImport);

        gbcImport.gridx = 2;
        gbcImport.gridy = 0;
        gbcImport.insets = labelMargin;
        importInfoPanel.add(new JLabel("Số lượng :"), gbcImport);

        quantityTextField = new JTextField(10);
        quantityTextField.setText("1");
        gbcImport.gridx = 3;
        gbcImport.gridy = 0;
        gbcImport.insets = fieldMargin;
        importInfoPanel.add(quantityTextField, gbcImport);

        // Hàng 2: Kích thước - Trọng lượng
        gbcImport.gridx = 0;
        gbcImport.gridy = 1;
        gbcImport.insets = labelMargin;
        importInfoPanel.add(new JLabel("Kích thước :"), gbcImport);

        dimensionsTextField = new JTextField(20);
        dimensionsTextField.setText("12x12x1 cm");
        gbcImport.gridx = 1;
        gbcImport.gridy = 1;
        gbcImport.insets = fieldMargin;
        importInfoPanel.add(dimensionsTextField, gbcImport);

        gbcImport.gridx = 2;
        gbcImport.gridy = 1;
        gbcImport.insets = labelMargin;
        importInfoPanel.add(new JLabel("Trọng lượng :"), gbcImport);

        weightTextField = new JTextField(10);
        weightTextField.setText("0.1");
        gbcImport.gridx = 3;
        gbcImport.gridy = 1;
        gbcImport.insets = fieldMargin;
        importInfoPanel.add(weightTextField, gbcImport);

        // Hàng 3: Giá bán - Giá nhập
        gbcImport.gridx = 0;
        gbcImport.gridy = 2;
        gbcImport.insets = labelMargin;
        importInfoPanel.add(new JLabel("Giá bán :"), gbcImport);

        sellingPriceTextField = new JTextField(10);
        sellingPriceTextField.setText("0.0");
        gbcImport.gridx = 1;
        gbcImport.gridy = 2;
        gbcImport.insets = fieldMargin;
        importInfoPanel.add(sellingPriceTextField, gbcImport);

        gbcImport.gridx = 2;
        gbcImport.gridy = 2;
        gbcImport.insets = labelMargin;
        importInfoPanel.add(new JLabel("Giá nhập :"), gbcImport);

        importPriceTextField = new JTextField(10);
        importPriceTextField.setText("0.0");
        gbcImport.gridx = 3;
        gbcImport.gridy = 2;
        gbcImport.insets = fieldMargin;
        importInfoPanel.add(importPriceTextField, gbcImport);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(importInfoPanel, gbc);
        gbc.gridwidth = 1; // Reset gridwidth

        // --- Mô tả ---
        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.setBorder(new TitledBorder("Mô tả"));
        descriptionTextArea = new JTextArea(5, 40);
        descriptionTextArea.setText("");
        descriptionPanel.add(new JScrollPane(descriptionTextArea), BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0; // Cho phép mô tả chiếm nhiều không gian dọc
        contentPanel.add(descriptionPanel, gbc);
        gbc.weighty = 0.0; // Reset weighty

        // Buttons Panel ở phía dưới
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
	    JButton cancelButton = new RoundedButton("Hủy", 10, new Color(255, 77, 77), new Color(204, 0, 0), new Color(204, 0, 0));
	    JButton saveButton = new RoundedButton("Lưu", 10, new Color(0, 155, 229), new Color(0, 104, 190), new Color(0, 104, 190));
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH); 

        // --- Action Listeners cho nút Lưu và Hủy ---
        saveButton.addActionListener(e -> {
            try {
                CDFormData data = getCDFormData();
                // Kiểm tra dữ liệu bắt buộc
                if (data.title.isEmpty() || data.artists.isEmpty() || data.warehouseEntryDate.isEmpty()) {
                    JOptionPane.showMessageDialog(AddCDDialog.this, "Vui lòng điền đầy đủ các trường bắt buộc.", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                CD cd = new CD(0, data.title, "CD", data.value, data.price, "0", data.description,
                        data.quantity, data.weight, data.dimensions, data.warehouseEntryDate, data.artists, data.recordLabel, data.tracklist, data.genre, data.releaseDate);
                CDController cdController = new CDController();
                boolean success = cdController.addCD(cd);
                if (success) {
                    JOptionPane.showMessageDialog(AddCDDialog.this, "Thêm CD thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    isSaveClicked = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(AddCDDialog.this, "Lỗi khi lưu CD vào cơ sở dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(AddCDDialog.this, "Vui lòng nhập đúng định dạng số cho số lượng, giá.", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(AddCDDialog.this, "Đã xảy ra lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> {
            isSaveClicked = false;
            dispose();
        });

        pack();
        setLocationRelativeTo(parent);
    }

    public boolean isSaveClicked() { return isSaveClicked; }

    // Thêm class tiện ích bên trong AddCDDialog
    private static class CDFormData {
        String title, warehouseEntryDate, dimensions, weight, description, artists, recordLabel, tracklist, genre, releaseDate;
        double value, price;
        int quantity;
    }
    private double parseDoubleField(JTextField field) {
        try {
            return Double.parseDouble(field.getText().trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private int parseIntField(JTextField field) {
        try {
            return Integer.parseInt(field.getText().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private CDFormData getCDFormData() {
        CDFormData data = new CDFormData();
        data.title = titleTextField.getText().trim();
        data.warehouseEntryDate = importDateTextField.getText().trim();
        data.dimensions = dimensionsTextField.getText().trim();
        data.weight = weightTextField.getText().trim();
        data.description = descriptionTextArea.getText().trim();
        data.value = parseDoubleField(sellingPriceTextField);
        data.price = parseDoubleField(importPriceTextField);
        data.quantity = parseIntField(quantityTextField);
        data.artists = artistTextField.getText().trim();
        data.recordLabel = recordLabelTextField.getText().trim();
        data.tracklist = trackListTextArea.getText().trim();
        data.genre = genreTextField.getText();
        data.releaseDate = releaseDateTextField.getText().trim();
        return data;
    }
}
