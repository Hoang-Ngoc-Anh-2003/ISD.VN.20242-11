package aimsfinal.Controller;

import aimsfinal.DAO.DVDDAO;
import aimsfinal.Model.DVD;

import java.util.List;

public class DVDController {
    private DVDDAO DVDDAO;

    public DVDController() {
        DVDDAO = new DVDDAO();
    }

    // public boolean insertDVD(DVD DVD) {
    //     return DVDDAO.insertDVD(DVD);
    // }

    // public boolean updateDVD(DVD DVD) {
    //     return DVDDAO.updateDVD(DVD);
    // }

    // public boolean deleteDVD(int productId) {
    //     return DVDDAO.deleteDVD(productId);
    // }

    public List<DVD> getAllDVDs() {
        return DVDDAO.getAllDVDs();
    }
}
