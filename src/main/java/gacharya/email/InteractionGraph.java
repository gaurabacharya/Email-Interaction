package gacharya.email;

public interface InteractionGraph {

    /**
     * Adds a Vertex object to the Graph
     * @param v is not in current Graph
     */
    public void addVertex(Vertex v);

    /**
     * Adds an Edge from v1 to v2 on the Graph
     * @param v1 is in current graph
     * @param v2 is in current graph
     * @param timestamp is the time in seconds (Timestamp >= 0) that v1 and v2 interacted
     */
    public void addEdge(Vertex v1, Vertex v2, int timestamp);

    /**
     * Checks if an Edge exists from v1 to v2 in current Graph
     * @param v1 is in current graph
     * @param v2 is in current graph
     * @return true iff an edge exists from v1 to v2 and false otherwise
     */
    public boolean edgeExists(Vertex v1, Vertex v2, int timeStamp);

}
