
import java.util.ArrayList;
import java.util.Iterator;

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
    private ArrayList<Bin> bins;
    private ArrayList<ADT_Rectangle> rectanglesToBePlaced;
    private int[] rectangleAreas; // index = area, a[i] = sum of all the areas of the rectangles with area == index
    private int totalAreaRectangles;
    private static final int NOTSET = -2;
    int maxAreaOfRectangles = 0;
    
    @Override
    public boolean reject (ADT_Area area, ADT_Rectangle last) {
         boolean reject = false;
         totalAreaRectangles = area.getTotalAreaRectangles(); // store sum areas rectangle
         
         /**
          * Operations that are needed to initialize and fill the array
          * The index of the array represents the area value
          * The value at array[index] = the sum of all the rectangles that have 
          * an area that is equal to the index
          */
         collectRectanglesToBePlaced(area); // add the rectangles that still neet to be placed to the collection
         rectangleAreas = new int[maxAreaOfRectangles]; // initialize the desribed array
         fillRectangleAreaArray(); // fill the rectangle
         
         
         findAndInitializeEmptyCells(area);
         
        //this.horizontalEmptySpaceStrips = getEmptySpaceStrips(true);
        //this.verticalEmptySpaceStrips = getEmptySpaceStrips(false);
        //this.horizontalRectangleStrips = getRectangleStrips(true);
        //this.verticalRectangleStrips = getRectangleStrips(false);
        
        
        return reject;
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
        for (int i = 0; i < ((ADT_AreaExtended)area).getWidth(); i++) {
            for (int j = 0; j < ((ADT_AreaExtended)area).getHeight(); j++) {
                ADT_Vector vector = new ADT_Vector (i, j);
                if (! area.isOccupied(vector)) {
                    int horizontalspace;
                    int verticalspace;
                }   
            }
        }
    }
    
    private class EmptyCell {
        int x;
        int y;
        int horizontal_space; // empty horizontal space to which EmptyCell belongs
        int vertical_space; // empty vertical space to which EmptyCell belongs
        
        public EmptyCell (int x, int y, int horizontal_space, int vertical_space) {
            this.x = x;
            this.y = y;
            this.horizontal_space = horizontal_space;
            this.vertical_space = vertical_space;
        }
    }
    
    private class Bin {
        int capacity = 0; // number of emptycells it contains
        int width;
        int height;
        int area;
        
        public Bin (int width, int height) {
            this.width = width;
            this.height = height;
        }
        
        public void increaseCapacity () {
            capacity++;
        }
    }
    
}