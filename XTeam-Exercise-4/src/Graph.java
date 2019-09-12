/////////////////////////////////////////////////////////////////////////////
// Semester: CS400 Spring 2018
// PROJECT: XTeam-Exercise-4
// FILES:Graph.java GraphADT.java GraphTest.java WordProcessor.java
//       GraphProcessor.java
//
// USER: lnashold
//
// Instructor: Deb Deppeler (deppeler@cs.wisc.edu)
// Bugs: no known bugs
//
// 2018 Mar 28, 2018 5:16 PM
//////////////////////////// 80 columns wide //////////////////////////////////
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Undirected and unweighted graph implementation
 * Vertices stored in Hash Table linking Adjacency ArrayLists to each Vertex.
 * @param <E> type of a vertex
 * 
 * @author sapan (sapan@cs.wisc.edu)
 * 
 */
public class Graph<E> implements GraphADT<E> {
    /** Keys are data of vertices, Values are a list of successors */ 
    private HashMap<E,ArrayList<E>> vertices;
    /**
     * Initializes members as empty
     */
    public Graph(){
        vertices = new HashMap<>();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public E addVertex(E vertex) {
        if(vertex == null || isVertexInGraph(vertex))
            return null;
        //Add Vertex, initialize its List of successors
        vertices.put(vertex,new ArrayList<E>());
        return vertex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E removeVertex(E vertex) {
        // Checks Argument Conditions
        if(vertex == null || !vertices.containsKey(vertex))
            return null;
        // Removes all edges with vertex as their destination
        for(E adjacentVertex : vertices.get(vertex))
            vertices.get(adjacentVertex).remove(vertex);
        vertices.remove(vertex);
        return vertex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addEdge(E vertex1, E vertex2) {
        // Checks Argument Conditions
        if(!isVertexInGraph(vertex1) || !isVertexInGraph(vertex2) || vertex1.equals(vertex2) || vertices.get(vertex1).contains(vertex2) ||vertices.get(vertex2).contains(vertex1))
            return false;
        // Add to both lists because its an undirected graph
        vertices.get(vertex1).add(vertex2);
        vertices.get(vertex2).add(vertex1);
        return true;
    }    

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeEdge(E vertex1, E vertex2) {
        // Checks Argument Conditions
        if(!isVertexInGraph(vertex1) || !isVertexInGraph(vertex2) || vertex1.equals(vertex2) || !vertices.get(vertex1).contains(vertex2) || !vertices.get(vertex2).contains(vertex1))
            return false;
        // Removes from adjacency lists attached to vertices
        vertices.get(vertex1).remove(vertex2);
        vertices.get(vertex2).remove(vertex1);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAdjacent(E vertex1, E vertex2) {
        if(!isVertexInGraph(vertex1) || !isVertexInGraph(vertex2))
            return false;
        // Checks if adjancency list of vertex1 contains vertex2
        return vertices.get(vertex1).contains(vertex2);
    }

    /**
     * {@inheritDoc}
     * If vertex doesn't exist, returns empty ArrayList
     */
    @Override
    public Iterable<E> getNeighbors(E vertex) {
        if(!isVertexInGraph(vertex))
            return new  ArrayList<E>();
        return vertices.get(vertex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<E> getAllVertices() {
        // Method returns a Set of all keys in no particular order
        return vertices.keySet();
    }
    
    /**
     * Returns true if vertex is both not null and in the Map storing the vertices
     */
    private boolean isVertexInGraph(E vertex) {
        return vertex != null && vertices.containsKey(vertex);
    }
    
}
