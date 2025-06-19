package Model;

public class LP extends Product {
    private String artists;      // Danh sách nghệ sĩ
    private String recordLabel;  // Hãng thu âm
    private String tracklist;    // Danh sách bài hát
    private String genre;        // Thể loại
    private String releaseDate;    // Ngày phát hành

    public LP(){
        
    }

    public LP(int productId, String title, String category, double value, double price, String barcode,
    String description, int quantity, String weight, String dimensions, String warehouseEntryDate, String artists, String recordLabel,
              String tracklist, String genre, String releaseDate) {
        super(productId, title, "lp", value, price, barcode, description, quantity, weight,
              dimensions, warehouseEntryDate);
        this.artists = artists;
        this.recordLabel = recordLabel;
        this.tracklist = tracklist;
        this.genre = genre;
        this.releaseDate = releaseDate;
    }

    // Getters & Setters
    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public String getRecordLabel() {
        return recordLabel;
    }

    public void setRecordLabel(String recordLabel) {
        this.recordLabel = recordLabel;
    }

    public String getTracklist() {
        return tracklist;
    }

    public void setTracklist(String tracklist) {
        this.tracklist = tracklist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
