package GUI.dialog.editDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import GUI.component.ButtonUI.*;
import Model.DVD;
import Controller.DVDController;

import javax.swing.border.TitledBorder;

public class EditDVDDialog extends JDialog {

    private final JTextField titleTextField;
    private final JTextField directorTextField;
    private final JTextField durationTextField;
    private final JTextField productionCompanyTextField;
    private final JComboBox<String> languageComboBox;
    private final JComboBox<String> subtitleComboBox;
    private final JComboBox<String> genreComboBox;
    private final JFormattedTextField releaseDateTextField; // Sử dụng JFormattedTextField cho định dạng ngày
    private final JRadioButton blurayRadioButton;
	private final JFormattedTextField importDateTextField; // Sử dụng JFormattedTextField cho định dạng ngày
    private final JTextField quantityTextField;
    private final JTextField dimensionsTextField;
    private final JTextField weightTextField;
    private final JTextField sellingPriceTextField;
    private final JTextField importPriceTextField;
    private final JTextArea descriptionTextArea;
	private boolean isSaveClicked = false;
    private final DVD dvdToEdit; // Thêm biến để lưu trữ thông tin DVD cần sửa

	public EditDVDDialog(JFrame parent, String title, boolean modal, DVD dvd) {
        super(parent, title, modal);
        this.dvdToEdit = dvd; // Lưu trữ đối tượng DVD được truyền vào
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
	    Insets labelMargin = new Insets(10, 10, 10, 15);
	    gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Tiêu đề :"), gbcGeneral);

        gbcGeneral.gridx = 1;
        gbcGeneral.gridy = 0;
		Insets fieldMargin = new Insets(10, 0, 10, 15);
		gbcGeneral.insets = fieldMargin;
        generalInfoPanel.add(titleTextField = new JTextField(20), gbcGeneral);
        titleTextField.setText(dvdToEdit.getTitle()); // Điền thông tin

        gbcGeneral.gridx = 2;
        gbcGeneral.gridy = 0;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Ngôn ngữ :"), gbcGeneral);

        gbcGeneral.gridx = 3;
        gbcGeneral.gridy = 0;
        gbcGeneral.insets = fieldMargin;
        languageComboBox = new JComboBox<>(new String[]{"Tiếng Việt", "Tiếng Anh", "Khác"});
        generalInfoPanel.add(languageComboBox, gbcGeneral);
        languageComboBox.setSelectedItem(dvdToEdit.getLanguage()); // Điền thông tin

        // Hàng 2: Đạo diễn - Phụ đề
        gbcGeneral.gridx = 0;
        gbcGeneral.gridy = 1;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Đạo diễn :"), gbcGeneral);

        gbcGeneral.gridx = 1;
        gbcGeneral.gridy = 1;
        gbcGeneral.insets = fieldMargin;
        generalInfoPanel.add(directorTextField = new JTextField(20), gbcGeneral);
        directorTextField.setText(dvdToEdit.getDirector()); // Điền thông tin

        gbcGeneral.gridx = 2;
        gbcGeneral.gridy = 1;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Phụ đề :"), gbcGeneral);

        gbcGeneral.gridx = 3;
        gbcGeneral.gridy = 1;
        gbcGeneral.insets = fieldMargin;
        subtitleComboBox = new JComboBox<>(new String[]{"Không", "Tiếng Việt", "Tiếng Anh", "Khác"});
        generalInfoPanel.add(subtitleComboBox, gbcGeneral);
        subtitleComboBox.setSelectedItem(dvdToEdit.getSubtitles()); // Điền thông tin

        // Hàng 3: Thời lượng - Thể loại
        gbcGeneral.gridx = 0;
        gbcGeneral.gridy = 2;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Thời lượng :"), gbcGeneral);

        gbcGeneral.gridx = 1;
        gbcGeneral.gridy = 2;
        gbcGeneral.insets = fieldMargin;
        generalInfoPanel.add(durationTextField = new JTextField(20), gbcGeneral);
        durationTextField.setText(String.valueOf(dvdToEdit.getRuntime())); // Điền thông tin

        gbcGeneral.gridx = 2;
        gbcGeneral.gridy = 2;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Thể loại :"), gbcGeneral);

        gbcGeneral.gridx = 3;
        gbcGeneral.gridy = 2;
        gbcGeneral.insets = fieldMargin;
        genreComboBox = new JComboBox<>(new String[]{"Hành động", "Tình cảm", "Kinh dị", "Khác"});
        generalInfoPanel.add(genreComboBox, gbcGeneral);
        genreComboBox.setSelectedItem(dvdToEdit.getGenre()); // Điền thông tin

        // Hàng 4: Hãng sản xuất - Ngày phát hành
        gbcGeneral.gridx = 0;
        gbcGeneral.gridy = 3;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Hãng sản xuất :"), gbcGeneral);

        gbcGeneral.gridx = 1;
        gbcGeneral.gridy = 3;
        gbcGeneral.insets = fieldMargin;
        generalInfoPanel.add(productionCompanyTextField = new JTextField(20), gbcGeneral);
        productionCompanyTextField.setText(dvdToEdit.getStudio()); // Điền thông tin

        gbcGeneral.gridx = 2;
        gbcGeneral.gridy = 3;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Ngày phát hành :"), gbcGeneral);

        gbcGeneral.gridx = 3;
        gbcGeneral.gridy = 3;
        gbcGeneral.insets = fieldMargin;
        releaseDateTextField = new JFormattedTextField();
        releaseDateTextField.setColumns(20);
        releaseDateTextField.setText(dvdToEdit.getReleaseDate()); // Điền thông tin
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
	    JRadioButton hdvddRadioButton = new JRadioButton("HD-DVD");
        ButtonGroup discTypeGroup = new ButtonGroup();
        discTypeGroup.add(blurayRadioButton);
        discTypeGroup.add(hdvddRadioButton);

        if (dvdToEdit.getDiscType().equalsIgnoreCase("Blu-ray")) {
            blurayRadioButton.setSelected(true);
        } else if (dvdToEdit.getDiscType().equalsIgnoreCase("HD-DVD")) {
            hdvddRadioButton.setSelected(true);
        }

        discTypePanel.add(blurayRadioButton);
        discTypePanel.add(Box.createHorizontalStrut(30)); // Thêm khoảng trống 20 pixel
        discTypePanel.add(hdvddRadioButton);
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
        importDateTextField.setColumns(20);
        importDateTextField.setText(dvdToEdit.getWarehouseEntryDate()); // Điền thông tin
        importInfoPanel.add(importDateTextField, gbcImport);

        gbcImport.gridx = 2;
        gbcImport.gridy = 0;
        gbcImport.insets = labelMargin;
        importInfoPanel.add(new JLabel("Số lượng :"), gbcImport);

        gbcImport.gridx = 3;
        gbcImport.gridy = 0;
        gbcImport.insets = fieldMargin;
        quantityTextField = new JTextField(10);
        quantityTextField.setText(String.valueOf(dvdToEdit.getQuantity())); // Điền thông tin
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
        dimensionsTextField.setText(dvdToEdit.getDimensions()); // Điền thông tin
        importInfoPanel.add(dimensionsTextField, gbcImport);

        gbcImport.gridx = 2;
        gbcImport.gridy = 1;
        gbcImport.insets = labelMargin;
        importInfoPanel.add(new JLabel("Trọng lượng :"), gbcImport);

        gbcImport.gridx = 3;
        gbcImport.gridy = 1;
        gbcImport.insets = fieldMargin;
        weightTextField = new JTextField(10);
        weightTextField.setText(dvdToEdit.getWeight()); // Điền thông tin
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
        sellingPriceTextField.setText(String.valueOf(dvdToEdit.getValue())); // Điền thông tin
        importInfoPanel.add(sellingPriceTextField, gbcImport);

        gbcImport.gridx = 2;
        gbcImport.gridy = 2;
        gbcImport.insets = labelMargin;
        importInfoPanel.add(new JLabel("Giá nhập :"), gbcImport);

        gbcImport.gridx = 3;
        gbcImport.gridy = 2;
        gbcImport.insets = fieldMargin;
        importPriceTextField = new JTextField(10);
        importPriceTextField.setText(String.valueOf(dvdToEdit.getPrice())); // Điền thông tin
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
        descriptionTextArea.setText(dvdToEdit.getDescription()); // Điền thông tin
        descriptionPanel.add(new JScrollPane(descriptionTextArea), BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 3;
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

        // Action Listeners cho nút Lưu và Hủy
        saveButton.addActionListener(e -> saveDVD());
        cancelButton.addActionListener(e -> {
            isSaveClicked = false;
            dispose();
        });

        pack();
        setLocationRelativeTo(parent);
    }

    public boolean isSaveClicked() { return isSaveClicked; }

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

    private void copyUneditableFields(DVD from, DVD to) {
        to.setProductId(from.getProductId());
        to.setCategory(from.getCategory());
        to.setBarcode(from.getBarcode());
    }

    private void saveDVD() {
        try {
            String title = titleTextField.getText().trim();
            String director = directorTextField.getText().trim();
            int runtime = parseIntField(durationTextField);
            String studio = productionCompanyTextField.getText().trim();
            String language = (String) languageComboBox.getSelectedItem();
            String subtitles = (String) subtitleComboBox.getSelectedItem();
            String genre = (String) genreComboBox.getSelectedItem();
            String releaseDate = releaseDateTextField.getText().trim();
            String discType = blurayRadioButton.isSelected() ? "Blu-ray" : "HD-DVD";
            String warehouseEntryDate = importDateTextField.getText().trim();
            int quantity = parseIntField(quantityTextField);
            String dimensions = dimensionsTextField.getText().trim();
            String weight = weightTextField.getText().trim();
            double value = parseDoubleField(sellingPriceTextField);
            double price = parseDoubleField(importPriceTextField);
            String description = descriptionTextArea.getText().trim();

            // Kiểm tra dữ liệu bắt buộc
            if (title.isEmpty() || director.isEmpty() || studio.isEmpty() || warehouseEntryDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ các trường bắt buộc.", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                return;
            }

            DVD dvd = new DVD();
            copyUneditableFields(dvdToEdit, dvd);
            dvd.setTitle(title);
            dvd.setPrice(price);
            dvd.setValue(value);
            dvd.setDescription(description);
            dvd.setQuantity(quantity);
            dvd.setWeight(weight);
            dvd.setDimensions(dimensions);
            dvd.setWarehouseEntryDate(warehouseEntryDate);
            dvd.setDiscType(discType);
            dvd.setDirector(director);
            dvd.setRuntime(runtime);
            dvd.setStudio(studio);
            dvd.setLanguage(language);
            dvd.setSubtitles(subtitles);
            dvd.setReleaseDate(releaseDate.isEmpty() ? null : releaseDate);
            dvd.setGenre(genre);

            // Gọi Controller để lưu vào database
            boolean success = new DVDController().updateDVD(dvd);
            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin DVD thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                isSaveClicked = true;
                dispose(); // Đóng dialog
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật thông tin DVD vào cơ sở dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số cho thời lượng, số lượng, giá và trọng lượng.", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}