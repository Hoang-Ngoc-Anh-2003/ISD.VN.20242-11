package GUI.dialog.viewDialog;

import Model.CD;
import javax.swing.*;
import java.awt.*;

public class ViewCDDialog extends JDialog {
    private CD cd;

    public ViewCDDialog(Frame owner, CD cd) {
        super(owner, "CD Details", true);
        this.cd = cd;
        initUI();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        panel.add(new JLabel("ID:"));
        panel.add(new JLabel(String.valueOf(cd.getProductId())));

        panel.add(new JLabel("Title:"));
        panel.add(new JLabel(cd.getTitle()));

        panel.add(new JLabel("Artists:"));
        panel.add(new JLabel(cd.getArtists()));

        panel.add(new JLabel("Record Label:"));
        panel.add(new JLabel(cd.getRecordLabel()));

        panel.add(new JLabel("Tracklist:"));
        panel.add(new JLabel(cd.getTracklist()));

        panel.add(new JLabel("Genre:"));
        panel.add(new JLabel(cd.getGenre()));

        panel.add(new JLabel("Release Date:"));
        panel.add(new JLabel(cd.getReleaseDate()));

        panel.add(new JLabel("Price:"));
        panel.add(new JLabel(String.format("%.2f", cd.getPrice())));

        panel.add(new JLabel("Quantity:"));
        panel.add(new JLabel(String.valueOf(cd.getQuantity())));

        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(btnClose, BorderLayout.SOUTH);
    }
}
