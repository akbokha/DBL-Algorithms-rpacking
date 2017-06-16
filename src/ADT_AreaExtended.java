import java.util.ArrayList;

/*
 * @todo requires rotate rectangle function
 * @notice currently it is possible that the position of the rectangle in the array is different
 *      than what it defined in its own coordinate variables. Has potential for unreliable behaviour.
 *      When using this class, do not alter the rectangle positions directly, but use methods supplied
 *      by this class instead.
 */
public class ADT_AreaExtended extends ADT_Area implements Cloneable {
    private final short EMPTY_INDEX = Short.MIN_VALUE;
    private ADT_Rectangle[] rectangles;
    private boolean[] placedRectangles;
    private short[] array;
    private final int version; // 3, 5, 10, 25 or 10000
    
    public ADT_AreaExtended(int width, int height, boolean flippable, ADT_Rectangle[] rectangles) {
        super(width, height, flippable, rectangles);
        sortAs(rectangles, new ADT_SortOnDimension());
        setDimensions(width, height);
        this.rectangles = rectangles;
        this.placedRectangles = new boolean[rectangles.length];
        this.version = rectangles.length;
    }
    
    /**
     * Converts this to ADT_Area
     * @return 
     */
    public ADT_Area toArea() {
        return new ADT_Area(width, height, flippable, rectangles);
    }

    @Override
    public ADT_AreaExtended clone() {
        super.clone();
        ADT_Rectangle[] newShapes = new ADT_Rectangle[rectangles.length];
        
        for(int i = 0; i < rectangles.length; i++) {
            newShapes[i] = rectangles[i].clone();
        }
        ADT_AreaExtended newArea = new ADT_AreaExtended(getWidth(), getHeight(), canFlip(), newShapes);
        
        return newArea;
    }
    
    /**
     * Get an index for the array in which 
     * @param x
     * @param y
     * @return 
     */
    private int getIndex(int x, int y) {
        return y * getWidth() + x;
    }
    
    /**
     * 
     * @param x
     * @param y
     * @param val 
     */
    private void setArrayAt(int x, int y, short val) {
        assert x >= 0 && y >= 0 && x < width && y < height;

        int i = getIndex(x, y);
        array[i] = val;
    }

    /**
     * Returns true if there is no rectangle border at this position
     * @param x
     * @param y
     * @return 
     */
    boolean isEmptyAt(int x, int y) {
        assert x >= 0 && y >= 0 && x < width && y < height;

        int i = getIndex(x, y);
        if(i >= array.length || i < 0) {
            throw new IllegalArgumentException(Integer.toString(i) + ", max = " + Integer.toString(array.length));
        }
        return array[i] == EMPTY_INDEX;
    }

    /**
     * Places a rectangle inside the area.
     *
     * @param i The index of the rectangle.
     * @param x The x-coordinate where the rectangle should be placed.
     * @param y The y-coordinate where the rectangle should be placed.
     */
    void add(int i, int x, int y) {
        placedRectangles[i] = true;
        // Define its coordinates.
        rectangles[i].setX(x);
        rectangles[i].setY(y);

        // Update the two-dimensional array
        fillRectangleBordersWith(rectangles[i], (short)i);
    }
    
    /**
     * Undoes the placement of a rectangle.
     *
     * @param i The index of the rectangle.
     */
    void remove(int i) {
        assert placedRectangles[i];
        
        placedRectangles[i] = false;
        ADT_Rectangle rectangle = rectangles[i];
        removeRectangleBorders(rectangle);

        rectangle.setX(ADT_Rectangle.NOTSET);
        rectangle.setY(ADT_Rectangle.NOTSET);
    }

    /**
     * Fills the inner borders of a rectangle in the two-dimensional placement array.
     *
     * @param shape The shape which borders should be added.
     * @param id The value the borders should have.
     */
    private void fillRectangleBordersWith(ADT_Rectangle shape, short id) {
        int xx = shape.getX();
        int yy = shape.getY();
        int height = shape.getHeight();
        int width = shape.getWidth();

        //Set horizontal borders to this shape's id
        for (int x = xx, max = x + width; x < max; x++) {
            setArrayAt(x, yy, id);
            setArrayAt(x, yy + height-1, id);
        }

        //Set vertical borders
        for (int y = yy + 1, max = y + height - 1; y < max; y++) { // Skipping the corners (o1)
            setArrayAt(xx, y, id);
            setArrayAt(xx + width-1, y, id);
        }
    }

    /**
     * Checks if an rectangular area with would intersect with any other currently placed rectangles.
     *
     * @param posX The x-coordinate of the area to be checked.
     * @param posY The y-coordinate of the area to be checked.
     * @param width The width of the area to be checked.
     * @param height The height of the area to be checked.
     * @return 0 if no intersection was detected, or the x-coordinates adjacent on the right side of one of the intersecting
     *      rectangles.
     * Optimization (to be verified): Might be more efficient to first check the right most rectangles, since they will
     *      potentially cause a larger horizontal displacement. Thus decreasing the amount of coordinates which have to be checked.
     */
    int checkIntersection(int posX, int posY, int width, int height) {
        // Check horizontal borders to this shape's id
        for (int x = posX, max = posX + width; x < max; x++) {
            int res = isRectangleAt(x, posY);
            if (res != 0) {
                return res;
            }

            res = isRectangleAt(x, posY + height - 1);
            if (res != 0) {
                return res;
            }
        }

        // Check vertical borders
        for (int y = posY + 1, max = posY + height - 1; y < max; y++) { // Skipping the corners (o1)
            int res = isRectangleAt(posX, y);
            if(res != 0) {
                return res;
            }

            res = isRectangleAt(posX + width - 1, y);
            if(res != 0) {
                return res;
            }
        }

        return 0;
    }

    /**
     * Checks if there is a rectangle border at a specific coordinate.
     *
     * @param x The x-coordinate to be checked.
     * @param y The y-coordinate to be checked.
     * @return 0 if no rectangle was found at this coordinate, else the x-coordinate adjacent to the
     *      right side of the rectangle inhabiting this border.
     */
    private int isRectangleAt(int x, int y) {
        assert x >= 0 && y >= 0 && x < width && y < height;

        int index = array[getIndex(x, y)];

        if (index == EMPTY_INDEX) {
            return 0;
        } else {
            return rectangles[index].getWidth() + rectangles[index].getX();
        }
    }
    
    /**
     * Checks is a rectangle is placed.
     *
     * @param index The index of the rectangle.
     * @return True if the rectangle is placed, else false.
     */
    boolean getRectangleIsPlaced(int index) {
        return placedRectangles[index];
    }

    /**
     * Gives all rectangles which currently are not placed.
     *
     * @return An array containing all rectangles that still need to be placed

     */
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
     * Calculates an array of the empty space cut in strips
     * 
     * @return an array <code>a</code> with the index, the length of the strips
     * and <code>a[i]</code> the amount of strips of that length
     */
    @Override
    public int[] getEmptySpaceStrips(boolean horizontal) {
        int[] vals;
        if (horizontal) {
            vals = new int[getWidth()+1];
            int stripLength = 0;
            for (int y = 0; y < getHeight(); y++) {
                if(stripLength > 0) {
                    vals[stripLength]++;
                    stripLength = 0;
                }
                for(int x = 0; x < getWidth(); x++) {
                    if(isEmptyAt(x, y)) {
                        stripLength++;
                    } else if(stripLength > 0){
                        vals[stripLength]++;
                        stripLength = 0;
                        x += rectangles[array[getIndex(x, y)]].getWidth();
                    }
                }
            }
        } else {
            vals = new int[getHeight()+1];
            int stripLength = 0;
            for (int x = 0; x < getWidth(); x++) {
                if(stripLength > 0) {
                    vals[stripLength]++;
                    stripLength = 0;
                }
                for(int y = 0; y < getHeight(); y++) {
                    if(isEmptyAt(x, y)) {
                        stripLength++;
                    } else if(stripLength > 0){
                        vals[stripLength]++;
                        stripLength = 0;
                        y += rectangles[array[getIndex(x, y)]].getHeight();
                    }
                }
            }
        }
        return vals;
    }

    /**
     * Calculates an array of the rectangles to be placed cut in strips
     * 
     * @return an array <code>a</code> with the index, the length of the strips
     * and <code>a[i]</code> the amount of strips of that length
     */
    @Override
    public int[] getRectangleStrips(boolean horizontal) {
        ADT_Rectangle[] toBePlacedRectangles = getRectanglesToBePlaced();
        if(toBePlacedRectangles.length == 0) return new int[]{0};
        int[] vals;
        if(horizontal) {
            int maxWidth = 0;
            
            for(ADT_Rectangle rec : rectangles) {
                if(maxWidth < rec.getWidth()) maxWidth = rec.getWidth();
            }
            
            vals = new int[maxWidth + 1];
            for(ADT_Rectangle rec : toBePlacedRectangles) {
                vals[rec.getWidth()] = rec.getHeight();
            }
        } else {
            int maxHeight = 0;
            
            for(ADT_Rectangle rec : rectangles) {
                if(maxHeight < rec.getHeight()) maxHeight = rec.getHeight();
            }
            
            vals = new int[maxHeight+1];
            for(ADT_Rectangle rec : toBePlacedRectangles) {
                vals[rec.getHeight()] = rec.getWidth();
            }
        }
        
        return vals;
    }
    
    /**
     * Sets the width, height, and multi-dimensional array.
     *
     * @param width The new width of this area.
     * @param height The height of this area.
     */
    private void setDimensions(int width, int height) {

        this.width = width;
        this.height = height;

        array = new short[width * height];
        for (int i = 0, max = array.length; i < max; i++) {
            array[i] = EMPTY_INDEX;
        }
    }
    
    /**
     * Computes the sum of the area of all rectangles which still have to be placed.
     *
     * @return The sum of the area of all rectangles which still have to be placed.
     */
    int getTotalAreaRectanglesToBePlaced() {

        int total = 0;
        for (int i = 0; i < rectangles.length; i++) {
            if (! placedRectangles[i]) {
                ADT_Rectangle rec = rectangles[i];
                total += rec.getHeight() * rec.getWidth();
            }
        }
        return total;
    }
    
    float getFillRate() {
        float total = 0;
        for(int i = 0; i < rectangles.length; i++){
            if(placedRectangles[i]) {
                total += rectangles[i].getHeight() * rectangles[i].getHeight();
            }
        }
        return total/(width*height);
    }
    
    @Override
    public int getTotalAreaRectangles(){
        int total = 0;
        for(ADT_Rectangle rec : rectangles){
            total += rec.getHeight() * rec.getHeight();
        }
        return total;
    }

    @Override
    public boolean isValid() {
        throw new UnsupportedOperationException();
    }

    /**
     * Checks if the body of two rectangles overlap, the edges may intersect.
     *
     * @param rec1 The first rectangle to be taken into account
     * @param rec2 The second rectangle to be taken into account
     * @return False if the body of the rectangles intersect, true otherwise.
     */
    @Override
    public boolean checkRectangleOverlap(ADT_Rectangle rec1, ADT_Rectangle rec2) {
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
/*
 * o1) First and last y coordinate can be skipped, since these are the corners which are already checked when the
 *      horizontal strips are checked.
 */