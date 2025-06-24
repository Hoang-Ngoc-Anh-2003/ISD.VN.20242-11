package GUI.component.ButtonAction;

import javax.swing.*;
import javax.swing.table.TableCellEditor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Controller.LPController;
import GUI.component.ButtonUI.*;
import GUI.dialog.editDialog.*;
import Interface.ReloadablePanel;
import Model.*;
import Controller.BookController;
import Controller.CDController;
import Controller.DVDController;
import Controller.ProductController;
// xử lý sự kiện button update/deletedelete
public class EditButtonAction extends AbstractCellEditor implements TableCellEditor {
    private JPanel panel;
    private int row;
    private boolean isCellEditing = false;

    public EditButtonAction(JTable table, ReloadablePanel reloadablePanel) {
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));

        RoundedButton btnEdit = new RoundedButton("Sửa", 10, new Color(0, 155, 229), new Color(0, 104, 190), new Color(0, 104, 190));
        RoundedButton btnDelete = new RoundedButton("Xóa", 10, new Color(255, 77, 77), new Color(204, 0, 0), new Color(204, 0, 0));

        btnEdit.addActionListener(new ActionListener() {
           @Override
            public void actionPerformed(ActionEvent e) {
               if (!isCellEditing) {
                   return; // Prevent multiple clicks while processing
               }

               try {
                   isCellEditing = false; // Mark as not editing before any operations

                   int selectedRow = table.convertRowIndexToModel(row);
                   int productId = (int) table.getModel().getValueAt(selectedRow, 0);
                   JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(table);

                   String category = ProductController.getProductCategory(productId);
                   switch (category != null ? category.toUpperCase() : "") {
                       case "BOOK":
                           BookController bookController = new BookController();
                           Book book = bookController.getBookInfo(productId);
                           EditBookDialog editBookDialog = new EditBookDialog(parentFrame, "Book Edit", true, book);
                           editBookDialog.setVisible(true);
                           if (editBookDialog.isSaveClicked()) {
                               reloadablePanel.reloadData();
                           }
                           break;
                       case "CD":
                           CDController cdController = new CDController();
                           CD cd = cdController.getCDInfo(productId);
                           EditCDDialog editCDDialog = new EditCDDialog(parentFrame, "CD Edit", true, cd);
                           editCDDialog.setVisible(true);
                           if (editCDDialog.isSaveClicked()) {
                               reloadablePanel.reloadData();
                           }
                           break;
                       case "DVD":
                           DVDController dvdController = new DVDController();
                           DVD dvd = dvdController.getDVDInfo(productId);
                           EditDVDDialog editDVDDialog = new EditDVDDialog(parentFrame, "DVD Edit", true, dvd);
                           editDVDDialog.setVisible(true);
                           if (editDVDDialog.isSaveClicked()) {
                               reloadablePanel.reloadData();
                           }
                           break;
                       case "LP":
                           LPController lpController = new LPController();
                           LP lp = lpController.getLPInfo(productId);
                           EditLPDialog editLPDialog = new EditLPDialog(parentFrame, "LP Edit", true, lp);
                           editLPDialog.setVisible(true);
                           if (editLPDialog.isSaveClicked()) {
                               reloadablePanel.reloadData();
                           }
                           break;
                       default:
                           JOptionPane.showMessageDialog(null, "Lỗi truy vấn dữ liệu.");
                           break;
                   }
               } finally {
                   // Always call fireEditingStopped in finally block to ensure it happens
                   SwingUtilities.invokeLater(() -> fireEditingStopped());
               }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isCellEditing) {
                    return; // Prevent multiple clicks while processing
                }

                // Lấy dữ liệu trước khi đặt isCellEditing = false
                int selectedRow = table.convertRowIndexToModel(row);
                int productId = (int) table.getModel().getValueAt(selectedRow, 0);
                String displayCategory = (String) table.getModel().getValueAt(selectedRow, 2);
                String category = ProductController.getProductCategory(productId);

                // Kết thúc chỉnh sửa ngay lập tức để tránh lỗi
                isCellEditing = false;
                fireEditingStopped();

                // Thực hiện thao tác xóa sau khi đã kết thúc chỉnh sửa
                int confirm = JOptionPane.showConfirmDialog(null,
                    "Bạn có chắc muốn xóa sản phẩm ID " + productId + " thuộc loại " + displayCategory + "?",
                    "Xác nhận", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean deleted = switch (category.toLowerCase()) {
                        case "book" -> new BookController().deleteBook(productId);
                        case "cd" -> new CDController().deleteCD(productId);
                        case "dvd" -> new DVDController().deleteDVD(productId);
                        case "lp" -> new LPController().deleteLP(productId);
                        default -> false;
                    };

                    if (deleted) {
                        JOptionPane.showMessageDialog(null, "Xóa thành công!");
                        reloadablePanel.reloadData();
                    } else {
                        System.err.println("[EditButtonAction] Delete failed for productId=" + productId +
                            ", categoryKey=" + category + ", displayCategory=" + displayCategory);
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
        this.isCellEditing = true; // Set editing flag to true when editor is activated
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }
}
