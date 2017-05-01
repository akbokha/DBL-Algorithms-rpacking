package rectanglepacking;

/**
 * @date Apr 26, 2017
 * @author Adriaan Knapen <a.d.knapen@student.tue.nl>
 */
public class Rectangle implements ShapeInterface {

    protected int width;
    protected int height;
    protected int x;
    protected int y;
    protected boolean flipped = false;
    protected boolean flippable;

    public Rectangle(int width, int height, int x, int y, boolean flippable) {
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
        return flipped ? width : height;
    }

    @Override
    public int getHeight() {
        return flipped ? height : width;
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
