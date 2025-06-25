package view.dialog.editDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import view.component.ButtonUI.*;
import model.entity.*;

import javax.swing.border.TitledBorder;

public class EditBookDialog extends JDialog {

    private JTextField bookTitleTextField;
    private JTextField authorTextField;
    private JTextField publisherTextField;
    private JTextField pageCountTextField;
    private JComboBox<String> languageComboBox;
    private JComboBox<String> genreComboBox;
    private JFormattedTextField publishDateTextField; // Sử dụng JFormattedTextField cho định dạng ngày
    private JRadioButton paperbackRadioButton;
    private JRadioButton hardcoverRadioButton;
    private JFormattedTextField importDateTextField; // Sử dụng JFormattedTextField cho định dạng ngày
    private JTextField quantityTextField;
    private JTextField dimensionsTextField;
    private JTextField weightTextField;
    private JTextField sellingPriceTextField;
    private JTextField importPriceTextField;
    private JTextArea descriptionTextArea;
    private JButton cancelButton;
    private JButton saveButton;
    private Book bookToEdit; // Thêm biến để lưu trữ thông tin sách cần sửa
    private final Insets labelMargin = new Insets(10, 10, 10, 15);
    private final Insets fieldMargin = new Insets(10, 0, 10, 15);

    public EditBookDialog(JFrame parent, String title, boolean modal, Book book) {
        super(parent, title, modal);
        this.bookToEdit = book; // Lưu trữ đối tượng Book được truyền vào
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

        // Hàng 1: Tên sách - Số trang
        gbcGeneral.gridx = 0;
        gbcGeneral.gridy = 0;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Tên sách :"), gbcGeneral);

        gbcGeneral.gridx = 1;
        gbcGeneral.gridy = 0;
        gbcGeneral.insets = fieldMargin;
        generalInfoPanel.add(bookTitleTextField = new JTextField(20), gbcGeneral);
        bookTitleTextField.setText(bookToEdit.getTitle()); // Điền thông tin

        gbcGeneral.gridx = 2;
        gbcGeneral.gridy = 0;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Số trang :"), gbcGeneral);

        gbcGeneral.gridx = 3;
        gbcGeneral.gridy = 0;
        gbcGeneral.insets = fieldMargin;
        pageCountTextField = new JTextField(10);
        generalInfoPanel.add(pageCountTextField, gbcGeneral);
        pageCountTextField.setText(String.valueOf(bookToEdit.getNumPages())); // Điền thông tin

        // Hàng 2: Tác giả - Ngôn ngữ
        gbcGeneral.gridx = 0;
        gbcGeneral.gridy = 1;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Tác giả :"), gbcGeneral);

        gbcGeneral.gridx = 1;
        gbcGeneral.gridy = 1;
        gbcGeneral.insets = fieldMargin;
        generalInfoPanel.add(authorTextField = new JTextField(20), gbcGeneral);
        authorTextField.setText(bookToEdit.getAuthors()); // Điền thông tin

        gbcGeneral.gridx = 2;
        gbcGeneral.gridy = 1;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Ngôn ngữ :"), gbcGeneral);

        gbcGeneral.gridx = 3;
        gbcGeneral.gridy = 1;
        gbcGeneral.insets = fieldMargin;
        languageComboBox = new JComboBox<>(new String[]{"Tiếng Việt", "Tiếng Anh", "Khác"});
        generalInfoPanel.add(languageComboBox, gbcGeneral);
        languageComboBox.setSelectedItem(bookToEdit.getLanguage()); // Điền thông tin

        // Hàng 3: Nhà xuất bản - Thể loại
        gbcGeneral.gridx = 0;
        gbcGeneral.gridy = 2;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Nhà xuất bản :"), gbcGeneral);

        gbcGeneral.gridx = 1;
        gbcGeneral.gridy = 2;
        gbcGeneral.insets = fieldMargin;
        generalInfoPanel.add(publisherTextField = new JTextField(20), gbcGeneral);
        publisherTextField.setText(bookToEdit.getPublisher()); // Điền thông tin

        gbcGeneral.gridx = 2;
        gbcGeneral.gridy = 2;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Thể loại :"), gbcGeneral);

        gbcGeneral.gridx = 3;
        gbcGeneral.gridy = 2;
        gbcGeneral.insets = fieldMargin;
        genreComboBox = new JComboBox<>(new String[]{"Tiểu thuyết", "Truyện ngắn", "Trinh thám", "Kinh dị", "Khác"});
        generalInfoPanel.add(genreComboBox, gbcGeneral);
        genreComboBox.setSelectedItem(bookToEdit.getGenre()); // Điền thông tin

        // Hàng 4: Ngày xuất bản
        gbcGeneral.gridx = 0;
        gbcGeneral.gridy = 3;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Ngày xuất bản :"), gbcGeneral);

        gbcGeneral.gridx = 1;
        gbcGeneral.gridy = 3;
        gbcGeneral.insets = fieldMargin;
        publishDateTextField = new JFormattedTextField();
        publishDateTextField.setColumns(20);
        publishDateTextField.setText(bookToEdit.getPublicationDate()); // Điền thông tin
        generalInfoPanel.add(publishDateTextField, gbcGeneral);
        gbcGeneral.gridwidth = 3; // Để trống cột bên phải
        gbcGeneral.gridx = 2;
        generalInfoPanel.add(new JLabel(""), gbcGeneral); // Spacer
        gbcGeneral.gridwidth = 1; // Reset

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(generalInfoPanel, gbc);
        gbc.gridwidth = 1; // Reset gridwidth

        // --- Thông tin bìa ---
        JPanel coverInfoPanel = new JPanel(new GridBagLayout());
        coverInfoPanel.setBorder(new TitledBorder("Thông tin bìa"));
        GridBagConstraints gbcCover = new GridBagConstraints();
        gbcCover.fill = GridBagConstraints.HORIZONTAL;
        gbcCover.weightx = 0.5;
        gbcCover.insets = new Insets(5, 5, 5, 5);

        // Hàng 1: Loại bìa
        gbcCover.gridx = 0;
        gbcCover.gridy = 0;
        gbcCover.insets = labelMargin;
        coverInfoPanel.add(new JLabel("Loại bìa :"), gbcCover);

        gbcCover.gridx = 1;
        gbcCover.gridy = 0;
        gbcCover.gridwidth = 3;
        gbcCover.insets = fieldMargin;
        JPanel coverTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        paperbackRadioButton = new JRadioButton("paperback");
        hardcoverRadioButton = new JRadioButton("hardcover");
        ButtonGroup coverTypeGroup = new ButtonGroup();
        coverTypeGroup.add(paperbackRadioButton);
        coverTypeGroup.add(hardcoverRadioButton);

        if (bookToEdit.getCoverType().equalsIgnoreCase("paperback")) {
            paperbackRadioButton.setSelected(true);
        } else if (bookToEdit.getCoverType().equalsIgnoreCase("hardcover")) {
            hardcoverRadioButton.setSelected(true);
        }

        coverTypePanel.add(paperbackRadioButton);
        coverTypePanel.add(Box.createHorizontalStrut(30)); // Thêm khoảng trống 20 pixel
        coverTypePanel.add(hardcoverRadioButton);
        coverInfoPanel.add(coverTypePanel, gbcCover);
        gbcCover.gridwidth = 1; // Reset gridwidth

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(coverInfoPanel, gbc);
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
        importDateTextField.setText(bookToEdit.getWarehouseEntryDate()); // Điền thông tin
        importInfoPanel.add(importDateTextField, gbcImport);

        gbcImport.gridx = 2;
        gbcImport.gridy = 0;
        gbcImport.insets = labelMargin;
        importInfoPanel.add(new JLabel("Số lượng :"), gbcImport);

        gbcImport.gridx = 3;
        gbcImport.gridy = 0;
        gbcImport.insets = fieldMargin;
        quantityTextField = new JTextField(10);
        quantityTextField.setText(String.valueOf(bookToEdit.getQuantity())); // Điền thông tin
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
        dimensionsTextField.setText(bookToEdit.getDimensions()); // Điền thông tin
        importInfoPanel.add(dimensionsTextField, gbcImport);

        gbcImport.gridx = 2;
        gbcImport.gridy = 1;
        gbcImport.insets = labelMargin;
        importInfoPanel.add(new JLabel("Trọng lượng :"), gbcImport);

        gbcImport.gridx = 3;
        gbcImport.gridy = 1;
        gbcImport.insets = fieldMargin;
        weightTextField = new JTextField(10);
        weightTextField.setText(bookToEdit.getWeight()); // Điền thông tin
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
        sellingPriceTextField.setText(String.valueOf(bookToEdit.getValue())); // Điền thông tin
        importInfoPanel.add(sellingPriceTextField, gbcImport);

        gbcImport.gridx = 2;
        gbcImport.gridy = 2;
        gbcImport.insets = labelMargin;
        importInfoPanel.add(new JLabel("Giá nhập :"), gbcImport);

        gbcImport.gridx = 3;
        gbcImport.gridy = 2;
        gbcImport.insets = fieldMargin;
        importPriceTextField = new JTextField(10);
        importPriceTextField.setText(String.valueOf(bookToEdit.getPrice())); // Điền thông tin
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
        descriptionTextArea.setText(bookToEdit.getDescription()); // Điền thông tin
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
        if (bookToEdit != null) {
            populateFields(bookToEdit);
        }

        // Action Listeners cho nút Hủy
        cancelButton.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(parent);
    }

    // Phương thức để điền dữ liệu từ đối tượng Book vào các trường
    private void populateFields(Book book) {
        bookTitleTextField.setText(book.getTitle());
        authorTextField.setText(book.getAuthors());
        publisherTextField.setText(book.getPublisher());
        pageCountTextField.setText(String.valueOf(book.getNumPages()));
        languageComboBox.setSelectedItem(book.getLanguage());
        genreComboBox.setSelectedItem(book.getGenre());
        publishDateTextField.setText(book.getPublicationDate());
        if (book.getCoverType() != null) { // Check for null before using
             if (book.getCoverType().equalsIgnoreCase("paperback")) {
                paperbackRadioButton.setSelected(true);
            } else if (book.getCoverType().equalsIgnoreCase("hardcover")) {
                hardcoverRadioButton.setSelected(true);
            }
        }
       
        importDateTextField.setText(book.getWarehouseEntryDate());
        quantityTextField.setText(String.valueOf(book.getQuantity()));
        dimensionsTextField.setText(book.getDimensions());
        weightTextField.setText(book.getWeight());
        sellingPriceTextField.setText(String.valueOf(book.getValue()));
        importPriceTextField.setText(String.valueOf(book.getPrice()));
        descriptionTextArea.setText(book.getDescription());
    }

    // Các getter để Controller lấy dữ liệu từ form
    public String getBookTitle() { return bookTitleTextField.getText().trim(); }
    public String getAuthor() { return authorTextField.getText().trim(); }
    public String getPublisher() { return publisherTextField.getText().trim(); }
    public String getPageCount() { return pageCountTextField.getText().trim(); }
    public String getLanguage() { return (String) languageComboBox.getSelectedItem(); }
    public String getGenre() { return (String) genreComboBox.getSelectedItem(); }
    public String getPublishDate() { return publishDateTextField.getText().trim(); }
    public String getCoverType() { return paperbackRadioButton.isSelected() ? "paperback" : "hardcover"; }
    public String getImportDate() { return importDateTextField.getText().trim(); }
    public String getQuantity() { return quantityTextField.getText().trim(); }
    public String getDimensions() { return dimensionsTextField.getText().trim(); }
    public String getWeight() { return weightTextField.getText().trim(); }
    public String getSellingPrice() { return sellingPriceTextField.getText().trim(); }
    public String getImportPrice() { return importPriceTextField.getText().trim(); }
    public String getDescription() { return descriptionTextArea.getText().trim(); }


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