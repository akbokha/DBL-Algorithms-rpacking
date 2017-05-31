import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by s157035 on 15-5-2017.
 */
public class ADT_AreaExtended {
    private final short EMPTY_INDEX = 0;
    private final int INF = ADT_Area.INF;
    private HashMap<Short, ADT_Rectangle> shapes;
    private short[] array;
    private short lastIssuedIndex;
    private int width;
    private int height;
    private boolean flippable;

    private RectangleType[] toBePlacedRects;

    public ADT_AreaExtended(int width, int height, boolean flippable, RectangleType[] rectangleTypes) {
        this.width = width;
        this.height = height;
        this.flippable = flippable;
        lastIssuedIndex = 0;
        shapes = new HashMap<>();
        toBePlacedRects = rectangleTypes;
    }

    public boolean canFlip() {
        return flippable;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ADT_AreaExtended clone() throws CloneNotSupportedException {
        return new ADT_AreaExtended(getWidth(), getHeight(), canFlip(), getRectangleTypesToBePlaced());
    }

    private short getNewId() {
        short i = (short)(lastIssuedIndex + 1);
        while (shapes.containsKey(i)) {
            i++;
        }
        lastIssuedIndex = i;
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
        if (x < 0 || y < 0 || y > getHeight() || x > getWidth()) {
            return false;
        }
        int i = getIndex(x, y);
        return array[i] == EMPTY_INDEX;
    }

    /**
     * Adds an rectangle to this area.
     *
     * @param shape
     */
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

    private boolean checkRectangleBordersWith(ADT_Rectangle shape) {
        return checkRectangleBordersFrom(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
    }

    private boolean checkRectangleBordersFrom(int posX, int posY, int width, int height) {
        //Check horizontal borders to this shape's id
        for (int x = posX; x <= posX + width; x++) {
            if(!isEmptyAt(x, posY)) return false;
            if(!isEmptyAt(x, posY + height)) return false;
        }

        //Check vertical borders
        for (int y = posY; y <= posY + height; y++) {
            if(!isEmptyAt(posX, y)) return false;
            if(!isEmptyAt(posX + width, y)) return false;
        }

        return true;
    }


    /*
     * Returns all rectangle types which can still be instantiated
     */
    public RectangleType[] getRectangleTypesToBePlaced() {

        List<RectangleType> list = new ArrayList<>();
        for(RectangleType type : toBePlacedRects) {
            if(type.canInstantiate()) {
                list.add(type);
            }
        }

        return list.toArray(new RectangleType[list.size()]);
    }

    /*
     * Returns the already placed rectangles in the area
     */
    public Iterable<ADT_Rectangle> getPlacedRectangles() {
        return shapes.values();
    }

    /**
     * Gives the amount of rectangles this area contains.
     *
     * @return the amount of rectangles this area contains.
     */
    public int getCount() {
        return shapes.size();
    }



    ADT_Vector getMinimalDimensions() {
        Iterable<ADT_Rectangle> i = getPlacedRectangles();
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
     * Constructs an iterator over all rectangles.
     *
     * @return iterator over all rectangles it contains.
     */
    public Iterator<ADT_Rectangle> getRectangleIterator() {
        return shapes.values().iterator();
    }

    public ADT_Rectangle[] getRectangles() {
        return shapes.values().toArray(new ADT_Rectangle[shapes.size()]);
    }

    public boolean isNewRectangleValid(ADT_Rectangle rectangle) {
        return checkRectangleBordersWith(rectangle);
    }

    public boolean isOccupied(ADT_Vector position) {
        return !isEmptyAt(position.x, position.y);
    }

    /**
     * Returns the strips populated with the number of empty spaces in each strip
     */
    public int[] getEmptySpaceStrips(boolean horizontal) {
        return scanStrips(horizontal, true);
    }

    /**
     * Returns the strips populated with the total sum of (minimal side) occupied spaces of the to be placed rectangles.
     */
    public int[] getRectangleStrips(boolean horizontal) {
        return scanStrips(horizontal, false);
    }

    public boolean moveRectangle(ADT_Rectangle rectangle, int newX, int newY) {
        if(checkRectangleBordersFrom(newX, newY, rectangle.getWidth(), rectangle.getHeight())) {
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
        for (RectangleType type : getRectangleTypesToBePlaced()) {
            total += type.getHeight() * type.getWidth() * type.getNumberOfinstances();
        }
        return total;
    }

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
}
