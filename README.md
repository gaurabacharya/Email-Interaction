# Email-Interaction
Uses an adjacency matrix graph representation in Java to analyze email interactions

## Reason for Building:
To develop a system to analyze email interaction of user within organizations. Using datasets from Stanford (https://snap.stanford.edu/data/email-Eu-core-temporal.html) which includes sender User ID, receiver User ID, and send time of the emails in ".txt" files. 

## Description of Classes:

### InteractionGraph Interface: 
* To have the specifications of basic graph functions that could be added to other graph classes if needed 

### Vertex: 
* To create a Vertex object that includes the label and content so that the vertex to be added to a graph

### Edge:
* Creates direction from one vertex to another needed for a directed graph 

### DWInteractionGraph 
* Represents a directed weighted graph of user email interactions from one user to another within a specific time window.
* Collects basic metrics such as activity within a time window, report on specific user, and checks for most active user 
* Reports more advanced metrics using search algorithms such as Breadth-First-Search (BFS) and Depth-First-Search (DFS) and will return the path from one user to another if it exists
