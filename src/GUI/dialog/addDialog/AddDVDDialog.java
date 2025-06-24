package GUI.dialog.addDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import javax.swing.border.TitledBorder;
import Controller.DVDController;
import Model.DVD;
import GUI.component.ButtonUI.*;

public class AddDVDDialog extends JDialog {

    private final JTextField titleTextField;
    private final JTextField directorTextField;
	private final JTextField productionCompanyTextField;
    private final JComboBox<String> languageComboBox;
    private final JComboBox<String> subtitleComboBox;
    private final JComboBox<String> genreComboBox;
    private final JFormattedTextField releaseDateTextField; // Sử dụng JFormattedTextField cho định dạng ngày
	private final JFormattedTextField importDateTextField; // Sử dụng JFormattedTextField cho định dạng ngày
    private final JTextField quantityTextField;
    private final JTextField dimensionsTextField;
    private final JTextField weightTextField;
    private final JTextField sellingPriceTextField;
    private final JTextField importPriceTextField;
    private final JTextArea descriptionTextArea;
    private final JTextField durationTextField;
    private final JRadioButton blurayRadioButton;
	private boolean isSaveClicked = false;
    private static final Insets labelMargin = new Insets(10, 10, 10, 15);
    private static final Insets fieldMargin = new Insets(10, 0, 10, 15);

	public AddDVDDialog(JFrame parent, String title, boolean modal) {
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

        // --- Thông tin chung ---
        JPanel generalInfoPanel = new JPanel(new GridBagLayout());
        generalInfoPanel.setBorder(new TitledBorder("Thông tin chung"));
        GridBagConstraints gbcGeneral = new GridBagConstraints();
        gbcGeneral.fill = GridBagConstraints.HORIZONTAL;
        gbcGeneral.weightx = 0.5;
        gbcGeneral.insets = new Insets(5, 5, 5, 5);

        // Hàng 1: Tiêu đề - Ngôn ngữ
        gbcGeneral.gridx = 0;
        gbcGeneral.gridy = 0;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Tiêu đề :"), gbcGeneral);

        gbcGeneral.gridx = 1;
        gbcGeneral.gridy = 0;
        gbcGeneral.insets = fieldMargin;
        titleTextField = new JTextField(20);
        titleTextField.setText("DVD mới");
        generalInfoPanel.add(titleTextField, gbcGeneral);

        gbcGeneral.gridx = 2;
        gbcGeneral.gridy = 0;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Ngôn ngữ :"), gbcGeneral);

        gbcGeneral.gridx = 3;
        gbcGeneral.gridy = 0;
        gbcGeneral.insets = fieldMargin;
        languageComboBox = new JComboBox<>(new String[]{"Tiếng Việt", "Tiếng Anh", "Khác"});
        languageComboBox.setSelectedIndex(0);
        generalInfoPanel.add(languageComboBox, gbcGeneral);

        // Hàng 2: Đạo diễn - Phụ đề
        gbcGeneral.gridx = 0;
        gbcGeneral.gridy = 1;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Đạo diễn :"), gbcGeneral);

        gbcGeneral.gridx = 1;
        gbcGeneral.gridy = 1;
        gbcGeneral.insets = fieldMargin;
        directorTextField = new JTextField(20);
        directorTextField.setText("Đạo diễn mới");
        generalInfoPanel.add(directorTextField, gbcGeneral);

        gbcGeneral.gridx = 2;
        gbcGeneral.gridy = 1;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Phụ đề :"), gbcGeneral);

        gbcGeneral.gridx = 3;
        gbcGeneral.gridy = 1;
        gbcGeneral.insets = fieldMargin;
        subtitleComboBox = new JComboBox<>(new String[]{"Không", "Tiếng Việt", "Tiếng Anh", "Khác"});
        subtitleComboBox.setSelectedIndex(0);
        generalInfoPanel.add(subtitleComboBox, gbcGeneral);

        // Hàng 3: Thời lượng - Thể loại
        gbcGeneral.gridx = 0;
        gbcGeneral.gridy = 2;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Thời lượng (phút) :"), gbcGeneral);

        gbcGeneral.gridx = 1;
        gbcGeneral.gridy = 2;
        gbcGeneral.insets = fieldMargin;
		durationTextField = new JTextField(10);
        durationTextField.setText("120");
        generalInfoPanel.add(durationTextField, gbcGeneral);

        gbcGeneral.gridx = 2;
        gbcGeneral.gridy = 2;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Thể loại :"), gbcGeneral);

        gbcGeneral.gridx = 3;
        gbcGeneral.gridy = 2;
        gbcGeneral.insets = fieldMargin;
        genreComboBox = new JComboBox<>(new String[]{"Hành động", "Tình cảm", "Kinh dị", "Khác"});
        genreComboBox.setSelectedIndex(0);
        generalInfoPanel.add(genreComboBox, gbcGeneral);

        // Hàng 4: Hãng sản xuất - Ngày phát hành
        gbcGeneral.gridx = 0;
        gbcGeneral.gridy = 3;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Hãng sản xuất :"), gbcGeneral);

        gbcGeneral.gridx = 1;
        gbcGeneral.gridy = 3;
        gbcGeneral.insets = fieldMargin;
        productionCompanyTextField = new JTextField(20);
        productionCompanyTextField.setText("Hãng mới");
        generalInfoPanel.add(productionCompanyTextField, gbcGeneral);

        gbcGeneral.gridx = 2;
        gbcGeneral.gridy = 3;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Ngày phát hành :"), gbcGeneral);

        gbcGeneral.gridx = 3;
        gbcGeneral.gridy = 3;
        gbcGeneral.insets = fieldMargin;
        releaseDateTextField = new JFormattedTextField();
        releaseDateTextField.setText("2025-01-01");
        releaseDateTextField.setColumns(20);
        generalInfoPanel.add(releaseDateTextField, gbcGeneral);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        contentPanel.add(generalInfoPanel, gbc);
        gbc.gridwidth = 1; // Reset gridwidth

        // --- Thông tin đĩa ---
        JPanel discInfoPanel = new JPanel(new GridBagLayout());
        discInfoPanel.setBorder(new TitledBorder("Thông tin đĩa"));
        GridBagConstraints gbcDisc = new GridBagConstraints();
        gbcDisc.fill = GridBagConstraints.HORIZONTAL;
        gbcDisc.weightx = 0.5;
        gbcDisc.insets = new Insets(5, 5, 5, 5);

        // Hàng 1: Loại đĩa
        gbcDisc.gridx = 0;
        gbcDisc.gridy = 0;
        gbcDisc.insets = labelMargin;
        discInfoPanel.add(new JLabel("Loại đĩa :"), gbcDisc);

        gbcDisc.gridx = 1;
        gbcDisc.gridy = 0;
        gbcDisc.gridwidth = 3;
        gbcDisc.insets = fieldMargin;
        JPanel discTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        blurayRadioButton = new JRadioButton("Blu-ray");
        JRadioButton hdDvdRadioButton = new JRadioButton("HD-DVD");
        ButtonGroup discTypeGroup = new ButtonGroup();
        discTypeGroup.add(blurayRadioButton);
        discTypeGroup.add(hdDvdRadioButton);
        blurayRadioButton.setSelected(true); // Mặc định chọn Blu-ray
        discTypePanel.add(blurayRadioButton);
        discTypePanel.add(Box.createHorizontalStrut(30)); // Thêm khoảng trống 30 pixel
        discTypePanel.add(hdDvdRadioButton);
        discInfoPanel.add(discTypePanel, gbcDisc);
        gbcDisc.gridwidth = 1; // Reset gridwidth

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(discInfoPanel, gbc);
        gbc.gridwidth = 1; // Reset gridwidth

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

        gbcImport.gridx = 1;
        gbcImport.gridy = 0;
        gbcImport.insets = fieldMargin;
        importDateTextField = new JFormattedTextField();
        importDateTextField.setText("2025-01-01");
        importDateTextField.setColumns(20);
        importInfoPanel.add(importDateTextField, gbcImport);

        gbcImport.gridx = 2;
        gbcImport.gridy = 0;
        gbcImport.insets = labelMargin;
        importInfoPanel.add(new JLabel("Số lượng :"), gbcImport);

        gbcImport.gridx = 3;
        gbcImport.gridy = 0;
        gbcImport.insets = fieldMargin;
        quantityTextField = new JTextField(10);
        quantityTextField.setText("1");
        importInfoPanel.add(quantityTextField, gbcImport);

        // Hàng 2: Kích thước - Trọng lượng
        gbcImport.gridx = 0;
        gbcImport.gridy = 1;
        gbcImport.insets = labelMargin;
        importInfoPanel.add(new JLabel("Kích thước :"), gbcImport);

        gbcImport.gridx = 1;
        gbcImport.gridy = 1;
        gbcImport.insets = fieldMargin;
        dimensionsTextField = new JTextField(20);
        dimensionsTextField.setText("19x13.5x1.5 cm");
        importInfoPanel.add(dimensionsTextField, gbcImport);

        gbcImport.gridx = 2;
        gbcImport.gridy = 1;
        gbcImport.insets = labelMargin;
        importInfoPanel.add(new JLabel("Trọng lượng :"), gbcImport);

        gbcImport.gridx = 3;
        gbcImport.gridy = 1;
        gbcImport.insets = fieldMargin;
        weightTextField = new JTextField(10);
        weightTextField.setText("0.2");
        importInfoPanel.add(weightTextField, gbcImport);

        // Hàng 3: Giá bán - Giá nhập
        gbcImport.gridx = 0;
        gbcImport.gridy = 2;
        gbcImport.insets = labelMargin;
        importInfoPanel.add(new JLabel("Giá bán :"), gbcImport);

        gbcImport.gridx = 1;
        gbcImport.gridy = 2;
        gbcImport.insets = fieldMargin;
        sellingPriceTextField = new JTextField(10);
        sellingPriceTextField.setText("0.0");
        importInfoPanel.add(sellingPriceTextField, gbcImport);

        gbcImport.gridx = 2;
        gbcImport.gridy = 2;
        gbcImport.insets = labelMargin;
        importInfoPanel.add(new JLabel("Giá nhập :"), gbcImport);

        gbcImport.gridx = 3;
        gbcImport.gridy = 2;
        gbcImport.insets = fieldMargin;
        importPriceTextField = new JTextField(10);
        importPriceTextField.setText("0.0");
        importInfoPanel.add(importPriceTextField, gbcImport);

        gbc.gridx = 0;
        gbc.gridy = 2;
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
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0; // Cho phép panel mô tả mở rộng theo chiều dọc
        contentPanel.add(descriptionPanel, gbc);
        gbc.gridwidth = 1; // Reset gridwidth
        gbc.weighty = 0.0; // Reset weighty

        // --- Buttons Panel ở phía dưới ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
		JButton cancelButton = new RoundedButton("Hủy", 10, new Color(255, 77, 77), new Color(204, 0, 0), new Color(204, 0, 0));
		JButton saveButton = new RoundedButton("Lưu", 10, new Color(0, 155, 229), new Color(0, 104, 190), new Color(0, 104, 190));
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners cho nút Lưu và Hủy
        saveButton.addActionListener(e -> saveDVD());

        cancelButton.addActionListener(e -> {
            isSaveClicked = false;
            dispose();
        });

        pack();
        setLocationRelativeTo(parent);
    }

    public boolean isSaveClicked() {
        return isSaveClicked;
    }

    private void saveDVD() {
        try {
            DVDFormData data = getDVDFormData();
            // Kiểm tra dữ liệu bắt buộc
            if (data.title.isEmpty() || data.director.isEmpty() || data.releaseDate.isEmpty() || data.importDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ các trường bắt buộc.", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                return;
            }
            DVD dvd = new DVD(0, data.title, "DVD", data.sellingPrice, data.importPrice, "0", data.description, data.quantity, data.weight, data.dimensions, data.importDate, data.discType, data.director, data.runtime, data.productionCompany, data.language, data.subtitles, data.releaseDate, data.genre);
            boolean success = new DVDController().addDVD(dvd);
            if (success) {
                JOptionPane.showMessageDialog(this, "Thêm DVD thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                isSaveClicked = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi lưu DVD vào cơ sở dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số cho thời lượng, số lượng, giá và trọng lượng.", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Tiện ích gom dữ liệu form DVD
        private record DVDFormData(String title, String director, String productionCompany, String language,
                                   String subtitles, String genre, String releaseDate, String importDate, String dimensions,
                                   String weight, String description, String discType, int quantity, int runtime,
                                   double sellingPrice, double importPrice) {
    }
    private DVDFormData getDVDFormData() {
        return new DVDFormData(
            titleTextField.getText().trim(),
            directorTextField.getText().trim(),
            productionCompanyTextField.getText().trim(),
            (String) languageComboBox.getSelectedItem(),
            (String) subtitleComboBox.getSelectedItem(),
            (String) genreComboBox.getSelectedItem(),
            releaseDateTextField.getText().trim(),
            importDateTextField.getText().trim(),
            dimensionsTextField.getText().trim(),
            weightTextField.getText().trim(),
            descriptionTextArea.getText().trim(),
            blurayRadioButton.isSelected() ? "Blu-ray" : "HD-DVD",
            Integer.parseInt(quantityTextField.getText().trim()),
            Integer.parseInt(durationTextField.getText().trim()),
            Double.parseDouble(sellingPriceTextField.getText().trim()),
            Double.parseDouble(importPriceTextField.getText().trim())
        );
    }
}
