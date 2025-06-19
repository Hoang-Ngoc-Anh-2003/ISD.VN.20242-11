package Model;

public class Book extends Product {
    private String authors;     // tác giả
    private String coverType;   // Loại bìa (paperback, hardcover)
    private String publisher;   // Nhà xuất bản
    private String publicationDate; // Ngày xuất bản
    private int numPages;       // Số trang
    private String language;    // Ngôn ngữ
    private String genre;       // Thể loại

    public Book(){  
    }
    
    public Book(int productId, String title, String category, double value, double price, String barcode,
            String description, int quantity, String weight, String dimensions, String warehouseEntryDate, String authors,
            String coverType, String publisher, String publicationDate, int numPages, String language, String genre) {
        super(productId, title, "book", value, price, barcode, description, quantity, weight, dimensions,
                warehouseEntryDate);
        this.authors = authors;
        this.coverType = coverType;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
        this.numPages = numPages;
        this.language = language;
        this.genre = genre;
    }

    // Getters & Setters
    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getCoverType() {
        return coverType;
    }

    public void setCoverType(String coverType) {
        this.coverType = coverType;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public int getNumPages() {
        return numPages;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
