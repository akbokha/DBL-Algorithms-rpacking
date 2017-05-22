import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

/**
 * @date 01-05-2017
 * @author Adriaan Knapen <a.d.knapen@student.tue.nl>
 */
public class Output_GraphicalOutput extends Output_AbstractOutput {

    private int rectWidth;
    private int rectHeight;
    private int maxWindowWidth = 1500;
    private int maxWindowHeight = 500;
    private int border = 20;
    private float scale;
    private JFrame window;

    Output_GraphicalOutput(ADT_Area area) {
        super(area);
        ADT_Vector dim = area.getDimensions();
        rectWidth = dim.x;
        rectHeight = dim.y;

        scale = Math.min(maxWindowWidth/rectWidth, maxWindowHeight/rectHeight);

        int windowWidth = (int) (rectWidth * scale) + 2 * border + 15;
        int windowHeight = (int) (rectHeight * scale) + 2 * border + 40;

        window = new JFrame();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setBounds(30, 30, windowWidth, windowHeight);
        window.setPreferredSize(new Dimension(windowWidth + 2 * border, windowHeight + 2 * border));
        window.getContentPane().add(new RectanglesCanvas());
        window.setTitle("Width: " + rectWidth + " Height: " + rectHeight + " Area: " + rectWidth * rectHeight);
        window.setVisible(true);
    }

    @Override
    public void draw() {
        window.repaint();
    }

    class RectanglesCanvas extends JComponent {
        @Override
        public void paint(Graphics g) {
            g.drawRect(border - 1, border - 1, (int) (rectWidth * scale) + 2, (int) (rectHeight * scale) + 2);

            g.setColor(Color.red);

            Iterator<ADT_Rectangle> i = area.getRectangleIterator();
            while (i.hasNext()) {
                ADT_Rectangle rect = i.next();
                int w = (int) Math.floor(rect.getWidth() * scale);
                int h = (int) Math.floor(rect.getHeight() * scale);
                int x = (int) Math.floor(rect.getX() * scale) + border;
                int y = (int) Math.floor((rectHeight - rect.getY()) * scale + border - h);

                g.setColor(Color.darkGray);
                g.fillRect(x, y, w, h);

                g.setColor(Color.red);
                g.drawRect(x, y, w, h);
            }
        }
    }
}
