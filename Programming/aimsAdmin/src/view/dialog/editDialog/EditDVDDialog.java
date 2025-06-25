package view.dialog.editDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import view.component.ButtonUI.*;
import model.entity.*;

import javax.swing.border.TitledBorder;

public class EditDVDDialog extends JDialog {

    private JTextField titleTextField;
    private JTextField directorTextField;
    private JTextField durationTextField;
    private JTextField productionCompanyTextField;
    private JComboBox<String> languageComboBox;
    private JComboBox<String> subtitleComboBox;
    private JComboBox<String> genreComboBox;
    private JFormattedTextField releaseDateTextField; // Sử dụng JFormattedTextField cho định dạng ngày
    private JRadioButton blurayRadioButton;
    private JRadioButton hdvddRadioButton;
    private JFormattedTextField importDateTextField; // Sử dụng JFormattedTextField cho định dạng ngày
    private JTextField quantityTextField;
    private JTextField dimensionsTextField;
    private JTextField weightTextField;
    private JTextField sellingPriceTextField;
    private JTextField importPriceTextField;
    private JTextArea descriptionTextArea;
    private JButton cancelButton;
    private JButton saveButton;
    private DVD dvdToEdit; // Thêm biến để lưu trữ thông tin DVD cần sửa
    private final Insets labelMargin = new Insets(10, 10, 10, 15);
    private final Insets fieldMargin = new Insets(10, 0, 10, 15);

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
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Tiêu đề :"), gbcGeneral);

        gbcGeneral.gridx = 1;
        gbcGeneral.gridy = 0;
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
        gbc.fill = GridBagConstraints.HORIZONTAL;
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
        hdvddRadioButton = new JRadioButton("HD-DVD");
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
        cancelButton = new RoundedButton("Hủy", 10, new Color(255, 77, 77), new Color(204, 0, 0), new Color(204, 0, 0));
        saveButton = new RoundedButton("Lưu", 10, new Color(0, 155, 229), new Color(0, 104, 190), new Color(0, 104, 190));
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Điền dữ liệu ban đầu
        if (dvdToEdit != null) {
            populateFields(dvdToEdit);
        }

        // Action Listeners cho nút Hủy
        cancelButton.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(parent);
    }

    public void populateFields(DVD dvd) {
        if (dvd != null) {
            titleTextField.setText(dvd.getTitle());
            directorTextField.setText(dvd.getDirector());
            durationTextField.setText(String.valueOf(dvd.getRuntime()));
            productionCompanyTextField.setText(dvd.getStudio());
            
            // Set selected item for JComboBoxes
            languageComboBox.setSelectedItem(dvd.getLanguage());
            subtitleComboBox.setSelectedItem(dvd.getSubtitles());
            genreComboBox.setSelectedItem(dvd.getGenre());
            
            releaseDateTextField.setText(dvd.getReleaseDate());
            
            // Set selected radio button
            if (dvd.getDiscType().equalsIgnoreCase("Blu-ray")) {
                blurayRadioButton.setSelected(true);
            } else if (dvd.getDiscType().equalsIgnoreCase("HD-DVD")) {
                hdvddRadioButton.setSelected(true);
            } else {
                // Xử lý trường hợp không khớp hoặc giá trị mặc định nếu cần
                blurayRadioButton.setSelected(false);
                hdvddRadioButton.setSelected(false);
            }

            importDateTextField.setText(dvd.getWarehouseEntryDate());
            quantityTextField.setText(String.valueOf(dvd.getQuantity()));
            dimensionsTextField.setText(dvd.getDimensions());
            weightTextField.setText(dvd.getWeight());
            sellingPriceTextField.setText(String.valueOf(dvd.getValue()));
            importPriceTextField.setText(String.valueOf(dvd.getPrice()));
            descriptionTextArea.setText(dvd.getDescription());
        }
    }

    // --- Getter methods for form data ---
    public String getTitle() { return titleTextField.getText().trim();}
    public String getWarehouseEntryDate() { return importDateTextField.getText().trim();}
    public String getDimensions() { return dimensionsTextField.getText().trim();}
    public String getWeight() { return weightTextField.getText().trim();}
    public String getDescription() { return descriptionTextArea.getText().trim();}
    public String getValue() { return sellingPriceTextField.getText().trim();}
    public String getPrice() { return importPriceTextField.getText().trim();}
    public String getQuantity() { return quantityTextField.getText().trim();}
    public String getDiscType() { return blurayRadioButton.isSelected() ? "Blu-ray" : "HD-DVD";}
    public String getDirector() { return directorTextField.getText().trim();}
    public String getRuntime() { return durationTextField.getText().trim();}
    public String getStudio() { return productionCompanyTextField.getText().trim(); }
    public String getLangue() { return (String) languageComboBox.getSelectedItem(); }
    public String getSubtitles() { return (String) subtitleComboBox.getSelectedItem(); }
    public String getreleaseDate() { return releaseDateTextField.getText().trim(); }
    public String getGenre() { return (String) genreComboBox.getSelectedItem(); }


    // Phương thức để Controller thêm ActionListener cho nút Save
    public void addSaveButtonListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }
    
    // Phương thức để hiển thị thông báo lỗi
    public void showErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    // Phương thức để hiển thị thông báo thành công
    public void showSuccessMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}