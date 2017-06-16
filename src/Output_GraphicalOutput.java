import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

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

        int windowWidth = Math.max(400, (int) (rectWidth * scale) + 2 * border + 15);
        int windowHeight = (int) (rectHeight * scale) + 2 * border + 40;

        window = new JFrame();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setBounds(30, 30, windowWidth, windowHeight);
        window.setPreferredSize(new Dimension(windowWidth + 2 * border, windowHeight + 2 * border));
        window.getContentPane().add(new RectanglesCanvas());
        setTitle();
        window.setVisible(true);
    }

    @Override
    public void draw() {
        window.repaint();
    }

    class RectanglesCanvas extends JComponent {

        @Override
        public void paint(Graphics graphics) {
            setTitle();

            graphics.setColor(Color.darkGray);
            graphics.drawRect(border - 1, border - 1, (int) (rectWidth * scale) + 2, (int) (rectHeight * scale) + 2);

            ADT_Rectangle[] recs = area.getRectangles();
            for(int i = 0; i < recs.length && recs[i].getX() != ADT_Rectangle.NOTSET; i++) {
                ADT_Rectangle rect = recs[i];
                int w = (int) Math.floor(rect.getWidth() * scale);
                int h = (int) Math.floor(rect.getHeight() * scale);
                int x = (int) Math.floor(rect.getX() * scale) + border;
                int y = (int) Math.floor((rectHeight - rect.getY()) * scale + border - h);

                float c = (float) rect.hashCode() / Integer.MAX_VALUE;
                graphics.setColor(new Color((int)(c * 0x1000000)));
                graphics.fillRect(x, y, w, h);

                graphics.setColor(Color.red);
                graphics.drawRect(x, y, w, h);
            }
        }
    }

    private void setTitle() {
        // Print fillrate such that it can only be printed out of Momotor (-g)
        long surface = area.getDimensions().x*area.getDimensions().y;
        float fillRate = ((float) area.getTotalAreaRectangles() / surface) * 100;
        DecimalFormat df = new DecimalFormat("#0.0000");

        window.setTitle("Width: " + rectWidth + " Height: " + rectHeight + " Area: " + rectWidth * rectHeight
                + " Fill rate " + df.format(fillRate) + "%");
    }
}
