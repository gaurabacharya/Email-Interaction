package gacharya.email;

public class Edge {
    public final Vertex v1;
    public final Vertex v2;
    public final int timeStamp;
    /*
        RI: v1 is base vertex where or starting node, v2 is vertex where edge is going to or
            end node, TimeStamp is the time in seconds (Timestamp >= 0) that v1 and v2 interacted.
        AF: Represented an Edge that is directed from v1 to v2
    */

    /**
     * Create an Edge that is directed from v1 to v2
     *
     * @param v1        starting vertex
     * @param v2        end vertex
     * @param timeStamp
     */
    public Edge(Vertex v1, Vertex v2, int timeStamp) {
        this.v1 = v1;
        this.v2 = v2;
        this.timeStamp = timeStamp;
    }

    /**
     * Check equality of edges. This method overrides equals( ) in Object.
     *
     * @return true if this edge is equal to the obj otherwise return false
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Edge)) {
            return false;
        }
        Edge other = (Edge) o;
        return v1.equals(other.v1) && v2.equals(other.v2);
    }
}
