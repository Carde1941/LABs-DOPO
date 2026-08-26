import java.util.Objects;

public class Song {

    private String title;
    private String artist;
    private String genre;     
    private Integer duration; 
    private Integer rating;   

    public Song(String title, String artist, String genre, Integer duration, Integer rating) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
    }

    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getGenre() { return genre; }
    public Integer getDuration() { return duration; }
    public Integer getRating() { return rating; }

    public boolean sameKey(Song other) {
        if (other == null) return false;
        return this.title.equalsIgnoreCase(other.title)
            && this.artist.equalsIgnoreCase(other.artist);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Song)) return false;
        Song other = (Song) obj;
        return equalsIgnoreCase(title, other.title)
            && equalsIgnoreCase(artist, other.artist)
            && equalsIgnoreCase(genre, other.genre)
            && Objects.equals(duration, other.duration)
            && Objects.equals(rating, other.rating);
    }

    private static boolean equalsIgnoreCase(String a, String b) {
        if (a == null || b == null) return a == b;
        return a.equalsIgnoreCase(b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            title == null ? null : title.toLowerCase(),
            artist == null ? null : artist.toLowerCase(),
            genre == null ? null : genre.toLowerCase(),
            duration,
            rating
        );
    }
}