package gacharya.email;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class DWInteractionGraph  implements InteractionGraph {

    private final ArrayList<Vertex> vertices = new ArrayList<>();
    private final ArrayList<Edge> edges = new ArrayList<>();
    private int[][] representation = new int[1][1];
    private int matSize = 1;

    /*
       RI: Graph is an adjacency matrix, with vertices and edges, where each vertices is a point
       and each edge connects two points together from sender to receiver. No two vertices can contain the
       same data, and no two edges are equivalent.
       masSize >= 1 and representation[][] is square matrix.

       AF: The graph represents the email interaction between two users and the time in which these
       interaction took place.
     */

    /**
     * Creates a new DWInteractionGraph using an email interaction file.
     * The email interaction file will be in the resources directory.
     *
     * @param fileName the name of the file in the resources
     *                 directory containing email interactions
     */
    public DWInteractionGraph(String fileName) {
        int sender = 0;
        int receiver = 1;
        int timeStamp = 2;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                List<String> dataLine = Arrays.asList(currentLine.split(" ").clone());

                Vertex v1 =
                    new Vertex(dataLine.get(sender), Integer.parseInt(dataLine.get(sender)));
                Vertex v2 =
                    new Vertex(dataLine.get(receiver), Integer.parseInt(dataLine.get(receiver)));

                addToGraph(v1, v2, Integer.parseInt(dataLine.get(timeStamp)));
            }
            reader.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates a new DWInteractionGraph from a DWInteractionGraph object
     * and considering a time window filter.
     *
     * @param inputDWIG  a DWInteractionGraph object
     * @param timeFilter an integer array of length 2: [t0, t1]
     *                   where t0 <= t1. The created DWInteractionGraph
     *                   should only include those emails in the input
     *                   DWInteractionGraph with send time t in the
     *                   t0 <= t <= t1 range.
     */
    public DWInteractionGraph(DWInteractionGraph inputDWIG, int[] timeFilter) {

        for (Edge inputEdge : inputDWIG.edges) {
            if (withinTimeFilter(inputEdge, timeFilter)) {
                addToGraph(inputEdge.v1, inputEdge.v2, inputEdge.timeStamp);
            }
        }
    }

    /**
     * Helper Method for DWInteractionGraph
     *
     * @param edge       time to check against the timeFilter
     * @param timeFilter an integer array of length 2: [t0, t1]
     *                   where t0 <= t1. The created DWInteractionGraph
     *                   should only include those emails in the input
     *                   DWInteractionGraph with send time t in the
     *                   t0 <= t <= t1 range.
     * @return True if t0 <= inputTime <= t1 range, false otherwise
     */
    private boolean withinTimeFilter(Edge edge, int[] timeFilter) {
        return edge.timeStamp <= timeFilter[1] && edge.timeStamp >= timeFilter[0];
    }


    /**
     * Creates a new DWInteractionGraph from a DWInteractionGraph object
     * and considering a list of User IDs.
     *
     * @param inputDWIG  a DWInteractionGraph object
     * @param userFilter a List of User IDs. The created DWInteractionGraph
     *                   should exclude those emails in the input
     *                   DWInteractionGraph for which neither the sender
     *                   nor the receiver exist in userFilter.
     */
    public DWInteractionGraph(DWInteractionGraph inputDWIG, List<Integer> userFilter) {
        for (Edge inputEdge : inputDWIG.edges) {
            if (withinUserFilter(inputEdge, userFilter)) {
                addToGraph(inputEdge.v1, inputEdge.v2, inputEdge.timeStamp);
            }
        }
    }

    /**
     * Creates a new DWInteractionGraph using an email interaction file
     * and considering a time window filter.
     * The email interaction file will be in the resources directory.
     *
     * @param fileName the name of the file in the resources
     *                 directory containing email interactions
     * @param timeFilter an integer array of length 2: [t0, t1]
     *                   where t0 <= t1. The created DWInteractionGraph
     *                   should only include those emails in the input
     *                   DWInteractionGraph with send time t in the
     *                   t0 <= t <= t1 range.
     */
    public DWInteractionGraph(String fileName, int[] timeFilter) {
        int sender = 0;
        int receiver = 1;
        int timeStamp = 2;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                List<String> dataLine = Arrays.asList(currentLine.split(" ").clone());

                if (Integer.parseInt(dataLine.get(timeStamp)) >= timeFilter[0]
                    && Integer.parseInt(dataLine.get(timeStamp)) <= timeFilter[1]) {

                    Vertex v1 =
                        new Vertex(dataLine.get(sender), Integer.parseInt(dataLine.get(sender)));
                    Vertex v2 =
                        new Vertex(dataLine.get(receiver), Integer.parseInt(dataLine.get(receiver)));

                    addToGraph(v1, v2, Integer.parseInt(dataLine.get(timeStamp)));
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Helper method for DWInteractionGraph
     *
     * @param inputEdge  Edge object to check
     * @param userFilter a List of User IDs. The created DWInteractionGraph
     *                   should exclude those emails in the input
     *                   DWInteractionGraph for which neither the sender
     *                   nor the receiver exist in userFilter.
     * @return True if user is contained in userFiler, false otherwise
     */
    private boolean withinUserFilter(Edge inputEdge, List<Integer> userFilter) {
        return userFilter.contains(inputEdge.v1.getContent())
            || userFilter.contains(inputEdge.v2.getContent());
    }

    /**
     * @return a Set of Integers, where every element in the set is a User ID
     * in this DWInteractionGraph.
     */
    public Set<Integer> getUserIDs() {
        Set<Integer> userIDs = new HashSet<>();
        this.vertices.stream().map(Vertex::getContent).forEach(userIDs::add);
        return userIDs;
    }

    /**
     * @param sender   the User ID of the sender in the email transaction.
     * @param receiver the User ID of the receiver in the email transaction.
     * @return the number of emails sent from the specified sender to the specified
     * receiver in this DWInteractionGraph.
     */
    public int getEmailCount(int sender, int receiver) {
        return representation[findUserIDInRepresentation(sender)][findUserIDInRepresentation(
            receiver)];
    }


    /**
     * Given an int array, [t0, t1], reports email transaction details.
     * Suppose an email in this graph is sent at time t, then all emails
     * sent where t0 <= t <= t1 are included in this report.
     *
     * @param timeWindow is an int array of size 2 [t0, t1] where t0<=t1.
     * @return an int array of length 3, with the following structure:
     * [NumberOfSenders, NumberOfReceivers, NumberOfEmailTransactions]
     */
    public int[] ReportActivityInTimeWindow(int[] timeWindow) {
        return new int[] {getSenders(timeWindow).size()
            , getReceivers(timeWindow).size()
            , getNumInteractions(timeWindow)};
    }

    /**
     * Given an int array, [t0, t1], reports the number of emails sent in time frame;
     * Suppose an email in this graph is sent at time t, then all emails
     * sent where t0 <= t <= t1 are included the count.
     *
     * @param timeWindow is an int array of size 2 [t0, t1] where t0<=t1.
     * @return the number of email interactions
     */
    private int getNumInteractions(int[] timeWindow) {
        int count = 0;

        for (Edge currentEdge : this.edges) {
            if (withinTimeFilter(currentEdge, timeWindow)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Gets the userIDs of all the senders, if a time window is given, then only the ID's
     * of users sent within that time frame are returned
     *
     * @param timeWindow timeframe given in int[t0][t1] S.T t0 < t1
     * @return Set</ Integers> of senders
     */
    private Set<Integer> getSenders(int[] timeWindow) {
        return getIntegers(timeWindow, "s");
    }

    /**
     * Gets the userIDs of all the senders, if a time window is given, then only the ID's
     * of users sent within that time frame are returned
     *
     * @return Set</ Integers> of senders
     */
    private Set<Integer> getSenders() {
        return getIntegers(new int[] {0, Integer.MAX_VALUE}, "s");
    }

    /**
     * Gets the userIDs of all the receivers, if a time window is given, then only the ID's
     * of users sent within that time frame are returned
     *
     * @param timeWindow timeframe given in int[t0][t1] S.T t0 < t1
     * @return Set</ Integers> of receivers
     */
    private Set<Integer> getReceivers(int[] timeWindow) {
        return getIntegers(timeWindow, "r");
    }

    /**
     * Gets the userIDs of all the receivers, if a time window is given, then only the ID's
     * of users sent within that time frame are returned
     *
     * @return Set</ Integers> of receivers
     */
    private Set<Integer> getReceivers() {
        return getIntegers(new int[] {0, Integer.MAX_VALUE}, "r");
    }

    /**
     * Gets the userIDs of of the email interactions within the specified time window
     *
     * @param timeWindow    timeframe given in int[t0][t1] S.T t0 < t1
     * @param sendORReceive "r" for receivers and "s" for senders
     * @return Set</ Integers> of receivers or senders
     */
    private Set<Integer> getIntegers(int[] timeWindow, String sendORReceive) {
        Set<Integer> validIDS = new HashSet<>();

        for (Edge currentEdge : this.edges) {
            int senderID = currentEdge.v1.getContent();
            int receiverID = currentEdge.v2.getContent();
            boolean withinTime = withinTimeFilter(currentEdge, timeWindow);

            if (withinTime && sendORReceive.equals("s")) {
                validIDS.add(senderID);
            }
            if (withinTime && sendORReceive.equals("r")) {
                validIDS.add(receiverID);
            }
        }
        return validIDS;
    }


    /**
     * Given a User ID, reports the specified User's email transaction history.
     *
     * @param userID the User ID of the user for which the report will be
     *               created.
     * @return an int array of length 3 with the following structure:
     * [NumberOfEmailsSent, NumberOfEmailsReceived, UniqueUsersInteractedWith]
     * If the specified User ID does not exist in this instance of a graph,
     * returns [0, 0, 0].
     */
    public int[] ReportOnUser(int userID) {

        if (!(getSenders().contains(userID)) && !(getReceivers().contains(userID))) {
            return new int[] {0, 0, 0};
        }

        int currentUserID = findUserIDInRepresentation(userID);

        int NumberOfEmailsSent = 0;
        int NumberOfEmailsReceived = 0;
        int UniqueUsersInteractedWith = 0;

        for (int i = 0; i < representation.length; i++) {

            NumberOfEmailsSent += representation[currentUserID][i];

            NumberOfEmailsReceived += representation[i][currentUserID];

            if (!(representation[currentUserID][i] == 0
                && representation[i][currentUserID] == 0)) {
                UniqueUsersInteractedWith++;
            }
        }

        return new int[] {NumberOfEmailsSent, NumberOfEmailsReceived, UniqueUsersInteractedWith};
    }

    /**
     * Helper Function to find the matching index in representation[][] to the given user ID
     * from the data source
     *
     * @param userID ID of the user to find
     * @return int the matches the user in representation[][]
     */
    private int findUserIDInRepresentation(int userID) {
        return this.vertices.indexOf(new Vertex(Integer.toString(userID), userID));
    }

    /**
     * @param N               a positive number representing rank. N=1 means the most active.
     * @param interactionType Represent the type of interaction to calculate the rank for
     *                        Can be SendOrReceive.Send or SendOrReceive.RECEIVE
     * @return the User ID for the Nth most active user in specified interaction type.
     * Sorts User IDs by their number of sent or received emails first. In the case of a
     * tie, secondarily sorts the tied User IDs in ascending order.
     */
    public int NthMostActiveUser(int N, SendOrReceive interactionType) {

        Map<Integer, Integer> rankedMap = new HashMap<>();

        Set<Integer> interactions;

        boolean send = false;

        if (interactionType == SendOrReceive.SEND) {
            interactions = getSenders();
            send = true;
        } else {
            interactions = getReceivers();
        }

        for (Integer currentUser : interactions) {
            if (send) {
                rankedMap.put(currentUser, ReportOnUser(currentUser)[0]);
            } else {
                rankedMap.put(currentUser, ReportOnUser(currentUser)[1]);
            }
        }

        if (rankedMap.size() < N) {
            return -1;
        }

        List<Integer> sortedValues = new ArrayList<>(rankedMap.values());
        sortedValues.sort(Collections.reverseOrder());

        int NthUserValue = sortedValues.get(N - 1);

        Map<Integer, Integer> mostActive = new HashMap<>();

        for (Map.Entry<Integer, Integer> entry : rankedMap.entrySet()) {
            if (entry.getValue() == NthUserValue) {
                mostActive.put(entry.getKey(), entry.getValue());
            }
        }

        List<Integer> mostActiveUserIDs = new ArrayList<>(mostActive.keySet());
        Collections.sort(mostActiveUserIDs);

        if (mostActiveUserIDs.size() == 1) {
            return mostActiveUserIDs.get(0);
        }

        return mostActiveUserIDs.get(N - 1);
    }

    /* ------- Task 3 ------- */

    /**
     * performs breadth first search on the DWInteractionGraph object
     * to check path between user with userID1 and user with userID2.
     *
     * @param userID1 the user ID for the first user
     * @param userID2 the user ID for the second user
     * @return if a path exists, returns aa list of user IDs
     * in the order encountered in the search.
     * if no path exists, should return null.
     */
    public List<Integer> BFS(int userID1, int userID2) {
        int NOT_FOUND = -1;
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[vertices.size()];
        visited[findUserIDInRepresentation(userID1)] = true;
        queue.add(findUserIDInRepresentation(userID1));
        List<Integer> path = new ArrayList<Integer>();
        Integer[] previous = new Integer[vertices.size()];

        while (!queue.isEmpty()) {
            int current_user = queue.poll();
            int userId = vertices.get(current_user).getContent();
            if (!path.contains(userId)) {
                path.add(userId);
            }

            int unvisitedNeighbourIndex;
            List<Integer> nextLayer = new ArrayList<>();

            while ((unvisitedNeighbourIndex = findUnvisitedNeighbour(current_user, visited)) != NOT_FOUND) {
                queue.add(unvisitedNeighbourIndex);
                nextLayer.add(vertices.get(unvisitedNeighbourIndex).getContent());
                visited[unvisitedNeighbourIndex] = true;
                previous[unvisitedNeighbourIndex] = current_user;
            }
            Collections.sort(nextLayer);

            for (int user : nextLayer) {
                if (user == userID2) {
                    path.add(user);
                    break;
                }
                if (!path.contains(user)) {
                    path.add(user);
                }
            }

            if (path.contains(userID2)) {
                break;
            }
        }

        boolean isPath = isPath(findUserIDInRepresentation(userID1),
            findUserIDInRepresentation(userID2), previous);
        if (!isPath) {
            return null;
        }

        return path;
    }

    /**
     * Checks if there is a  path that exists from the endNode to the startNode
     *
     * @param startNode int index of the base node of path in this.vertices
     * @param endNode   int index of the end node of the path in this.vertices
     * @param previous  Integer list of which the index of a node refers to the parent node
     * @return true if there is a path from the endNode to the startNode, and false otherwise
     */
    private boolean isPath(int startNode, int endNode, Integer[] previous) {
        List<Integer> path = new ArrayList<Integer>();

        for (Integer i = endNode; i != null; i = previous[i]) {
            path.add(i);
        }
        Collections.reverse(path);

        return path.get(0) == startNode;
    }


    /**
     * Find a neighbouring vertex that has not been visited from a given vertex
     *
     * @param indexOfVertex   int index of a vertex v in this.vertices
     * @param visitedVertices a boolean array that is true if a vertex has been visited and false
     *                        otherwise. Array is not null.
     * @return int index of a vertex in this.vertices that is a neighbour of the vertex that
     * indexOfVertex refers to. If no neighbours exits returns -1.
     */
    private int findUnvisitedNeighbour(int indexOfVertex, boolean[] visitedVertices) {
        int NOT_FOUND = -1;
        for (int i = 0; i < representation.length; i++) {
            if (representation[indexOfVertex][i] > 0 && !visitedVertices[i]) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * performs depth first search on the DWInteractionGraph object
     * to check path between user with userID1 and user with userID2.
     *
     * @param userID1 the user ID for the first user
     * @param userID2 the user ID for the second user
     * @return if a path exists, returns aa list of user IDs
     * in the order encountered in the search.
     * if no path exists, should return null.
     */
    public List<Integer> DFS(int userID1, int userID2) {
        List<Integer> path = new ArrayList<>();
        boolean[] visitedVertices = new boolean[vertices.size()];
        Stack<Integer> stack = new Stack<>();
        stack.add(userID1);

        while (!stack.isEmpty()) {
            int nodeContent = stack.pop();
            int nodeInRep = findUserIDInRepresentation(nodeContent);
            if (!path.contains(nodeContent)) {
                path.add(nodeContent);
                if (path.contains(userID2)) {
                    break;
                }
            }

            List<Integer> neighbours = new ArrayList<>();
            if (!visitedVertices[nodeInRep]) {
                visitedVertices[nodeInRep] = true;
                for (int i = 0; i < representation.length; i++) {
                    if (representation[nodeInRep][i] > 0 && !visitedVertices[i]) {
                        int neighbourContent = vertices.get(i).getContent();
                        neighbours.add(neighbourContent);
                    }
                }
            }
            Collections.sort(neighbours, Collections.reverseOrder());
            stack.addAll(neighbours);
        }

        if (path.get(path.size()-1) != userID2) {
            return null;
        }

        return path;
    }

    @Override
    public void addVertex(Vertex v) {
        if (!vertices.contains(v)) {
            this.vertices.add(v);
            if (vertices.size() <= matSize) {
                updateRep();
            }
        }

    }

    @Override
    public void addEdge(Vertex v1, Vertex v2, int timeStamp) {
        int v1Index = this.vertices.indexOf(v1);
        int v2Index = this.vertices.indexOf(v2);

        if (edgeExists(v1, v2, timeStamp)) {
            representation[v1Index][v2Index] += 1;
            this.edges.add(new Edge(v1, v2, timeStamp));
        } else {
            this.edges.add(new Edge(v1, v2, timeStamp));
            representation[v1Index][v2Index] = 1;
        }

    }

    @Override
    public boolean edgeExists(Vertex v1, Vertex v2, int timeStamp) {
        return edges.contains(new Edge(v1, v2, timeStamp));
    }

    private void updateRep() {
        int newLength = representation.length + 1;
        int[][] tempRep = new int[newLength][newLength];
        for (int i = 0; i < representation.length; i++) {
            System.arraycopy(representation[i], 0, tempRep[i], 0, representation.length);
        }
        representation = tempRep;
        matSize = newLength;
    }

    /**
     * Adds a new entry to the graph
     *
     * @param sender    vertex object of the sender
     * @param receiver  vertex object of the receiver
     * @param timeStamp timestamp of the email interaction
     */
    private void addToGraph(Vertex sender, Vertex receiver, int timeStamp) {
        addVertex(sender);
        addVertex(receiver);
        addEdge(sender, receiver, timeStamp);
    }

}
