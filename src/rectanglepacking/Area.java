package rectanglepacking;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @date Apr 26, 2017
 * @author Adriaan Knapen <a.d.knapen@student.tue.nl>
 */
public class Area extends Rectangle {

    private Collection<Rectangle> shapes;

    public Area(int width, int height, boolean flippable) {
        super(width, height, 0, 0, flippable);
        shapes = new HashSet<>();
    }

    /**
     * Adds an rectangle to this area.
     *
     * @param shape
     */
    public void add(Rectangle shape) {
        shapes.add(shape);
    }

    /**
     * Gives the amount of rectangles this area contains.
     *
     * @return the amount of rectangles this area contains.
     */
    public int getCount() {
        return shapes.size();
    }

    /**
     * Constructs an iterator over all rectangles.
     *
     * @return iterator over all rectangles it contains.
     */
    public Iterator<Rectangle> getRectangles() {
        return shapes.iterator();
    }
    
    public Collection getShapes() {
        return shapes;
    }
}
