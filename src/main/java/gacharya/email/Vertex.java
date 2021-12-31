package gacharya.email;

import java.util.Arrays;

public class Vertex {

    private String label;
    private int content;

    private int weight;
    private int[] times;

    /*
       RI: label - is a description of the vertex of a graph
           content - is the object being place as a vertex of a graph
           weight - is an integer representing how many transactions
                    occurred at this vertex
           times - is an array holding all of the times of the interactions
                   at this vertex
       AF: Creates a Vertex object that can be placed on the graph
    */

    /**
     * Create a new Vertex with a label and content
     * @param label to identify the vertex
     * @param content the object placed in the graph
     */
    public Vertex(String label, int content) {
        this.label = label;
        this.content = content;
    }

    /**
     * Get the label of the Vertex
     * @return label associated to this Vertex
     */
    public String getLabel() {
        return label;
    }

    /**
     * Set the label of the Vertex
     * @param label - label to be set for the Vertex
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Get content from the Vertex
     * @return content associated with the Vertex
     */
    public int getContent() {
        return content;
    }

    /**
     * Set content for the Vertex
     * @param content - content to be set for the Vertex
     */
    public void setContent(int content) {
        this.content = content;
    }

    /**
     * Check equality of vertices. This method overrides equals( ) in Object.
     *
     * @return true if this vertex is equal to the obj otherwise return false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Vertex)) {
            return false;
        }
        Vertex other = (Vertex) obj;
        return content == other.content;
    }



    /**
     * Create a new Vertex at a given time with default weight
     * @param time the time for the given vertex
     */
    public Vertex(int time) {
        this.weight = 1;
        this.times = new int[1];
        times[0] = time;
    }

    /**
     * Adds another interaction between the user at the given vertex
     * @param time the time of the new interaction
     */
    public void addTransaction(int time) {
        weight += 1;
        times = Arrays.copyOf(times, times.length + 1);
        times[times.length - 1] = time;
    }

    /**
     * Get the weight of the Vertex
     * @return weight associated with this Vertex
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Get the times of the interactions at this Vertex
     * @return the array of times
     */
    public int[] getTimes() {
        return times;
    }
}
