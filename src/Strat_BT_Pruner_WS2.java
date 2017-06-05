import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Empty Space pruning as described in R. E. Korf - Optimal Rectangle Packing
 */
public class Strat_BT_Pruner_WS2 implements Strat_BT_PrunerInterface {
    
    private List<Bin> bins;
    private List<ADT_Rectangle> rectanglesToBePlaced; // the rectangles that still need to be placed
    private List<EmptyCell> emptyCells; // the emptycells at in the partial solution
    
    private int[] rectangleAreas; // index = area, a[i] = sum of all the areas of the rectangles with area == index
    private int [] capacityBins; // index = capacity, a[i] = sum of capacities of all the bins that have a capacity == index
    
    private int totalAreaRectangles; // the total area of all the input rectangles
    private static final int NOTSET = -2;
    int maxAreaOfRectangles = 0;
    int maxCapacity = 1;
    
    private int wastedSpace; 
    private int carryOver;
    
    @Override
    public boolean reject (ADT_AreaExtended area, ADT_Rectangle last) {
         totalAreaRectangles = area.getTotalAreaRectanglesToBePlaced(); // store sum areas rectangle
         
         /**
          * Operations that are needed to initialize and fill the array
          * The index of the array represents the area value
          * The value at array[index] = the sum of all the rectangles that have 
          * an area that is equal to the index
          */
         rectanglesToBePlaced = new ArrayList<>();
         collectRectanglesToBePlaced(area); // add the rectangles that still neet to be placed to the collection
         rectangleAreas = new int[maxAreaOfRectangles+1]; // initialize the described array
         fillRectangleAreaArray(); // fill the rectangle area array
         
         emptyCells = new ArrayList<>();
         findAndInitializeEmptyCells(area); // fill the collection with all the empty cells and 
         bins = new ArrayList<>();
         makeBins(); // fills the collection that will hold the bins
         capacityBins = new int[maxCapacity+1];
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
    
    /**
     * Collects the rectangles that still need to be placed
     * And sets {@code MaxAreaOfRectangles} to the right value 
     */
    private void collectRectanglesToBePlaced (ADT_AreaExtended area) {
        for (Iterator<ADT_Rectangle> rectangles = area.getRectangleIterator(); rectangles.hasNext();) {
            ADT_Rectangle current_rec = rectangles.next(); // next rectangle
            if (current_rec.getX() == NOTSET && current_rec.getY() == NOTSET) { // rectangle not placed yet
                rectanglesToBePlaced.add(current_rec); // add to collection
                /**
                 * need to keep track of the largest area we encounter among the rectangles that still
                 * need to be placed. This will be used in the initialization of {@code rectangleAreas}
                 */
                int areaRectangle = current_rec.getWidth() * current_rec.getHeight();
                if (areaRectangle > maxAreaOfRectangles) { 
                    maxAreaOfRectangles = areaRectangle;
                }
            }  
        }
    }
    
    /**
     * Fill the rectangleAreaArray such that
     * the value at index i (where i represents an area between 1 and maxAreaOfRectanlges)
     * should be the summation of the areas of all the rectangles with this area 
     */
    private void fillRectangleAreaArray () {
        for (ADT_Rectangle rectangle : rectanglesToBePlaced) {
            int areaOfRectangle = rectangle.getWidth() * rectangle.getHeight();
            rectangleAreas[areaOfRectangle] = rectangleAreas[areaOfRectangle] + areaOfRectangle; 
        }
    }
    
    // findAndInitializeEmptyCells makes a EmptyCell instance for each empty cell that it finds
    private void findAndInitializeEmptyCells(ADT_AreaExtended area) {
        int areaWidth = area.getWidth();
        int areaHeight = area.getHeight();
        for (int i = 0; i < areaWidth;  i++) {
            for (int j = 0; j < areaHeight; j++) {
                ADT_Vector vector = new ADT_Vector (i, j);
                if (! area.isOccupied(vector)) { // empty cell
                    int horizontalSpace = emptyHorizontalSpace(i, j, areaWidth, area);
                    int verticalSpace = emptyVerticalSpace(i, j, areaHeight, area);
                    EmptyCell emptyCell = new EmptyCell(i, j, horizontalSpace, verticalSpace);
                    emptyCells.add(emptyCell); // add to collection of emptyCells
                }   
            }
        }
    }
    
    // calculate the length of the empty horizontal space to which the emptyCell (x,y) belongs
    private int emptyHorizontalSpace (int x, int y, int maxWidth, ADT_AreaExtended area) {
        int emptyHorizontalSpace = 1; // the emptycell itself has a width of 1
        // go to the right
        for (int i = x; i < maxWidth && ! area.isOccupied(new ADT_Vector(i, y)); i++) {
            emptyHorizontalSpace++;
        }
        // go to the left
        for (int j = x; j >= 0 && ! area.isOccupied(new ADT_Vector(j, y)); j--) {
            emptyHorizontalSpace++;
        }
        return emptyHorizontalSpace;
    }
    
    // calculate the length of the empty vertical space to which the emptyCell (x,y) belongs
    private int emptyVerticalSpace (int x, int y, int maxHeight, ADT_AreaExtended area) {
        int emptyVerticalSpace = 1; // the emptycell itself has a height of 1
        // go up
        for (int i = y; i < maxHeight && ! area.isOccupied(new ADT_Vector(x, i)); i++) {
            emptyVerticalSpace++;
        }
        // go down
        for (int j = y; j >= 0 && ! area.isOccupied(new ADT_Vector(x, j)); j--) {
            emptyVerticalSpace++;
        }
        return emptyVerticalSpace;
    }
    
    /**
     * MakeBins makes bins (hèhè) such that each bin can be seen as a collection
     * of EmptyCells that have the same horizontalSpace and verticalSpace length
     * In addition it tracks the highest BinCapacity it encounters for the initialization of the capacityBins array
     */
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
            if (! found_bin) { // there is not already a bin with the same lengths (horizontal & vertical)
                Bin new_bin = new Bin (emptycell.horizontal_space, emptycell.vertical_space);
                bins.add(new_bin);
            }
        }
    }
    
    // fill the capacityBins array
    private void fillCapacityBinsArray() {
        for (Bin bin : bins) {
            capacityBins[bin.capacity] = capacityBins[bin.capacity] + bin.capacity;
        }
    }
    
    /**
     * the EmptyCell class can be seen as a record type in which we store the location (x, y), 
     * the horizontal empty space to which the empty cell belongs, and the vertical empty space.
     */
    private class EmptyCell {
        private int x;
        private int y;
        private int horizontal_space; // length of empty horizontal space to which EmptyCell belongs
        private int vertical_space; // length of empty vertical space to which EmptyCell belongs
        
        public EmptyCell (int x, int y, int horizontal_space, int vertical_space) {
            this.x = x;
            this.y = y;
            this.horizontal_space = horizontal_space;
            this.vertical_space = vertical_space;
        }
        
    }
    
    /**
     * Bin class can be seen as a record type that stores the number of cells (capacity)
     * that have the same length of horizontal and vertical space as a particular instance
     */
    private class Bin {
        private int capacity; // number of emptycells it contains
        private int horizontalSpace; // length of horizontal space
        private int verticalSpace; // length of vertical space
        
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