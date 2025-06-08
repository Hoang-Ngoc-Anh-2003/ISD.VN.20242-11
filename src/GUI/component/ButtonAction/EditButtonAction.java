package GUI.component.ButtonAction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import GUI.component.ButtonUI.*;
import GUI.dialog.editDialog.*;
import Interface.ReloadablePanel;
import Model.*;
import DAO.*;
// xử lý sự kiện button update/deletedelete
public class EditButtonAction extends AbstractCellEditor implements TableCellEditor {
    private JPanel panel;
    private int row;
    
    public EditButtonAction(JTable table, ReloadablePanel reloadablePanel) {
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));

        RoundedButton btnEdit = new RoundedButton("Sửa", 10, new Color(0, 155, 229), new Color(0, 104, 190), new Color(0, 104, 190));
        RoundedButton btnDelete = new RoundedButton("Xóa", 10, new Color(255, 77, 77), new Color(204, 0, 0), new Color(204, 0, 0));

        btnEdit.addActionListener(new ActionListener() {
           @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
                int selectedRow = table.convertRowIndexToModel(row);
                int productId = (int) table.getModel().getValueAt(selectedRow, 0);
                String category = ProductDAO.getInstance().getProductCategory(productId);
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(table);

                switch (category) {
                    case "book":
                        Book book = BookDAO.getInstance().getBookInfor(productId);
                        EditBookDialog editBookDialog = new EditBookDialog(parentFrame, "Book Edit", true, book);
                        editBookDialog.setVisible(true);
                        if (editBookDialog.isSaveClicked()) { // Kiểm tra nếu nút "Lưu" đã được nhấn (nếu bạn đã thêm logic này vào AddBookDialog)
                        reloadablePanel.reloadData();
                        }
                        break;
                    case "CD" :
                        CD cd = CDDAO.getInstance().getCDInfor(productId);
                        EditCDDialog editCDDialog = new EditCDDialog(parentFrame, "CD Edit", true, cd);
                        editCDDialog.setVisible(true);
                        break;
                    case "DVD":
                        DVD dvd = DVDDAO.getInstance().getDVDInfor(productId);
                        EditDVDDialog editDVDDialog = new EditDVDDialog(parentFrame, "DVD Edit", true, dvd);
                        editDVDDialog.setVisible(true);
                        break;
                    case "LP" :
                        LP lp = LPDAO.getInstance().getLPInfor(productId);
                        EditLPDialog editLPDialog = new EditLPDialog(parentFrame, "LP Edit", true, lp);
                        editLPDialog.setVisible(true);
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Lỗi truy vấn dữ liệu.");
                        break;
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.convertRowIndexToModel(row);
                int productId = (int) table.getModel().getValueAt(selectedRow, 0);
                String displayCategory = (String) table.getModel().getValueAt(selectedRow, 2);
                // use DAO to get actual category key for logic
                String category = ProductDAO.getInstance().getProductCategory(productId);

                int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn xóa sản phẩm ID " + productId + " thuộc loại " + displayCategory + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean deleted = false;
                    if (category.equalsIgnoreCase("book")) {
                        deleted = BookDAO.getInstance().deleteBook(productId);
                    } else if (category.equalsIgnoreCase("cd")) {
                        deleted = CDDAO.getInstance().deleteCD(productId);
                    } else if (category.equalsIgnoreCase("dvd")) {
                        deleted = DVDDAO.getInstance().deleteDVD(productId);
                    } else if (category.equalsIgnoreCase("lp")) {
                        deleted = LPDAO.getInstance().deleteLP(productId);
                    }

                    if (deleted) {
                        ((DefaultTableModel) table.getModel()).removeRow(row);
                        JOptionPane.showMessageDialog(null, "Xóa thành công!");
                        reloadablePanel.reloadData();
                    } else {
                        System.err.println("[EditButtonAction] Delete failed for productId=" + productId + ", categoryKey=" + category + ", displayCategory=" + displayCategory);
                        JOptionPane.showMessageDialog(null, "Lỗi khi xóa.");
                    }
                }
            }
        });


        panel.add(btnEdit);
        panel.add(btnDelete);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.row = row;
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }
}
