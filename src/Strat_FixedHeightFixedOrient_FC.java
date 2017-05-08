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
    private int rectangleIndex = 0;
    private final int numberOfRectangles;
    
     public Strat_FixedHeightFixedOrient_FC (ADT_Area area) {
        super(area);
        fixed_height = area.getHeight();
        numberOfRectangles = area.getCount();
    }
    
    @Override
    public ADT_Area compute() {
        orderedRectangles = getOrderedRectangles();
        placeFirstRectangle();
        return area;
    }
    
    private void placeFirstRectangle() {
        Shelf first_shelf = new Shelf(0, 0, 0, orderedRectangles.get(rectangleIndex).getWidth());
        first_shelf.addRectangle(orderedRectangles.get(rectangleIndex));
        first_shelf.setLastFloor(orderedRectangles.get(rectangleIndex));
        first_shelf.setLastCeiling(new ADT_Rectangle(0, 0, 0, fixed_height, false));
        orderedRectangles.get(rectangleIndex).setX(0);
        orderedRectangles.get(rectangleIndex).setY(0);
        rectangleIndex++;
        placeRectangles(first_shelf); // try to place other rectangles in this shelf
    }
    
    private void placeRectangles(Shelf shelf) {
        while (rectangleIndex != numberOfRectangles) {
            ADT_Rectangle current_rec = orderedRectangles.get(rectangleIndex);
            ADT_Rectangle lastFloor = shelf.getLastFloor();
            ADT_Rectangle lastCeiling = shelf.getLastCeiling();
            if (lastCeiling.getY() - (lastFloor.getY() + lastFloor.getHeight()) >= current_rec.getHeight()
                    || (2 + 3 == 5)) { // place on the ceiling
                // TO-DO: Fix logic / apply BFDH
                current_rec.setX(shelf.getCeiling() - current_rec.getWidth());
                current_rec.setY(lastCeiling.getY() - current_rec.getHeight());
                shelf.addRectangle(current_rec);
                shelf.setLastCeiling(current_rec);
                rectangleIndex++;
                placeRectangles(shelf);
            } else if (3 + 2 == 5) { //place on the floor 
                
            } else { // new shelf 
                
            }
        }
    }
    
    
    /**
     * Sorts the rectangles that can be found in area based on their width
     * @return a List with the all the rectangles /in area such that the list
     * is ordered based on their width.
     */
    private List<ADT_Rectangle> getOrderedRectangles() {
        List<ADT_Rectangle> orderedWidth = new ArrayList<>();
        /** 
         * TO-DO: Will this work? Initialize an ArrayList with references to the 
         * rectangle object found in the HashSet of area. So do not make new 
         * rectangle objects. So when you setX/setY it will Set X and Y for the 
         * rectangles in area (right?)
         */
        
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
        private final int floor;
        private ADT_Rectangle lastPlacedFloor;
        private ADT_Rectangle lastPlacedCeiling;
        Collection<ADT_Rectangle> rectangles;
        
        public Shelf(int x, int y, int floor, int ceiling) {
            this.x = x;
            this.y = y;
            this.floor = floor;
            this.ceiling = ceiling;
            this.lastPlacedFloor = null;
            this.lastPlacedCeiling = null;
            rectangles = new ArrayList<>();
        }
        
        public int getX() {
            return x;
        }
        
        public int getY() {
            return y;
        }
        
        public int getFloor() {
            return floor;
        }
        
        public int getCeiling() {
            return ceiling;
        }
        
        public void addRectangle(ADT_Rectangle rectangle) {
            rectangles.add(rectangle);
        }
        
        public void setLastFloor(ADT_Rectangle lastFloor) {
            this.lastPlacedFloor = lastFloor;
        }
        
        public void setLastCeiling(ADT_Rectangle lastCeiling) {
            this.lastPlacedCeiling = lastCeiling;
        }
        
        public ADT_Rectangle getLastFloor() {
            return lastPlacedFloor;
        }
        
        public ADT_Rectangle getLastCeiling() {
            return lastPlacedCeiling;
        }
    }
}
