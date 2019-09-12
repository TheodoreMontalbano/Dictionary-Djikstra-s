import java.util.ArrayList;

public class Vertex {
    /**
     * Name of the vertice/dictionary word it contains
     */
    private String name;
    /**
     * the number edges that needed to be traversed to reach this vertice
     */
    private int edgesTraversed;
    /**
     * has this vertice been visited during traversal
     */
    private boolean visited;
    /**
     * the predecessor of this vertice
     */
    private Vertex predecessor;

    /**
     * constructor for vertice class initializing all fields
     * 
     * @param name
     * @param edgesTraversed
     * @param predecessor
     */
    public Vertex(String name, int edgesTraversed, Vertex predecessor) {
        this.name = name;
        this.edgesTraversed = edgesTraversed;
        visited = false;
        this.predecessor = predecessor;
    }

    public void setEdges(int numEdges) {
        edgesTraversed = numEdges;
    }

    public int getEdgeNum() {
        return edgesTraversed;
    }

    /**
     * Sets visited to true for this vertice
     */
    public void visit() {
        visited = true;
    }

    public void notVisited() {
        visited = false;
    }

    public boolean hasBeenVisited() {
        return visited;
    }

    public String getName() {
        return name;
    }

    public Vertex getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(Vertex p) {
        predecessor = p;
    }

    /**
     * Get a list of all the predecessors' names and returns it BACKWARDS
     * 
     * @return - list of predecessors names
     */
    public static ArrayList<String> getPredecessorlist(Vertex a) {

        ArrayList<String> pList = new ArrayList<String>();
        pList.add(a.getName());
        if (a.predecessor != null)
            pList.addAll(getPredecessorlist(a.predecessor));
        return pList;
    }

    /**
     * tests if two vertices have the same name if so return true
     * 
     * @param other
     * @return - true if both vertices have the same name
     */
    public boolean hasSameNameAs(Vertex other) {
        return name.equals(other.getName());
    }

    public String toString() {
        return name;
    }

    public int hashCode() {
        return name.hashCode();
    }
}
