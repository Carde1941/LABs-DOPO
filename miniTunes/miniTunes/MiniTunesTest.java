import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Pruebas de unidad para MiniTunes, organizadas por ciclo de desarrollo
 * (ver enunciado del laboratorio 2, Parte III).
 */
public class MiniTunesTest {

    private MiniTunes mt;

    @Before
    public void setUp() {
        mt = new MiniTunes();
    }

    //  Ciclo 1: operaciones basicas 

    @Test
    public void shouldDefineANewPlaylist() {
        mt.define("rock");
        assertTrue(mt.ok());
        assertEquals("rock", mt.toString());
    }

    @Test
    public void shouldNotRedefineAnExistingPlaylist() {
        mt.define("rock");
        mt.define("rock");
        assertFalse(mt.ok());
    }

    @Test
    public void shouldAssignSongsToAnExistingName() {
        mt.define("rock");
        String[][] songs = {{"One", "U2", "Rock", "4", "*****"}};
        mt.assign("rock", songs);
        assertTrue(mt.ok());
        assertEquals(1, mt.size("rock"));
    }

    @Test
    public void shouldNotAssignToANameThatWasNotDefined() {
        String[][] songs = {{"One", "U2", "Rock", "4", "*****"}};
        mt.assign("rock", songs);
        assertFalse(mt.ok());
    }

    @Test
    public void shouldReturnMinusOneWhenAskingTheSizeOfAnUnknownPlaylist() {
        assertEquals(-1, mt.size("unknown"));
        assertFalse(mt.ok());
    }

    @Test
    public void shouldListPlaylistNamesInAlphabeticalOrder() {
        mt.define("rock");
        mt.define("pop");
        assertEquals("pop, rock", mt.toString());
    }

    @Test
    public void shouldShowTheSongsOfAPlaylist() {
        mt.define("rock");
        String[][] songs = {{"One", "U2", "Rock", "4", "*****"}};
        mt.assign("rock", songs);
        assertEquals(new Playlist(songs).toString(), mt.toString("rock"));
    }

    @Test
    public void shouldNotShowTheSongsOfAnUnknownPlaylist() {
        assertNull(mt.toString("unknown"));
        assertFalse(mt.ok());
    }

    //  Ciclo 2: operaciones unarias 

    @Test
    public void shouldAddASongToAPlaylist() {
        mt.define("rock");
        mt.assign("rock", new String[][]{{"One", "U2", "Rock", "4", "*****"}});
        mt.assignUnary("rock", "rock", 'a', new String[]{"Numb", "Linkin Park", "Rock", "3", null});
        assertTrue(mt.ok());
        assertEquals(2, mt.size("rock"));
    }

    @Test
    public void shouldNotReportOkWhenAddingADuplicateSong() {
        mt.define("rock");
        mt.assign("rock", new String[][]{{"One", "U2", "Rock", "4", "*****"}});
        mt.assignUnary("rock", "rock", 'a', new String[]{"One", "U2", "Rock", "4", "*****"});
        assertFalse(mt.ok());
        assertEquals(1, mt.size("rock"));
    }

    @Test
    public void shouldDeleteASongFromAPlaylist() {
        mt.define("rock");
        mt.assign("rock", new String[][]{
            {"One", "U2", "Rock", "4", "*****"},
            {"Numb", "Linkin Park", "Rock", "3", null}});
        mt.assignUnary("rock", "rock", 'd', new String[]{"One", "U2"});
        assertTrue(mt.ok());
        assertEquals(1, mt.size("rock"));
    }

    @Test
    public void shouldSelectSongsByGenreIntoANewPlaylist() {
        mt.define("all");
        mt.assign("all", new String[][]{
            {"One", "U2", "Rock", "4", "*****"},
            {"Poker Face", "Lady Gaga", "Pop", "3", "****"}});
        mt.assignUnary("rock", "all", 's', new String[]{null, null, "Rock", null, null});
        assertTrue(mt.ok());
        assertEquals(1, mt.size("rock"));
    }

    @Test
    public void shouldNotOperateUnaryOnAnUnknownPlaylist() {
        mt.assignUnary("rock", "unknown", 'a', new String[]{"One", "U2", "Rock", "4", "*****"});
        assertFalse(mt.ok());
    }

    //  Ciclo 3: operaciones binarias 

    @Test
    public void shouldUnionTwoPlaylists() {
        mt.define("a"); mt.assign("a", new String[][]{{"One", "U2", "Rock", "4", "*****"}});
        mt.define("b"); mt.assign("b", new String[][]{{"Numb", "Linkin Park", "Rock", "3", null}});
        mt.assignBinary("c", "a", 'u', "b");
        assertTrue(mt.ok());
        assertEquals(2, mt.size("c"));
    }

    @Test
    public void shouldIntersectTwoPlaylists() {
        mt.define("a");
        mt.assign("a", new String[][]{
            {"One", "U2", "Rock", "4", "*****"},
            {"Numb", "Linkin Park", "Rock", "3", null}});
        mt.define("b");
        mt.assign("b", new String[][]{{"One", "U2", "Rock", "4", "*****"}});
        mt.assignBinary("c", "a", 'i', "b");
        assertTrue(mt.ok());
        assertEquals(1, mt.size("c"));
    }

    @Test
    public void shouldGetTheDifferenceOfTwoPlaylists() {
        mt.define("a");
        mt.assign("a", new String[][]{
            {"One", "U2", "Rock", "4", "*****"},
            {"Numb", "Linkin Park", "Rock", "3", null}});
        mt.define("b");
        mt.assign("b", new String[][]{{"One", "U2", "Rock", "4", "*****"}});
        mt.assignBinary("c", "a", 'd', "b");
        assertTrue(mt.ok());
        assertEquals(1, mt.size("c"));
    }

    @Test
    public void shouldNotOperateBinaryWhenOneOfThePlaylistsDoesNotExist() {
        mt.define("a");
        mt.assign("a", new String[][]{{"One", "U2", "Rock", "4", "*****"}});
        mt.assignBinary("c", "a", 'u', "unknown");
        assertFalse(mt.ok());
    }
}
