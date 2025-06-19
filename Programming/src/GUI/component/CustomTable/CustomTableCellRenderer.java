package GUI.component.CustomTable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CustomTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        // Giữ nguyên màu nền ban đầu của bảng
        cell.setBackground(table.getBackground());
        cell.setForeground(table.getForeground());

        return cell;
    }
}
