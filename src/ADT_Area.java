import java.util.*;

/**
 * @date Apr 26, 2017
 * @author Adriaan Knapen <a.d.knapen@student.tue.nl>
 */
@SuppressWarnings("Duplicates")
public class ADT_Area extends ADT_Rectangle implements Cloneable {

    private TreeSet<ADT_Rectangle> shapes;

    public ADT_Area(int width, int height, boolean flippable) {
        super(width, height, 0, 0, flippable);
        shapes = new TreeSet<>();
    }
    
    @Override
    public ADT_Area clone() throws CloneNotSupportedException {
        ADT_Area newArea = new ADT_Area(this.getWidth(), this.getHeight(), this.canFlip());
        for(ADT_Rectangle rec : this.getRectangles()) {
            newArea.add(rec.clone());
        }
        return newArea;
    }

    /**
     * Adds an rectangle to this area.
     *
     * @param shape
     */
    public void add(ADT_Rectangle shape) {
        shapes.add(shape);
    }
    
    /**
     * Sets the array of shapes to new one.
     * @param shapes new array of shapes
     */
    public void setRectangles(ADT_Rectangle[] shapes) {
        this.shapes = new TreeSet<>();
        for(ADT_Rectangle rec : shapes) {
            rec.setX(NOTSET);
            rec.setY(NOTSET);
            this.add(rec);
        }
    }

    /**
     * Gives the amount of rectangles this area contains.
     *
     * @return the amount of rectangles this area contains.
     */
    public int getCount() {
        return shapes.size();
    }

    /**
     * Constructs an iterator over all rectangles.
     *
     * @return iterator over all rectangles it contains.
     */
    public Iterator<ADT_Rectangle> getRectangleIterator() {
        return shapes.iterator();
    }

    public ADT_Rectangle[] getRectangles() {
        return shapes.toArray(new ADT_Rectangle[shapes.size()]);
    }

    ADT_Vector getMinimalDimensions() {
        Iterator<ADT_Rectangle> i = getRectangleIterator();
        int maxX = 0;
        int maxY = 0;

        while (i.hasNext()) {
            ADT_Rectangle r = i.next();
            if (r.getWidth() != ADT_Rectangle.INF) {
                maxX = Math.max(r.getWidth() + r.getX(), maxX);
                maxY = Math.max(r.getHeight() + r.getY(), maxY);
            }
        }

        return new ADT_Vector(maxX, maxY);
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
     * Return true if none of the already placed rectangles overlap with the parameter rectangle
     * @param rectangle
     * @return 
     */
    public boolean isNewRectangleValid(ADT_Rectangle rectangle) {
        assert rectangle != null;
        assert rectangle.getX() != NOTSET;
        assert rectangle.getY() != NOTSET;

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
    public int[] getEmptySpaceStrips(boolean horizontal) {
        return null;
    }

    /**
     * Returns the strips populated with the total sum of (minimal side) occupied spaces of the to be placed rectangles.
     * 
     * @param horizontal is true for horizontal strips and false for vertical strips
     * @return an array of all the strips with the index as width and
     * <code>a[i]</code> as number of strips
     */
    public int[] getRectangleStrips(boolean horizontal) {
        return null;
    }

    /**
     * 
     * @return true if the area has none overlapping rectangles
     */
    public boolean isValid() {
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
    protected boolean checkRectangleOverlap(ADT_Rectangle rec1, ADT_Rectangle rec2) {
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
        } else if (l1.getY() <= r2.getY() || l2.getY() <= r1.getY()) { // Check if one rectangle is above the other rectangle.
            return false;
        } else {
            return true;
        }
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
     * getTotalAreaRectangles returns only the summation of the areas of the 
     * rectangles in this. This can be used for e.g. pruning
     * @return the area of all the rectangles in this
     */
    public int getTotalAreaRectangles() {
        int totalArea = 0;
        for(Iterator<ADT_Rectangle> rectangles = getRectangleIterator(); rectangles.hasNext();) {
             ADT_Rectangle rec = rectangles.next();
             totalArea += (rec.getWidth() * rec.getHeight());   
         }
        return totalArea;
    }
}
