
import java.util.ArrayList;
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
    
     public Strat_FixedHeightFixedOrient_FC (ADT_Area area) {
        super(area);
    }
    
    @Override
    public ADT_Area compute() {
        orderedRectangles = getOrderedRectangles();
        // TO DO: Implementation
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
}
