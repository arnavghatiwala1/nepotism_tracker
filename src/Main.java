import java.io.IOException;
import java.util.TreeMap;

public class Main {

    public static void main(String[] args) throws IOException {
        WikipediaScraper wiki = new WikipediaScraper();
        BFSTraversal bfs = new BFSTraversal(wiki.getActorToAdj());
        TreeMap<String, Node> nodeMap = bfs.getNodeMap();

        TreeMap<String, Integer> bfsOutput = bfs.breadthFirstSearch(nodeMap.get("Abhishek Bachchan"), bfs.getNodeList());

        System.out.println(bfsOutput.get("Aishwarya Rai"));
        System.out.println(bfs.getNumConnectedComps());
        System.out.println(bfs.getConnectedCompSize(nodeMap.get("Abhishek Bachchan")));
    }
}
