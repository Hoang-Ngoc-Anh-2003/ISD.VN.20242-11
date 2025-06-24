package aimsfinal.Controller;

import aimsfinal.DAO.LPDAO;
import aimsfinal.Model.LP;

import java.util.List;

public class LPController {
    private LPDAO LPDAO;

    public LPController() {
        LPDAO = new LPDAO();
    }

    // public boolean insertLP(LP LP) {
    //     return LPDAO.insertLP(LP);
    // }

    // public boolean updateLP(LP LP) {
    //     return LPDAO.updateLP(LP);
    // }

    // public boolean deleteLP(int productId) {
    //     return LPDAO.deleteLP(productId);
    // }

    public List<LP> getAllLPs() {
        return LPDAO.getAllLPs();
    }
}
