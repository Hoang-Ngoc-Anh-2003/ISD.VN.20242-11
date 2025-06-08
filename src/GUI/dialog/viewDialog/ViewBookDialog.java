package GUI.dialog.viewDialog;

import Model.Book;
import javax.swing.*;
import java.awt.*;

public class ViewBookDialog extends JDialog {
    private Book book;

    public ViewBookDialog(Frame owner, Book book) {
        super(owner, "Book Details", true);
        this.book = book;
        initUI();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 10, 10));

        panel.add(new JLabel("ID:"));
        panel.add(new JLabel(String.valueOf(book.getProductId())));

        panel.add(new JLabel("Title:"));
        panel.add(new JLabel(book.getTitle()));

        panel.add(new JLabel("Authors:"));
        panel.add(new JLabel(book.getAuthors()));

        panel.add(new JLabel("Price:"));
        panel.add(new JLabel(String.format("%.2f", book.getPrice())));

        panel.add(new JLabel("Quantity:"));
        panel.add(new JLabel(String.valueOf(book.getQuantity())));

        panel.add(new JLabel("Category:"));
        panel.add(new JLabel(book.getCategory()));

        // add more fields as needed

        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(btnClose, BorderLayout.SOUTH);
    }
}
