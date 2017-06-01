/**
 * Created by s157035 on 12-5-2017.
 */
public class RectangleType {
    private final int width;
    private final int height;
    private final boolean canRotate;

    private int instances;

    public RectangleType(int width, int height, boolean canRotate) {
        this.width = width;
        this.height = height;
        //Only allow rotations if the width and height are not equal (rotating does not make any difference then), and if rotations are allowed by input
        this.canRotate = canRotate && width != height;

        instances = 1;
    }

    public void increaseInstances() {
        instances++;
    }

    public void decreaseInstances() {
        instances--;
    }

    public boolean canInstantiate() { return instances > 0; }

    public boolean canRotate() {
        return width == height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getSmallestDimension() {
        return width > height ? height : width;
    }

    public int getLargestDimension() {
        return width > height ? width : height;
    }

    public int getNumberOfinstances() {
        return instances;
    }

    public ADT_Rectangle createInstance() {
        if(instances <= 0) {
            throw new IllegalStateException("Tried to create an instance of a rectangle type while there were no instances left to add.");
        }

        decreaseInstances();
        ADT_Rectangle rectangle = new ADT_Rectangle(width, height, 0, 0, canRotate());
        return rectangle;
    }
    
    public ADT_Rectangle createInstance(int x, int y) {
        if(instances <= 0) {
            throw new IllegalStateException("Tried to create an instance of a rectangle type while there were no instances left to add.");
        }

        decreaseInstances();
        ADT_Rectangle rectangle = new ADT_Rectangle(width, height, x, y, canRotate());
        return rectangle;
    }
}
