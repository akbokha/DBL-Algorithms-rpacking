import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @date Apr 26, 2017
 * @author Adriaan Knapen <a.d.knapen@student.tue.nl>
 */
public class ADT_Area extends ADT_Rectangle {

    private Collection<ADT_Rectangle> shapes;

    public ADT_Area(int width, int height, boolean flippable) {
        super(width, height, 0, 0, flippable);
        shapes = new HashSet<>();
    }

    /**
     * Adds an rectangle to this area.
     *
     * @param shape
     */
    public void add(ADT_Rectangle shape) {
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
    public Iterator<ADT_Rectangle> getRectangles() {
        return shapes.iterator();
    }
    
    /**
     * 
     * @return true if the area has none overlapping rectangles
     */
    public boolean isValid() {
        ArrayList<ADT_Rectangle> checkedRecs = new ArrayList<>();
        
        for(Iterator<ADT_Rectangle> recs = getRectangles(); recs.hasNext();) {
            ADT_Rectangle currentRec = recs.next();
            for(ADT_Rectangle rec : checkedRecs) {
                if(currentRec.x > rec.x && currentRec.x < (rec.x + rec.width) && currentRec.y > rec.y && currentRec.y < (rec.y + rec.height)) {
                    return false;
                }
            }
            checkedRecs.add(currentRec);
        }
        return true;
    }
}
