import java.util.LinkedList;

public class Node implements Comparable<Node>{
    private LinkedList<Node> adjacencyList;
    String name;

    /**
     * Constructor for node.
     */
    public Node(String name) {
        this.name = name;
        this.adjacencyList = new LinkedList<>();
    }

    /**
     * Takes in a node and adds it to the adjacency list of this node, unless it's already there.
     * @param neighbor node being added to the adjacency list of this node
     * @return boolean representing whether or not the task was completed successfully (primarily for
     * debugging)
     */
    boolean addNeighbor(Node neighbor) {
        if (!this.adjacencyList.contains(neighbor)) {
            this.adjacencyList.add(neighbor);
            return true;
        }
        return false;
    }

    /**
     * Getter for the adjacency list.
     * @return adjacency list of node
     */
    LinkedList<Node> getAdjacencyList() {
        return this.adjacencyList;
    }

    @Override
    /**
     * Implemented so that I can use the Collections interface.
     *
     * @param other Node being compared to this node
     * @return int 1 if the nodes are equal, 0 else
     */
    public int compareTo(Node other){
        if (this.name.equals(other.name)) {
            return 1;
        }
        return 0;
    }
}
