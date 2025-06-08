package GUI.dialog.viewDialog;

import Model.LP;
import javax.swing.*;
import java.awt.*;

public class ViewLPDialog extends JDialog {
    private LP lp;

    public ViewLPDialog(Frame owner, LP lp) {
        super(owner, "LP Details", true);
        this.lp = lp;
        initUI();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        panel.add(new JLabel("ID:"));
        panel.add(new JLabel(String.valueOf(lp.getProductId())));

        panel.add(new JLabel("Title:"));
        panel.add(new JLabel(lp.getTitle()));

        panel.add(new JLabel("Artists:"));
        panel.add(new JLabel(lp.getArtists()));

        panel.add(new JLabel("Record Label:"));
        panel.add(new JLabel(lp.getRecordLabel()));

        panel.add(new JLabel("Tracklist:"));
        panel.add(new JLabel(lp.getTracklist()));

        panel.add(new JLabel("Genre:"));
        panel.add(new JLabel(lp.getGenre()));

        panel.add(new JLabel("Release Date:"));
        panel.add(new JLabel(lp.getReleaseDate()));

        panel.add(new JLabel("Price:"));
        panel.add(new JLabel(String.format("%.2f", lp.getPrice())));

        panel.add(new JLabel("Quantity:"));
        panel.add(new JLabel(String.valueOf(lp.getQuantity())));

        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(btnClose, BorderLayout.SOUTH);
    }
}
