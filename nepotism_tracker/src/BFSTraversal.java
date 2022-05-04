import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeMap;

public class BFSTraversal {

    private TreeMap<String, Node> nodeMap;
    private final LinkedList<Node> nodeList;
    private int numConnectedComps;

    /**
     * Constructor for the class.
     */

    public BFSTraversal(TreeMap<String, HashSet<String>> adjLists) {
        nodeMap = new TreeMap<>();
        nodeList = new LinkedList<>();
        loadData(adjLists);
        numConnectedComps = 0;
    }

    public TreeMap<String, Node> getNodeMap() {
        return nodeMap;
    }

    public LinkedList<Node> getNodeList() {
        return nodeList;
    }

    public int getNumConnectedComps() {
        return numConnectedComps;
    }

    public int getConnectedCompSize(Node source) {
        int counter = 0;
        LinkedList<Node> nodesLeft = nodeList;
        TreeMap<String, Integer> bfsOutput = breadthFirstSearch(source, nodesLeft);

        for (Integer i : bfsOutput.values()) {
            if (i >= 0) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Loads data from the given TreeMap of adjacency lists to create nodes and adjacency lists.
     */
    private void loadData(TreeMap<String, HashSet<String>> adjLists) {
        for (String actor : adjLists.keySet()) {
            Node curr;
            if (nodeMap.containsKey(actor)) {
                curr = nodeMap.get(actor);
            } else {
                curr = new Node(actor);
                nodeMap.put(actor, curr);
                nodeList.add(curr);
            }

            for (String link : adjLists.get(actor)) {
                Node neighbor;
                if (nodeMap.containsKey(link)) {
                    neighbor = nodeMap.get(link);
                } else {
                    neighbor = new Node(link);
                    nodeMap.put(link, neighbor);
                    nodeList.add(neighbor);
                }
                curr.addNeighbor(neighbor);
                neighbor.addNeighbor(curr);
            }
        }

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //BFS IMPLEMENTATION
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * BFS main method.
     *
     * @param source starting node for BFS
     * @return TreeMap<String, Integer> TreeMap storing the node name as the key and the associated
     * frontier (based on the source node) as the value
     */
    public TreeMap<String, Integer> breadthFirstSearch(Node source, LinkedList<Node> nodesLeft) {
        LinkedList<Node> queue = new LinkedList<>();
        LinkedList<Node> discovered = new LinkedList<>();

        TreeMap<String, Integer> output = new TreeMap<>();
        helperBFS(source, discovered, queue, nodesLeft, output, 0);

        return output;
    }


    /**
     * Helper function for BFS.
     *
     * @param source     starting node for BFS
     * @param discovered the nodes that have been discovered
     * @param queue      the nodes that are in the queue
     * @param nodesLeft  the nodes that have not been looked at yet
     * @param output     the output TreeMap that is being updated as we go along
     * @param frontier   the current frontier that we are on
     */
    private void helperBFS(Node source, LinkedList<Node> discovered, LinkedList<Node> queue,
                           LinkedList<Node> nodesLeft, TreeMap<String, Integer> output, int frontier) {

        discovered.add(source);
        numConnectedComps++;
        output.put(source.name, frontier);
        if (frontier != -1) {
            frontier++;
        }
        nodesLeft.remove(source);

        checkNeighborsBFS(source, discovered, queue, output, nodesLeft);

        while (!queue.isEmpty()) {
            checkNeighborsBFS(queue.pollFirst(), discovered, queue, output, nodesLeft);
        }

        if (!nodesLeft.isEmpty()) {
            helperBFS(nodesLeft.getFirst(), discovered, queue, nodesLeft, output, -1);
        }
    }

    /**
     * Helper function for BFS. Checks the neighbors of a source node and updates the discovered list
     * and queue accordingly. Also updates the output list based on the frontiers of these neighbors.
     *
     * @param source     starting node for BFS whose neighbors we are looking at
     * @param discovered the nodes that have been discovered
     * @param queue      the nodes that are in the queue
     * @param output     the output TreeMap that is being updated as we go along
     * @param nodesLeft  the remaining nodes that have not been looked at
     */
    private void checkNeighborsBFS(Node source, LinkedList<Node> discovered,
                                   LinkedList<Node> queue, TreeMap<String, Integer> output, LinkedList<Node> nodesLeft) {
        LinkedList<Node> adjList = source.getAdjacencyList();
        for (Node n : adjList) {
            if (!discovered.contains(n)) {
                queue.add(n);
                discovered.add(n);
                nodesLeft.remove(n);

                int thisFrontier = output.get(source.name);
                int newFrontier = -1;
                if (thisFrontier != -1) {
                    newFrontier = thisFrontier + 1;
                }
                output.put(n.name, newFrontier);
            }
        }
    }

    TreeMap<String, Integer> findNumComponents(LinkedList<Node> nodes) {
        TreeMap<String, Integer> numComps = new TreeMap<>();
        LinkedList<Node> nodesCopy = new LinkedList<>();
        nodesCopy.addAll(nodes);
        for (Node n : nodesCopy) {
            numComps.put(n.name, getConnectedCompSize(n));
        }
        return numComps;
    }

}