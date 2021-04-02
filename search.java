import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
        g.nodes = path;
    }

    /*
     * Calculates the angle of AB to C
     */
    private static double dir(Point2D a, Point2D b, Point2D c) {
        return ((c.getX() - a.getX()) * (b.getY() - a.getY()) - (b.getX() - a.getX()) * (c.getY() - a.getY()));
    }

    /*
     * Checks if array is collinear
     * https://www.geeksforgeeks.org/program-check-three-points-collinear/
     */
    static boolean collinear(Point2D p1, Point2D p2, Point2D p3) {

        double a = p1.getX() * (p2.getY() - p3.getY()) + p2.getX() * (p3.getY() - p1.getY())
                + p3.getX() * (p1.getY() - p2.getY());

        if (a == 0.0)
            return true;
        else
            return false;
    }

    /*
     * Checks if the segments Intersect https://www.youtube.com/watch?v=R08OY6yDNy0
     */
    private static boolean segmentsIntersect(Point2D p1, Point2D p2, Point2D p3, Point2D p4) {
        double dir1 = dir(p3, p4, p1);
        double dir2 = dir(p3, p4, p2);
        double dir3 = dir(p1, p2, p3);
        double dir4 = dir(p1, p2, p4);

        if (dir1 * dir2 < 0 && dir3 * dir4 < 0)
            return true;
        else if (collinear(p1, p2, p3))
            return true;
        else if (collinear(p1, p2, p4))
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
                && ((Math.min(a.getY(), b.getY()) < c.getY()) && (c.getY() <= Math.max(a.getY(), b.getY()))))
            return true;
        else
            return false;
    }

    /*
     * Prints all the points from a given list
     */
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

    /*
     * REVERT O ARRAY CASO ESPECIAL
     */
    private static void swap(Graph g, Point2D a, Point2D b, Point2D c, Point2D d) {
        int i = g.nodes.indexOf(b);
        int j = g.nodes.indexOf(c);
        Point2D tmp1;
        Point2D tmp2;
        while (i < j) {
            tmp1 = g.nodes.get(i);
            tmp2 = g.nodes.get(j);
            g.nodes.remove(i);
            g.nodes.add(i, tmp2);
            g.nodes.remove(j);
            g.nodes.add(j, tmp1);
            i++;
            j--;
        }
    }

    /*
     * Returns index of son with less perimeter
     */
    private static int byPerimeter(Graph g, List<Graph> visited) {
        // Calculates perimeter
        int maxPerimeter = Integer.MAX_VALUE;
        int curPerimeter = 0;
        int indexNext = -1;
        for (Graph a : g.sons) {
            if (!alreadyExists(a, visited)) {
                curPerimeter = a.perimeter();
                if (curPerimeter < maxPerimeter) {
                    maxPerimeter = curPerimeter;
                    indexNext = g.sons.indexOf(a);
                }
            }
        }
        return indexNext;
    }

    /*
     * Returns index of first son
     */
    private static int firstSon(Graph g, List<Graph> visited) {
        int indexNext = 0;
        if (g.sons.size() > 0) {
            if (alreadyExists(g.sons.get(indexNext), visited)) {
                indexNext = -1;
            }
        }
        return indexNext;
    }

    /*
     * Returns index of son with less conflicts (by two exchange)
     */
    private static int lessConflits(Graph g, List<Graph> visited) {
        int minConflit = Integer.MAX_VALUE;
        int curOperation = 0;
        int indexNext = -1;
        for (Graph a : g.sons) {
            if (!alreadyExists(a, visited)) {
                List<Point2D> tmp; // list of intersections of the possible son
                tmp = twoExchange(a);
                curOperation = tmp.size();
                if (curOperation < minConflit) {
                    minConflit = curOperation;
                    indexNext = g.sons.indexOf(a);
                }
            }
        }
        return indexNext;
    }

    /*
     * Picks randomly the next son
     */
    private static int randomSon(Graph g, List<Graph> visited) {
        Random random = new Random();
        int indexNext = random.nextInt(g.sons.size());
        return indexNext;
    }

    private static void generateSons(Graph g) {
        List<Point2D> intersection; // list of intersections
        intersection = twoExchange(g);

        // Create possible sons
        while (intersection.size() > 0) {
            Point2D d = intersection.remove(3);
            Point2D c = intersection.remove(2);
            Point2D b = intersection.remove(1);
            Point2D a = intersection.remove(0);

            Graph tmp = new Graph(g);

            swap(tmp, a, b, c, d);
            g.sons.add(tmp);
        }
    }

    private static boolean alreadyExists(Graph cur, List<Graph> visited) {
        for (Graph tmp : visited) {
            if (cur.equals(tmp))
                return true;
        }
        return false;
    }

    /*
     * Hill Climbing implementation (Not working properly)
     */
    // receives a function returns the next node
    public static void hillClimbing(Graph g, int option) {

        List<Graph> visited = new ArrayList<Graph>(); // list of visited permutations
        boolean existNext = true; // checks if hillClimbing can continue
        int indexNext = -1; // index of next permutation

        Graph cur = g;

        int count = 0;

        do {
            visited.add(cur);

            generateSons(cur);

            if (option == 1) {
                indexNext = byPerimeter(cur, visited);
            }

            else if (option == 2) {
                indexNext = firstSon(cur, visited);
            }

            else if (option == 3) {
                indexNext = lessConflits(cur, visited);
            }

            else if (option == 4) {
                indexNext = randomSon(cur, visited);
            }

            // Checks if hillClimbing can continue
            if (indexNext == -1)
                existNext = false;
            else {
                cur = cur.sons.get(indexNext);
                existNext = true;
                indexNext = -1;
            }
            count++;
            System.out.println(count);
        } while (existNext);

        System.out.println("Acabei ao fim de " + count + " iterações");

        printList(cur.nodes);

    }
}
