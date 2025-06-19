package GUI.dialog.editDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import GUI.component.ButtonUI.*;
import Model.*;
import Controller.CDController;
import javax.swing.border.TitledBorder;

public class EditCDDialog extends JDialog {
    private final JTextField titleTextField;
    private final JTextField artistTextField;
    private final JTextField recordLabelTextField;
    private final JTextField genreTextField;
    private final JFormattedTextField releaseDateTextField;
    private final JTextArea trackListTextArea;
    private final JFormattedTextField importDateTextField;
    private final JTextField quantityTextField;
    private final JTextField dimensionsTextField;
    private final JTextField weightTextField;
    private final JTextField sellingPriceTextField;
    private final JTextField importPriceTextField;
    private final JTextArea descriptionTextArea;
	private boolean isSaveClicked = false;
    private final CD cdToEdit;

	public EditCDDialog(JFrame parent, String title, boolean modal, CD cd) {
        super(parent, title, modal);
        this.cdToEdit = cd;
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
	    Insets labelMargin = new Insets(10, 10, 10, 15);
	    gbcCD.insets = labelMargin;
        cdInfoPanel.add(new JLabel("Tiêu đề :"), gbcCD);

        titleTextField = new JTextField(20);
        titleTextField.setText(cdToEdit.getTitle());
        gbcCD.gridx = 1;
        gbcCD.gridy = 0;
		Insets fieldMargin = new Insets(10, 0, 10, 15);
		gbcCD.insets = fieldMargin;
        cdInfoPanel.add(titleTextField, gbcCD);

        gbcCD.gridx = 2;
        gbcCD.gridy = 0;
        gbcCD.insets = labelMargin;
        cdInfoPanel.add(new JLabel("Hãng thu :"), gbcCD);

        recordLabelTextField = new JTextField(20);
        recordLabelTextField.setText(cdToEdit.getRecordLabel());
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
        artistTextField.setText(cdToEdit.getArtists());
        gbcCD.gridx = 1;
        gbcCD.gridy = 1;
        gbcCD.insets = fieldMargin;
        cdInfoPanel.add(artistTextField, gbcCD);

        gbcCD.gridx = 2;
        gbcCD.gridy = 1;
        gbcCD.insets = labelMargin;
        cdInfoPanel.add(new JLabel("Thể loại :"), gbcCD);

        genreTextField = new JTextField(20);
        genreTextField.setText(cdToEdit.getGenre());
        gbcCD.gridx = 3;
        gbcCD.gridy = 1;
        gbcCD.insets = fieldMargin;
        cdInfoPanel.add(genreTextField, gbcCD);

        // Hàng 3: Ngày phát hành - Danh sách bài hát
        gbcCD.gridx = 0;
        gbcCD.gridy = 2;
        gbcCD.insets = labelMargin;
        gbcCD.anchor = GridBagConstraints.NORTHWEST;
        cdInfoPanel.add(new JLabel("Ngày phát hành :"), gbcCD);

        releaseDateTextField = new JFormattedTextField();
        releaseDateTextField.setText(cdToEdit.getReleaseDate());
        releaseDateTextField.setColumns(20);
        gbcCD.gridx = 1;
        gbcCD.gridy = 2;
        gbcCD.insets = fieldMargin;
        gbcCD.anchor = GridBagConstraints.NORTHWEST;
        cdInfoPanel.add(releaseDateTextField, gbcCD);
        gbcCD.anchor = GridBagConstraints.WEST;

        gbcCD.gridx = 2;
        gbcCD.gridy = 2;
        gbcCD.insets = labelMargin;
        gbcCD.anchor = GridBagConstraints.NORTHWEST;
        cdInfoPanel.add(new JLabel("Danh sách bài hát :"), gbcCD);

        trackListTextArea = new JTextArea(3, 20);
        trackListTextArea.setText(cdToEdit.getTracklist());
        gbcCD.gridx = 3;
        gbcCD.gridy = 2;
        gbcCD.gridwidth = GridBagConstraints.REMAINDER;
        gbcCD.gridheight = 2;
        gbcCD.insets = fieldMargin;
        gbcCD.fill = GridBagConstraints.BOTH;
        cdInfoPanel.add(new JScrollPane(trackListTextArea), gbcCD);
        gbcCD.gridwidth = 1;
        gbcCD.gridheight = 1;
        gbcCD.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.0;
        contentPanel.add(cdInfoPanel, gbc);
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

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
        importDateTextField.setText(cdToEdit.getImportDate());
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
        quantityTextField.setText(String.valueOf(cdToEdit.getQuantity()));
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
        dimensionsTextField.setText(cdToEdit.getDimensions());
        gbcImport.gridx = 1;
        gbcImport.gridy = 1;
        gbcImport.insets = fieldMargin;
        importInfoPanel.add(dimensionsTextField, gbcImport);

        gbcImport.gridx = 2;
        gbcImport.gridy = 1;
        gbcImport.insets = labelMargin;
        importInfoPanel.add(new JLabel("Trọng lượng :"), gbcImport);

        weightTextField = new JTextField(10);
        weightTextField.setText(cdToEdit.getWeight());
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
        sellingPriceTextField.setText(cdToEdit.getSellingPrice());
        gbcImport.gridx = 1;
        gbcImport.gridy = 2;
        gbcImport.insets = fieldMargin;
        importInfoPanel.add(sellingPriceTextField, gbcImport);

        gbcImport.gridx = 2;
        gbcImport.gridy = 2;
        gbcImport.insets = labelMargin;
        importInfoPanel.add(new JLabel("Giá nhập :"), gbcImport);

        importPriceTextField = new JTextField(10);
        importPriceTextField.setText(cdToEdit.getImportPrice());
        gbcImport.gridx = 3;
        gbcImport.gridy = 2;
        gbcImport.insets = fieldMargin;
        importInfoPanel.add(importPriceTextField, gbcImport);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(importInfoPanel, gbc);
        gbc.gridwidth = 1;

        // --- Mô tả ---
        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.setBorder(new TitledBorder("Mô tả"));
        descriptionTextArea = new JTextArea(5, 40);
        descriptionTextArea.setText(cdToEdit.getDescription());
        descriptionPanel.add(new JScrollPane(descriptionTextArea), BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        contentPanel.add(descriptionPanel, gbc);
        gbc.weighty = 0.0;

        // Buttons Panel ở phía dưới
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
	    JButton cancelButton = new RoundedButton("Hủy", 10, new Color(255, 77, 77), new Color(204, 0, 0), new Color(204, 0, 0));
	    JButton saveButton = new RoundedButton("Lưu", 10, new Color(0, 155, 229), new Color(0, 104, 190), new Color(0, 104, 190));
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> saveCD());
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

    private void saveCD() {
        try {
            String title = titleTextField.getText().trim();
            String warehouseEntryDate = importDateTextField.getText().trim();
            String dimensions = dimensionsTextField.getText().trim();
            String weight = weightTextField.getText().trim();
            String description = descriptionTextArea.getText().trim();
            double value  = parseDoubleField(sellingPriceTextField);
            double price  = parseDoubleField(importPriceTextField);
            int quantity = parseIntField(quantityTextField);
            String artists = artistTextField.getText().trim();
            String recordLabel = recordLabelTextField.getText().trim();
            String tracklist = trackListTextArea.getText().trim();
            String genre = genreTextField.getText();
            String releaseDate = releaseDateTextField.getText().trim();

            if (title.isEmpty() || artists.isEmpty() || warehouseEntryDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ các trường bắt buộc.", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                return;
            }

            cdToEdit.setTitle(title);
            cdToEdit.setImportDate(warehouseEntryDate);
            cdToEdit.setDimensions(dimensions);
            cdToEdit.setWeight(weight);
            cdToEdit.setDescription(description);
            cdToEdit.setSellingPrice(String.valueOf(value));
            cdToEdit.setImportPrice(String.valueOf(price));
            cdToEdit.setQuantity(quantity);
            cdToEdit.setArtists(artists);
            cdToEdit.setRecordLabel(recordLabel);
            cdToEdit.setTracklist(tracklist);
            cdToEdit.setGenre(genre);
            cdToEdit.setReleaseDate(releaseDate);

            boolean success = new CDController().updateCD(cdToEdit);
            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật CD thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                isSaveClicked = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật CD vào cơ sở dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số cho số lượng, giá và trọng lượng.", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
