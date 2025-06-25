package controller;

import view.*;
import view.Panel.*;
import controller.PanelControler.*;
import model.dao.*;

public class MainController {

    private final MainFrame mainView;
    private BookController bookController;
    private CDController cdController;
    private DVDController dvdController;
    private LPController lpController;

    public MainController(MainFrame mainView) {
        this.mainView = mainView;
        setupEventListeners();
        updateProductCounts();
    }
    
    public void setupEventListeners() {
        // Listeners cho các nút Sidebar
        mainView.addHomeButtonListener(e -> showPanel("Home", "Trang chủ"));
        mainView.addBooksButtonListener(e -> showBooksPanel());
        mainView.addDVDsButtonListener(e -> showDVDsPanel());
        mainView.addCDsButtonListener(e -> showCDsPanel());
        mainView.addLPsButtonListener(e -> showLPsPanel());

        // Listeners cho các nút trên HomePanel
        mainView.addBookCategoryButtonListener(e -> showBooksPanel());
        mainView.addDVDCategoryButtonListener(e -> showDVDsPanel());
        mainView.addCDCategoryButtonListener(e -> showCDsPanel());
        mainView.addLPCategoryButtonListener(e -> showLPsPanel());
    }

    private void showPanel(String panelName, String title) {
        mainView.showPanel(panelName, title);
        updateProductCounts();
    }

    private void showBooksPanel() {
        BookPanel bookPanel = mainView.getBookPanel();
        if (bookController == null) {
            bookController = new BookController(bookPanel);
        }
        bookController.loadBooks();
        mainView.showPanel("Books", "Danh sách Book");
        updateProductCounts();
    }

    private void showDVDsPanel() {
        DVDPanel dvdPanel = mainView.getDVDPanel();
        if (dvdController == null) {
            dvdController = new DVDController(dvdPanel);
        }
        dvdController.loadDVDs();
        mainView.showPanel("DVDs", "Danh sách DVD");
        updateProductCounts();
    }

    private void showCDsPanel() {
        CDPanel cdPanel = mainView.getCDPanel();
        if (cdController == null) {
            cdController = new CDController(cdPanel);
        }
        cdController.loadCDs();
        mainView.showPanel("CDs", "Danh sách CD");
        updateProductCounts();
    }

    private void showLPsPanel() {
        LPPanel lpPanel = mainView.getLPPanel();
        if (lpController == null) {
            lpController = new LPController(lpPanel);
        }
        lpController.loadLPs();
        mainView.showPanel("LPs", "Danh sách LP");
        updateProductCounts();
    }


    public void updateProductCounts() {
        int books = BookDAO.getProductCount("Books");
        int dvds = DVDDAO.getProductCount("DVDs");
        int cds = CDDAO.getProductCount("CDs");
        int lps = LPDAO.getProductCount("LPs");
        mainView.updateHomeProductCounts(books, dvds, cds, lps);
    }

    public int getBookCount() {
        return BookDAO.getProductCount("Books");
    }

    public int getDVDCount() {
        return DVDDAO.getProductCount("DVDs");
    }

    public int getCDCount() {
        return CDDAO.getProductCount("CDs");
    }

    public int getLPCount() {
        return LPDAO.getProductCount("LPs");
    }
}