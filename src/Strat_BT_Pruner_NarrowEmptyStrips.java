
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
        boolean[] rectangleHeightDisallowed; // array specifying for each rectangle width, the heights of empty space that are disallowed below the rectangle
        boolean[] rectangleWidthDisallowed; // array specifies for each rectangle height, the widths of empty space that are disallowed to the left of the rectangle
        boolean rotations; // rotations are allowed
        rotations = area.canFlip();

        emptySpaceHeight = calculateEmptySpaceHeight(last);
        RectangleType[] rectanglesToBePlaced = this.area.getRectangleTypesToBePlaced(); // array which stores the rectangles with height <= height empty strip to be placed
        ArrayList<RectangleType> rectangleFitsHeight = new ArrayList<>();
        // horizontal rectangle strips
        for(int i=0; i<rectanglesToBePlaced.length; ++i){
            if(rectanglesToBePlaced[i].getHeight() <= emptySpaceHeight){
                rectangleFitsHeight.add(rectanglesToBePlaced[i]);
            }
        }


        emptySpaceWidth = calculateEmptySpaceHeight(last);
        ArrayList<RectangleType> rectangleFitsWidth = new ArrayList<>(); // array list which stores the rectangles with width <= width empty strip to be placed
        for (int i = 0; i < rectanglesToBePlaced.length; i++) {
            if (rectanglesToBePlaced[i].getWidth() <= emptySpaceWidth) {
                rectangleFitsWidth.add(rectanglesToBePlaced[i]);
            }
        }

        // create one binary matrix if rotations are allowed
        if (rotations) {
            for (int i = 0; i < area.getCount(); i++) {
                // if rectangle w
                // boolean[i] rectangleWidthDisallowed = true;
            }
        } else {
            // create two binary matrices
        }
        // if all future placements fit below or to the left of the candidate rectangle
        // then reject (return true)
        return narrowStripsBelow(last) || narrowStripsLeft(last);
    }

    /**
     * Checks if all rectangles less than or equal to h to be placed can be
     * placed in the empty strip below the rectangle. h is the minimum dimension
     * if rotations are allowed.
     * If all placements are possible, then do not allow the original rectangle
     * to be placed in this candidate position.
     *
     * @param last rectangle to look under for rectangle of empty space
     * @return height of the empty space
     */
    private int calculateEmptySpaceHeight(ADT_AreaExtended area, ADT_Rectangle last){
        int lowestPoint = Integer.MAX_VALUE; // lowest point of possible perfect rectangle
        int beginX = last.getX();
        int endX = last.getX()+last.getWidth();

        // Scan horizontally to find bottom of a possibly perfect rectangle
        int y = last.getY();

        if(y==0) return false; // There is no dominance below possible

        while(lowestPoint == Integer.MAX_VALUE){ // We haven't reached the bottom of the rectangle
            y--;

            for(int x = beginX; x < endX; ++x){
                if(!area.isEmptyAt(x, y) && x == beginX){
                    /*
                    This possibly the beginning of a line of borders
                    */
                    lowestPoint = y;
                }else if(!area.isEmptyAt(x, y) && lowestPoint == Integer.MAX_VALUE){
                    /*
                    We encountered a border halfway on the line, but this line hasn't
                    seen a border yet. This means the rectangle is not perfect.
                    */
                    return false;
                }else if(area.isEmptyAt(x, y) && lowestPoint != Integer.MAX_VALUE){
                    /*
                    We found no border at this point, because we did already find
                    border on this line, the rectangle is not perfect.
                    */
                    return false;
                }
            }

            // The bottom of the bounding rectangle is a good bottom
            if(y-1 == 0) lowestPoint = y;
        }

        // Verify if the rectangle is perfect (there is a solid border on the sides)
        for(int i=lowestPoint; i<last.getY(); ++i){
            if(area.isEmptyAt(last.getX()-1, i)){
                /*
                If there is no border on the left side, the rectangle is not perfect
                */
                return false;
            }

            if(area.isEmptyAt(last.getX()+last.getWidth(), i)){
                /*
                Check the same on the right side
                */
                return false;
            }
        }

        /*
        All checks passed, so we found a perfect rectangle
        */
        return 0;
    }

    /**
     * Checks if all rectangles less than or equal to w to be placed can be
     * placed in the empty strip to the left of the rectangle.
     * w is the minimum dimension if rotations are allowed.
     * If all placements are possible, then do not allow the original rectangle
     * to be placed in this candidate position.
     *
     * @param last rectangle to look left for rectangle of empty space
     * @return height of the empty space
     */
    private int calculateEmptySpaceWidth(ADT_AreaExtended area, ADT_Rectangle last) {
        int leftMostPoint = Integer.MAX_VALUE; // lowest point of possible perfect rectangle
        int beginY = last.getY();
        int endY = last.getY()+last.getHeight();

        // Scan vertically to find bottom of a possibly perfect rectangle
        int x = last.getX();
        if(x==0) return false;

        while(leftMostPoint == Integer.MAX_VALUE){
            x--;

            for(int y = beginY; y < endY; ++y){
                if(!area.isEmptyAt(x, y) && y == beginY){
                    /*
                    This possibly the beginning of a row of borders
                    */
                    leftMostPoint = x;
                }else if(!area.isEmptyAt(x, y) && leftMostPoint == Integer.MAX_VALUE){
                    /*
                    We encountered a border halfway on the line, but this line hasn't
                    seen a border yet. This means the rectangle is not perfect.
                    */
                    return false;
                }else if(area.isEmptyAt(x, y) && leftMostPoint != Integer.MAX_VALUE){
                    /*
                    We found no border at this point, because we did already find
                    border on this line, the rectangle is not perfect.
                    */
                    return false;
                }
            }

            // The left side of the bounding rectangle is a good leftmost point
            if(x-1 == 0) leftMostPoint = x;
        }

        // Verify if the rectangle is perfect (there is a solid border on the sides)
        for(int i=leftMostPoint; i<last.getX(); ++i){
            if(area.isEmptyAt(i, last.getY()-1)){
                /*
                If there is no border on the bottom side, the rectangle is not perfect
                */
                return false;
            }

            if(area.isEmptyAt(i, last.getY()+last.getHeight())){
                /*
                Check the same on the top side
                */
                return false;
            }
        }

        /*
        All checks passed, so we found a perfect rectangle
        */
        return 0;
    }
}