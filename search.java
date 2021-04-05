import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.geom.Point2D;

public class search {

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
     * Calculates the dot product
     */
    private static double dotProduct(Point2D p1, Point2D p2, Point2D p3, Point2D p4) {
        Point2D x = new Point2D.Double(p2.getX() - p1.getX(), p2.getY() - p1.getY());
        Point2D y = new Point2D.Double(p4.getX() - p3.getX(), p4.getY() - p3.getY());
        return (x.getX() * y.getX()) + (x.getY() * y.getY());
    }

    /*
     * Check if a , lies on BC
     */
    static boolean inBox(Point2D a, Point2D b, Point2D c) {
        if (b.getX() <= Math.max(a.getX(), c.getX()) && b.getX() >= Math.min(a.getX(), c.getX())
                && b.getY() <= Math.max(a.getY(), c.getY()) && b.getY() >= Math.min(a.getY(), c.getY()))
            return true;

        return false;
    }

    /*
     * To find orientation Returns orientation, 0) colinear , 1) Clockwise 2)
     * CounterClockwise
     */
    static int dir(Point2D a, Point2D b, Point2D c) {
        double val = (b.getY() - a.getY()) * (c.getX() - b.getX()) - (b.getX() - a.getX()) * (c.getY() - b.getY());

        if (val == 0.0)
            return 0;

        return (val > 0) ? 1 : 2;
    }

    /*
     * Checks if intersects https://www.youtube.com/watch?v=R08OY6yDNy0
     */
    private static boolean segmentsIntersect(Point2D p1, Point2D p2, Point2D p3, Point2D p4) {
        int dir1 = dir(p1, p2, p3);
        int dir2 = dir(p1, p2, p4);
        int dir3 = dir(p3, p4, p1);
        int dir4 = dir(p3, p4, p2);

        if (dir1 != dir2 && dir3 != dir4)
            return true;
        else if ((dir1 == 0 && dir2 == 0 && dir3 == 0 && dir4 == 0) && dotProduct(p1, p3, p3, p4) > 0)
            return true;
        else if (dir1 == 0 && inBox(p1, p3, p2))
            return true;
        else if (dir2 == 0 && inBox(p1, p4, p2))
            return true;
        else if (dir3 == 0 && inBox(p3, p1, p4))
            return true;
        else if (dir4 == 0 && inBox(p3, p2, p4))
            return true;

        return false;
    }

    /*
     * Checks if intersection isn't already on intersected List
     */
    private static boolean intersectionAlreadyExists(List<Point2D> list, Point2D a, Point2D b, Point2D c, Point2D d) {
        if (list.size() > 0 && list.size() % 4 == 0) {
            for (int i = 3; i < list.size(); i += 4) {

                Point2D q = list.get(i - 3);
                Point2D w = list.get(i - 2);
                Point2D e = list.get(i - 1);
                Point2D r = list.get(i);

                if (q == a && w == b && e == c && r == d) {
                    return true;
                }

            }
        }
        return false;
    }

    /*
     * 2-Exchange
     */
    public static List<Point2D> twoExchange(Graph g) {
        List<Point2D> intersections = new ArrayList<Point2D>(); // list of nodes that intersect

        Point2D a, b, c, d;

        for (int i = 1; i < g.nodes.size(); i++) {
            a = g.nodes.get(i - 1);
            b = g.nodes.get(i);

            for (int j = 1; j < g.nodes.size(); j++) {
                c = g.nodes.get(j - 1);
                d = g.nodes.get(j);

                if (a != c && a != d && b != c && b != d) {
                    if (segmentsIntersect(a, b, c, d)) {
                        if (!intersectionAlreadyExists(intersections, c, d, a, b)) {
                            intersections.add(a);
                            intersections.add(b);
                            intersections.add(c);
                            intersections.add(d);
                        }
                    }
                }
            }
        }

        // test last connection

        a = g.nodes.get(0);
        b = g.nodes.get(g.nodes.size() - 1);

        for (int i = 2; i < g.nodes.size() - 1; i++) {
            c = g.nodes.get(i - 1);
            d = g.nodes.get(i);

            if (segmentsIntersect(a, b, c, d)) {
                intersections.add(a);
                intersections.add(b);
                intersections.add(c);
                intersections.add(d);
            }

        }

        return intersections;
    }

    /*
     * Swaps intersection
     */
    private static void swap(Graph g, Point2D b, Point2D c) {
        int i = g.nodes.indexOf(b);
        int j = g.nodes.indexOf(c);
        if (i > j) {
            int tmp = i;
            i = j;
            j = tmp;
        }
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
     * Generate the candidates
     */
    private static void generateSons(Graph g) {
        // list of intersections
        List<Point2D> intersection = twoExchange(g);

        // Create possible sons
        while (intersection.size() > 0) {
            Point2D c = intersection.remove(3);
            c = intersection.remove(2);
            Point2D b = intersection.remove(1);
            b = intersection.remove(0);

            Graph tmp = new Graph(g);

            swap(tmp, b, c);
            g.sons.add(tmp);
        }
    }

    /*
     * Returns index of son with less perimeter
     */
    private static int selectSonPerimeter(Graph g,List<Graph> visited) {
        // Calculates perimeter
        double minPerimeter = Integer.MAX_VALUE;
        double curPerimeter = 0;
        int indexNext = -1;
        for (Graph a : g.sons) {
            if (!visited.contains(a)) {
                curPerimeter = a.perimeter();
                if (curPerimeter < minPerimeter) {
                    minPerimeter = curPerimeter;
                    indexNext = g.sons.indexOf(a);
                }
            }
        }
        if (indexNext == -1) {
            return -1;
        } else {
            return indexNext;
        }
    }

    /*
     * Returns index of first son (only if available)
     */
    private static int selectFirstSon(Graph g,List<Graph> visited) {
        if (g.sons.size() > 0 && !visited.contains(g.sons.get(0)))
            return 0;
        else
            return -1;
    }

    /*
     * Returns number of conflicts
     */
    private static int numberOfConficts(Graph g){
        List<Point2D> tmp = twoExchange(g); // list of intersections of the possible son
        return tmp.size();
    }

    /*
     * Returns index of son with less conflicts (by two exchange)
     */
    private static int selectSonLessConflits(Graph g,List<Graph> visited) {
        int minConflit = Integer.MAX_VALUE;
        int curConflict = 0;
        int indexNext = -1;
        for (Graph a : g.sons) {
            if (!visited.contains(a)) {
                curConflict = numberOfConficts(a);
                if (curConflict < minConflit) {
                    minConflit = curConflict;
                    indexNext = g.sons.indexOf(a);
                }
            }
        }
        return indexNext;
    }

    /*
     * Returns index of a random son
     */
    private static int selectSonRandom(Graph g,List<Graph> visited) {
        int indexNext = -1;
        if (g.sons.size() > 0) {
            Random random = new Random();
            while (true) {
                indexNext = random.nextInt(g.sons.size());
                if (!visited.contains(g.sons.get(indexNext))) {
                    return indexNext;
                }
            }
        }
        return indexNext;
    }

    /*
     * Implements hill Climbing
     */
    public static Graph hillClimbing(Graph cur, int option) {
        final long startTime = System.nanoTime(); // to calculate Time
        List<Graph> visited = new ArrayList<Graph>(); // list of visited permutations
        boolean existNext = false; // check if hill climb can continue
        int indexNext = -1; // index of son
        int count = 0;

        do {
            visited.add(cur);
            generateSons(cur);

            if (option == 1) {
                indexNext = selectSonPerimeter(cur,visited);
            } else if (option == 2) {
                indexNext = selectFirstSon(cur,visited);
            } else if (option == 3) {
                indexNext = selectSonLessConflits(cur,visited);
            } else if (option == 4) {
                indexNext = selectSonRandom(cur,visited);
            }

            if (indexNext >= 0) {
                cur = cur.sons.get(indexNext);
                existNext = true;
                indexNext = -1;
                count++;
            } else {
                existNext = false;
            }
        } while (existNext == true);

        final long duration = System.nanoTime() - startTime;
        System.out.println("-------- Acabei ---------");
        System.out.println("Ao fim de " + count + " iterações e " + duration + " nanosegundos");
        return cur;
    }


    /*
     * Implements the Simulated Annealing
     */
    public static Graph simulatedAnnealing(Graph cur){
        final long startTime = System.nanoTime(); // to calculate Time
        List<Graph> visited = new ArrayList<Graph>(); // list of visited permutations
        int temperature = 1000;
        double coolingFactor = 0.95;
        int deltaE;
        int indexNext = -1;
        double probability = 0;
        for (int i = 0 ;  i < Integer.MAX_VALUE ; i++){
            probability = Math.random();
            visited.add(cur);
            generateSons(cur);
            temperature*= coolingFactor;
            if (temperature == 0 || cur.sons.size() == 0) {
                final long duration = System.nanoTime() - startTime;
                System.out.println("-------- Acabei ---------");
                System.out.println("Ao fim de " + duration + " nanosegundos");
                return cur;
            }
            indexNext = selectSonRandom(cur,visited);

            deltaE = numberOfConficts(cur.sons.get(indexNext))  - numberOfConficts(cur);

            if (deltaE > 0) {
                cur = cur.sons.get(indexNext);
            }
            else if(Math.exp(deltaE / temperature) > probability) {
                cur = cur.sons.get(indexNext);
            }
        }
        final long duration = System.nanoTime() - startTime;
        System.out.println("-------- Acabei ---------");
        System.out.println("Ao fim de " + duration + " nanosegundos");
        return cur;
    }
}
