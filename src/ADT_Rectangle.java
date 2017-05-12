

/**
 * @date Apr 26, 2017
 * @author Adriaan Knapen <a.d.knapen@student.tue.nl>
 */
public class ADT_Rectangle implements ADT_ShapeInterface {

    public static final int INF = -1;
    public static final int NOTSET = -2;
    private int width;
    private int height;
    private int x;
    private int y;
    private boolean flipped = false;
    private boolean flippable;

    public ADT_Rectangle(int width, int height, int x, int y, boolean flippable) {
        assert height == INF || height > 0;
        assert width == INF || width > 0;

        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.flippable = flippable;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getWidth() {
        return flipped ? height : width;
    }

    @Override
    public int getHeight() {
        return flipped ? width : height;
    }

    @Override
    public void setFlipped(boolean flipped) {
        assert ! canFlip();

        this.flipped = flipped;
    }

    @Override
    public void toggleFlipped() {
        assert ! canFlip();

        this.flipped = !this.flipped;
    }

    @Override
    public boolean getFlipped() {
        return flipped;
    }

    @Override
    public boolean canFlip() {
        return flippable;
    }
}
