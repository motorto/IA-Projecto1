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
    List<Graph> sons; // list of possibleSons

    Graph() {
        this.nodes = new ArrayList<Point2D>();
        this.sons = new ArrayList<Graph>();
    }

    /*
     * Copys the points to new graph
     */
    Graph(Graph g) {
        this.nodes = new ArrayList<Point2D>(g.nodes);
        this.sons = new ArrayList<Graph>();
    }

    /*
     * Calculates the perimeter of the graph
     */
    int perimeter() {
        int answer = 0;
        int count = 0;

        while (count < nodes.size()) {
            if (count == nodes.size() - 1) {
                Point2D a = nodes.get(count++);
                Point2D b = nodes.get(0);
                answer += a.distance(b);
            } else {
                Point2D a = nodes.get(count++);
                Point2D b = nodes.get(count++);
                answer += a.distance(b);
            }
        }
        return answer;
    }

    boolean addNewPoint(double x, double y) {
        Point2D tmp = new Point2D.Double(x, y);

        if (nodes.contains(tmp))
            return false;

        nodes.add(tmp);
        return true;
    }

    boolean randomPointGenerator(int range) {
        Random random = new Random();
        // (upperbound-lowerbound) + lowerbound;
        if (addNewPoint((double) random.nextInt(range * 2) - range, (double) random.nextInt(range * 2) - range))
            return true;
        else
            return false;
    }

    void printGraph() {
        for (Point2D a : nodes) {
            printPoint(a);
        }
    }

    void printPoint(Point2D point) {
        System.out.println("(" + point.getX() + ";" + point.getY() + ")");
    }

    /*
     * Compares the permutation
     */
    boolean equals(Graph a) {
        if (this.nodes.size() != a.nodes.size())
            return false;

        Point2D graph1;
        Point2D graph2;
        for (int i = 0; i < this.nodes.size(); i++) {
            graph1 = this.nodes.get(i);
            graph2 = a.nodes.get(i);
            if (graph1.getX() != graph2.getX() || graph1.getY() != graph2.getY())
                return false;
        }
        return true;
    }

    void randomPermutation() {
        Collections.shuffle(this.nodes);
    }
}

public class solve {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Insira o numero de nós.");
        int size = scan.nextInt();
        System.out.println("Insira o limite:");
        int range = scan.nextInt();

        Graph g = new Graph();

        for (int i = 0; i < size; i++) {
            if (!g.randomPointGenerator(range))
                i--;
        }

        int option = 0;
        do {
            System.out.println("--------------------");
            System.out.println("0-Sair");
            System.out.println("1-Imprimir Figura");
            System.out.println("2-Permutação Random");
            System.out.println("3-Permutação com Nearest Neighbour First");
            System.out.println("4-Two-Exchange");
            System.out.println("5-Hill Climbing");
            System.out.println("--------------------");

            option = scan.nextInt();

            switch (option) {
            case 0:
                System.exit(0);
                break;
            case 1:
                g.printGraph();
                break;
            case 2:
                g.randomPermutation();
                break;
            case 3:
                search.nearestNeighbourFirst(g);
                break;
            case 4:
                int count = 1;
                for (Point2D tmp : search.twoExchange(g)) {
                    g.printPoint(tmp);
                    if (count % 4 == 0)
                        System.out.println("-------------");
                    count++;
                }
                break;
            case 5:
                System.out.println("--------------------");
                System.out.println("Escolha uma das 4 opçoes:");
                System.out.println("1-Menor Perimetro");
                System.out.println("2-Primeiro Filho");
                System.out.println("3-Menos confiltos de Arestas");
                System.out.println("4-Qualquer candidato");
                System.out.println("--------------------");
                option = scan.nextInt();
                switch (option) {
                case 1:
                    search.hillClimbing(g, 1);
                    break;
                case 2:
                    search.hillClimbing(g, 2);
                    break;
                case 3:
                    search.hillClimbing(g, 3);
                    break;
                case 4:
                    search.hillClimbing(g, 4);
                    break;
                default:
                    System.out.println("Opção Invalida");
                    break;
                }

                break;

            default:
                System.out.println("Opção Invalida");
                break;
            }
        } while (option != 0);

        scan.close();
    }
}