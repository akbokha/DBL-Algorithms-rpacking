import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * An implementation of the Floor Ceiling Strategy
 * The original Floor Ceiling algorithms considers a fixed width and tries to 
 * minimize the number of bins of this width/the height.
 * In our context we use this implementation when we have a fixed height and 
 * when rotations of rectangles is not allowed (try to minimize the width).
 *
 * @author Abdel K.
 * date: 08-05-17
 */
public class Strat_FixedHeightFixedOrient_FC extends Strat_AbstractStrat {
    
    private List<ADT_Rectangle> orderedRectangles;
    private final int fixed_height;
    
     public Strat_FixedHeightFixedOrient_FC (ADT_Area area) {
        super(area);
        fixed_height = area.getHeight();
    }
    
    @Override
    public ADT_Area compute() {
        orderedRectangles = getOrderedRectangles();
        Shelf first_shelf = new Shelf(0, 0, orderedRectangles.get(0).getWidth());
        return area;
    }
    
    
    /**
     * Sorts the rectangles that can be found in area based on their widht
     * @return a List with the all the rectangles /in area such that the list
     * is ordered based on their width.
     */
    private List<ADT_Rectangle> getOrderedRectangles() {
        List<ADT_Rectangle> orderedWidth = new ArrayList<>();
        
        for(Iterator<ADT_Rectangle> rectangles = area.getRectangles(); rectangles.hasNext();) {
             ADT_Rectangle rec = rectangles.next();  
             orderedWidth.add(rec);
         }
        
        Collections.sort(orderedWidth, new Comparator<ADT_Rectangle>(){
            @Override
            public int compare(ADT_Rectangle rec1, ADT_Rectangle rec2) {
                return (rec1.getWidth() - rec2. getWidth());
            }
        });    
        return orderedWidth;
    }
    
    private class Shelf {
        private final int x;
        private final int y;
        private final int ceiling;
        Collection<ADT_ShapeInterface> rectangles;
        
        public Shelf(int x, int y, int ceiling) {
            this.x = x;
            this.y = y;
            this.ceiling = ceiling;
            rectangles = new ArrayList<>();
        }
        
        public int getX() {
            return x;
        }
        
        public int getY() {
            return y;
        }
        
        public int getCeiling() {
            return ceiling;
        }
        
        public void addRectangle(ADT_ShapeInterface rectangle) {
            rectangles.add(rectangle);
        }
    }
}
