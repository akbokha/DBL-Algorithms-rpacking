import java.util.ArrayList;

/**
 * Prunes by dominance, i.e. prunes rectangle placements if they leave
 * narrow empty horizontal strips that are not bounded on both sides,
 * or narrow empty vertical strips that are not bounded above and below.
 */
public class Strat_BT_Pruner_NarrowEmptyStrips implements Strat_BT_PrunerInterface {
    private ADT_AreaExtended area;
    private int emptySpaceHeight;
    private int emptySpaceWidth;
    private ADT_AreaExtended areaSpaceBelow = new ADT_AreaExtended(int calculateEmptySpaceHeight(ADT_AreaExtended area, ADT_Rectangle last),
    int calculateEmptySpaceWidth(ADT_AreaExtended area, ADT_Rectangle last), boolean flippable); // area of the empty strip
    private ADT_AreaExtended areaSpaceLeft = new ADT_AreaExtended(int calculateEmptySpaceHeight(ADT_AreaExtended area, ADT_Rectangle last),
    int calculateEmptySpaceWidth(ADT_AreaExtended area, ADT_Rectangle last), boolean flippable); // area of the empty strip

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
        return false;
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
        int endY = 0; // the empty strip may end at the bottom

        // Scan vertically to find the bottom of the empty strip
        if(beginY == 0) return 0; // There is no dominance below possible

        while(lowestPoint == Integer.MAX_VALUE){ // We haven't reached the bottom of the empty strip
            beginY--;

            for (int y = beginY; y => endY; --y) {
                if (!area.isEmptyAt(x, y) && y == beginY) {
                    // Possibly the beginning of a row of borders
                    lowestPoint = y;
                } else if(!area.isEmptyAt(x, y) && lowestPoint == Integer.MAX_VALUE) {
                    /*
                    We encountered a border halfway on the line, but this line hasn't
                    seen a border yet. This means the rectangle is not perfect.
                    */
                    return last.getY() - y;
                }
            }
            // The left side of the bounding rectangle is a good leftmost point
            if(y - 1 == 0) lowestPoint = 0;
        }
        // return the height of the empty strip
        return last.getY() - lowestPoint;
    }

    /**
     * Calculates the width of the empty strip.
     *
     * @param last rectangle to look left for empty strip of rectangle
     * @return width of the empty strip
     */
    private int calculateEmptySpaceWidth(ADT_AreaExtended area, ADT_Rectangle last) {
        int leftMostPoint = Integer.MAX_VALUE; // lowest point of possible perfect rectangle
        int beginX = last.getX();
        int endX = 0;

        // Scan horizontally to find the leftmost point of the strip
        if(beginx == 0) return 0; // There is no dominance below possible

        while (leftMostPoint == Integer.MAX_VALUE) { // We haven't reached the left of the rectangle
            beginX--;

            for(int x = beginX; x => endX; --x) {
                if(!area.isEmptyAt(x, y) && x == beginX) {
                    // This possibly the beginning of a line of borders
                    leftMostPoint = x;
                } else if(!area.isEmptyAt(x, y) && leftMostPoint == Integer.MAX_VALUE) {
                    /*
                    We encountered a border halfway on the line, but this line hasn't
                    seen a border yet. This means the rectangle is not perfect.
                    */
                    return last.getX() -  x;
                }
            }
            // The bottom of the bounding rectangle is a good bottom
            if (x - 1 == 0) leftMostPoint = 0;
        }
        // return the width of the empty strip
        return last.getX() - leftMostPoint;
    }

    /**
     * Creates a separate area with the dimensions of the empty strip.
     *
     * @param area, last, emptySpaceHeight
     * @return area with width of the last placed rectangle, calculated height of the empty space
     */
    public SetStripAreaBottom(ADT_AreaExtended area, ADT_Rectangle last, int emptySpaceHeight) {
        this.area = area; // SET ARRAY LIST OF RECTANGLES TO ARRAY OF AREA
        this.last.getX() = last.getX();
        this.emptySpaceHeight = calculateEmptySpaceHeight(ADT_AreaExtended area, ADT_Rectangle last);
        // if (!rotations) [] rectangleFitsHeight = areaSpaceBelow;  rectangleFitsWidth = areaSpaceLeft
        // else [] rectangleFitsBottom = areaSpaceBelow; rectangleFitsLeft = areaSpaceLeft;
    }

    public void getStripAreaBottom() {
        return false;
    }

    /**
     * Creates a separate area with the dimensions of the empty strip.
     *
     * @param area, last, emptySpaceWidth
     * @return area with width of the last placed rectangle, calculated height of the empty space
     */
    public SetStripAreaLeft(ADT_AreaExtended area, ADT_Rectangle last, int emptySpaceWidth) {
        this.area = area; // SET ARRAY LIST OF RECTANGLES TO ARRAY OF AREA
        this.last.getY() = last.getY();
        this.emptySpaceWidth = calculateEmptySpaceHeight(ADT_AreaExtended area, ADT_Rectangle last);
    }

    public void getStripAreaBottom() {
        return false;
    }

    /**
     * Calls the algorithm on both setStripAreaBottom and setStripAreaLeft to perform rectangle packing with array list of rectangles as input.
     * Returns whether a solution can be found and stops.
     *
     * @return
     */
    public boolean callAlgorithm() {
        return false;
    }
}