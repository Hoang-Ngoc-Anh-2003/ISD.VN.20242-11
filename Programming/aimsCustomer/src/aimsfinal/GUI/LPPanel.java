package aimsfinal.GUI;

import aimsfinal.DAO.LPDAO;
import aimsfinal.Model.LP;
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
import aimsfinal.GUI.dialog.addDialog.AddLPDialog;
import aimsfinal.Interface.ReloadablePanel;

public class LPPanel extends JPanel implements ReloadablePanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd;
    private LPDAO lpDAO;

    public LPPanel() {
        setLayout(new BorderLayout(10, 10));
        lpDAO = new LPDAO();
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAdd = new RoundedButton("+ Thêm LP", 15, new Color(0, 155, 229), new Color(0, 104, 190),new Color(0, 104, 190));
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setBackground(new Color(0, 155, 229));
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);
        btnAdd.setPreferredSize(new Dimension(140, 35));
        headerPanel.add(btnAdd);

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

        // Cố định kích thước cột Hành động
        TableColumn actionColumn = table.getColumnModel().getColumn(5);
        actionColumn.setPreferredWidth(150);
        actionColumn.setMaxWidth(180);
        actionColumn.setMinWidth(180);

        JScrollPane scrollPane = new JScrollPane(table);
        loadLPs();

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
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(LPPanel.this);
                AddLPDialog addLPDialog = new AddLPDialog(parentFrame, "Thêm LP", true); // Thêm title và modal
                addLPDialog.setVisible(true);
                if (addLPDialog.isSaveClicked()) { 
                    loadLPs();
                    // Thông báo cho ManagerPanel cập nhật số lượng
                    notifyManagerPanelUpdate();
                }
            }
        });
    }

    public void loadLPs() {
        List<LP> lps = lpDAO.getAllLPs();
        tableModel.setRowCount(0);
        for (LP lp : lps) {
            tableModel.addRow(new Object[]{
                lp.getProductId(), lp.getTitle(), lp.getArtists(),
                lp.getPrice(), lp.getQuantity(), ""
            });
        }
    }

    @Override
    public void reloadData() {
        loadLPs(); // gọi method load hiện tại
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