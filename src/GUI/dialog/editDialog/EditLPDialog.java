package GUI.dialog.editDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import GUI.component.ButtonUI.*;
import Model.*;
import Controller.LPController;
import javax.swing.border.TitledBorder;

public class EditLPDialog extends JDialog {

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
	private boolean isSaveClicked = false;
    private final LP lpToEdit; // Thêm biến để lưu trữ thông tin LP cần sửa

	public EditLPDialog(JFrame parent, String title, boolean modal, LP lp) {
        super(parent, title, modal);
        this.lpToEdit = lp; // Lưu trữ đối tượng LP được truyền vào
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(contentPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(5, 5, 5, 5);

        // --- Thông tin LP ---
        JPanel LPInfoPanel = new JPanel(new GridBagLayout());
        LPInfoPanel.setBorder(new TitledBorder("Thông tin chung"));
        GridBagConstraints gbcLP = new GridBagConstraints();
        gbcLP.fill = GridBagConstraints.HORIZONTAL;
        gbcLP.weightx = 0.5;
        gbcLP.insets = new Insets(5, 5, 5, 5);

        // Hàng 1: Tiêu đề - Hãng thu
        gbcLP.gridx = 0;
        gbcLP.gridy = 0;
	    Insets labelMargin = new Insets(10, 10, 10, 15);
	    gbcLP.insets = labelMargin;
        LPInfoPanel.add(new JLabel("Tiêu đề :"), gbcLP);

        gbcLP.gridx = 1;
        gbcLP.gridy = 0;
		Insets fieldMargin = new Insets(10, 0, 10, 15);
		gbcLP.insets = fieldMargin;
        LPInfoPanel.add(titleTextField = new JTextField(20), gbcLP);
        titleTextField.setText(lpToEdit.getTitle()); // Điền thông tin

        gbcLP.gridx = 2;
        gbcLP.gridy = 0;
        gbcLP.insets = labelMargin;
        LPInfoPanel.add(new JLabel("Hãng thu :"), gbcLP);

        gbcLP.gridx = 3;
        gbcLP.gridy = 0;
        gbcLP.insets = fieldMargin;
        recordLabelTextField = new JTextField(20);
        LPInfoPanel.add(recordLabelTextField, gbcLP);
        recordLabelTextField.setText(lpToEdit.getRecordLabel()); // Điền thông tin

        // Hàng 2: Nghệ sĩ - Thể loại
        gbcLP.gridx = 0;
        gbcLP.gridy = 1;
        gbcLP.insets = labelMargin;
        LPInfoPanel.add(new JLabel("Nghệ sĩ :"), gbcLP);

        gbcLP.gridx = 1;
        gbcLP.gridy = 1;
        gbcLP.insets = fieldMargin;
        LPInfoPanel.add(artistTextField = new JTextField(20), gbcLP);
        artistTextField.setText(lpToEdit.getArtists()); // Điền thông tin

        gbcLP.gridx = 2;
        gbcLP.gridy = 1;
        gbcLP.insets = labelMargin;
        LPInfoPanel.add(new JLabel("Thể loại :"), gbcLP);

        gbcLP.gridx = 3;
        gbcLP.gridy = 1;
        gbcLP.insets = fieldMargin;
        genreTextField = new JTextField(20);
        LPInfoPanel.add(genreTextField, gbcLP);
        genreTextField.setText(lpToEdit.getGenre()); // Điền thông tin

        // Hàng 3: Ngày phát hành - Danh sách bài hát
        gbcLP.gridx = 0;
        gbcLP.gridy = 2;
        gbcLP.insets = labelMargin;
        gbcLP.anchor = GridBagConstraints.NORTHWEST; // Neo label lên trên
        LPInfoPanel.add(new JLabel("Ngày phát hành :"), gbcLP);

        gbcLP.gridx = 1;
        gbcLP.gridy = 2;
        gbcLP.insets = fieldMargin;
        gbcLP.anchor = GridBagConstraints.NORTHWEST; // Neo textField lên trên
        releaseDateTextField = new JFormattedTextField();
        releaseDateTextField.setColumns(20);
        releaseDateTextField.setText(lpToEdit.getReleaseDate()); // Điền thông tin
        LPInfoPanel.add(releaseDateTextField, gbcLP);
        gbcLP.anchor = GridBagConstraints.WEST; // Trả lại anchor mặc định

        gbcLP.gridx = 2;
        gbcLP.gridy = 2;
        gbcLP.insets = labelMargin;
        gbcLP.anchor = GridBagConstraints.NORTHWEST; // Neo label lên trên
        LPInfoPanel.add(new JLabel("Danh sách bài hát :"), gbcLP);

        gbcLP.gridx = 3;
        gbcLP.gridy = 2;
        gbcLP.gridwidth = GridBagConstraints.REMAINDER; // Chiếm toàn bộ không gian còn lại bên phải
        gbcLP.gridheight = 2; // Cho phép JTextArea có chiều cao 2 hàng (hoặc hơn nếu cần)
        gbcLP.insets = fieldMargin;
        gbcLP.fill = GridBagConstraints.BOTH; // Lấp đầy cả chiều ngang và dọc
        LPInfoPanel.add(new JScrollPane(trackListTextArea = new JTextArea(3, 20)), gbcLP);
        trackListTextArea.setText(lpToEdit.getTracklist()); // Điền thông tin
        gbcLP.gridwidth = 1; // Trả lại gridwidth mặc định
        gbcLP.gridheight = 1; // Trả lại gridheight mặc định
        gbcLP.fill = GridBagConstraints.HORIZONTAL; // Trả lại fill mặc định

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH; // Fill cả chiều ngang và dọc cho panel thông tin LP
        gbc.weighty = 0.0; // Reset weighty
        contentPanel.add(LPInfoPanel, gbc);
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

        gbcImport.gridx = 1;
        gbcImport.gridy = 0;
        gbcImport.insets = fieldMargin;
        importDateTextField = new JFormattedTextField();
        importDateTextField.setColumns(20);
        importDateTextField.setText(lpToEdit.getWarehouseEntryDate()); // Điền thông tin
        importInfoPanel.add(importDateTextField, gbcImport);

        gbcImport.gridx = 2;
        gbcImport.gridy = 0;
        gbcImport.insets = labelMargin;
        importInfoPanel.add(new JLabel("Số lượng :"), gbcImport);

        gbcImport.gridx = 3;
        gbcImport.gridy = 0;
        gbcImport.insets = fieldMargin;
        quantityTextField = new JTextField(10);
        quantityTextField.setText(String.valueOf(lpToEdit.getQuantity())); // Điền thông tin
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
        dimensionsTextField.setText(lpToEdit.getDimensions()); // Điền thông tin
        importInfoPanel.add(dimensionsTextField, gbcImport);

        gbcImport.gridx = 2;
        gbcImport.gridy = 1;
        gbcImport.insets = labelMargin;
        importInfoPanel.add(new JLabel("Trọng lượng :"), gbcImport);

        gbcImport.gridx = 3;
        gbcImport.gridy = 1;
        gbcImport.insets = fieldMargin;
        weightTextField = new JTextField(10);
        weightTextField.setText(lpToEdit.getWeight()); // Điền thông tin
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
        sellingPriceTextField.setText(String.valueOf(lpToEdit.getValue())); // Điền thông tin
        importInfoPanel.add(sellingPriceTextField, gbcImport);

        gbcImport.gridx = 2;
        gbcImport.gridy = 2;
        gbcImport.insets = labelMargin;
        importInfoPanel.add(new JLabel("Giá nhập :"), gbcImport);

        gbcImport.gridx = 3;
        gbcImport.gridy = 2;
        gbcImport.insets = fieldMargin;
        importPriceTextField = new JTextField(10);
        importPriceTextField.setText(String.valueOf(lpToEdit.getPrice())); // Điền thông tin
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
        descriptionTextArea.setText(lpToEdit.getDescription()); // Điền thông tin
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

        // Action Listeners cho nút Lưu và Hủy
        saveButton.addActionListener(e -> saveEditedLP());
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

    private void copyUneditableFields(LP from, LP to) {
        to.setProductId(from.getProductId());
        to.setCategory(from.getCategory());
        to.setBarcode(from.getBarcode());
    }

    private void saveEditedLP() {
        try {
            // Lấy dữ liệu từ form
            String title = titleTextField.getText().trim();
            String artists = artistTextField.getText().trim();
            String recordLabel = recordLabelTextField.getText().trim();
            String genre = genreTextField.getText().trim();
            String releaseDate = releaseDateTextField.getText().trim(); // Định dạng:<ctrl3348>-MM-dd
            String tracklist = trackListTextArea.getText().trim();
            String warehouseEntryDate = importDateTextField.getText().trim(); // Định dạng:<ctrl3348>-MM-dd
            int quantity = parseIntField(quantityTextField);
            String dimensions = dimensionsTextField.getText().trim();
            String weight = weightTextField.getText().trim();
            double value = parseDoubleField(sellingPriceTextField);
            double price = parseDoubleField(importPriceTextField);
            String description = descriptionTextArea.getText().trim();

            // Kiểm tra dữ liệu bắt buộc
            if (title.isEmpty() || artists.isEmpty() || recordLabel.isEmpty() || genre.isEmpty() || tracklist.isEmpty() || warehouseEntryDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ các trường bắt buộc.", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                return;
            }

            LP lp = new LP();
            copyUneditableFields(lpToEdit, lp);
            lp.setTitle(title);
            lp.setPrice(price);
            lp.setValue(value);
            lp.setDescription(description);
            lp.setQuantity(quantity);
            lp.setWeight(weight);
            lp.setDimensions(dimensions);
            lp.setWarehouseEntryDate(warehouseEntryDate);
            lp.setArtists(artists);
            lp.setRecordLabel(recordLabel);
            lp.setTracklist(tracklist);
            lp.setGenre(genre);
            lp.setReleaseDate(releaseDate.isEmpty() ? null : releaseDate);

            // Gọi Controller để lưu vào database
            boolean success = new LPController().updateLP(lp);
            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin LP thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                isSaveClicked = true;
                dispose(); // Đóng dialog
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật thông tin LP vào cơ sở dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số cho số lượng, giá và trọng lượng.", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

}