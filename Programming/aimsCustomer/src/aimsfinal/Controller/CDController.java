package aimsfinal.Controller;

import aimsfinal.DAO.CDDAO;
import aimsfinal.Model.CD;

import java.util.List;

public class CDController {
    private CDDAO CDDAO;

    public CDController() {
        CDDAO = new CDDAO();
    }

    // public boolean insertCD(CD CD) {
    //     return CDDAO.insertCD(CD);
    // }

    // public boolean updateCD(CD CD) {
    //     return CDDAO.updateCD(CD);
    // }

    // public boolean deleteCD(int productId) {
    //     return CDDAO.deleteCD(productId);
    // }

    public List<CD> getAllCDs() {
        return CDDAO.getAllCDs();
    }
}
