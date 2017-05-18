
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/*
 * Empty Space pruning as described in R. E. Korf - Optimal Rectangle Packing
 */

/**
 * date: 12-05-2017
 * @author s158881
 */
public class Strat_BT_Pruner_WS2 implements Strat_BT_PrunerInterface {
   
    private int [] horizontalEmptySpaceStrips;
    private int [] verticalEmptySpaceStrips;
    private int [] horizontalRectangleStrips;
    private int [] verticalRectangleStrips;
    
    private List<Bin> bins;
    private List<ADT_Rectangle> rectanglesToBePlaced;
    private List<EmptyCell> emptyCells;
    
    private int[] rectangleAreas; // index = area, a[i] = sum of all the areas of the rectangles with area == index
    private int [] capacityBins; // index = capacity, a[i] = sum of capacities of all the bins that have a capacity == index
    
    private int totalAreaRectangles;
    private static final int NOTSET = -2;
    int maxAreaOfRectangles = 0;
    int maxCapacity = 0;
    
    private int wastedSpace; 
    private int carryOver;
    
    
    @Override
    public boolean reject (ADT_Area area, ADT_Rectangle last) {
         totalAreaRectangles = area.getTotalAreaRectangles(); // store sum areas rectangle
         
         /**
          * Operations that are needed to initialize and fill the array
          * The index of the array represents the area value
          * The value at array[index] = the sum of all the rectangles that have 
          * an area that is equal to the index
          */
         rectanglesToBePlaced = new ArrayList<>();
         collectRectanglesToBePlaced(area); // add the rectangles that still neet to be placed to the collection
         rectangleAreas = new int[maxAreaOfRectangles]; // initialize the described array
         fillRectangleAreaArray(); // fill the rectangle area array
         
         emptyCells = new ArrayList<>();
         findAndInitializeEmptyCells(area);
         bins = new ArrayList<>();
         makeBins();
         capacityBins = new int[maxCapacity];
         fillCapacityBinsArray(); // fill the bin capacity array
         
         wastedSpace = 0;
         carryOver = 0;
         
         for (int i = 1; i < rectangleAreas.length; i++) {
             if (capacityBins[i] > (rectangleAreas[i] + carryOver)) {
                 wastedSpace += (capacityBins[i] - (rectangleAreas[i] + carryOver));
                 carryOver = 0;
             } else if (capacityBins[i] == (rectangleAreas[i] + carryOver)){
                 carryOver = 0;   
             } else if (capacityBins[i] < (rectangleAreas[i] + carryOver)) {
                 carryOver = (rectangleAreas[i] + carryOver) - capacityBins[i];
             }
         }
        
         return ((wastedSpace + totalAreaRectangles) > (area.getWidth() * area.getHeight()));
    }
    
    private void collectRectanglesToBePlaced (ADT_Area area) {
        for (Iterator<ADT_Rectangle> rectangles = area.getRectangleIterator(); rectangles.hasNext();) {
            ADT_Rectangle current_rec = rectangles.next(); // next rectamgle
            if (current_rec.getX() == NOTSET && current_rec.getY() == NOTSET) {
                rectanglesToBePlaced.add(current_rec);
                int areaRectangle = current_rec.getWidth() * current_rec.getHeight();
                if (areaRectangle > maxAreaOfRectangles) {
                    maxAreaOfRectangles = areaRectangle;
                }
            }  
        }
    }
    
    private void fillRectangleAreaArray () {
        for (ADT_Rectangle rectangle : rectanglesToBePlaced) {
            int areaOfRectangle = rectangle.getWidth() * rectangle.getHeight();
            rectangleAreas[areaOfRectangle] = rectangleAreas[areaOfRectangle] + areaOfRectangle; 
        }
    }
    
    private void findAndInitializeEmptyCells(ADT_Area area) {
        int areaWidth = area.getWidth();
        int areaHeight = area.getHeight();
        for (int i = 0; i <= areaWidth;  i++) {
            for (int j = 0; j < areaHeight; j++) {
                ADT_Vector vector = new ADT_Vector (i, j);
                if (! area.isOccupied(vector)) {
                    int horizontalSpace = emptyHorizontalSpace(i, j, areaWidth, area);
                    int verticalSpace = emptyVerticalSpace(i, j, areaHeight, area);
                    EmptyCell emptyCell = new EmptyCell(i, j, horizontalSpace, verticalSpace);
                    emptyCells.add(emptyCell);
                }   
            }
        }
    }
    
    private int emptyHorizontalSpace (int x, int y, int maxWidth, ADT_Area area) {
        int emptyHorizontalSpace = 1; // the emptycell itself has a width of 1
        // go to the right
        for (int i = x; i <= maxWidth && ! area.isOccupied(new ADT_Vector(i, y)); i++) {
            emptyHorizontalSpace++;
        }
        // go to the left
        for (int j = x; j >= 0 && ! area.isOccupied(new ADT_Vector(j, y)); j--) {
            emptyHorizontalSpace++;
        }
        return emptyHorizontalSpace;
    }
    
    private int emptyVerticalSpace (int x, int y, int maxHeight, ADT_Area area) {
        int emptyVerticalSpace = 1; // the emptycell itself has a height of 1
        // go up
        for (int i = y; i <= maxHeight && ! area.isOccupied(new ADT_Vector(x, i)); i++) {
            emptyVerticalSpace++;
        }
        // go down
        for (int j = y; j >= 0 && ! area.isOccupied(new ADT_Vector(x, j)); j--) {
            emptyVerticalSpace++;
        }
        return emptyVerticalSpace;
    }
    
    private void makeBins() {
        for (EmptyCell emptycell : emptyCells) {
            boolean found_bin = false;
            for (Bin bin : bins) {
                if ((bin.horizontalSpace == emptycell.horizontal_space) && bin.verticalSpace == emptycell.vertical_space) {
                    bin.increaseCapacity();
                    found_bin = true;
                    if (bin.capacity > maxCapacity) {
                        maxCapacity = bin.capacity;
                    }
                }
            }
            if (! found_bin) {
                Bin new_bin = new Bin (emptycell.horizontal_space, emptycell.vertical_space);
                bins.add(new_bin);
            }
        }
    }
    
    private void fillCapacityBinsArray() {
        for (Bin bin : bins) {
            capacityBins[bin.capacity] = capacityBins[bin.capacity] + bin.capacity;
        }
    }
    
    private class EmptyCell {
        private int x;
        private int y;
        private int horizontal_space; // empty horizontal space to which EmptyCell belongs
        private int vertical_space; // empty vertical space to which EmptyCell belongs
        
        public EmptyCell (int x, int y, int horizontal_space, int vertical_space) {
            this.x = x;
            this.y = y;
            this.horizontal_space = horizontal_space;
            this.vertical_space = vertical_space;
        }
        
    }
    
    private class Bin {
        private int capacity; // number of emptycells it contains
        private int horizontalSpace; 
        private int verticalSpace;
        
        public Bin (int horizontalSpace, int verticalSpace) {
            this.verticalSpace = verticalSpace;
            this.horizontalSpace = horizontalSpace;
            // a bin is only constructed if there is not already a bin that has 
            // the specific horizontalSpace & verticalSpace
            this.capacity = 1;
        }
        
        /**
         * There is an emptyCell with the same horizontal and vertical space
         * increase capacity (number of cells) of the bin
         */
        public void increaseCapacity () {
            capacity++;
        }
    }
    
}