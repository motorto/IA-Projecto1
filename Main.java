import java.util.Scanner;

import javax.swing.JFrame;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.geom.Point2D;

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
        printPoint(this.nodes.get(0));
        System.out.println();
    }

    void printPoint(Point2D point) {
        System.out.print("(" + (int) point.getX() + "," + (int) point.getY() + ") ");
    }

    void randomPermutation() {
        Collections.shuffle(this.nodes);
    }

    /*
     * Calculates the perimeter of the graph
     */
    double perimeter() {
        double answer = 0;
        Point2D a;
        Point2D b;

        for (int i = 1; i < this.nodes.size(); i++) {
            a = this.nodes.get(i - 1);
            b = this.nodes.get(i);
            answer += a.distanceSq(b);
        }
        a = this.nodes.get(this.nodes.size() - 1);
        b = this.nodes.get(0);
        answer += a.distanceSq(b);

        return answer;
    }
}

public class Main {

    private static void createGui(Graph cur) {
        JFrame f = new JFrame();
        gui applet = new gui(cur);
        f.add(applet);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Insira o numero de nós.");
        int size = scan.nextInt();
        System.out.println("Insira o limite:");
        int range = scan.nextInt();
        Graph g = new Graph();

        int option = 0;

        System.out.println("1) Inserir pontos manualmente");
        System.out.println("2) Inserir pontos aleatorios");
        option = scan.nextInt();

        switch (option) {
            case 1:

                int x,y;
                x = 0;
                y = 0;

                for (int i = 0 ;i<size;i++) {
                    x = scan.nextInt();
                    y = scan.nextInt();
                    if (!g.addNewPoint((double)x, (double)y)) {
                        System.out.println("Ponto repetido!!!");
                        i--;
                    }
                }
                break;
            case 2:
                for (int i = 0; i < size; i++) {
                    if (!g.randomPointGenerator(range))
                        i--;
                }
                break;
            default:
                System.exit(0);
                break;
        }

        option = 0;
        do {
            System.out.println("--------------------");
            System.out.println("0-Sair");
            System.out.println("1-Imprimir Figura");
            System.out.println("2-Permutação Random");
            System.out.println("3-Permutação com Nearest Neighbour First");
            System.out.println("4-Two-Exchange");
            System.out.println("5-Hill Climbing");
            System.out.println("6-Simulated Annealing");
            System.out.println("--------------------");

            option = scan.nextInt();

            switch (option) {
            case 0:
                System.exit(0);
                break;
            case 1:
                g.printGraph();
                createGui(g);
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
                    g = search.hillClimbing(g, 1);
                    g.printGraph();
                    createGui(g);
                    System.out.println("-------- ---------");
                    break;
                case 2:
                    g = search.hillClimbing(g, 2);
                    g.printGraph();
                    createGui(g);
                    System.out.println("-------- ---------");
                    break;
                case 3:
                    g = search.hillClimbing(g, 3);
                    g.printGraph();
                    createGui(g);
                    System.out.println("-------- ---------");
                    break;
                case 4:
                    g = search.hillClimbing(g, 4);
                    g.printGraph();
                    createGui(g);
                    System.out.println("-------- ---------");
                    break;
                default:
                    System.out.println("Opção Invalida");
                    break;
                }
                break;
            case 6: {
                g = search.simulatedAnnealing(g);
                System.out.println("-------- Acabei ---------");
                System.out.println("-------- ---------");
                g.printGraph();
                createGui(g);
                System.out.println("-------- ---------");
                break;
            }
            default:
                System.out.println("Opção Invalida");
                break;
            }
        } while (option != 0);

        scan.close();
    }
}
