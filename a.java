import java.util.Scanner;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.geom.Point2D;

/**
 * Graph
 */
class Graph {
    List<Point2D> nodes; // list of nodes (order sorts)
    List<Point2D> visited; // list of visited nodes (visited)

    Graph(){
    this.nodes = new ArrayList<Point2D>(); ; 
    this.visited = new ArrayList<Point2D>();

    }

    void addNewPoint(double x, double y) {
        Point2D tmp = new Point2D.Double(x, y);
        System.out.println(tmp);
        nodes.add(tmp);
    }

    void randomPointGenerator(int range) {
        Random random  = new Random();
        // (upperbound-lowerbound) + lowerbound;
        addNewPoint( (double)random.nextInt(range*2) - range , (double)random.nextInt(range*2) - range);
    }

    void printGraph() {
        for (Point2D a : nodes) {
            printPoint(a);
        }
    }

    void printPoint(Point2D point) {
        System.out.print("(" + point.getX() + ";" + point.getY()  + ")");
    }

    void randomGenerator() {
        Collections.shuffle(this.nodes); 
    }

}

public class a {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Insira o numero de nós.");
        int size = scan.nextInt();
        System.out.println("Insira o limite:");
        int range = scan.nextInt();

        Graph g = new Graph();

        for (int i  = 0 ; i < size;i++){
            g.randomPointGenerator(range);
        }

        g.randomGenerator();

        g.printGraph();

        scan.close();
    }
}