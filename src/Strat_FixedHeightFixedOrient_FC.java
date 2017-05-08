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
        Shelf first_shelf = new Shelf(0, orderedRectangles.get(rectangleIndex).getWidth());
        orderedRectangles.get(rectangleIndex).setX(0);
        orderedRectangles.get(rectangleIndex).setY(0);
        first_shelf.addRectangle(orderedRectangles.get(rectangleIndex));
        first_shelf.setLastFloor(orderedRectangles.get(rectangleIndex));
        first_shelf.setLastCeiling(new ADT_Rectangle(0, 0, 0, fixed_height, false));
        rectangleIndex++;
        placeRectangles(first_shelf); // try to place other rectangles in this shelf
    }
    
    private void placeRectangles(Shelf shelf) {
        while (rectangleIndex != numberOfRectangles) {
            ADT_Rectangle current_rec = orderedRectangles.get(rectangleIndex);
            ADT_Rectangle lastFloor = shelf.lastPlacedFloor;
            ADT_Rectangle lastCeiling = shelf.lastPlacedCeiling;
            if (shelf.firstCeil) { // first item has already been placed on a ceiling
               // costly (temporarily) solution
               ADT_Rectangle dummyRecCeiling = dummyCeil(shelf, current_rec, lastCeiling);
               ADT_Rectangle dummyRecFloor = dummyFloor(shelf, current_rec, lastFloor);
               if (! rectangleOverlap(shelf, dummyRecCeiling)) { // can safely place it on the ceiling
                   current_rec.setX(shelf.ceiling - current_rec.getWidth());
                   current_rec.setY(fixed_height - current_rec.getHeight());
                   rectangleIndex++;
                   shelf.addRectangle(current_rec);
                   shelf.setLastCeiling(current_rec);
               } else if (! rectangleOverlap(shelf, dummyRecFloor)) { // can safely place it on the floor
                   current_rec.setX(shelf.x);
                   current_rec.setY(lastFloor.getY() + lastFloor.getHeight());
                   shelf.addRectangle(current_rec);
                   shelf.setLastFloor(current_rec);
                   rectangleIndex++;  
               } else { // new shelf
                   Shelf newShelf = newShelf(shelf, current_rec);
                   placeRectangles(newShelf);
               }
              
            } else { // no rectangles have been placed on the ceiling yet
                if (fixed_height - (lastFloor.getY() + lastFloor.getHeight()) >= current_rec.getHeight()) {
                    // place rectangle on the same shelf on the floor
                    current_rec.setX(shelf.x);
                    current_rec.setY(lastFloor.getY() + lastFloor.getHeight());
                    shelf.addRectangle(current_rec);
                    shelf.setLastFloor(current_rec);
                    rectangleIndex++;
                } else if (! rectangleOverlap(shelf, dummyCeil(shelf, current_rec, lastCeiling))) { // place first rectangle on the ceiling
                    current_rec.setX(shelf.ceiling - current_rec.getWidth());
                    current_rec.setY(fixed_height - current_rec.getHeight());
                    rectangleIndex++;
                    shelf.addRectangle(current_rec);
                    shelf.setLastCeiling(current_rec);
                    shelf.setFirstCeil();
                } else { // new shelf
                   Shelf newShelf = newShelf(shelf, current_rec);
                   placeRectangles(newShelf);
                }
                
            }
        }
    }
    
    private ADT_Rectangle dummyCeil(Shelf shelf, ADT_Rectangle rec, ADT_Rectangle lastCeil) {
        int x = shelf.ceiling - rec.getWidth();
        int y = lastCeil.getY() - rec.getHeight();
        ADT_Rectangle dummy = new ADT_Rectangle(rec.getWidth(), rec.getHeight(), x, y, false);
        return dummy;
    }
    
    private ADT_Rectangle dummyFloor(Shelf shelf, ADT_Rectangle rec, ADT_Rectangle lastFloor) {
        int x = shelf.x;
        int y = lastFloor.getY() + lastFloor.getHeight();
        ADT_Rectangle dummy = new ADT_Rectangle(rec.getWidth(), rec.getHeight(), x, y, false);
        return dummy;
    }
    
    private boolean rectangleOverlap (Shelf shelf, ADT_Rectangle rec) {
        for (Iterator<ADT_Rectangle> rectangles = area.getRectangles(); rectangles.hasNext();) {
            ADT_Rectangle cur_rec = rectangles.next();
            if (shelf.checkRectangleOverlap(cur_rec, rec)) {
                return true;
            }
        }
        return false;
    }
    
    private Shelf newShelf (Shelf shelf, ADT_Rectangle rec) {
        Shelf newShelf = new Shelf(shelf.x + shelf.ceiling, rec.getWidth());
        rec.setX(newShelf.x);
        rec.setY(0);
        newShelf.addRectangle(rec);
        newShelf.setLastFloor(rec);
        rectangleIndex++;
        return newShelf;
    }
    
    /**
     * Sorts the rectangles that can be found in area based on their width
     * @return a List with the all the rectangles /in area such that the list
     * is ordered based on their width.
     */
    private List<ADT_Rectangle> getOrderedRectangles() {
        List<ADT_Rectangle> orderedWidth = new ArrayList<>();
        /*
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
                return (rec2.getWidth() - rec1.getWidth());
            }
        });    
        return orderedWidth;
    }
    
    private class Shelf {
        
        private final int x;
        private final int ceiling;
        private ADT_Rectangle lastPlacedFloor;
        private ADT_Rectangle lastPlacedCeiling;
        Collection<ADT_Rectangle> rectangles;
        private boolean firstCeil;
        
        public Shelf(int x, int ceiling) {
            this.firstCeil = false;
            this.x = x;
            this.ceiling = ceiling;
            this.lastPlacedFloor = null;
            this.lastPlacedCeiling = null;
            rectangles = new ArrayList<>();
        }
        
        // The first item packed on a ceiling can only be one which cannot be packed on the floor below
        public void setFirstCeil() {
            this.firstCeil = true;
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
        
        public Iterator<ADT_Rectangle> getRectangles() {
            return rectangles.iterator();
        }
        
        // copied from ADT_Area
        private boolean checkRectangleOverlap(ADT_Rectangle rec1, ADT_Rectangle rec2) {
            assert rec1 != null;
            assert rec2 != null;
            assert rec1.getWidth() != ADT_Rectangle.INF;
            assert rec2.getWidth() != ADT_Rectangle.INF;
            assert rec1.getHeight() != ADT_Rectangle.INF;
            assert rec2.getWidth() != ADT_Rectangle.INF;

            Point l1 = new Point(rec1.getX(), rec1.getY() + rec1.getHeight()); // Top left coordinate of first rectangle
            Point r1 = new Point(rec1.getX() + rec1.getWidth(), rec1.getY()); // Bottom right coordinate of first rectangle
            Point l2 = new Point(rec2.getX(), rec2.getY() + rec2.getHeight()); // Top left coordinate of second rectangle
            Point r2 = new Point(rec2.getX() + rec2.getWidth(), rec2.getY()); // Bottom right coordinate of second rectangle

            if (l1.getX() >= r2.getX() || l2.getX() >= r1.getX()) { // Check if one rectangle is on the left side of the other rectangle.
                return false;
            } else return !(l1.getY() <= r2.getY() || l2.getY() <= r1.getY()); // Check if one rectangle is above the other rectangle.
            
        }

        private class Point {
            private int x;
            private int y;

            Point(int x, int y) {
                this.x = x;
                this.y = y;
            }

            int getX() {
                return x;
            }

            int getY() {
                return y;
            }
        }
    }
}
