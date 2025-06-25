package controller.PanelControler;

import model.dao.*;
import model.entity.*;
import view.dialog.addDialog.*;
import view.dialog.editDialog.*;
import view.Panel.*;
import view.component.ButtonAction.EditButtonAction;
import view.component.ButtonUI.EditButtonRenderer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import controller.AddProductControler.*;
import controller.EditProductControler.*;
import Interface.*;

import java.util.List;

public class DVDController {
    private final DVDDAO DVDDAO ;
    private final DVDPanel DVDPanel;

    public DVDController(DVDPanel DVDPanel) {
        this.DVDDAO = new DVDDAO();
        this.DVDPanel = DVDPanel;
        setupEventListeners();
        loadDVDs();
    }
    private void setupEventListeners() {
        DVDPanel.addAddButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lấy JFrame cha của LPPanel để hiển thị dialog
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(DVDPanel);
                openAddDVDDialog(parentFrame);
            }
        });

        // Thiết lập Renderer và Editor cho cột "Hành động"
        // Điều này được thực hiện trong Controller vì Controller là nơi kết nối View và xử lý logic
        DVDPanel.setActionColumnRendererAndEditor(
            new EditButtonRenderer(), // Renderer chỉ để hiển thị
            new EditButtonAction(DVDPanel.getTable(), new ProductActionListener() { // Editor xử lý click
                @Override
                public void onEdit(int productId, int row) {
                    openEditDVDDialog(productId); // Gọi phương thức chỉnh sửa của controller này
                }

                @Override
                public void onDelete(int productId, int row) {
                    deleteDVD(productId, row);
                }
            })
        );
    }
    
    public void loadDVDs() {
        List<DVD> DVDs = DVDDAO.getAllDVDs();
        DVDPanel.updateDVDTable(DVDs);
    }

    public void openAddDVDDialog(JFrame parentFrame) {
        AddDVDDialog dialog = new AddDVDDialog(parentFrame, "Thêm DVD", true);
        AddDVDController addDVDController = new AddDVDController(dialog);
        dialog.setVisible(true);
        loadDVDs();
    }

    public void openEditDVDDialog(int DVDId) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(DVDPanel);
        DVD DVD = DVDDAO.getDVDInfor(DVDId);
        EditDVDDialog editDVDDialog = new EditDVDDialog(parentFrame, "Chỉnh sửa Sách", true, DVD);
        EditDVDController editDVDController = new EditDVDController(editDVDDialog, DVD);
        editDVDDialog.setVisible(true);
        loadDVDs();
    }

    private void deleteDVD(int dvdId, int row) {
        int confirm = JOptionPane.showConfirmDialog(DVDPanel,
                "Bạn có chắc chắn muốn xóa DVD này không?", "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = DVDDAO.deleteDVD(dvdId); // Gọi phương thức deleteDVD từ DAO
            if (success) {
                JOptionPane.showMessageDialog(DVDPanel, "Xóa DVD thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadDVDs(); // Tải lại bảng sau khi xóa
            } else {
                JOptionPane.showMessageDialog(DVDPanel, "Có lỗi xảy ra khi xóa DVD.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}