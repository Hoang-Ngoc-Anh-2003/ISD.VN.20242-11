package GUI.component.ButtonUI;

import javax.swing.*;
import java.awt.*;

// render button in table
public class TableButtonPanel extends JPanel {
    private RoundedButton btnEdit;
    private RoundedButton btnDelete;

    public TableButtonPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 6, 0));
        RoundedButton btnEdit = new RoundedButton("Sửa", 10, new Color(0, 155, 229), new Color(0, 104, 190), new Color(0, 104, 190));
        RoundedButton btnDelete = new RoundedButton("Xóa", 10, new Color(255, 77, 77), new Color(204, 0, 0), new Color(204, 0, 0));
    
        add(btnEdit);
        add(btnDelete);
    }

    public RoundedButton getEditButton() {
        return btnEdit;
    }

    public RoundedButton getDeleteButton() {
        return btnDelete;
    }
}
