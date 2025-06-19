package GUI.Panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;
import Controller.LPController;
import Model.*;
import GUI.component.ButtonAction.EditButtonAction;
import GUI.component.ButtonUI.*;
import GUI.component.CustomTable.CustomTableCellRenderer;
import GUI.dialog.addDialog.AddLPDialog;
import GUI.dialog.viewDialog.ViewLPDialog;
import Interface.ReloadablePanel;
import GUI.Session;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel hiển thị danh sách và quản lý đĩa than (LP)
 */
public class LPPanel extends JPanel implements ReloadablePanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd;
    private LPController lpController; // Thay đổi từ LPDAO sang LPController

    /**
     * Khởi tạo LPPanel
     */
    public LPPanel() {
        boolean canManage = Session.isAdminFlag || Session.isManagerFlag;
        setLayout(new BorderLayout(10, 10));

        // Sử dụng LPController thay vì LPDAO
        lpController = new LPController();

        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel chứa nút "Thêm LP"
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAdd = new RoundedButton("+ Thêm LP", 15, new Color(0, 155, 229), new Color(0, 104, 190), new Color(0, 104, 190));
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setBackground(new Color(0, 155, 229));
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);
        btnAdd.setPreferredSize(new Dimension(140, 35));
        headerPanel.add(btnAdd);

        // Thêm các cột cho bảng dữ liệu
        String[] columnNames = {"ID", "Tên LP", "Nghệ sĩ", "Giá", "Số lượng", "Hành động"};
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

        // Cấu hình cột Hành động
        TableColumn actionColumn = table.getColumnModel().getColumn(5);
        if (canManage) {
            btnAdd.setVisible(true);
            actionColumn.setPreferredWidth(150);
            table.getColumnModel().getColumn(5).setCellRenderer(new EditButtonRenderer());
            table.getColumnModel().getColumn(5).setCellEditor(new EditButtonAction(table, this));
        } else {
            btnAdd.setVisible(false);
            // Hiển thị nút Chi tiết trong cột Hành động (cho người dùng thường)
            table.getColumnModel().getColumn(5).setCellRenderer((TableCellRenderer)(tbl, val, sel, focus, r, c) -> new JButton("Chi Tiết"));
            // Xử lý sự kiện khi nhấp vào nút Chi tiết
            table.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    int col = table.columnAtPoint(e.getPoint());
                    int row = table.rowAtPoint(e.getPoint());
                    if (col == 5 && row >= 0) {
                        int modelRow = table.convertRowIndexToModel(row);
                        int productId = (int) tableModel.getValueAt(modelRow, 0);
                        LP lp = lpController.getLPInfo(productId);
                        new ViewLPDialog((Frame) SwingUtilities.getWindowAncestor(LPPanel.this), lp).setVisible(true);
                    }
                }
            });
        }

        JScrollPane scrollPane = new JScrollPane(table);
        loadLPs();

        // Áp dụng giao diện tùy chỉnh cho tất cả các cột trừ cột Hành động
        for (int i = 0; i < table.getColumnCount() - 1; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new CustomTableCellRenderer());
        }

        // Loại bỏ hiệu ứng nền khi chọn
        table.setSelectionBackground(table.getBackground());
        table.setSelectionForeground(table.getForeground());

        // Căn giữa nội dung các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount() - 1; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Xử lý sự kiện cho nút "Thêm LP"
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(LPPanel.this);
                AddLPDialog addLPDialog = new AddLPDialog(parentFrame, "Thêm LP", true);
                addLPDialog.setVisible(true);
                if (addLPDialog.isSaveClicked()) {
                    loadLPs();
                }
            }
        });
    }

    /**
     * Tải danh sách đĩa than từ cơ sở dữ liệu và hiển thị lên bảng
     */
    public void loadLPs() {
        List<LP> lps = lpController.getAllLPs();
        tableModel.setRowCount(0);
        for (LP lp : lps) {
            tableModel.addRow(new Object[]{
                lp.getProductId(), lp.getTitle(), lp.getArtists(),
                lp.getPrice(), lp.getQuantity(), ""
            });
        }
        System.out.println("Số lượng đĩa than: " + lps.size());
    }

    /**
     * Tải lại dữ liệu đĩa than khi có thay đổi
     */
    @Override
    public void reloadData() {
        loadLPs();
    }
}
