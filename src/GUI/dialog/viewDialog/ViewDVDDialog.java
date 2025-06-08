package GUI.dialog.viewDialog;

import Model.DVD;
import javax.swing.*;
import java.awt.*;

public class ViewDVDDialog extends JDialog {
    private DVD dvd;

    public ViewDVDDialog(Frame owner, DVD dvd) {
        super(owner, "DVD Details", true);
        this.dvd = dvd;
        initUI();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        panel.add(new JLabel("ID:"));
        panel.add(new JLabel(String.valueOf(dvd.getProductId())));

        panel.add(new JLabel("Title:"));
        panel.add(new JLabel(dvd.getTitle()));

        panel.add(new JLabel("Director:"));
        panel.add(new JLabel(dvd.getDirector()));

        panel.add(new JLabel("Disc Type:"));
        panel.add(new JLabel(dvd.getDiscType()));

        panel.add(new JLabel("Runtime (min):"));
        panel.add(new JLabel(String.valueOf(dvd.getRuntime())));

        panel.add(new JLabel("Studio:"));
        panel.add(new JLabel(dvd.getStudio()));

        panel.add(new JLabel("Language:"));
        panel.add(new JLabel(dvd.getLanguage()));

        panel.add(new JLabel("Subtitles:"));
        panel.add(new JLabel(dvd.getSubtitles()));

        panel.add(new JLabel("Genre:"));
        panel.add(new JLabel(dvd.getGenre()));

        panel.add(new JLabel("Release Date:"));
        panel.add(new JLabel(dvd.getReleaseDate()));

        panel.add(new JLabel("Price:"));
        panel.add(new JLabel(String.format("%.2f", dvd.getPrice())));

        panel.add(new JLabel("Quantity:"));
        panel.add(new JLabel(String.valueOf(dvd.getQuantity())));

        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(btnClose, BorderLayout.SOUTH);
    }
}
