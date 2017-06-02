import java.util.ArrayList;
import java.util.Arrays;

/*
 * @todo requires rotate rectangle function
 * @todo requires function to remove rectangles from the set of placed rectangles.
 * @notice currently it is possible that the position of the rectangle in the array is different
 *      than what it defined in its own coordinate variables. Has potential for unreliable behaviour.
 */
public class ADT_AreaExtended extends ADT_Area implements Cloneable {
    private final short EMPTY_INDEX = 0;
    private ADT_Rectangle[] rectangles;
    private boolean[] placedRectangles;
    private short[] array;
    private final int version; // 3, 5, 10, 25 or 10000
    
    public ADT_AreaExtended(int width, int height, boolean flippable, ADT_Rectangle[] rectangles) {
        super(width, height, flippable, rectangles);
        setDimensions(width, height);
        this.rectangles = rectangles;
        this.placedRectangles = new boolean[rectangles.length];
        this.version = rectangles.length;
    }
    
    @Override
    public int getVersion(){
        return this.version;
    }
    
    public ADT_Area toArea() {
        return new ADT_Area(width, height, flippable, rectangles);
    }

    @Override
    public ADT_AreaExtended clone() throws CloneNotSupportedException {
        super.clone();
        
        ADT_AreaExtended newArea = new ADT_AreaExtended(getWidth(), getHeight(), canFlip(), Arrays.copyOf(rectangles, rectangles.length));
        
        newArea.rectangles = Arrays.copyOf(rectangles, rectangles.length);
        
        return newArea;
    }

    private int getIndex(int x, int y) {
        return y * getWidth() + x;
    }

    private void setArrayAt(int x, int y, short val) {
        int i = getIndex(x, y);
        array[i] = val;
    }

    /*
     * Returns true if there is no rectangle border at this position
     */
    public boolean isEmptyAt(int x, int y) {
        if (x < 0 || y < 0 || y > getHeight() || x > getWidth()) { // @todo replace for assert in final version
            throw new IllegalArgumentException();
        }
        int i = getIndex(x, y);
        return array[i] == EMPTY_INDEX;
    }

    /**
     * Adds an rectangle to this area.
     *
     * @param i
     */
    public void add(int i, int x, int y) {
        placedRectangles[i] = true;

        // Define its coordinates.
        rectangles[i].setX(x);
        rectangles[i].setY(y);

        // Update the two-dimensional array
        fillRectangleBordersWith(rectangles[i], (short)i);
    }
    
    /**
     *
     * @param i
     */
    public void remove(int i) {
        placedRectangles[i] = false;
        removeRectangleBorders(rectangles[i]);
    }

    private void fillRectangleBordersWith(ADT_Rectangle shape, short id) {
        //Set horizontal borders to this shape's id
        for (int x = shape.getX(), max = x + shape.getWidth() - 1; x <= max; x++) {
            setArrayAt(x, shape.getY(), id);
            setArrayAt(x, shape.getY() + shape.getHeight(), id);
        }

        //Set vertical borders
        for (int y = shape.getX(), max = y + shape.getHeight() - 1; y <= max; y++) {
            setArrayAt(shape.getX(), y, id);
            setArrayAt(shape.getX() + shape.getWidth(), y, id);
        }
    }

    /**
     * Checks if an rectangle with would intersect on a specific position.
     *
     * @param posX
     * @param posY
     * @param width
     * @param height
     * @return True if an intersection was detected, else false.
     */
    boolean checkIntersection(int posX, int posY, int width, int height) {
        //Check horizontal borders to this shape's id
        for (int x = posX, max = posX + width - 1; x <= max; x++) {
            if (! isEmptyAt(x, posY)) {
                return true;
            }
            if (! isEmptyAt(x, posY + height)) {
                return true;
            }
        }

        //Check vertical borders
        for (int y = posY, max = posY + height - 1; y <= max; y++) {
            if(! isEmptyAt(posX, y)) {
                return true;
            }
            if(! isEmptyAt(posX + width, y)) {
                return true;
            }
        }

        return false;
    }

    boolean getRectangleIsPlaced(int index) {
        return placedRectangles[index];
    }

    ADT_Rectangle[] getRectanglesToBePlaced() {
        ArrayList<ADT_Rectangle> result = new ArrayList<>(rectangles.length / 2);
        for (int i = 0; i < rectangles.length; i++) {
            if (!placedRectangles[i]) {
                result.add(rectangles[i]);
            }
        }

        ADT_Rectangle[] arrayResult = new ADT_Rectangle[result.size()];
        result.toArray(arrayResult);

        return arrayResult;
    }

    /*
     * Returns the already placed rectangles in the area
     */
    public Iterable<ADT_Rectangle> getPlacedRectangles() {
        return Arrays.asList(rectangles);
    }

    /**
     * Gives the amount of rectangles this area contains.
     *
     * @return the amount of rectangles this area contains.
     */
    @Override
    public int getCount() {
        return rectangles.length;
    }

    @Override
    public ADT_Rectangle[] getRectangles() {
        return rectangles;
    }

    @Override
    public boolean isNewRectangleValid(ADT_Rectangle rectangle) {
        throw new UnsupportedOperationException("Currently not supported because of the ambiguity of its definition");
    }

    @Override
    public boolean isOccupied(ADT_Vector position) {
        return !isEmptyAt(position.x, position.y);
    }

    /**
     * Returns the strips populated with the number of empty spaces in each strip
     * @return 
     */
    @Override
    public int[] getEmptySpaceStrips(boolean horizontal) {
        return scanStrips(horizontal, true);
    }

    /**
     * Returns the strips populated with the total sum of (minimal side) occupied spaces of the to be placed rectangles.
     * @return 
     */
    @Override
    public int[] getRectangleStrips(boolean horizontal) {
        return scanStrips(horizontal, false);
    }

    public boolean moveRectangle(ADT_Rectangle rectangle, int newX, int newY) {
        if(! checkIntersection(newX, newY, rectangle.getWidth(), rectangle.getHeight())) {
            return false;
        }

        //Remember the original rectangle id by getting the id on it's position
        short id = (short)getIndex(rectangle.getX(), rectangle.getY());
        //Remove the original rectangle information
        fillRectangleBordersWith(rectangle, EMPTY_INDEX);

        rectangle.setX(newX);
        rectangle.setY(newY);

        fillRectangleBordersWith(rectangle, id);

        return true;
    }

    private int[] scanStrips(boolean horizontal, boolean lookingForEmpty) {
        int[] vals;
        if (horizontal) {
            vals = new int[getHeight()];
            for (int i = 0; i < vals.length; i++) {
                for (int x = 0; x < getWidth(); x++) {
                    if(isEmptyAt(x, i) == lookingForEmpty) {
                        vals[i]++;
                    }
                }
            }
        } else {
            vals = new int[getWidth()];
            for (int i = 0; i < vals.length; i++) {
                for (int y = 0; y < getHeight(); y++) {
                    if(isEmptyAt(i, y) == lookingForEmpty) {
                        vals[i]++;
                    }
                }
            }
        }
        return vals;
    }

    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;

        array = new short[width * height];
    }

    public int getTotalAreaRectanglesToBePlaced() {
        int total = 0;
        for (int i = 0; i < rectangles.length; i++) {
            if (! placedRectangles[i]) {
                ADT_Rectangle rec = rectangles[i];
                total += rec.getHeight() * rec.getWidth();
            }
        }
        return total;
    }
    
    @Override
    public int getTotalAreaRectangles(){
        int total = 0;
        for(ADT_Rectangle rec : rectangles){
            total += rec.getHeight() * rec.getHeight();
        }
        total += getTotalAreaRectanglesToBePlaced();
        return total;
    }

    @Override
    public boolean isValid() {
        ArrayList<ADT_Rectangle> checkedRecs = new ArrayList<>();

        for(ADT_Rectangle currentRec : getPlacedRectangles()) {

            // Check if the newly added rectangle has valid coordinates;
            if (currentRec.getX() < 0 || currentRec.getY() < 0) {
                return false;
            } else if (
                    (this.getWidth() != ADT_Rectangle.INF && currentRec.getX() + currentRec.getWidth() > this.getWidth()) // !(this.w != inf ==> rec.x + rec.w <= this.w)
                            || (this.getHeight() != ADT_Rectangle.INF && currentRec.getY() + currentRec.getHeight() > this.getHeight()) // !(this.h != inf ==> rec.y + rec.h <= this.h)
                    ) {
                return false;
            }

            // Check if the newly added rectangle intersects with any previously checked rectangle.
            for(ADT_Rectangle rec : checkedRecs) {
                if (checkRectangleOverlap(currentRec, rec)) {
                    return false;
                }
            }
            checkedRecs.add(currentRec);
        }
        return true;
    }

    /**
     * Checks if the body of two rectangles overlap, the edges may intersect.
     *
     * @param rec1 The first rectangle to be taken into account
     * @param rec2 The second rectangle to be taken into account
     * @return False if the body of the rectangles intersect, true otherwise.
     */
    private boolean checkRectangleOverlap(ADT_Rectangle rec1, ADT_Rectangle rec2) {
        assert rec1 != null;
        assert rec2 != null;
        assert rec1.getWidth() != ADT_Rectangle.INF;
        assert rec2.getWidth() != ADT_Rectangle.INF;
        assert rec1.getHeight() != ADT_Rectangle.INF;
        assert rec2.getWidth() != ADT_Rectangle.INF;
        assert rec1.getX() != ADT_Rectangle.NOTSET;
        assert rec2.getX() != ADT_Rectangle.NOTSET;
        assert rec1.getY() != ADT_Rectangle.NOTSET;
        assert rec2.getY() != ADT_Rectangle.NOTSET;

        Point l1 = new Point(rec1.getX(), rec1.getY() + rec1.getHeight()); // Top left coordinate of first rectangle
        Point r1 = new Point(rec1.getX() + rec1.getWidth(), rec1.getY()); // Bottom right coordinate of first rectangle
        Point l2 = new Point(rec2.getX(), rec2.getY() + rec2.getHeight()); // Top left coordinate of second rectangle
        Point r2 = new Point(rec2.getX() + rec2.getWidth(), rec2.getY()); // Bottom right coordinate of second rectangle

        if (l1.getX() >= r2.getX() || l2.getX() >= r1.getX()) { // Check if one rectangle is on the left side of the other rectangle.
            return false;
        } else return !(l1.getY() <= r2.getY() || l2.getY() <= r1.getY()); // Check if one rectangle is above the other rectangle.
        
    }

    private void removeRectangleBorders(ADT_Rectangle shape) {
        fillRectangleBordersWith(shape, EMPTY_INDEX);
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
