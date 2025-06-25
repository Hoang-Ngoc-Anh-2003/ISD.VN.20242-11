package Interface;

public interface ProductActionListener {
    void onEdit(int productId, int row);
    void onDelete(int productId, int row);
}