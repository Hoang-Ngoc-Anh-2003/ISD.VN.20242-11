package controller.PanelControler;

import model.dao.LPDAO;
import model.entity.LP;
import view.dialog.addDialog.*;
import view.dialog.editDialog.*;
import view.Panel.LPPanel;
import view.component.ButtonAction.EditButtonAction;
import view.component.ButtonUI.EditButtonRenderer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import controller.AddProductControler.*;
import controller.EditProductControler.*;
import Interface.*;

import java.util.List;

public class LPController {
    private final LPDAO LPDAO;
    private final LPPanel LPPanel;

    public LPController(LPPanel LPPanel) {
        this.LPDAO = new LPDAO();
        this.LPPanel = LPPanel;
        setupEventListeners();
        loadLPs();
    }

    private void setupEventListeners() {
        LPPanel.addAddButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lấy JFrame cha của LPPanel để hiển thị dialog
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(LPPanel);
                openAddLPDialog(parentFrame);
            }
        });

        // Thiết lập Renderer và Editor cho cột "Hành động"
        LPPanel.setActionColumnRendererAndEditor(
            new EditButtonRenderer(), // Renderer chỉ để hiển thị
            new EditButtonAction(LPPanel.getTable(), new ProductActionListener() { // Editor xử lý click
                @Override
                public void onEdit(int productId, int row) {
                    openEditLPDialog(productId); // Gọi phương thức chỉnh sửa của controller này
                }

                @Override
                public void onDelete(int productId, int row) {
                    deleteLP(productId, row);
                }
            })
        );
    }

    public void loadLPs() {
        List<LP> LPs = LPDAO.getAllLPs();
        LPPanel.updateLPTable(LPs);
    }

    public void openAddLPDialog(JFrame parentFrame) {
        AddLPDialog dialog = new AddLPDialog(parentFrame, "Thêm LP", true);
        AddLPController addLPController = new AddLPController(dialog);
        dialog.setVisible(true);
        loadLPs();
    }

    public void openEditLPDialog(int LPId) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(LPPanel);
        LP LP = LPDAO.getLPInfor(LPId);
        EditLPDialog editLPDialog = new EditLPDialog(parentFrame, "Chỉnh sửa Sách", true, LP);
        EditLPController editLPController = new EditLPController(editLPDialog, LP);
        editLPDialog.setVisible(true);
        loadLPs();
    }

    private void deleteLP(int lpId, int row) {
        int confirm = JOptionPane.showConfirmDialog(LPPanel,
                "Bạn có chắc chắn muốn xóa LP này không?", "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = LPDAO.deleteLP(lpId); // Gọi phương thức deleteLP từ DAO
            if (success) {
                JOptionPane.showMessageDialog(LPPanel, "Xóa LP thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadLPs(); // Tải lại bảng sau khi xóa
            } else {
                JOptionPane.showMessageDialog(LPPanel, "Có lỗi xảy ra khi xóa LP.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
