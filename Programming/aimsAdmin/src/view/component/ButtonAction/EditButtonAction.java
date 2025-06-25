package view.component.ButtonAction;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import view.component.ButtonUI.RoundedButton;
import Interface.*;

public class EditButtonAction extends AbstractCellEditor implements TableCellEditor {
    private JPanel panel;
    private int row; // Lưu trữ hàng hiện tại mà editor đang hoạt động
    private JTable table; // Thêm tham chiếu đến bảng
    private ProductActionListener listener; // Listener để thông báo sự kiện

    public EditButtonAction(JTable table, ProductActionListener listener) {
        this.table = table;
        this.listener = listener;

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));

        RoundedButton btnEdit = new RoundedButton("Sửa", 10, new Color(0, 155, 229), new Color(0, 104, 190), new Color(0, 104, 190));
        RoundedButton btnDelete = new RoundedButton("Xóa", 10, new Color(255, 77, 77), new Color(204, 0, 0), new Color(204, 0, 0));

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped(); //kết thúc chỉnh sửa
                int selectedRowInModel = table.convertRowIndexToModel(row);
                int productId = (int) table.getModel().getValueAt(selectedRowInModel, 0);
                if (listener != null) {
                    listener.onEdit(productId, selectedRowInModel); // Thông báo cho Controller
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped(); //kết thúc chỉnh sửa
                int selectedRowInModel = table.convertRowIndexToModel(row);
                int productId = (int) table.getModel().getValueAt(selectedRowInModel, 0);
                String category = (String) table.getModel().getValueAt(selectedRowInModel, 2);
                if (listener != null) {
                    listener.onDelete(productId, selectedRowInModel); // Thông báo cho Controller
                }
            }
        });

        panel.add(btnEdit);
        panel.add(btnDelete);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.row = row; // Lưu trữ hàng hiện tại
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null; // Không trả về giá trị nào cho ô
    }
}