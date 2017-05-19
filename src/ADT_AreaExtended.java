import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by s157035 on 15-5-2017.
 */
public class ADT_AreaExtended extends ADT_Area {
    private final int EMPTY_INDEX = 0;
    private HashMap<Short, ADT_Rectangle> shapes;
    private short[] array;


    public ADT_AreaExtended(int width, int height, boolean flippable) {
        super(width, height, flippable);
        array = new short[width * height];
        shapes = new HashMap<>();
    }

    @Override
    public ADT_Area clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private short getNewId() {
        short i = EMPTY_INDEX + 1;
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
        if (x < 0 || y < 0 || y > super.getHeight() || x > super.getWidth()) {
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

    private boolean checkRectangleBordersWith(ADT_Rectangle shape) {
        //Check horizontal borders to this shape's id
        for (int x = shape.getX(); x <= shape.getX() + shape.getWidth(); x++) {
            if(!isEmptyAt(x, shape.getY())) return false;
            if(!isEmptyAt(x, shape.getY() + shape.getHeight())) return false;
        }

        //Check vertical borders
        for (int y = shape.getX(); y <= shape.getX() + shape.getHeight(); y++) {
            if(!isEmptyAt(shape.getX(), y)) return false;
            if(!isEmptyAt(shape.getX() + shape.getWidth(), y)) return false;
        }

        return true;
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
    public Iterator<ADT_Rectangle> getRectangleIterator() {
        return shapes.values().iterator();
    }

    @Override
    public ADT_Rectangle[] getRectangles() {
        return shapes.values().toArray(new ADT_Rectangle[shapes.size()]);
    }

    @Override
    public boolean isNewRectangleValid(ADT_Rectangle rectangle) {
        return checkRectangleBordersWith(rectangle);
    }

    @Override
    public boolean isOccupied(ADT_Vector position) {
        return !isEmptyAt(position.x, position.y);
    }

    /**
     * Returns the strips populated with the number of empty spaces in each strip
     */
    @Override
    public int[] getEmptySpaceStrips(boolean horizontal) {
        return scanStrips(horizontal, true);
    }

    /**
     * Returns the strips populated with the total sum of (minimal side) occupied spaces of the to be placed rectangles.
     */
    @Override
    public int[] getRectangleStrips(boolean horizontal) {
        return scanStrips(horizontal, false);
    }

    public RectangleType[] getRectangleTypesToBePlaced() {
        return null;
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

    @Override
    public void setWidth(int val) {
        throw new UnsupportedOperationException("Set width cannot be called on an extended area after construction.");
    }

    @Override
    public void setHeight(int val) {
        throw new UnsupportedOperationException("Set height cannot be called on an extended area after construction.");
    }
}
