import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

/**
 * @date 01-05-2017
 * @author Adriaan Knapen <a.d.knapen@student.tue.nl>
 */
public class Output_GraphicalOutput extends Output_AbstractOutput {

    private int width;
    private int height;
    private int maxWidth = 1000;
    private int maxHeight = 1000;
    private int border = 10;
    private float scale;

    Output_GraphicalOutput(ADT_Area area) {
        super(area);
        int[] dim = area.getMinDimensions();
        width = dim[0];
        height = dim[1];
    }

    @Override
    public void draw() {
        float xScale = maxWidth / width;
        float yScale = maxHeight / height;
        scale = Math.min(xScale, yScale);

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        int windowWidth = (int) Math.floor(width * scale + 2 * border);
        int windowHeight = (int) Math.floor(height * scale + 2 * border);
        window.setBounds(30, 30, windowWidth, windowHeight);
        window.getContentPane().add(new RectanglesCanvas());
        window.setVisible(true);
    }

    class RectanglesCanvas extends JComponent {
        public void paint(Graphics g) {
            g.drawRect(border, border, (int) (width * scale), (int) (height * scale));

            g.setColor(Color.red);

            Iterator<ADT_Rectangle> i = area.getRectangles();
            while (i.hasNext()) {
                ADT_Rectangle rect = i.next();
                int w = (int) Math.floor(rect.getWidth() * scale);
                int h = (int) Math.floor(rect.getHeight() * scale);
                int x = (int) Math.floor(rect.getX() * scale) + border;
                int y = (int) Math.floor((height - rect.getY()) * scale + border - h);

                g.drawRect(x, y, w, h);
            }
        }
    }
}
