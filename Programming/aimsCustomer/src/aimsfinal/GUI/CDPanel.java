package aimsfinal.GUI;

import aimsfinal.DAO.CDDAO;
import aimsfinal.Model.CD;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import aimsfinal.GUI.component.ButtonAction.EditButtonAction;
import aimsfinal.GUI.component.ButtonUI.*;
import aimsfinal.GUI.component.CustomTable.CustomTableCellRenderer;
import aimsfinal.GUI.dialog.addDialog.AddCDDialog;
import aimsfinal.Interface.ReloadablePanel;

public class CDPanel extends JPanel implements ReloadablePanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd;
    private CDDAO cdDAO;

    public CDPanel() {
        setLayout(new BorderLayout(10, 10));
        cdDAO = new CDDAO();
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header chứa nút "Thêm CD"
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAdd = new RoundedButton("+ Thêm CD", 15, new Color(0, 155, 229), new Color(0, 104, 190), new Color(0, 104, 190));
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setBackground(new Color(0, 155, 229));
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);
        btnAdd.setPreferredSize(new Dimension(140, 35));
        headerPanel.add(btnAdd);

        // Định nghĩa bảng
        String[] columnNames = {"ID", "Tên CD", "Nghệ sĩ", "Giá", "Số lượng", "Hành động"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setIntercellSpacing(new Dimension(10, 5));
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(220, 220, 220));
        table.getTableHeader().setReorderingAllowed(false);
        
        // Cố định kích thước cột Hành động
        TableColumn actionColumn = table.getColumnModel().getColumn(5);
        actionColumn.setPreferredWidth(150);
        actionColumn.setMaxWidth(180);
        actionColumn.setMinWidth(180);

        JScrollPane scrollPane = new JScrollPane(table);

        // Load dữ liệu từ database
        loadCDs();

        // Đặt renderer để giữ màu nguyên bản ngay cả khi chọn
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new CustomTableCellRenderer());
        }

        // Loại bỏ hiệu ứng nền khi chọn
        table.setSelectionBackground(table.getBackground());
        table.setSelectionForeground(table.getForeground());


        // Đặt renderer cho cột cuối cùng (Hành động)
        // Áp dụng EditButtonRenderer và EditButtonAction
        table.getColumnModel().getColumn(5).setCellRenderer(new EditButtonRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new EditButtonAction(table, this));


        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount() - 1; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(CDPanel.this);
                AddCDDialog addCDDialog = new AddCDDialog(parentFrame, "Thêm CD", true); // Thêm title và modal
                addCDDialog.setVisible(true);
                if (addCDDialog.isSaveClicked()) { // Kiểm tra nếu nút "Lưu" đã được nhấn (nếu bạn đã thêm logic này vào AddBookDialog)
                    loadCDs();
                    // Thông báo cho ManagerPanel cập nhật số lượng
                    notifyManagerPanelUpdate();
                }
            }
        });
    }

    public void loadCDs() {
        List<CD> cds = cdDAO.getAllCDs();
        tableModel.setRowCount(0);
        for (CD cd : cds) {
            tableModel.addRow(new Object[]{
                cd.getProductId(), cd.getTitle(), cd.getArtists(),
                cd.getPrice(), cd.getQuantity(), ""
            });
        }
        System.out.println("So luong CD: " + cds.size());
    }

    @Override
    public void reloadData() {
        loadCDs(); // gọi method load hiện tại
    }

    // Method để thông báo cho ManagerPanel cập nhật
    private void notifyManagerPanelUpdate() {
        // Tìm ManagerPanel trong parent container
        Container parent = getParent();
        while (parent != null) {
            if (parent instanceof ManagerPanel) {
                ((ManagerPanel) parent).updateProductCounts();
                break;
            }
            parent = parent.getParent();
        }
    }
}