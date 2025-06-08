package GUI.dialog.addDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import GUI.component.ButtonUI.*;
import Model.DVD;

import javax.swing.border.TitledBorder;

import DAO.DVDDAO;

public class AddDVDDialog extends JDialog {

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
    private boolean isSaveClicked = false;
    private final Insets labelMargin = new Insets(10, 10, 10, 15);
    private final Insets fieldMargin = new Insets(10, 0, 10, 15);

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

        // Hàng 2: Đạo diễn - Hãng sản xuất
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
        generalInfoPanel.add(new JLabel("Hãng sản xuất :"), gbcGeneral);

        gbcGeneral.gridx = 3;
        gbcGeneral.gridy = 1;
        gbcGeneral.insets = fieldMargin;
        productionCompanyTextField = new JTextField(20);
        productionCompanyTextField.setText("Hãng mới");
        generalInfoPanel.add(productionCompanyTextField, gbcGeneral);

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
        genreComboBox = new JComboBox<>(new String[]{"Hành động", "Tình cảm", "Hài", "Khác"});
        genreComboBox.setSelectedIndex(0);
        generalInfoPanel.add(genreComboBox, gbcGeneral);

        // Hàng 4: Ngày phát hành - Phụ đề
        gbcGeneral.gridx = 0;
        gbcGeneral.gridy = 3;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Ngày phát hành :"), gbcGeneral);

        gbcGeneral.gridx = 1;
        gbcGeneral.gridy = 3;
        gbcGeneral.insets = fieldMargin;
        releaseDateTextField = new JFormattedTextField();
        releaseDateTextField.setText("2025-01-01");
        releaseDateTextField.setColumns(20);
        generalInfoPanel.add(releaseDateTextField, gbcGeneral);

        gbcGeneral.gridx = 2;
        gbcGeneral.gridy = 3;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Phụ đề :"), gbcGeneral);

        gbcGeneral.gridx = 3;
        gbcGeneral.gridy = 3;
        gbcGeneral.insets = fieldMargin;
        subtitleComboBox = new JComboBox<>(new String[]{"Không", "Tiếng Việt", "Tiếng Anh", "Khác"});
        subtitleComboBox.setSelectedIndex(0);
        generalInfoPanel.add(subtitleComboBox, gbcGeneral);

        // Hàng 5: Ngày nhập kho
        gbcGeneral.gridx = 0;
        gbcGeneral.gridy = 4;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Ngày nhập kho :"), gbcGeneral);

        gbcGeneral.gridx = 1;
        gbcGeneral.gridy = 4;
        gbcGeneral.insets = fieldMargin;
        importDateTextField = new JFormattedTextField();
        importDateTextField.setText("2025-01-01");
        importDateTextField.setColumns(20);
        generalInfoPanel.add(importDateTextField, gbcGeneral);

        // Hàng 6: Số lượng - Kích thước
        gbcGeneral.gridx = 0;
        gbcGeneral.gridy = 5;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Số lượng :"), gbcGeneral);

        gbcGeneral.gridx = 1;
        gbcGeneral.gridy = 5;
        gbcGeneral.insets = fieldMargin;
        quantityTextField = new JTextField(10);
        quantityTextField.setText("1");
        generalInfoPanel.add(quantityTextField, gbcGeneral);

        gbcGeneral.gridx = 2;
        gbcGeneral.gridy = 5;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Kích thước :"), gbcGeneral);

        gbcGeneral.gridx = 3;
        gbcGeneral.gridy = 5;
        gbcGeneral.insets = fieldMargin;
        dimensionsTextField = new JTextField(20);
        dimensionsTextField.setText("19x13.5x1.5 cm");
        generalInfoPanel.add(dimensionsTextField, gbcGeneral);

        // Hàng 7: Trọng lượng - Giá bán
        gbcGeneral.gridx = 0;
        gbcGeneral.gridy = 6;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Trọng lượng (kg) :"), gbcGeneral);

        gbcGeneral.gridx = 1;
        gbcGeneral.gridy = 6;
        gbcGeneral.insets = fieldMargin;
        weightTextField = new JTextField(10);
        weightTextField.setText("0.2");
        generalInfoPanel.add(weightTextField, gbcGeneral);

        gbcGeneral.gridx = 2;
        gbcGeneral.gridy = 6;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Giá bán :"), gbcGeneral);

        gbcGeneral.gridx = 3;
        gbcGeneral.gridy = 6;
        gbcGeneral.insets = fieldMargin;
        sellingPriceTextField = new JTextField(10);
        sellingPriceTextField.setText("0.0");
        generalInfoPanel.add(sellingPriceTextField, gbcGeneral);

        // Hàng 8: Giá nhập - Mô tả
        gbcGeneral.gridx = 0;
        gbcGeneral.gridy = 7;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Giá nhập :"), gbcGeneral);

        gbcGeneral.gridx = 1;
        gbcGeneral.gridy = 7;
        gbcGeneral.insets = fieldMargin;
        importPriceTextField = new JTextField(10);
        importPriceTextField.setText("0.0");
        generalInfoPanel.add(importPriceTextField, gbcGeneral);

        gbcGeneral.gridx = 2;
        gbcGeneral.gridy = 7;
        gbcGeneral.insets = labelMargin;
        generalInfoPanel.add(new JLabel("Mô tả :"), gbcGeneral);

        gbcGeneral.gridx = 3;
        gbcGeneral.gridy = 7;
        gbcGeneral.insets = fieldMargin;
        descriptionTextArea = new JTextArea(5, 40);
        descriptionTextArea.setText("");
        generalInfoPanel.add(descriptionTextArea, gbcGeneral);

        contentPanel.add(generalInfoPanel, gbc);

        // --- Buttons Panel ở phía dưới ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        cancelButton = new RoundedButton("Hủy", 10, new Color(255, 77, 77), new Color(204, 0, 0), new Color(204, 0, 0));
        saveButton = new RoundedButton("Lưu", 10, new Color(0, 155, 229), new Color(0, 104, 190), new Color(0, 104, 190));
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners cho nút Lưu và Hủy
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDVD();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isSaveClicked = false;
                dispose();
            }
        });

        pack();
        setLocationRelativeTo(parent);
    }

    public boolean isSaveClicked() {
        return isSaveClicked;
    }

    private void saveDVD() {
        try {
            // Lấy dữ liệu từ form
            String title = titleTextField.getText().trim();
            String director = directorTextField.getText().trim();
            String productionCompany = productionCompanyTextField.getText().trim();
            String language = (String) languageComboBox.getSelectedItem();
            String subtitles = (String) subtitleComboBox.getSelectedItem();
            String genre = (String) genreComboBox.getSelectedItem();
            String releaseDate = releaseDateTextField.getText().trim();
            String importDate = importDateTextField.getText().trim();
            int quantity = Integer.parseInt(quantityTextField.getText().trim());
            String dimensions = dimensionsTextField.getText().trim();
            String weight = weightTextField.getText().trim();
            double sellingPrice = Double.parseDouble(sellingPriceTextField.getText().trim());
            double importPrice = Double.parseDouble(importPriceTextField.getText().trim());
            String description = descriptionTextArea.getText().trim();
            String discType = blurayRadioButton != null && blurayRadioButton.isSelected() ? "Blu-ray" : "HD-DVD";
            int runtime = 120;
            try {
                runtime = Integer.parseInt(durationTextField.getText().trim());
            } catch (Exception ex) {}

            // Kiểm tra dữ liệu bắt buộc
            if (title.isEmpty() || director.isEmpty() || releaseDate.isEmpty() || importDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ các trường bắt buộc.", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Tạo đối tượng DVD
            Model.DVD dvd = new Model.DVD(0, title, "DVD", sellingPrice, importPrice, "0", description, quantity, weight, dimensions, importDate, discType, director, runtime, productionCompany, language, subtitles, releaseDate, genre);

            // Gọi DAO để lưu vào database
            boolean success = DAO.DVDDAO.getInstance().addDVD(dvd);
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
}
