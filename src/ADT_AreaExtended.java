import java.util.*;

/**
 * Created by s157035 on 15-5-2017.
 */
public class ADT_AreaExtended extends ADT_Area {
    private HashMap<Short, ADT_Rectangle> shapes;
    private short[] array;

    public ADT_AreaExtended(int width, int height, boolean flippable) {
        super(width, height, flippable);
        array = new short[width * height];
        for (int i = 0; i < array.length; i++) {
            array[i] = -1;
        }
        shapes = new HashMap<>();
    }

    @Override
    public ADT_Area clone() throws CloneNotSupportedException {
        return (ADT_Area) super.clone();
    }

    private short getNewId() {
        short i = 0;
        while (shapes.containsKey(i)) {
            i++;
        }
        return i;
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
        int i = getIndex(x, y);
        return array[i] == -1;
    }

    /**
     * Adds an rectangle to this area.
     *
     * @param shape
     */
    @Override
    public void add(ADT_Rectangle shape) {
        short id = getNewId();
        shapes.put(id, shape);
        fillRectangleBordersWith(shape, id);
    }

    private void fillRectangleBordersWith(ADT_Rectangle shape, short id) {
        //Set horizontal borders to this shape's id
        for (int x = shape.getX(); x <= shape.getX() + shape.getWidth(); x++) {
            setArrayAt(x, shape.getY(), id);
            setArrayAt(x, shape.getY() + shape.getHeight(), id);
        }

        //Set vertical borders
        for (int y = shape.getX(); y <= shape.getX() + shape.getHeight(); y++) {
            setArrayAt(shape.getX(), y, id);
            setArrayAt(shape.getX() + shape.getWidth(), y, id);
        }
    }

    /**
     * Gives the amount of rectangles this area contains.
     *
     * @return the amount of rectangles this area contains.
     */
    @Override
    public int getCount() {
        return shapes.size();
    }

    /**
     * Constructs an iterator over all rectangles.
     *
     * @return iterator over all rectangles it contains.
     */
    @Override
    public Iterator<ADT_Rectangle> getRectangles() {
        return shapes.values().iterator();
    }

    Vector2 getMinDimensions() {
        Iterator<ADT_Rectangle> i = getRectangles();
        int maxX = 0;
        int maxY = 0;

        while (i.hasNext()) {
            ADT_Rectangle r = i.next();
            if (r.getWidth() != ADT_Rectangle.INF) {
                maxX = Math.max(r.getWidth() + r.getX(), maxX);
                maxY = Math.max(r.getHeight() + r.getY(), maxY);
            }
        }

        return new Vector2(maxX, maxY);
    }

    /*
     * Return true if none of the already placed rectangles overlap with the paramater rectangle
     */
    @Override
    public boolean isNewRectangleValid(ADT_Rectangle rectangle) {
        for(Iterator<ADT_Rectangle> recs = getRectangles(); recs.hasNext();) {
            ADT_Rectangle currentRec = recs.next();
            if(!checkRectangleOverlap(currentRec, rectangle)) {
                return false;
            }
        }

        return true;
    }

    /*
     * Returns true if the parameter position is in an already placed rectangle
     */
    @Override
    public boolean isOccupied(Vector2 position) {
        for(Iterator<ADT_Rectangle> recs = getRectangles(); recs.hasNext();) {
            ADT_Rectangle currentRec = recs.next();
            if((position.x >= currentRec.getX() && position.x < currentRec.getX() + currentRec.getWidth())
                    && (position.y >= currentRec.getY() && position.y < currentRec.getY() + currentRec.getHeight())) {
                return true;
            }
        }

        return false;
    }

    /*
     * Returns the strips populated with the number of empty spaces in each strip
     */
    @Override
    public int[] getEmptySpaceStrips(boolean horizontal) {
        return scanStrips(horizontal, true);
    }

    /*
     * Returns the strips populated with the total sum of (minimal side) occupied spaces of the to be placed rectangles.
     */
    @Override
    public int[] getRectangleStrips(boolean horizontal) {
        return scanStrips(horizontal, false);
    }

    private int[] scanStrips(boolean horizontal, boolean lookingForEmpty) {
        int[] vals;
        if(horizontal) {
            vals = new int[getHeight()];
            for (int i = 0; i < vals.length; i++) {
                for (int x = 0; x < getWidth(); x++) {
                    if(isEmptyAt(x, i) == lookingForEmpty) {
                        vals[i]++;
                    }
                }
            }
        }
        else {
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

    /**
     *
     * @return true if the area has none overlapping rectangles
     */
    @Override
    public boolean isValid() {
        ArrayList<ADT_Rectangle> checkedRecs = new ArrayList<>();

        for(Iterator<ADT_Rectangle> recs = getRectangles(); recs.hasNext();) {
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
    private boolean checkRectangleOverlap(ADT_Rectangle rec1, ADT_Rectangle rec2) {
        assert rec1 != null;
        assert rec2 != null;
        assert rec1.getWidth() != ADT_Rectangle.INF;
        assert rec2.getWidth() != ADT_Rectangle.INF;
        assert rec1.getHeight() != ADT_Rectangle.INF;
        assert rec2.getWidth() != ADT_Rectangle.INF;

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
     * The difference between returning int area = getWidth() * getHeight() is
     * that this returns the total area of the "Enclosing Rectangle" while
     * getTotalAreaRectangles returns only the summation of the areas of the
     * rectangles in this. This can be used for e.g. pruning
     * @return the area of all the rectangles in this
     */
    @Override
    public int getTotalAreaRectangles() {
        int totalArea = 0;
        for(Iterator<ADT_Rectangle> rectangles = getRectangles(); rectangles.hasNext();) {
            ADT_Rectangle rec = rectangles.next();
            totalArea += (rec.getWidth() * rec.getHeight());
        }
        return totalArea;
    }
}
