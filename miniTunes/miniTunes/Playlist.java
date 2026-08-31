import java.util.ArrayList;

/**
 * A Playlist is an ordered collection of unique songs.
 */
public class Playlist {

    private ArrayList<Song> songs;

    /**
     * Builds a playlist from raw song rows {title, artist, genre, duration, rating}.
     * Rows that do not describe a valid song, or that duplicate the
     * (title, artist) of an earlier valid row, are silently ignored.
     */
    public Playlist(String[][] songs) {
        this.songs = new ArrayList<Song>();
        if (songs == null) {
            return;
        }
        for (String[] row : songs) {
            addIfValidAndNew(this.songs, row);
        }
    }

    /**
     * Internal constructor used only inside this class: builds a playlist
     * directly from a list of songs that are already known to be valid and
     * without duplicates (no re-validation is performed).
     */
    private Playlist(ArrayList<Song> validSongs) {
        this.songs = validSongs;
    }

    // Ciclo 2: operaciones unarias (add, delete, select)

    /**
     * Returns a NEW playlist with the given song added, provided that the
     * row describes a valid song and this playlist does not already have a
     * song with the same (title, artist). Otherwise, returns a copy of
     * this playlist, unchanged (the addition has no effect).
     */
    public Playlist add(String[] song) {
        ArrayList<Song> copy = new ArrayList<Song>(this.songs);
        addIfValidAndNew(copy, song);
        return new Playlist(copy);
    }

    /**
     * Returns a NEW playlist without the song identified by
     * (song[0] = title, song[1] = artist). If no song has that
     * (title, artist), returns a copy of this playlist, unchanged.
     */
    public Playlist delete(String[] song) {
        ArrayList<Song> copy = new ArrayList<Song>(this.songs);
        if (song != null && song.length >= 2 && song[0] != null && song[1] != null) {
            String t = normalize(song[0]);
            String a = normalize(song[1]);
            if (!t.isEmpty() && !a.isEmpty()) {
                Song key = new Song(t, a, null, null, null);
                for (int i = 0; i < copy.size(); i++) {
                    if (copy.get(i).sameKey(key)) {
                        copy.remove(i);
                        break;
                    }
                }
            }
        }
        return new Playlist(copy);
    }

    /**
     * Returns a NEW playlist with only the songs that match the given
     * search pattern values = {title, artist, genre, duration, rating}.
     * A null (or blank) position means "any value" for that field.
     * Title/artist/genre are compared ignoring case; duration and rating
     * are compared by their numeric value. The relative order of the
     * matching songs is preserved.
     */
    public Playlist select(String[] values) {
        ArrayList<Song> result = new ArrayList<Song>();
        for (Song s : this.songs) {
            if (matches(s, values)) {
                result.add(s);
            }
        }
        return new Playlist(result);
    }

    // Ciclo 3: operaciones binarias (union, intersection, difference)

    /**
     * Returns a NEW playlist with the songs of this playlist followed by
     * the songs of other that are not already in this playlist. Both
     * groups keep their original relative order.
     */
    public Playlist union(Playlist other) {
        ArrayList<Song> result = new ArrayList<Song>(this.songs);
        for (Song s : other.songs) {
            if (!result.contains(s)) {
                result.add(s);
            }
        }
        return new Playlist(result);
    }

    /**
     * Returns a NEW playlist with the songs that appear both in this
     * playlist and in other, in this playlist's original order.
     */
    public Playlist intersection(Playlist other) {
        ArrayList<Song> result = new ArrayList<Song>();
        for (Song s : this.songs) {
            if (other.songs.contains(s)) {
                result.add(s);
            }
        }
        return new Playlist(result);
    }

    /**
     * Returns a NEW playlist with the songs that are in this playlist but
     * not in other, in this playlist's original order.
     */
    public Playlist difference(Playlist other) {
        ArrayList<Song> result = new ArrayList<Song>();
        for (Song s : this.songs) {
            if (!other.songs.contains(s)) {
                result.add(s);
            }
        }
        return new Playlist(result);
    }

    /** Number of songs in this playlist. */
    public int size() {
        return songs.size();
    }

    /**
     * Text representation of the playlist as a table with columns
     * TITLE, ARTIST, GENRE, DURATION and RATING (uppercase, columns
     * aligned). An unknown genre is shown as "."; an unknown duration or
     * rating is shown blank. An empty playlist is shown as "".
     *
     * NOTA: el ancho de cada columna se calcula a partir del contenido
     * real (encabezado + valores) mas tres espacios de separacion, para
     * que la tabla quede alineada sin importar el largo de los datos.
     */
    public String toString() {
        if (songs.isEmpty()) {
            return "";
        }

        String[] titles = new String[songs.size()];
        String[] artists = new String[songs.size()];
        String[] genres = new String[songs.size()];
        String[] durations = new String[songs.size()];
        String[] ratings = new String[songs.size()];

        for (int i = 0; i < songs.size(); i++) {
            Song s = songs.get(i);
            titles[i] = s.getTitle().toUpperCase();
            artists[i] = s.getArtist().toUpperCase();
            genres[i] = s.getGenre() == null ? "." : s.getGenre().toUpperCase();
            durations[i] = s.getDuration() == null ? "" : String.valueOf(s.getDuration());
            ratings[i] = s.getRating() == null ? "" : stars(s.getRating());
        }

        int wTitle = widthOf("TITLE", titles);
        int wArtist = widthOf("ARTIST", artists);
        int wGenre = widthOf("GENRE", genres);
        int wDuration = widthOf("DURATION", durations);

        StringBuilder sb = new StringBuilder();
        sb.append(padRight("TITLE", wTitle))
          .append(padRight("ARTIST", wArtist))
          .append(padRight("GENRE", wGenre))
          .append(padLeft("DURATION", wDuration))
          .append("   RATING");

        for (int i = 0; i < songs.size(); i++) {
            sb.append("\n")
              .append(padRight(titles[i], wTitle))
              .append(padRight(artists[i], wArtist))
              .append(padRight(genres[i], wGenre))
              .append(padLeft(durations[i], wDuration))
              .append("   ")
              .append(ratings[i]);
            while (sb.charAt(sb.length() - 1) == ' ') {
                sb.deleteCharAt(sb.length() - 1);
            }
        }
        return sb.toString();
    }

    /** Two playlists are equal when they contain exactly the same songs (order does not matter). */
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

    public int hashCode() {
        int h = 0;
        for (Song s : songs) {
            h += s.hashCode();
        }
        return h;
    }

    // Metodos privados de apoyo

    /**
     * Validates row as a song description and, if valid and not a
     * duplicate of a song already in target, appends it to target.
     * Invalid rows and duplicates are silently ignored.
     */
    private static void addIfValidAndNew(ArrayList<Song> target, String[] row) {
        Song candidate = validate(row);
        if (candidate == null) {
            return;
        }
        for (Song existing : target) {
            if (existing.sameKey(candidate)) {
                return;
            }
        }
        target.add(candidate);
    }

    /**
     * Returns the Song described by row = {title, artist, genre, duration, rating},
     * or null when the row does not describe a valid song.
     */
    private static Song validate(String[] row) {
        if (row == null || row.length < 5) {
            return null;
        }
        if (row[0] == null || row[1] == null) {
            return null;
        }
        String t = normalize(row[0]);
        String a = normalize(row[1]);
        if (t.isEmpty() || a.isEmpty()) {
            return null;
        }

        String g = row[2];
        if (g != null) {
            g = normalize(g);
            if (g.isEmpty()) {
                g = null;
            }
        }

        Integer d = null;
        if (row[3] != null && !row[3].trim().isEmpty()) {
            try {
                d = Integer.parseInt(row[3].trim());
            } catch (NumberFormatException e) {
                return null;
            }
            if (d < 1 || d > 9) {
                return null;
            }
        }

        Integer r = null;
        if (row[4] != null) {
            String stars = row[4].replaceAll("\\s+", "");
            if (!stars.isEmpty()) {
                if (!stars.matches("\\*{1,5}")) {
                    return null;
                }
                r = stars.length();
            }
        }

        return new Song(t, a, g, d, r);
    }

    /** true when s matches every non-blank position of values = {title, artist, genre, duration, rating}. */
    private static boolean matches(Song s, String[] values) {
        if (values == null) {
            return true;
        }
        if (values.length > 0 && values[0] != null && !values[0].trim().isEmpty()
                && !s.getTitle().equalsIgnoreCase(values[0].trim())) {
            return false;
        }
        if (values.length > 1 && values[1] != null && !values[1].trim().isEmpty()
                && !s.getArtist().equalsIgnoreCase(values[1].trim())) {
            return false;
        }
        if (values.length > 2 && values[2] != null && !values[2].trim().isEmpty()) {
            if (s.getGenre() == null || !s.getGenre().equalsIgnoreCase(values[2].trim())) {
                return false;
            }
        }
        if (values.length > 3 && values[3] != null && !values[3].trim().isEmpty()) {
            Integer d;
            try {
                d = Integer.parseInt(values[3].trim());
            } catch (NumberFormatException e) {
                return false;
            }
            if (s.getDuration() == null || !s.getDuration().equals(d)) {
                return false;
            }
        }
        if (values.length > 4 && values[4] != null && !values[4].trim().isEmpty()) {
            int r = values[4].replaceAll("\\s+", "").length();
            if (s.getRating() == null || s.getRating() != r) {
                return false;
            }
        }
        return true;
    }

    private static String normalize(String s) {
        return s.trim().replaceAll("\\s+", " ");
    }

    private static String stars(int rating) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rating; i++) {
            sb.append('*');
        }
        return sb.toString();
    }

    private static int widthOf(String header, String[] values) {
        int max = header.length();
        for (String v : values) {
            if (v.length() > max) {
                max = v.length();
            }
        }
        return max + 3;
    }

    private static String padRight(String s, int width) {
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() < width) {
            sb.append(' ');
        }
        return sb.toString();
    }

    private static String padLeft(String s, int width) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < width - s.length(); i++) {
            sb.append(' ');
        }
        sb.append(s);
        return sb.toString();
    }
}
