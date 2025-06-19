package GUI.dialog.editDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import GUI.component.ButtonUI.*;
import Model.*;
import Controller.BookController;
import javax.swing.border.TitledBorder;

public class EditBookDialog extends JDialog {

    private final JTextField bookTitleTextField;
    private final JTextField authorTextField;
    private final JTextField publisherTextField;
    private final JTextField pageCountTextField;
    private final JComboBox<String> languageComboBox;
    private final JComboBox<String> genreComboBox;
    private final JFormattedTextField publishDateTextField; // Sử dụng JFormattedTextField cho định dạng ngày
    private final JRadioButton paperbackRadioButton;
	private final JFormattedTextField importDateTextField; // Sử dụng JFormattedTextField cho định dạng ngày
    private final JTextField quantityTextField;
    private final JTextField dimensionsTextField;
    private final JTextField weightTextField;
    private final JTextField sellingPriceTextField;
    private final JTextField importPriceTextField;
    private final JTextArea descriptionTextArea;
	private boolean isSaveClicked = false;
    private final Book bookToEdit; // Thêm biến để lưu trữ thông tin sách cần sửa

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
	    Insets labelMargin = new Insets(10, 10, 10, 15);
	    gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Tên sách :"), gbcGeneral);

        gbcGeneral.gridx = 1;
        gbcGeneral.gridy = 0;
		Insets fieldMargin = new Insets(10, 0, 10, 15);
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
	    JRadioButton hardcoverRadioButton = new JRadioButton("hardcover");
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
	    JButton cancelButton = new RoundedButton("Hủy", 10, new Color(255, 77, 77), new Color(204, 0, 0), new Color(204, 0, 0));
	    JButton saveButton = new RoundedButton("Lưu", 10, new Color(0, 155, 229), new Color(0, 104, 190), new Color(0, 104, 190));
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners cho nút Lưu và Hủy
        saveButton.addActionListener(e -> saveEditedBook());
        cancelButton.addActionListener(e -> {
            isSaveClicked = false;
            dispose();
        });

        pack();
        setLocationRelativeTo(parent);
    }
    public boolean isSaveClicked() { return isSaveClicked; }
    private void saveEditedBook(){
        try{
            // Lấy dữ liệu từ form
            String title = bookTitleTextField.getText().trim();
            String authors = authorTextField.getText().trim();
            String publisher = publisherTextField.getText().trim();
            int numPages = Integer.parseInt(pageCountTextField.getText().trim());
            String language = (String) languageComboBox.getSelectedItem();
            String genre = (String) genreComboBox.getSelectedItem();
            String publicationDate = publishDateTextField.getText().trim(); // Định dạng: yyyy-MM-dd
            String coverType = paperbackRadioButton.isSelected() ? "paperback" : "hardcover";
            String warehouseEntryDate = importDateTextField.getText().trim();  // Định dạng: yyyy-MM-dd
            int quantity = Integer.parseInt(quantityTextField.getText().trim());
            String dimensions = dimensionsTextField.getText().trim();
            String weight = weightTextField.getText().trim();
            double value  = Double.parseDouble(sellingPriceTextField.getText().trim());
            double price  = Double.parseDouble(importPriceTextField.getText().trim());
            String description = descriptionTextArea.getText().trim();

             // Kiểm tra dữ liệu bắt buộc
             if (title.isEmpty() || authors.isEmpty() || publicationDate.isEmpty() || warehouseEntryDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ các trường bắt buộc.", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Book book = new Book();
            book.setProductId(bookToEdit.getProductId());
            book.setTitle(title);
            book.setCategory(bookToEdit.getCategory());
            book.setPrice(price);
            book.setValue(value);
            book.setBarcode(bookToEdit.getBarcode());
            book.setDescription(description);
            book.setQuantity(quantity);
            book.setWeight(weight);
            book.setDimensions(dimensions);
            book.setWarehouseEntryDate(warehouseEntryDate);
            book.setAuthors(authors);
            book.setCoverType(coverType);
            book.setPublisher(publisher);
            book.setPublicationDate(publicationDate);
            book.setNumPages(numPages);
            book.setLanguage(language);
            book.setGenre(genre);

            // Gọi Controller để lưu vào database
            BookController bookController = new BookController();
            boolean success = bookController.updateBook(book);
            if (success) {
                JOptionPane.showMessageDialog(this, "Sửa thông tin sách thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                isSaveClicked = true;
                dispose(); // Đóng dialog
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi lưu sách vào cơ sở dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số cho số trang, số lượng, giá và trọng lượng.", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

}

