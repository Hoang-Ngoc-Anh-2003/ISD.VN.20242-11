package GUI.Panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;
import Controller.DVDController;
import Model.*;
import GUI.component.ButtonAction.EditButtonAction;
import GUI.component.ButtonUI.*;
import GUI.component.CustomTable.CustomTableCellRenderer;
import GUI.Session;
import GUI.dialog.viewDialog.ViewDVDDialog;
import GUI.dialog.addDialog.AddDVDDialog;
import Interface.ReloadablePanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Panel hiển thị danh sách và quản lý các DVD
 */
public class DVDPanel extends JPanel implements ReloadablePanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd;
    private DVDController dvdController; // Thay đổi từ DVDDAO sang DVDController

    /**
     * Khởi tạo DVDPanel
     */
    public DVDPanel() {
        boolean canManage = Session.isAdminFlag || Session.isManagerFlag;
        setLayout(new BorderLayout(10, 10));

        // Sử dụng DVDController thay vì DVDDAO
        dvdController = new DVDController();

        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Tạo panel chứa nút Thêm DVD
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAdd = new RoundedButton("+ Thêm DVD", 15, new Color(0, 155, 229), new Color(0, 104, 190), new Color(0, 104, 190));
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setBackground(new Color(0, 155, 229));
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);
        btnAdd.setPreferredSize(new Dimension(140, 35));
        headerPanel.add(btnAdd);

        String[] columnNames = {"ID", "Tên DVD", "Đạo diễn", "Giá", "Số lượng", "Hành động"};
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

        JScrollPane scrollPane = new JScrollPane(table);
        loadDVDs();

        // Đặt renderer cho tất cả cột, trừ cột Hành động
        for (int i = 0; i < table.getColumnCount() - 1; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new CustomTableCellRenderer());
        }

        // Loại bỏ hiệu ứng nền khi chọn
        table.setSelectionBackground(table.getBackground());
        table.setSelectionForeground(table.getForeground());

        if (canManage) {
            btnAdd.setVisible(true);
            actionColumn.setPreferredWidth(150);
            actionColumn.setMaxWidth(180);
            actionColumn.setMinWidth(180);
            table.getColumnModel().getColumn(5).setCellRenderer(new EditButtonRenderer());
            table.getColumnModel().getColumn(5).setCellEditor(new EditButtonAction(table, this));
        } else {
            btnAdd.setVisible(false);
            // Hiển thị nút Chi Tiết trong cột Hành động
            table.getColumnModel().getColumn(5).setCellRenderer((TableCellRenderer)(tbl,val,sel,focus,r,c) -> new JButton("Chi Tiết"));
            // Xử lý sự kiện khi nhấp vào nút Chi Tiết
            table.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    int col = table.columnAtPoint(e.getPoint());
                    int row = table.rowAtPoint(e.getPoint());
                    if (col == 5 && row >= 0) {
                        int modelRow = table.convertRowIndexToModel(row);
                        int productId = (int) tableModel.getValueAt(modelRow, 0);
                        DVD dvd = dvdController.getDVDInfo(productId);
                        new ViewDVDDialog((Frame) SwingUtilities.getWindowAncestor(DVDPanel.this), dvd).setVisible(true);
                    }
                }
            });
        }

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        // Xử lý sự kiện cho nút Thêm DVD
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(DVDPanel.this);
                AddDVDDialog addDVDDialog = new AddDVDDialog(parentFrame, "Thêm DVD", true);
                addDVDDialog.setVisible(true);
                if (addDVDDialog.isSaveClicked()) {
                    loadDVDs();
                }
            }
        });
    }

    /**
     * Nạp danh sách DVD từ cơ sở dữ liệu và hiển thị lên bảng
     */
    private void loadDVDs() {
        List<DVD> dvds = dvdController.getAllDVDs();
        tableModel.setRowCount(0);
        for (DVD dvd : dvds) {
            Object[] rowData = {dvd.getProductId(), dvd.getTitle(), dvd.getDirector(), dvd.getPrice(), dvd.getQuantity(), ""};
            tableModel.addRow(rowData);
        }
        System.out.println("Số lượng DVD: " + dvds.size());
    }

    /**
     * Tải lại dữ liệu DVD khi có thay đổi
     */
    @Override
    public void reloadData() {
        loadDVDs();
    }
}
