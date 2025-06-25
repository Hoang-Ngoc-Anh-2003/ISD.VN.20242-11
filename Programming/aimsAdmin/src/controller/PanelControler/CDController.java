package controller.PanelControler;

import model.dao.CDDAO;
import model.entity.CD;
import view.dialog.addDialog.*;
import view.dialog.editDialog.*;
import view.Panel.CDPanel;
import view.component.ButtonAction.EditButtonAction;
import view.component.ButtonUI.EditButtonRenderer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import controller.AddProductControler.*;
import controller.EditProductControler.*;
import Interface.*;

import java.util.List;

public class CDController {
    private final CDDAO CDDAO;
    private final CDPanel CDPanel;

    public CDController(CDPanel CDPanel) {
        this.CDDAO = new CDDAO();
        this.CDPanel = CDPanel;
        setupEventListeners();
        loadCDs();
    }

    private void setupEventListeners() {
        CDPanel.addAddButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(CDPanel);
                openAddCDDialog(parentFrame);
            }
        });

        // Thiết lập Renderer và Editor cho cột "Hành động"
        CDPanel.setActionColumnRendererAndEditor(
            new EditButtonRenderer(), // Renderer chỉ để hiển thị
            new EditButtonAction(CDPanel.getTable(), new ProductActionListener() { // Editor xử lý click
                @Override
                public void onEdit(int productId, int row) {
                    openEditCDDialog(productId); // Gọi phương thức chỉnh sửa của controller này
                }

                @Override
                public void onDelete(int productId, int row) {
                    deleteCD(productId, row);
                }
            })
        );
    }

    public void loadCDs() {
        List<CD> CDs = CDDAO.getAllCDs();
        CDPanel.updateCDTable(CDs);
    }

    public void openAddCDDialog(JFrame parentFrame) {
        AddCDDialog dialog = new AddCDDialog(parentFrame, "Thêm CD", true);
        AddCDController addCDController = new AddCDController(dialog);
        dialog.setVisible(true);
        loadCDs();
    }

    public void openEditCDDialog(int CDId) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(CDPanel);
        CD CD = CDDAO.getCDInfor(CDId);
        EditCDDialog editCDDialog = new EditCDDialog(parentFrame, "Chỉnh sửa Sách", true, CD);
        EditCDController editCDController = new EditCDController(editCDDialog, CD);
        editCDDialog.setVisible(true);
        loadCDs();
    }

    private void deleteCD(int cdId, int row) {
        int confirm = JOptionPane.showConfirmDialog(CDPanel,
                "Bạn có chắc chắn muốn xóa CD này không?", "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = CDDAO.deleteCD(cdId); // Gọi phương thức deleteCD từ DAO
            if (success) {
                JOptionPane.showMessageDialog(CDPanel, "Xóa CD thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadCDs(); // Tải lại bảng sau khi xóa
            } else {
                JOptionPane.showMessageDialog(CDPanel, "Có lỗi xảy ra khi xóa CD.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}