package rectanglepacking;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @date Apr 26, 2017
 * @author Adriaan Knapen <a.d.knapen@student.tue.nl>
 */
public class Area extends Rectangle {

    protected Collection<ShapeInterface> shapes;

    public Area(int width, int height, int x, int y, boolean flippable) {
        super(width, height, x, y, flippable);
        shapes = new HashSet<>();
    }

    public void add(ShapeInterface shape) {
        shapes.add(shape);
    }

    public int getAmount() {
        // Recursively get the amount of rectangles.
        int amount = 0;
        for (ShapeInterface shape : shapes) {
            amount += shape.getAmount();
        }

        return amount;
    }
    
    public Collection getShapes() {
        return shapes;
    }
}
