import java.util.ArrayList;
import java.util.List;
import java.awt.geom.Point2D;
public class search {

    /*
     * Performs a nearestNeighbourFirst search
     */ 
    public static void nearestNeighbourFirst(Graph g) {
        List<Point2D> visited = new ArrayList<Point2D>(); // list of visited nodes (visited)
        List<Point2D> path = new ArrayList<Point2D>(); // list of visited nodes (visited)

        boolean goalFound = false;
        int index = 0;
        double minDistance;

        while (!goalFound) {
            minDistance = Double.MAX_VALUE;
            Point2D cur = g.nodes.get(index);
            path.add(cur);
            visited.add(cur);

            if (visited.size() == g.nodes.size()){ 
                goalFound = true;
                continue;
            }

            for (Point2D K : g.nodes) {
                if (!visited.contains(K)) {
                    if (cur.distanceSq(K) < minDistance) {
                        index = g.nodes.indexOf(K);
                        minDistance = cur.distanceSq(K);
                    }
                }
            }
        }

        for (Point2D K : visited) {
            System.out.println("(" + K.getX() + ";" + K.getY() + ")");
        }
    }

    
}
