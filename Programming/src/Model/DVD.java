package Model;

public class DVD extends Product {
    private String discType;   // Loại đĩa (Blu-ray, HD-DVD)
    private String director;   // Đạo diễn
    private int runtime;       // Thời lượng (phút)
    private String studio;     // Hãng sản xuất
    private String language;   // Ngôn ngữ
    private String subtitles;  // Phụ đề
    private String releaseDate;  // Ngày phát hành
    private String genre;      // Thể loại
    
    public DVD(){
    }

    public DVD(int productId, String title, String category, double value, double price, String barcode,
    String description, int quantity, String weight, String dimensions, String warehouseEntryDate, String discType, String director,
               int runtime, String studio, String language, String subtitles, String releaseDate,
               String genre) {
        super(productId, title, "dvd", value, price, barcode, description, quantity, weight,
              dimensions, warehouseEntryDate);
        this.discType = discType;
        this.director = director;
        this.runtime = runtime;
        this.studio = studio;
        this.language = language;
        this.subtitles = subtitles;
        this.releaseDate = releaseDate;
        this.genre = genre;
    }


    // Getters & Setters
    public String getDiscType() {
        return discType;
    }

    public void setDiscType(String discType) {
        this.discType = discType;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(String subtitles) {
        this.subtitles = subtitles;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
