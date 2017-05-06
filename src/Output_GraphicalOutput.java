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
    private int maxWindowWidth = 500;
    private int maxWindowHeight = 500;
    private int border = 20;
    private float scale;

    Output_GraphicalOutput(ADT_Area area) {
        super(area);
        int[] dim = area.getMinDimensions();
        rectWidth = dim[0];
        rectHeight = dim[1];
    }

    @Override
    public void draw() {
        float areaScale = 1 / (float) Math.max(rectWidth, rectHeight);
        float windowScale = 1 / (float) Math.max(maxWindowWidth, maxWindowHeight);
        scale = areaScale / windowScale;

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        int windowWidth = (int) Math.floor(maxWindowWidth * maxWindowWidth * Math.min(windowScale, areaScale) + 3 * border);
        int windowHeight = (int) Math.floor(maxWindowHeight * maxWindowHeight * Math.min(windowScale, areaScale) + 3 * border);
        window.setBounds(30, 30, windowWidth, windowHeight);
        window.getContentPane().add(new RectanglesCanvas());
        window.setVisible(true);
    }

    class RectanglesCanvas extends JComponent {
        public void paint(Graphics g) {
            g.drawRect(border, border, (int) (rectWidth * scale), (int) (rectHeight * scale));

            g.setColor(Color.red);

            Iterator<ADT_Rectangle> i = area.getRectangles();
            while (i.hasNext()) {
                ADT_Rectangle rect = i.next();
                int w = (int) Math.floor(rect.getWidth() * scale);
                int h = (int) Math.floor(rect.getHeight() * scale);
                int x = (int) Math.floor(rect.getX() * scale) + border;
                //int y = (int) Math.floor((rectHeight - rect.getY()) * scale + border - h);
                int y = (int) Math.floor((rect.getY()) * scale + border);
                g.drawRect(x, y, w, h);
            }
        }
    }
}
