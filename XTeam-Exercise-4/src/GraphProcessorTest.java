/////////////////////////////////////////////////////////////////////////////
// Semester: CS400 Spring 2018
// PROJECT: XTeam-Exercise-4
// FILES:Graph.java GraphADT.java GraphTest.java WordProcessor.java
// GraphProcessor.java
//
// USER: lnashold
//
// Instructor: Deb Deppeler (deppeler@cs.wisc.edu)
// Bugs: no known bugs
//
// 2018 Mar 28, 2018 5:16 PM
//////////////////////////// 80 columns wide //////////////////////////////////
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GraphProcessorTest {

    /**
     * File path to a weirdly formatted file to test input sanitization Should have 10 elements with
     * one duplicate if inputed correctly
     */
    private String RANDOM_FORMATTING_PATH;
    /** A G.P. with a small set of 7 words */
    private GraphProcessor smallGraphProcessor;
    /** Only 4 words for testing the most basic level of functionality */
    private GraphProcessor extraSmallGP;
    /** A medium size test filepath with lots of long combinations */
    private GraphProcessor mediumGraphProcessor1;
    private GraphProcessor mediumGraphProcessor2;
    private GraphProcessor longChainGraphProcessor;

    @Before
    /**
     * Creates and Populates a set of small to medium size graphProcessors
     * 
     * @throws Exception
     */
    public void setUp() throws Exception {

        RANDOM_FORMATTING_PATH = "randomformatting.txt";
        smallGraphProcessor = new GraphProcessor();
        smallGraphProcessor.populateGraph("testfile.txt");
        smallGraphProcessor.shortestPathPrecomputation();
        extraSmallGP = new GraphProcessor();
        extraSmallGP.populateGraph("smalldataset.txt");
        extraSmallGP.shortestPathPrecomputation();
        mediumGraphProcessor1 = new GraphProcessor();
        mediumGraphProcessor1.populateGraph("testfile3.txt");
        mediumGraphProcessor1.shortestPathPrecomputation();
        // completeDictionaryGraphProcessor.populateGraph();
        mediumGraphProcessor2 = new GraphProcessor();
        mediumGraphProcessor2.populateGraph("testfile2.txt");
        mediumGraphProcessor2.shortestPathPrecomputation();
    }

    @After
    public void tearDown() throws Exception {}

    /**
     * Tests whether isAdjacent() returns true for various hardcoded adjacent words
     */
    @Test
    public final void test_01_isAdjacent_returns_true() {
        assertTrue(WordProcessor.isAdjacent("ABCEDF", "ABCEZF"));
        assertTrue(WordProcessor.isAdjacent("ABCDF", "ABCEDF"));
        assertTrue(WordProcessor.isAdjacent("TUVZ", "UVZ"));
        assertTrue(WordProcessor.isAdjacent("ABCEDF", "ABCED"));
        assertTrue(WordProcessor.isAdjacent("AC", "ACC"));
        assertTrue(WordProcessor.isAdjacent("CC", "CCC"));
        assertTrue(WordProcessor.isAdjacent("BASH", "HASH"));
        assertTrue(WordProcessor.isAdjacent("LAME", "LAM"));
        assertTrue(WordProcessor.isAdjacent("LAM", "LAME"));
        assertTrue(WordProcessor.isAdjacent("HAS", "HASH"));

    }

    /**
     * Tests to make sure isAdjacent returns false values when it it should for various hard coded
     * strings
     */
    @Test
    public final void test_02_isAdjacent_returns_false() {
        assertFalse(WordProcessor.isAdjacent("A", "ABCDEF"));
        assertFalse(WordProcessor.isAdjacent("ABCD", "ABCDEF"));
        assertFalse(WordProcessor.isAdjacent("ACDEF", "AQXEF"));
        assertFalse(WordProcessor.isAdjacent("CDE", "ACDEF"));
        // Identical strings should return false
        assertFalse(WordProcessor.isAdjacent("ABCDEF", "ABCDEF"));
        assertFalse(WordProcessor.isAdjacent("C", "CCC"));
        assertFalse(WordProcessor.isAdjacent("", "CAT"));
    }

    /**
     * Calls WordProcessor.getWordStream(RANDOM_FORMATTING_PATH), which is a file with elements with
     * a variety of capitalizations and whitespace.
     * 
     * It checks that each item is both trimmed and uppercase). Then checks the right amount of
     * items were added.
     *
     * The file referenced by RANDOM_FORMATTING_PATH should have 10 words, so checks to see that 10
     * words were added.
     */
    @Test
    public final void test_03_get_word_stream() {
        // Outside to have correct scope.
        Stream<String> stream = null;
        try {
            stream = WordProcessor.getWordStream(RANDOM_FORMATTING_PATH);
        } catch (IOException e) {
            // Most likely if RANDOM_FORMATTING_PATH is not valid
            fail(e.toString());
        }
        List<String> list = stream.collect(Collectors.toList());
        for (String str : list) {
            // Check proper formatting;
            assertEquals("String " + str + " is uppercase", true, str.toUpperCase().equals(str));
            assertEquals("String " + str + " is trimmed", true, str.trim().equals(str));
        }
        // Hard coded constant. Should change if file referenced is changed.
        assertEquals(10, list.size());
    }

    @Test
    /**
     * Another test to determine if the various
     */
    public final void test_07_medium_gp_2() {
        assertEquals("[SINK, LINK, LINT, CLINT]",
                        mediumGraphProcessor2.getShortestPath("SINK", "CLINT") + "");
        assertEquals(3, (int) mediumGraphProcessor2.getShortestDistance("SINK", "CLINT"));
        assertEquals(2, (int) mediumGraphProcessor2.getShortestDistance("LASH", "SASS"));
    }

    @Test
    /**
     * Populates graph with data stored in the file TEST_FILE_PATH Verifies result with
     * NUM_WORDS_IN_TEST_FILE
     */
    public final void test_04_populate_graph_processor() {
        smallGraphProcessor = new GraphProcessor();
        int result = smallGraphProcessor.populateGraph(RANDOM_FORMATTING_PATH);
        assertEquals(9, result);
    }

    @Test
    /**
     * Tests getShortestDistance for 3 paths in smallGraphProcessor
     */
    public final void test_05_shortestDistance() {
        assertEquals(2, (int) smallGraphProcessor.getShortestDistance("BAD", "AT"));
        assertEquals(2, (int) smallGraphProcessor.getShortestDistance("HAS", "BASH"));
        assertEquals(1, (int) smallGraphProcessor.getShortestDistance("AT", "BAT"));
    }

    @Test
    /**
     * Tests the getShortestPath for the a couple of the paths in smallGraphProcessor
     */
    public final void test_06_shortestPath() {
        assertEquals("[BAD, BAT, AT]", smallGraphProcessor.getShortestPath("BAD", "AT") + "");
        assertEquals("[BAT, AT]", smallGraphProcessor.getShortestPath("BAT", "AT") + "");
        assertEquals("[BASH, HASH, HAS]", smallGraphProcessor.getShortestPath("BASH", "HAS") + "");
    }

    @Test
    public final void test_08_testSmallData() {
        assertEquals("[SAME, LAME, LAM]", extraSmallGP.getShortestPath("SAME", "LAM") + "");
        assertEquals("[LAM, LAME, SAME]", extraSmallGP.getShortestPath("LAM", "SAME") + "");
        assertEquals("[LAM, LAME]", extraSmallGP.getShortestPath("LAM", "LAME") + "");
        assertEquals("[SAME, LAME]", extraSmallGP.getShortestPath("SAME", "LAME") + "");
        assertEquals(2, (int) extraSmallGP.getShortestDistance("LAM", "SAME"));
    }

    /**
     * Between two words that have no path between them, expects getShortestPath to return an empty
     * list and getShortestDistance to return -1
     */
    @Test
    public final void test_09_noPathBetweenWords_returns_true() {
        assertEquals(0, (int) smallGraphProcessor.getShortestPath("BASH", "AT").size());
        assertEquals(0, smallGraphProcessor.getShortestPath("ARM", "BAT").size());
        assertEquals(0, smallGraphProcessor.getShortestPath("AND", "HAS").size());
        assertEquals(-1, (int) smallGraphProcessor.getShortestDistance("BASH", "AT"));
        assertEquals(-1, (int) smallGraphProcessor.getShortestDistance("ARM", "BAT"));
        assertEquals(-1, (int) smallGraphProcessor.getShortestDistance("AND", "HAS"));
    }

    /**
     * Uses medium sized graph with long/convoluted paths. Tests getShortestPath and
     * getShortestDistance and symmetry.
     */
    @Test
    public final void test_10_mediumSizePath() {
        assertEquals("[WHITES, WHITERS, WAITERS, WATERS, LATERS, LATER, LATE, LANE, SANE, "
                        + "SAND, BAND, BANDS, LANDS, LADS]",
                        mediumGraphProcessor1.getShortestPath("WHITES", "LADS").toString());
        assertEquals(13, (int) mediumGraphProcessor1.getShortestDistance("WHITES", "LADS"));
        assertEquals(13, (int) mediumGraphProcessor1.getShortestDistance("LADS", "WHITES"));

        assertEquals("[CUTE, CITE, CITES, HITES, WHITES, WHITERS, WAITERS, WATERS, LATERS, LATER]",
                        mediumGraphProcessor1.getShortestPath("CUTE", "LATER").toString());
        assertEquals(9, (int) mediumGraphProcessor1.getShortestDistance("CUTE", "LATER"));
        assertEquals(9, (int) mediumGraphProcessor1.getShortestDistance("LATER", "CUTE"));
    }

    @Test
    /**
     * Input file is a file where each line is the previous line plus the addition of one random character.
     * Since input file is 251 lines long, the distance between shortest and last should be 250.
     * 
     */
    public final void test_11_long_chain() {
        // This is not in setup because its time consuming and is only used in this method
        longChainGraphProcessor = new GraphProcessor();
        longChainGraphProcessor.populateGraph("longchain.txt");
        longChainGraphProcessor.shortestPathPrecomputation();
        // Gets distance between shortest and longest element in file
        int shortestDist = longChainGraphProcessor.getShortestDistance("a",
                 "amqhqutsyvewczbnzqdzutzilfbefplfpcrlfcvwghhskmcnmimtf"
                 + "ctrdnnpophmmwfxvbtxaifjgqujudbkdkswceufjafprtkxezxqs"
                 + "rdbgusgjxtzekrjlmpeyaascrttltgtgeevvfeffnafkaqyowrmkd"+
                 "ynmpitwmisbwnvrgwagnwiqrciouoccrpigszzfyutvlhxonnmdmeurh"
                 + "mmrygrtpebudtyyimkdoygyfwyihvfcvdjcjm");
        assertEquals(250, shortestDist);
    }
    
    @Test
    /**
     * A similar file format to test 11, except the chain is less long but there are more
     * elements that differ from elements in the main path by one letter
     * Checks that the chain is really 50 elements long
     */
    public final void test_12_long_chain_with_more_words() {
     // This is not in setup because its time consuming and is only used in this method
        GraphProcessor longChainGraphProcessor2 = new GraphProcessor();
        longChainGraphProcessor2.populateGraph("longchain2.txt");
        longChainGraphProcessor2.shortestPathPrecomputation();
        // Gets distance between shortest and longest element in file
        int shortestDist = longChainGraphProcessor2.getShortestDistance("w",
                        "wzhjtamdmqoytfqwcsposqwfswfwqrpzrryqgqfxgjjpznmccm");
        assertEquals(49, shortestDist);
    }
}
