package GUI.dialog.addDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import GUI.component.ButtonUI.*;
import Model.*;
import Controller.BookController;
import javax.swing.border.TitledBorder;

public class AddBookDialog extends JDialog {

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
    private static final Insets labelMargin = new Insets(10, 10, 10, 15);
    private static final Insets fieldMargin = new Insets(10, 0, 10, 15);

    public AddBookDialog(JFrame parent, String title, boolean modal) {
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

        // Hàng 1: Tên sách - Số trang
        gbcGeneral.gridx = 0;
        gbcGeneral.gridy = 0;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Tên sách :"), gbcGeneral);

        gbcGeneral.gridx = 1;
        gbcGeneral.gridy = 0;
        gbcGeneral.insets = fieldMargin;
        bookTitleTextField = new JTextField(20);
        bookTitleTextField.setText("Sách mới");
        generalInfoPanel.add(bookTitleTextField, gbcGeneral);

        gbcGeneral.gridx = 2;
        gbcGeneral.gridy = 0;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Số trang :"), gbcGeneral);

        gbcGeneral.gridx = 3;
        gbcGeneral.gridy = 0;
        gbcGeneral.insets = fieldMargin;
        pageCountTextField = new JTextField(10);
        pageCountTextField.setText("100");
        generalInfoPanel.add(pageCountTextField, gbcGeneral);

        // Hàng 2: Tác giả - Ngôn ngữ
        gbcGeneral.gridx = 0;
        gbcGeneral.gridy = 1;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Tác giả :"), gbcGeneral);

        gbcGeneral.gridx = 1;
        gbcGeneral.gridy = 1;
        gbcGeneral.insets = fieldMargin;
        authorTextField = new JTextField(20);
        authorTextField.setText("Tác giả mới");
        generalInfoPanel.add(authorTextField, gbcGeneral);

        gbcGeneral.gridx = 2;
        gbcGeneral.gridy = 1;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Ngôn ngữ :"), gbcGeneral);

        gbcGeneral.gridx = 3;
        gbcGeneral.gridy = 1;
        gbcGeneral.insets = fieldMargin;
        languageComboBox = new JComboBox<>(new String[]{"Tiếng Việt", "Tiếng Anh", "Khác"});
        languageComboBox.setSelectedIndex(0);
        generalInfoPanel.add(languageComboBox, gbcGeneral);

        // Hàng 3: Nhà xuất bản - Thể loại
        gbcGeneral.gridx = 0;
        gbcGeneral.gridy = 2;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Nhà xuất bản :"), gbcGeneral);

        gbcGeneral.gridx = 1;
        gbcGeneral.gridy = 2;
        gbcGeneral.insets = fieldMargin;
        publisherTextField = new JTextField(20);
        publisherTextField.setText("NXB mới");
        generalInfoPanel.add(publisherTextField, gbcGeneral);

        gbcGeneral.gridx = 2;
        gbcGeneral.gridy = 2;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Thể loại :"), gbcGeneral);

        gbcGeneral.gridx = 3;
        gbcGeneral.gridy = 2;
        gbcGeneral.insets = fieldMargin;
        genreComboBox = new JComboBox<>(new String[]{"Văn học", "Khoa học", "Thiếu nhi", "Khác"});
        genreComboBox.setSelectedIndex(0);
        generalInfoPanel.add(genreComboBox, gbcGeneral);

        // Hàng 4: Ngày xuất bản
        gbcGeneral.gridx = 0;
        gbcGeneral.gridy = 3;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Ngày xuất bản :"), gbcGeneral);

        gbcGeneral.gridx = 1;
        gbcGeneral.gridy = 3;
        gbcGeneral.insets = fieldMargin;
        publishDateTextField = new JFormattedTextField();
        publishDateTextField.setText("2025-01-01");
        publishDateTextField.setColumns(20);
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
        dimensionsTextField.setText("20x15x2 cm");
        importInfoPanel.add(dimensionsTextField, gbcImport);

        gbcImport.gridx = 2;
        gbcImport.gridy = 1;
        gbcImport.insets = labelMargin;
        importInfoPanel.add(new JLabel("Trọng lượng :"), gbcImport);

        gbcImport.gridx = 3;
        gbcImport.gridy = 1;
        gbcImport.insets = fieldMargin;
        weightTextField = new JTextField(10);
        weightTextField.setText("0.3");
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
        gbc.weighty = 1.0; // Cho phép mô tả chiếm nhiều không gian dọc
        contentPanel.add(descriptionPanel, gbc);
        gbc.weighty = 0.0; // Reset weighty

        // Buttons Panel ở phía dưới
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        final JButton cancelButton = new RoundedButton("Hủy", 10, new Color(255, 77, 77), new Color(204, 0, 0), new Color(204, 0, 0));
        final JButton saveButton = new RoundedButton("Lưu", 10, new Color(0, 155, 229), new Color(0, 104, 190), new Color(0, 104, 190));
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH); 

        // Action Listeners cho nút Lưu và Hủy
        saveButton.addActionListener(e -> saveBook());

        cancelButton.addActionListener(e -> {
            isSaveClicked = false;
            dispose();
        });

        pack();
        setLocationRelativeTo(parent);
    }

    public boolean isSaveClicked() { return isSaveClicked; }
    private void saveBook() {
        System.out.println("[AddBookDialog] saveBook called");
        try {
            // Lấy dữ liệu từ form (sử dụng phương thức tiện ích để tránh lặp code)
            BookFormData data = getBookFormData();
            String title = data.title;
            String authors = data.authors;
            String publisher = data.publisher;
            int numPages = data.numPages;
            String language = data.language;
            String genre = data.genre;
            String publicationDate = data.publicationDate;
            String coverType = data.coverType;
            String warehouseEntryDate = data.warehouseEntryDate;
            int quantity = data.quantity;
            String dimensions = data.dimensions;
            String weight = data.weight;
            double value = data.value;
            double price = data.price;
            String description = data.description;

            System.out.println("[AddBookDialog] Data collected: " + title + ", " + authors);

            // Kiểm tra dữ liệu bắt buộc
            if (title.isEmpty() || authors.isEmpty() || publicationDate.isEmpty() || warehouseEntryDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ các trường bắt buộc.", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                System.out.println("[AddBookDialog] Missing required fields");
                return;
            }

            // Kiểm tra định dạng ngày tháng yyyy-MM-dd
            if (!publicationDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(this, "Ngày xuất bản phải có định dạng yyyy-MM-dd (VD: 2024-06-03)", "Sai định dạng ngày", JOptionPane.ERROR_MESSAGE);
                System.out.println("[AddBookDialog] Invalid publicationDate format: " + publicationDate);
                return;
            }
            if (!warehouseEntryDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(this, "Ngày nhập kho phải có định dạng yyyy-MM-dd (VD: 2024-06-03)", "Sai định dạng ngày", JOptionPane.ERROR_MESSAGE);
                System.out.println("[AddBookDialog] Invalid warehouseEntryDate format: " + warehouseEntryDate);
                return;
            }

            // Tạo đối tượng Book
            Book book = new Book(0, title, "book", value, price, "0", description,
            quantity, weight, dimensions, warehouseEntryDate,authors,
            coverType, publisher, publicationDate, numPages,
            language, genre);

            System.out.println("[AddBookDialog] Book object created: " + book);

            // Gọi Controller để lưu vào database
            BookController bookController = new BookController();
            boolean success = bookController.addBook(book);
            System.out.println("[AddBookDialog] BookController.addBook result: " + success);
            if (success) {
                JOptionPane.showMessageDialog(this, "Thêm sách thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                isSaveClicked = true;
                dispose(); // Đóng dialog
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi lưu sách vào cơ sở dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số cho số trang, số lượng, giá và trọng lượng.", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
            System.out.println("[AddBookDialog] NumberFormatException: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            System.out.println("[AddBookDialog] Exception: " + e.getMessage());
        }
    }

    // Thêm class tiện ích bên trong AddBookDialog
    private static class BookFormData {
        String title, authors, publisher, language, genre, publicationDate, coverType, warehouseEntryDate, dimensions, weight, description;
        int numPages, quantity;
        double value, price;
    }
    private BookFormData getBookFormData() {
        BookFormData data = new BookFormData();
        data.title = bookTitleTextField.getText().trim();
        data.authors = authorTextField.getText().trim();
        data.publisher = publisherTextField.getText().trim();
        data.numPages = Integer.parseInt(pageCountTextField.getText().trim());
        data.language = (String) languageComboBox.getSelectedItem();
        data.genre = (String) genreComboBox.getSelectedItem();
        data.publicationDate = publishDateTextField.getText().trim();
        data.coverType = paperbackRadioButton.isSelected() ? "paperback" : "hardcover";
        data.warehouseEntryDate = importDateTextField.getText().trim();
        data.quantity = Integer.parseInt(quantityTextField.getText().trim());
        data.dimensions = dimensionsTextField.getText().trim();
        data.weight = weightTextField.getText().trim();
        data.value = Double.parseDouble(sellingPriceTextField.getText().trim());
        data.price = Double.parseDouble(importPriceTextField.getText().trim());
        data.description = descriptionTextArea.getText().trim();
        return data;
    }
}
