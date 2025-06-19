package GUI.component.ButtonUI;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

// render button update/delete
public class EditButtonRenderer extends JPanel implements TableCellRenderer {
    public EditButtonRenderer() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 6, 0));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        removeAll();
    
        RoundedButton btnEdit = new RoundedButton("Sửa", 10, new Color(0, 155, 229), new Color(0, 104, 190), new Color(0, 104, 190));
        RoundedButton btnDelete = new RoundedButton("Xóa", 10, new Color(255, 77, 77), new Color(204, 0, 0), new Color(204, 0, 0));

        add(btnEdit);
        add(btnDelete);
        // match cell background
        setBackground(table.getBackground());
        setOpaque(true);

        return this;
    }
}
