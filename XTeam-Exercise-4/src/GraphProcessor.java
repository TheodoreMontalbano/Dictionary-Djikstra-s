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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Stream;
import java.util.Iterator;

/**
 * This class adds additional functionality to the graph as a whole.
 * 
 * Contains an instance variable, {@link #graph}, which stores information for all the vertices and
 * edges.
 * 
 * @see #populateGraph(String) - loads a dictionary of words as vertices in the graph. - finds
 *      possible edges between all pairs of vertices and adds these edges in the graph. - returns
 *      number of vertices added as Integer. - every call to this method will add to the existing
 *      graph. - this method needs to be invoked first for other methods on shortest path
 *      computation to work.
 * @see #shortestPathPrecomputation() - applies a shortest path algorithm to precompute data
 *      structures (that store shortest path data) - the shortest path data structures are used
 *      later to to quickly find the shortest path and distance between two vertices. - this method
 *      is called after any call to populateGraph. - It is not called again unless new graph
 *      information is added via populateGraph().
 * @see #getShortestPath(String, String) - returns a list of vertices that constitute the shortest
 *      path between two given vertices, computed using the precomputed data structures computed as
 *      part of {@link #shortestPathPrecomputation()}. - {@link #shortestPathPrecomputation()} must
 *      have been invoked once before invoking this method.
 * @see #getShortestDistance(String, String) - returns distance (number of edges) as an Integer for
 *      the shortest path between two given vertices - this is computed using the precomputed data
 *      structures computed as part of {@link #shortestPathPrecomputation()}. -
 *      {@link #shortestPathPrecomputation()} must have been invoked once before invoking this
 *      method.
 * 
 * @author sapan (sapan@cs.wisc.edu)
 * 
 */
public class GraphProcessor {

    /**
     * Holds the location of the list of shortest paths to a certain vertice
     */
    // ArrayList<String> locationHolder = new ArrayList<String>();
    /**
     * Graph of vertices used in djikstra's shortest path algorithm
     * Used to store data for djikstras, wheras the graph only stores
     * connections between words. 
     */
    public GraphADT<Vertex> verticesGraph = null;
    /**
     * Graph which stores the dictionary words and their associated connections
     */
    public GraphADT<String> graph = null;
    /**
     * Holds the all of the shortest path from each vertice to each other pathList level 1 - the
     * arrayList holding and arrayList of an arrayLists of strings pathList level 2 - the arrayList
     * holding an arrayList of strings pathList level 3 - the arrayList of strings
     */
    private HashMap<String, HashMap<String, ArrayList<String>>> pathLists;

    /**
     * Constructor for this class. Initializes instances variables to set the starting state of the
     * object
     */
    public GraphProcessor() {
        this.graph = new Graph<>();
    }

    /**
     * Builds a graph from the words in a file. Populate an internal graph, by adding words from the
     * dictionary as vertices and finding and adding the corresponding connections (edges) between
     * existing words.
     * 
     * Reads a word from the file and adds it as a vertex to a graph. Repeat for all words, skipping
     * any duplicates.
     * 
     * For all possible pairs of vertices, finds if the pair of vertices is adjacent
     * {@link WordProcessor#isAdjacent(String, String)} If a pair is adjacent, adds an undirected
     * and unweighted edge between the pair of vertices in the graph.
     * 
     * @param filepath file path to the dictionary
     * @return Integer the number of vertices (words) added
     */
    public Integer populateGraph(String filepath) {
        // Creates stream from WordProcessor to get sanitized input
        Stream<String> stream;
        int verticesAdded = 0;
        try {
            stream = WordProcessor.getWordStream(filepath);
        } catch (IOException e) {
            System.out.println("IO Exception Reading File");
            e.printStackTrace();
            return verticesAdded;
        }
        Iterator<String> iterator = stream.iterator();

        while (iterator.hasNext()) {
            String strInserted = graph.addVertex(iterator.next());
            if (strInserted != null) { // Non null if successfully inserted (e.g. not a duplicate)
                verticesAdded++;
                // Finds and adds all necessary edges
                for (String s : graph.getAllVertices()) {
                    if (WordProcessor.isAdjacent(s, strInserted)) {
                        graph.addEdge(s, strInserted);
                    }
                }
            }
        }
        stream.close();
        verticesGraph = null;
        pathLists = null;
        return verticesAdded;
    }


    /**
     * Gets the list of words that create the shortest path between word1 and word2
     * 
     * Example: Given a dictionary, cat rat hat neat wheat kit shortest path between cat and wheat
     * is the following list of words: [cat, hat, heat, wheat]
     * 
     * @param word1 first word
     * @param word2 second word
     * @return List<String> list of the words
     */
    public List<String> getShortestPath(String word1, String word2) {
        word1 = word1.toUpperCase().trim();
        word2 = word2.toUpperCase().trim();
        // Gets the HashMap of all pathLists for word1
        // Then gets the specific pathList between word1 and word2
        // Returned in reverse order
        List<String> pathList = pathLists.get(word1).get(word2);
        // Either word1 equals word2, or there is no path
        if (pathList == null)
            return new ArrayList<String>();
        // The ArrayList gotten from PathLists is reversed, this switches it to the correct order.
        ArrayList<String> reversedList = new ArrayList<String>();
        for (int i = pathList.size() - 1; i >= 0; i--) {
            reversedList.add(pathList.get(i));
        }
        return reversedList;
    }

    /**
     * Gets the distance of the shortest path between word1 and word2
     * 
     * Example: Given a dictionary, cat rat hat neat wheat kit distance of the shortest path between
     * cat and wheat, [cat, hat, heat, wheat] = 3 (the number of edges in the shortest path)
     * 
     * @param word1 first word
     * @param word2 second word
     * @return Integer distance
     */
    public Integer getShortestDistance(String word1, String word2) {
        return getShortestPath(word1, word2).size() - 1;
    }

    /**
     * Computes shortest paths and distances between all possible pairs of vertices. Should be
     * called by user after every set of updates. Calls Djikstras on each element, then stores every
     * resulting path as ArrayLists in the 2D HashMap called pathList
     */
    public void shortestPathPrecomputation() {
        if (verticesGraph != null)
            return; // Precomputation already happened
        // Creates a new graph but with an inner class instead of strings
        // This is necessary to store the information from djikstras
        // We created the graph initially because we were not allowed to override equals
        // in the Vertex class, so we could not effectively check for duplicates
        // However, this is an ineffective approach. It would be better to instantiate the verticesGraph directly. 
        verticesGraph = new Graph<>();
        // Copies all vertices from graph to verticeGraph
        for (String vertice : graph.getAllVertices()) {
            verticesGraph.addVertex(new Vertex(vertice, Integer.MAX_VALUE, null));
        }
        // Copies all edges from graph to vGraph
        for (Vertex a : verticesGraph.getAllVertices()) {
           for (Vertex v : verticesGraph.getAllVertices()) {
                if (graph.isAdjacent(a.getName(), v.getName())) {
                    verticesGraph.addEdge(v, a);
                }
            }
        }
        // Actual code for the method
        pathLists = new HashMap<>();
        // Djikstra's Algorithm

        // How many edges in the shortest path between curVertex and root vertex
        int distanceFromRoot;
        // Queue to hold the vertices. Works because graph is unweighted.
        Queue<Vertex> queue = new LinkedList<Vertex>();
        // Current Vertex that is being iterated over
        Vertex curVertex;
        // rootVertice is the root node of Djikstras where all paths are being calculated from
        for (Vertex rootVertex : verticesGraph.getAllVertices()) {
            pathLists.put(rootVertex.getName(), new HashMap<String, ArrayList<String>>());
            // current number of edges that have been passed over to get to curVertice
            distanceFromRoot = 0;
            // sets predecessor to null as this is the first vertice being iterated over
            queue.add(rootVertex);            
            rootVertex.visit();
            rootVertex.setEdges(0);
            while (!queue.isEmpty()) {
                // Gets the vertex with the shortest path from the queue
                curVertex = queue.remove();
                // sets the current number of edges passed to that required to get to the last
                // vertex + 1, because each edge has a weight of 1
                distanceFromRoot = curVertex.getEdgeNum() + 1;
                /*
                 * For each adjacent unvisited vertex:
                 * If it has not been visited updates the new shortest distance and adds to queue
                 * If shortestDistance can be improved it will update it
                 */
                for (Vertex adjacentVertex : verticesGraph.getNeighbors(curVertex)) {

                    if (!adjacentVertex.hasSameNameAs(curVertex)) {
                        if (!adjacentVertex.hasBeenVisited()) {
                            queue.add(adjacentVertex);
                            adjacentVertex.visit();
                            adjacentVertex.setEdges(distanceFromRoot);
                            adjacentVertex.setPredecessor(curVertex);
                        } else {
                            if (adjacentVertex.getEdgeNum() > distanceFromRoot) {
                                adjacentVertex.setEdges(distanceFromRoot);
                                adjacentVertex.setPredecessor(curVertex);
                            }
                        }
                    }
                }

            }
            // Stores pathList from rootVertice to every other Vertice in a HashTable
            for (Vertex v : verticesGraph.getAllVertices()) {
                // Only add paths that exist
                if (!Vertex.getPredecessorlist(v).get(Vertex.getPredecessorlist(v).size() - 1)
                                .equals(v.getName())) {
                    ArrayList<String> predecessorList = Vertex.getPredecessorlist(v);
                    pathLists.get(rootVertex.getName()).put(v.getName(), predecessorList);
                }
            }
            // Resets verticesGraph to run djikstras again
            for (Vertex v : verticesGraph.getAllVertices()) {
                v.setPredecessor(null);
                v.notVisited();
                v.setEdges(Integer.MAX_VALUE);
            }
            
        }

    }
}
