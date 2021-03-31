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
        g.nodes = path;
    }

    /*
     * Calculates the angle of AB to C
     */
    private static double dir(Point2D a, Point2D b, Point2D c) {
        return ((c.getX() - a.getX()) * (b.getY() - a.getY()) - (b.getX() - a.getX()) * (c.getY() - a.getY()));
    }

    /*
     *  Checks if the segments Intersect
     *  https://www.youtube.com/watch?v=R08OY6yDNy0
     */
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
        int indexB = g.nodes.indexOf(b);
        if (indexB == g.nodes.size() - 1) {
            g.nodes.remove(indexB);
            g.nodes.add(1, b);
        } else {
            g.nodes.remove(indexB);
            g.nodes.add(indexB + 1, b);
        }
    }

    /*
     * Hill Climbing implementation
     * (Not working properly)
     */
    // receives a function returns the next node
    public static void hillClimbing(Graph g, int option) {

        /*
        while (aindaHouverVizinho){
            cur = cur.filhoEscolhido
        }
        */

        List<Point2D> intersection; // list of intersections
        List<Graph> visited = new ArrayList<Graph>(); // list of visited permutations
        boolean existNext = true; // checks if hillClimbing continues
        int indexNext = -1; // index of next permutation

        int maxOperation = Integer.MAX_VALUE;

        Graph cur = g;

        int count =0;

        do {
            visited.add(cur);
            intersection = twoExchange(cur);

            // Create possible sons
            while (intersection.size() > 0) {
                Point2D d = intersection.remove(3);
                Point2D c = intersection.remove(2);
                Point2D b = intersection.remove(1);
                Point2D a = intersection.remove(0);

                Graph tmp = new Graph(cur);

                swap(tmp, a, b, c, d);
                cur.sons.add(tmp);
            }

            if (option == 1) {
                // Calculates perimeter
                int curPerimeter = 0;
                for (Graph a : cur.sons) {
                    if (!visited.contains(a)) {
                        curPerimeter = a.perimeter();
                        if (curPerimeter < maxOperation) {
                            maxOperation = curPerimeter;
                            indexNext = cur.sons.indexOf(a);
                        }
                    }
                }
            }

            else if (option == 2) {
                if (cur.sons.size() > 0) {
                    indexNext = 0;
                    if (visited.contains(cur.sons.get(indexNext))) {
                        indexNext = -1;
                    }
                }
            }

            else if (option == 3) {
                int curOperation = 0;
                for (Graph a : cur.sons) {
                    if (!visited.contains(a)) {
                        List<Point2D> tmp; // list of intersections of the possible son
                        tmp = twoExchange(a);
                        curOperation = tmp.size();
                        if (curOperation < maxOperation) {
                            maxOperation = curOperation;
                            indexNext = cur.sons.indexOf(a);
                        }
                    }
                }
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
        } while (existNext);
        System.out.println("Acabei ao fim de " + count +  " iterações");

        printList(cur.nodes);

    }
}
