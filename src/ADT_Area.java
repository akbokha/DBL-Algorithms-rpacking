import java.util.*;

@SuppressWarnings("Duplicates")
public class ADT_Area extends ADT_Rectangle implements Cloneable {

    private ADT_Rectangle[] shapes;

    public ADT_Area(int width, int height, boolean flippable, ADT_Rectangle[] rectangles) {
        super(width, height, 0, 0, flippable);
        shapes = rectangles;
    }
    
    
    
    @Override
    public ADT_Area clone() {
        super.clone();
        ADT_Rectangle[] newShapes = new ADT_Rectangle[shapes.length];
        
        for(int i = 0; i < shapes.length; i++) {
            newShapes[i] = shapes[i].clone();
        }
        
        ADT_Area newArea = new ADT_Area(this.getWidth(), this.getHeight(), this.canFlip(), newShapes);
        return newArea;
    }

    /**
     * Gives the amount of rectangles this area contains.
     *
     * @return the amount of rectangles this area contains.
     */
    public int getCount() {
        return shapes.length;
    }

    /**
     * Constructs an iterator over all rectangles.
     *
     * @return iterator over all rectangles it contains.
     */
    public Iterator<ADT_Rectangle> getRectangleIterator() {
        return Arrays.asList(shapes).iterator();
    }
    
    public ADT_Rectangle[] getRectangles() {
        return shapes;
    }
    
    public ADT_AreaExtended toExtended(int width, int height) {
        return new ADT_AreaExtended(width, height, canFlip(), shapes.clone());
    }
    
    @Override
    public ADT_Vector getDimensions() {
        int maxWidth = getWidth();
        int maxHeight = getHeight();

        // Check if either the width or the height is infinite, if so replace them with the minimal dimension.
        if (maxWidth == INF || maxHeight == INF) {
            ADT_Vector minDimensions = getMinimalDimensions();

            if (maxWidth == INF) {
                maxWidth = minDimensions.x;
            }
            if (maxHeight == INF) {
                maxHeight = minDimensions.y;
            }
        }

        return new ADT_Vector(maxWidth, maxHeight);
    }

    /**
     * Computes the actual area from its dimensions.
     * @return The area of this shape.
     */
    public int getArea() {
        ADT_Vector dimensions = this.getDimensions();

        return dimensions.x * dimensions.y;
    }
    
    private ADT_Vector getMinimalDimensions() {
        ADT_Rectangle[] i = getRectangles();
        int maxX = 0;
        int maxY = 0;

        for (ADT_Rectangle r : i) {
            if (r.getWidth() != ADT_Rectangle.INF) {
                maxX = Math.max(r.getWidth() + r.getX(), maxX);
                maxY = Math.max(r.getHeight() + r.getY(), maxY);
            }
        }

        return new ADT_Vector(maxX, maxY);
    }
    
    /**
     * Return true if none of the already placed rectangles overlap with the parameter rectangle
     * @pre Rectangle coordinates are non-negative.
     * @param rectangle
     * @return boolean
     */
    public boolean isNewRectangleValid(ADT_Rectangle rectangle) {
        assert rectangle != null;
        assert rectangle.getX() != NOTSET;
        assert rectangle.getY() != NOTSET;
        assert rectangle.getX() >= 0;
        assert rectangle.getY() >= 0;

        // Check if it overlaps
        for(Iterator<ADT_Rectangle> recs = getRectangleIterator(); recs.hasNext();) {
            ADT_Rectangle currentRec = recs.next();
            if(currentRec != rectangle && currentRec.getX() != NOTSET && checkRectangleOverlap(currentRec, rectangle)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns true if the parameter position is in an already placed rectangle
     * @param position
     * @return 
     */
    public boolean isOccupied(ADT_Vector position) {
        for(Iterator<ADT_Rectangle> recs = getRectangleIterator(); recs.hasNext();) {
            ADT_Rectangle currentRec = recs.next();
            if((position.x >= currentRec.getX() && position.x < currentRec.getX() + currentRec.getWidth())
                    && (position.y >= currentRec.getY() && position.y < currentRec.getY() + currentRec.getHeight())) {
                return true;
            }
        }

        return false;
    }

    /** 
     * Returns the strips populated with the number of empty spaces in each strip
     * 
     * @param horizontal is true for horizontal strips and false for vertical strips
     * @return an array of all the strips with the index as width and
     * <code>a[i]</code> as number of strips
     */
    int[] getEmptySpaceStrips(boolean horizontal) {
        return null;
    }

    /**
     * Returns the strips populated with the total sum of (minimal side) occupied spaces of the to be placed rectangles.
     * 
     * @param horizontal is true for horizontal strips and false for vertical strips
     * @return an array of all the strips with the index as width and
     * <code>a[i]</code> as number of strips
     */
    int[] getRectangleStrips(boolean horizontal) {
        return null;
    }
    
    /**
     * Check if there is already a rectangle placed at (x,y)
     * @param x coordinate to be checked
     * @param y coordinate to be checked
     * @param index of the rectangle in the array
     * @return true iff there is a rectangle at (x,y) or if x < 0 or y <0
     */
    public boolean isRectangleAt (int x, int y, int index) {
        if (x < 0 ||  y < 0) {
            return true;
        }
        for (int i = 0; i <= index; i++) {
            ADT_Rectangle rec = shapes[i];
            boolean x_dim = (x >= rec.getX()) && (x < (rec.getX() + rec.getWidth()));
            boolean y_dim = (y >= rec.getY()) && (y < rec.getY() + rec.getHeight());
            if (x_dim && y_dim) {
                return true;
            }
        } 
        return false;
    }

    /**
     * 
     * @return true if the area has none overlapping rectangles
     */
    boolean isValid() {
        ArrayList<ADT_Rectangle> checkedRecs = new ArrayList<>();
        
        for(Iterator<ADT_Rectangle> recs = getRectangleIterator(); recs.hasNext();) {
            ADT_Rectangle currentRec = recs.next();

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
    
    /**
     * Query to obtain the total area of all the rectangles in this
     * The difference between returning <code>int area = getWidth() * getHeight()</code> is 
     * that this returns the total area of the "Enclosing Rectangle" while
     * getTotalAreaRectanglesToBePlaced returns only the summation of the areas of the
     * rectangles in this. This can be used for e.g. pruning
     * @return the area of all the rectangles in this
     */
    protected long getTotalAreaRectangles() {
        long totalArea = 0;
        ADT_Rectangle[] recs = getRectangles();
        for(ADT_Rectangle rec : recs) {
             totalArea += (rec.getWidth() * rec.getHeight());   
         }
        return totalArea;
    }

    /**
     * Sorts the rectangles in this array in the same manner as a given rectangle.
     * @param rectangles The order of the rectangles which should be copied.
     */
    void sortAs(ADT_Rectangle[] rectangles) {
        assert shapes.length == rectangles.length;

        ADT_Rectangle[] newOrdering = new ADT_Rectangle[shapes.length];
        for (int i = 0; i < rectangles.length; i++) {
            ADT_Rectangle rec1 = rectangles[i];

            // Find the currently evaluated rectangle in the array of original rectangles.
            for (int j = 0; j < shapes.length; j++) {
                ADT_Rectangle rec2 = shapes[j];
                // Check if the rectangle was already placed. If so skip it.
                if (rec2 == null) {
                    continue;
                }

                // Check if both rectangles are equal, if so map it to the right position and make it unavailable for later iterations.
                if (rec1.width == rec2.width && rec1.height == rec2.height) {
                    newOrdering[i] = rec2;
                    shapes[j] = null;
                    break;
                }
            }
        }

        shapes = newOrdering;
    }
    
    void sortAs(ADT_Rectangle[] rectangles, Comparator<ADT_Rectangle> com) {
        Arrays.sort(rectangles, com);
    }
}
