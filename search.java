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

            if (visited.size() == g.nodes.size()) {
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
        for (Point2D K : path) {
            System.out.println("(" + K.getX() + ";" + K.getY() + ")");
        }
    }

    /*
     * Calculates the angle of AB to C
     */
    private static double dir(Point2D a, Point2D b, Point2D c) {
        return ((c.getX() - a.getX()) * (b.getY() - a.getY()) - (b.getX() - a.getX()) * (c.getY() - a.getY()));
    }

    private static boolean segmentsIntersect(Point2D p1, Point2D p2, Point2D p3, Point2D p4) {
        double dir1 = dir(p3, p4, p1);
        double dir2 = dir(p3, p4, p2);
        double dir3 = dir(p1, p2, p3);
        double dir4 = dir(p1, p2, p4);

        if (((dir1 > 0 && dir2 < 0) || (dir1 < 0 && dir2 > 0)) && ((dir3 > 0 && dir4 < 0) || (dir3 < 0 && dir4 > 0)))
            return true;
        else if ((dir1 == 0) && inBox(p3, p4, p1))
            return true;
        else if ((dir2 == 0) && inBox(p3, p4, p2))
            return true;
        else if ((dir3 == 0) && inBox(p1, p2, p3))
            return true;
        else if ((dir4 == 0) && inBox(p1, p2, p4))
            return true;
        else
            return false;
    }

    private static boolean inBox(Point2D a, Point2D b, Point2D c) {
        if (((Math.min(a.getX(), b.getX())) <= c.getX() && (c.getX() <= Math.max(a.getX(), b.getX())))
                && ((Math.min(a.getY(), b.getY()) < c.getY()) && (c.getY() <= Math.max(a.getY(), a.getY()))))
            return true;
        else
            return false;
    }

    public static void printList(List<Point2D> a) {
        for (Point2D K : a) {
            System.out.println("(" + K.getX() + ";" + K.getY() + ")");
        }
    }

    /*
     * 2-Exchange
     */
    public static List<Point2D> twoExchange(Graph g) {
        List<Point2D> intersections = new ArrayList<Point2D>(); // list of path nodes

        for (int i = 1; i < g.nodes.size(); i++) {
            Point2D a = g.nodes.get(i - 1);
            Point2D b = g.nodes.get(i);

            for (int j = 1; j <= g.nodes.size(); j++) {

                int q = j;
                int w = q - 1;
                if (j == g.nodes.size()) {
                    q = w;
                    w = 0;
                }

                Point2D c = g.nodes.get(w);
                Point2D d = g.nodes.get(q);

                if ((i != q) && (w != i) && (i - 1 != q) && (i - 1 != w)) {

                    if (segmentsIntersect(a, b, c, d)) {

                        intersections.add(a);
                        intersections.add(b);
                        intersections.add(c);
                        intersections.add(d);
                    }
                }
            }
        }
        return intersections;
    }

}