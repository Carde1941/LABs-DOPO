import java.util.ArrayList;
//Each song is described by its title, artist, genre, duration, and rating.
//The title and artist are mandatory. The genre, duration, and rating may be unknown.
//The combination (title, artist) must be unique. Two songs cannot have the same title and artist.
//The duration (minutes) must be between 1 and 9.
//The rating must be between * and *****.
public class Playlist {
    private String title;
    private String description;
    private String genre;
    private int numberSongs;
    private int totalDuration;
    private String totalRating;
    private ArrayList<Song> songs;

    public Playlist(String[][] songs) {
        this.songs = new ArrayList<Song>();
        if (songs == null) {
            return;
        }

        for (String[] song : songs) {
            if (song[0] == null || song[1] == null) {
                continue;
            }
            String t = song[0].trim().replaceAll("\\s+", " ");
            String a = song[1].trim().replaceAll("\\s+", " "); // quitar espacios
            if (t.isEmpty() || a.isEmpty()) {
                continue;
            }

            String g = song[2];
            if (g != null) {
                g = g.trim().replaceAll("\\s+", " ");
                if (g.isEmpty()) g = null;
            }

            Integer d = null;
            if (song[3] != null && !song[3].trim().isEmpty()) {
                try {
                    d = Integer.parseInt(song[3].trim());
                } catch (NumberFormatException e) {
                    continue;
                }
                if (d < 1 || d > 9) {
                    continue;
                }
            }

            Integer r = null;
            if (song[4] != null) {
                String stars = song[4].replaceAll("\\s+", "");
                if (!stars.isEmpty()) {
                    if (!stars.matches("\\*{1,5}")) {
                        continue;
                    }
                    r = stars.length();
                }
            }

            Song candidata = new Song(t, a, g, d, r);

            // --- (title, artist) debe ser unico dentro de la playlist ---
            boolean duplicada = false;
            for (Song existente : this.songs) {
                if (existente.sameKey(candidata)) {
                    duplicada = true;
                    break;
                }
            }
            if (duplicada) {
                continue;
            }

            this.songs.add(candidata);
        }
    }

    public Playlist add(String[] song) {
        return null;
    }

    public Playlist delete(String[] song) {
        return null;
    }

    public Playlist select(String[] values) {
        return null;
    }

    public int size() {
        return songs.size();
    }

    // Songs are in uppercase with unnecessary spaces removed.
    // Columns are aligned and separated by three spaces.
    //TITLE    ARTIST          GENRE   DURATION   RATING
    //ONE      U2              ROCK           4   *****
    //NUMB     LINKIN PARK     ROCK           3
    //ALIVE    PEARL JAM       ROCK           5   ****
    //CREEP    RADIOHEAD       ROCK               *****
    //DREAMS   FLEETWOOD MAC   .              4   ****
    public String toString() {
        return "";
    }

    public boolean equals(Playlist pl) {
        if (pl == null) return false;
        if (this.songs.size() != pl.songs.size()) return false;
        for (Song s : this.songs) {
            if (!pl.songs.contains(s)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Playlist)) return false;
        return equals((Playlist) o);
    }
}