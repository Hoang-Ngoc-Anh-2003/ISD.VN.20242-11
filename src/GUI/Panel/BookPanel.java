package GUI.Panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import Controller.BookController;
import Model.Book;
import GUI.component.ButtonAction.EditButtonAction;
import GUI.component.ButtonUI.*;
import GUI.component.CustomTable.CustomTableCellRenderer;
import GUI.dialog.addDialog.AddBookDialog;
import Interface.ReloadablePanel;
import GUI.Session;

/**
 * Panel hiển thị danh sách và quản lý sách
 */
public class BookPanel extends JPanel implements ReloadablePanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd;
    private BookController bookController; // Thay đổi từ BookDAO sang BookController

    /**
     * Khởi tạo BookPanel
     */
    public BookPanel() {
        setLayout(new BorderLayout(10, 10));

        // Sử dụng BookController thay vì BookDAO
        bookController = new BookController();

        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header chứa nút "Thêm Sách"
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAdd = new RoundedButton("+ Thêm Sách", 15, new Color(0, 155, 229), new Color(0, 104, 190), new Color(0, 104, 190));
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setBackground(new Color(0, 155, 229));
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);
        btnAdd.setPreferredSize(new Dimension(140, 35));
        headerPanel.add(btnAdd);

        // Định nghĩa bảng
        String[] columnNames = {"ID", "Tên sách", "Tác giả", "Giá", "Số lượng", "Hành động"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Chỉ cho phép chỉnh sửa cột Hành động
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
        loadBooks();

        // Đặt renderer để giữ màu nguyên bản ngay cả khi chọn
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new CustomTableCellRenderer());
        }

        // Loại bỏ hiệu ứng nền khi chọn
        table.setSelectionBackground(table.getBackground());
        table.setSelectionForeground(table.getForeground());

        boolean canManage = Session.isAdminFlag || Session.isManagerFlag;
        if (canManage) {
            // Admin hoặc Manager: hiển thị nút thêm và sửa/xóa
            btnAdd.setVisible(true);
            actionColumn.setPreferredWidth(150);
            table.getColumnModel().getColumn(5).setCellRenderer(new EditButtonRenderer());
            table.getColumnModel().getColumn(5).setCellEditor(new EditButtonAction(table, this));
        } else {
            btnAdd.setVisible(false);
            // Hiển thị nút Chi tiết trong cột Hành động
            table.getColumnModel().getColumn(5).setCellRenderer((TableCellRenderer)(tbl, val, sel, focus, r, c) -> new JButton("Chi Tiết"));
            // Xử lý sự kiện khi nhấp vào nút Chi tiết
            table.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    int col = table.columnAtPoint(e.getPoint());
                    int row = table.rowAtPoint(e.getPoint());
                    if (col == 5 && row >= 0) {
                        int modelRow = table.convertRowIndexToModel(row);
                        int productId = (int) tableModel.getValueAt(modelRow, 0);
                        Book book = bookController.getBookInfo(productId);
                        new GUI.dialog.viewDialog.ViewBookDialog((Frame)SwingUtilities.getWindowAncestor(BookPanel.this), book).setVisible(true);
                    }
                }
            });
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount() - 1; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Gắn sự kiện click cho nút "Thêm Sách"
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(BookPanel.this);
                AddBookDialog addBookDialog = new AddBookDialog(parentFrame, "Thêm Sách", true);
                addBookDialog.setVisible(true);

                // Tải lại dữ liệu sau khi thêm sách
                if (addBookDialog.isSaveClicked()) {
                    loadBooks();
                }
            }
        });
    }

    /**
     * Tải danh sách sách từ cơ sở dữ liệu và hiển thị lên bảng
     */
    public void loadBooks() {
        List<Book> books = bookController.getAllBooks();
        tableModel.setRowCount(0);
        for (Book book : books) {
            tableModel.addRow(new Object[]{
                    book.getProductId(), book.getTitle(), book.getAuthors(),
                    book.getPrice(), book.getQuantity(), ""
            });
        }
        System.out.println("Số lượng sách: " + books.size());
    }

    /**
     * Tải lại dữ liệu sách khi có thay đổi
     */
    @Override
    public void reloadData() {
        loadBooks();
    }
}
