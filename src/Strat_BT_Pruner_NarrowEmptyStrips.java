import java.util.ArrayList;

/**
 * Prunes by dominance, i.e. prunes rectangle placements if they leave
 * narrow empty horizontal strips that are not bounded on both sides,
 * or narrow empty vertical strips that are not bounded above and below.
 *
 * @author Phung
 */
public class Strat_BT_Pruner_NarrowEmptyStrips implements Strat_BT_PrunerInterface {
    private ADT_AreaExtended area;
    private int emptySpaceHeight;
    private int emptySpaceWidth;

    @Override
    public boolean reject(ADT_Area area, ADT_Rectangle last) {
        this.area = (ADT_AreaExtended) area;
        emptySpaceHeight = calculateEmptySpaceHeight((ADT_AreaExtended) area, last); // height of the empty strip on the left of the rectangle
        emptySpaceWidth = calculateEmptySpaceWidth((ADT_AreaExtended) area, last); // width of the empty strip below the rectangle
        RectangleType[] rectanglesToBePlaced = this.area.getRectangleTypesToBePlaced(); // rectangles to be placed
        boolean rotations = area.canFlip(); // rotations are allowed

        if (!rotations) {
            ArrayList<RectangleType> rectangleFitsHeight = new ArrayList<>(); // array list which stores the rectangles with height <= height empty strip

            for (int i = 0; i < rectanglesToBePlaced.length; ++i) { // add all rectangles <= height of the empty strip
                if (rectanglesToBePlaced[i].getHeight() <= emptySpaceHeight) {
                    rectangleFitsHeight.add(rectanglesToBePlaced[i]);
                }
            }
            ArrayList<RectangleType> rectangleFitsWidth = new ArrayList<>(); // array list which stores the rectangles with width <= width empty strip

            for (int i = 0; i < rectanglesToBePlaced.length; i++) { // adds all rectangles <= width to the empty strip
                if (rectanglesToBePlaced[i].getWidth() <= emptySpaceWidth) {
                    rectangleFitsWidth.add(rectanglesToBePlaced[i]);
                }
            }
        } else {
            ArrayList<RectangleType> rectangleFitsBottom = new ArrayList<>(); // stores rectangles that fit into the bottom empty strip
            ArrayList<RectangleType> rectangleFitsLeft = new ArrayList<>(); // stores rectangles that fit into the left empty strip

            for (int i = 0; i < rectanglesToBePlaced.length; i++) {
                if (rectanglesToBePlaced[i].getHeight() <= emptySpaceHeight || rectanglesToBePlaced[i].getWidth() <= emptySpaceHeight) {
                    rectangleFitsBottom.add(rectanglesToBePlaced[i]); // width or height <= width means a rectangle fits
                }
                if (rectanglesToBePlaced[i].getWidth() <= emptySpaceWidth || rectanglesToBePlaced[i].getHeight() <= emptySpaceWidth) {
                    rectangleFitsLeft.add(rectanglesToBePlaced[i]);
                }
            }
        }
        // if all future placements fit below or to the left of the candidate rectangle
        // then reject (return true)
        return true; // TODO: call the algorithm to perform rectangle packing with array list of rectangles and empty strip dimensions as input.
                    // Return true there is at least one solution.
    }

    /**
     * Calculates the height of the empty strip.
     *
     * @param last rectangle to look under for empty strip of rectangle
     * @return height of the empty space
     */
    private int calculateEmptySpaceHeight(ADT_AreaExtended area, ADT_Rectangle last){
        int lowestPoint = Integer.MAX_VALUE; // lowest point of possible empty strip
        int beginY = last.getY();
        int endY = 0; // the empty strip may

        // Scan vertically to find the bottom of the empty strip
        int x = last.getX();
        if(x == 0) return 0; // There is no dominance below possible

        while(lowestPoint == Integer.MAX_VALUE){ // We haven't reached the bottom of the empty strip
            x--;

            for (int y = beginY; y > endY; --y) {
                if (!area.isEmptyAt(x, y) && y == beginY) {
                    // Possibly the beginning of a row of borders
                    lowestPoint = x;
                } else if(!area.isEmptyAt(x, y) && lowestPoint == Integer.MAX_VALUE) {
                    /*
                    We encountered a border halfway on the line, but this line hasn't
                    seen a border yet. This means the rectangle is not perfect.
                    */
                    return x;
                }
            }
            // The left side of the bounding rectangle is a good leftmost point
            if(x-1 == 0) lowestPoint = x;
        }
        // return the height of the empty strip
        return x;
    }

    /**
     * Calculates the width of the empty strip
     *
     * @param last rectangle to look left for empty strip of rectangle
     * @return width of the empty strip
     */
    private int calculateEmptySpaceWidth(ADT_AreaExtended area, ADT_Rectangle last) {
        int leftMostPoint = Integer.MAX_VALUE; // lowest point of possible perfect rectangle
        int beginX = last.getX();
        int endX = 0;

        // Scan horizontally to find bottom of a possibly perfect rectangle
        int y = last.getY();

        if(y == 0) return 0; // There is no dominance below possible

        while (leftMostPoint == Integer.MAX_VALUE) { // We haven't reached the bottom of the rectangle
            y--;

            // First check right and left border
            if(!(area.isEmptyAt(beginX-1, y) && area.isEmptyAt(beginX+endX, y))) {
                // This must be the lowest level, otherwise it is not valid  (missing side border)
                leftMostPoint = y;
            }

            for(int x = beginX; x < endX; ++x) {
                if(!area.isEmptyAt(x, y) && x == beginX) {
                    // This possibly the beginning of a line of borders
                    leftMostPoint = y;
                } else if(!area.isEmptyAt(x, y) && leftMostPoint == Integer.MAX_VALUE) {
                    /*
                    We encountered a border halfway on the line, but this line hasn't
                    seen a border yet. This means the rectangle is not perfect.
                    */
                    return y;
                }
            }
            // The bottom of the bounding rectangle is a good bottom
            if (y - 1 == 0) leftMostPoint = y;
        }
        // return the width of the empty strip
        return y;
    }
}