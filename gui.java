import javax.swing.JPanel;
import java.awt.*;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public class gui extends JPanel {
    int size;
    int [] x;
    int [] y;

    private static final long serialVersionUID = 4771278118692905576L;
    private static final double PADDING = 6;
    private static final int MULTIPLY = 14;

    public gui (Graph cur){
        this.size = cur.nodes.size() + 1;
        this.x = new int [size];
        this.y = new int [size];

        this.setPreferredSize(new Dimension(420,420));

        int i = 0;
        for (Point2D tmp : cur.nodes) {
            x[i] = (int) tmp.getX();
            y[i] = (int) tmp.getY();
            i++;
        }
        x[size-1] = x[0];
        y[size-1] = y[0];
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.BLACK);

        double maxX = getMaxX();
        double minX = getMinX();
        double maxY = getMaxY();
        double minY = getMinY();
        int centerX = getWidth()/2;
        int centerY = getHeight()/2;

        Graphics2D g2D = (Graphics2D) g;

        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);


        g2D.setColor(Color.WHITE);
        for (int i = 0 ; i < size-1 ; i++) {
            int f = (int) ((x[i] - minX) * (centerX  - PADDING) / (maxX - minX));
            int f2 = (int) ((x[i + 1] - minX) * (centerX  - PADDING) / (maxX - minX));
            int h = (int) (((maxY - minY) - (y[i] - minY)) * (centerY - PADDING)/ (maxY - minY));
            int h2 = (int) (((maxY - minY) - (y[i + 1] - minY)) * (centerY - PADDING) / (maxY - minY));
            g2D.drawLine((int) (f + (MULTIPLY*PADDING)) , (int) (h + (MULTIPLY*PADDING)), (int)(f2 + (MULTIPLY*PADDING)),(int) (h2 + (MULTIPLY * PADDING)));
        }
    }

    private int getMaxX() {
        double maxX = Double.MIN_VALUE;

        for (int tmp : x) {
            maxX = Math.max((double)maxX, (double)tmp);
        }
        return (int)maxX;
    }

    private int getMinX() {
        double minX = Double.MAX_VALUE;

        for (int tmp : x) {
            minX = Math.min((double)minX, (double)tmp);
        }
        return (int)minX;
    }

    private int getMinY() {
        double minY = Double.MAX_VALUE;

        for (int tmp : y) {
            minY = Math.min((double)minY, (double)tmp);
        }
        return (int)minY;
    }

    private int getMaxY() {
        double maxY = Double.MIN_VALUE;

        for (int tmp : y) {
            maxY = Math.max((double)maxY, (double)tmp);
        }
        return (int)maxY;
    }

}
