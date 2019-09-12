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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * This class contains some utility helper methods
 * 
 * @author sapan (sapan@cs.wisc.edu)
 */
public class WordProcessor {

    /**
     * Gets a Stream of words from the filepath.
     * 
     * The Stream should only contain trimmed, non-empty and UPPERCASE words.
     * 
     * @see <a href=
     *      "http://www.oracle.com/technetwork/articles/java/ma14-java-se-8-streams-2177646.html">java8
     *      stream blog</a>
     * 
     * @param filepath file path to the dictionary file
     * @return Stream<String> stream of words read from the filepath
     * @throws IOException exception resulting from accessing the filepath
     */
    public static Stream<String> getWordStream(String filepath) throws IOException {
        /**
         * @see <a href=
         *      "https://docs.oracle.com/javase/8/docs/api/java/nio/file/Files.html">java.nio.file.Files</a>
         * @see <a href=
         *      "https://docs.oracle.com/javase/8/docs/api/java/nio/file/Paths.html">java.nio.file.Paths</a>
         * @see <a href=
         *      "https://docs.oracle.com/javase/8/docs/api/java/nio/file/Path.html">java.nio.file.Path</a>
         * @see <a href=
         *      "https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html">java.util.stream.Stream</a>
         * 
         *      class Files has a method lines() which accepts an interface Path object and produces
         *      a Stream<String> object via which one can read all the lines from a file as a
         *      Stream.
         * 
         *      class Paths has a method get() which accepts one or more strings (filepath), joins
         *      them if required and produces a interface Path object
         * 
         *      Combining these two methods: Files.lines(Paths.get(<string filepath>)) produces a
         *      Stream of lines read from the filepath
         * 
         *      Once this Stream of lines is available, you can use the powerful operations
         *      available for Stream objects to combine multiple pre-processing operations of each
         *      line in a single statement.
         * 
         *      Few of these features: 1. map( ) [changes a line to the result of the applied
         *      function. Mathematically, line = operation(line)] - trim all the lines - convert all
         *      the lines to UpperCase - example takes each of the lines one by one and apply the
         *      function toString on them as line.toString() and returns the Stream: streamOfLines =
         *      streamOfLines.map(String::toString)
         * 
         *      2. filter( ) [keeps only lines which satisfy the provided condition] - can be used
         *      to only keep non-empty lines and drop empty lines - example below removes all the
         *      lines from the Stream which do not equal the string "apple" and returns the Stream:
         *      streamOfLines = streamOfLines.filter(x -> x != "apple");
         * 
         *      3. collect( ) [collects all the lines into a java.util.List object] - can be used in
         *      the function which will invoke this method to convert Stream<String> of lines to
         *      List<String> of lines - example below collects all the elements of the Stream into a
         *      List and returns the List: List<String> listOfLines =
         *      streamOfLines.collect(Collectors::toList);
         * 
         *      Note: since map and filter return the updated Stream objects, they can chained
         *      together as: streamOfLines.map(...).filter(a -> ...).map(...) and so on
         */
        Stream<String> stream = Files.lines(Paths.get(filepath));
        return stream.map(s->s.toUpperCase().trim()).filter(str -> !str.isEmpty() && !(str==null));
    }

    /**
     * Adjacency between word1 and word2 is defined by: if the difference between word1 and word2 is
     * of 1 char replacement 1 char addition 1 char deletion then word1 and word2 are adjacent else
     * word1 and word2 are not adjacent
     * 
     * Note: if word1 is equal to word2, they are not adjacent
     * 
     * 1. Algorithm finds how many characters at the front of each string are the same
     *    Stores result in forwardIndex. For example, "ABCD" and "ABXZ" have a forwardIndex of 2
     * 2. Does the same thing but works back to front. 
     *    Ex. "SOLO" and "DORITO" have a backward index of 1
     * 3. If the words are adjacent, the sum of forward and backward index equals 1 less than 
     *    the length of the longer word.
     *    Ex. "KATE" and "KITE". Fwd index: 1 Bwd index: 2 Sum: 3 Length: 4
     *        "ACT" and "AC"     Fwd index: 2. Bwd index: 0 Sum: 2 Length 3
     * 
     * @param word1 first word
     * @param word2 second word
     * @return true if word1 and word2 are adjacent else false
     */
    public static boolean isAdjacent(String word1, String word2) {
        if(word1.equals(word2) || Math.abs(word1.length() - word2.length()) > 1)
            return false;
        int forwardIndex = 0;
        String word1part = ""; // The parts of the word to be tested for equality
        String word2part = "";
        int minLength = Math.min(word1.length(), word2.length());
        //Steps forward through the loop to find point where strings differ
        while (word1part.equals(word2part) && forwardIndex < minLength) {
            word1part = word1.substring(0, forwardIndex+1);
            word2part = word2.substring(0, forwardIndex+1);
            if(word1part.equals(word2part))
                forwardIndex++;
        }
        int backwardIndex = 0;
        word1part = "";
        word2part = "";
        //Steps backwards through loop to find point where strings differ
        while (word1part.equals(word2part) && backwardIndex < minLength) {
            word1part = word1.substring(word1.length() - backwardIndex-1);
            word2part = word2.substring(word2.length() - backwardIndex-1);
            //TODO: Restructure loops so it's less clunky. Maybe somehow remove these if statements
            if(word1part.equals(word2part))
                backwardIndex++;
        }
        return backwardIndex + forwardIndex >= Math.max(word1.length(), word2.length())-1;
    }
}
