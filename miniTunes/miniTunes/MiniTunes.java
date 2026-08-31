import java.util.TreeMap;

/**
 * MiniTunes.java
 * @author ESCUELA 2026-02
 */
public class MiniTunes {

    private TreeMap<String, Playlist> playlists;

    /** true if the last operation performed on this MiniTunes succeeded. */
    private boolean lastOk;

    public MiniTunes() {
        playlists = new TreeMap<String, Playlist>();
        lastOk = true;
    }

    //Ciclo 1: operaciones basicas

    /**
     * Defines a new, empty playlist name. Fails (ok() == false) when name
     * is null/blank or when the name is already defined.
     */
    public void define(String name) {
        if (name == null || name.trim().isEmpty() || playlists.containsKey(name)) {
            lastOk = false;
            return;
        }
        playlists.put(name, new Playlist(null));
        lastOk = true;
    }

    /**
     * Assigns the songs described by playlist to the EXISTING playlist
     * name a. Fails when a has not been defined yet.
     * a := playlist
     */
    public void assign(String a, String[][] playlist) {
        if (a == null || !playlists.containsKey(a)) {
            lastOk = false;
            return;
        }
        playlists.put(a, new Playlist(playlist));
        lastOk = true;
    }

    /** Returns the size of playlist a, or -1 (and ok() == false) when a is unknown. */
    public int size(String a) {
        if (a == null || !playlists.containsKey(a)) {
            lastOk = false;
            return -1;
        }
        lastOk = true;
        return playlists.get(a).size();
    }

    /** Returns the playlist names in alphabetical order, separated by ", ". */
    public String toString() {
        lastOk = true;
        StringBuilder sb = new StringBuilder();
        for (String name : playlists.keySet()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(name);
        }
        return sb.toString();
    }

    /**
     * Returns the string representation of playlist name, or null (and
     * ok() == false) when name is unknown.
     */
    public String toString(String name) {
        if (name == null || !playlists.containsKey(name)) {
            lastOk = false;
            return null;
        }
        lastOk = true;
        return playlists.get(name).toString();
    }

    //Ciclo 2: operaciones unarias

    /**
     * Assigns the value of a unary operation to playlist name a:
     * a = b op values
     * The operator characters are: 'a' (add), 'd' (delete), 's' (select).
     * For add and delete, values correspond to song data; for select,
     * values define the search pattern. Fails when b is unknown or op is
     * not one of 'a', 'd', 's'; also fails (but still creates a) when an
     * add/delete had no real effect (duplicate song, or song not found).
     */
    public void assignUnary(String a, String b, char op, String[] values) {
        if (b == null || !playlists.containsKey(b)) {
            lastOk = false;
            return;
        }
        Playlist source = playlists.get(b);
        Playlist result;
        switch (op) {
            case 'a':
                result = source.add(values);
                lastOk = result.size() == source.size() + 1;
                break;
            case 'd':
                result = source.delete(values);
                lastOk = result.size() == source.size() - 1;
                break;
            case 's':
                result = source.select(values);
                lastOk = true;
                break;
            default:
                lastOk = false;
                return;
        }
        playlists.put(a, result);
    }

    //Ciclo 3: operaciones binarias

    /**
     * Assigns the value of a binary operation to playlist name a:
     * a = b op c
     * The operator characters are: 'u' (union), 'i' (intersection),
     * 'd' (difference). Songs preserve their original order (b's order
     * first). Fails when b or c are unknown, or op is not one of
     * 'u', 'i', 'd'.
     */
    public void assignBinary(String a, String b, char op, String c) {
        if (b == null || c == null || !playlists.containsKey(b) || !playlists.containsKey(c)) {
            lastOk = false;
            return;
        }
        Playlist pb = playlists.get(b);
        Playlist pc = playlists.get(c);
        Playlist result;
        switch (op) {
            case 'u':
                result = pb.union(pc);
                break;
            case 'i':
                result = pb.intersection(pc);
                break;
            case 'd':
                result = pb.difference(pc);
                break;
            default:
                lastOk = false;
                return;
        }
        playlists.put(a, result);
        lastOk = true;
    }

    /** If the last operation was successfully completed. */
    public boolean ok() {
        return lastOk;
    }
}
