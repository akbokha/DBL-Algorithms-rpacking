public class ADT_Rectangle implements Comparable<ADT_Rectangle> {

    static final int INF = -1;
    static final int NOTSET = -2;
    protected int width;
    protected int height;
    private int x;
    private int y;
    private boolean flipped = false;
    boolean flippable;

    public ADT_Rectangle(int width, int height, int x, int y, boolean flippable) {
        assert height == INF || height > 0;
        assert width == INF || width > 0;
        assert x == NOTSET || x >= 0;
        assert y == NOTSET || y >= 0;

        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.flippable = flippable;
    }
    
    @Override
    public ADT_Rectangle clone() {
        ADT_Rectangle result = new ADT_Rectangle(width, height, this.x, this.y, this.flippable);

        result.flipped = this.flipped;

        return result;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        assert x == NOTSET || x >= 0;

        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        assert y == NOTSET || y >= 0;

        this.y = y;
    }

    public int getWidth() {
        return flipped ? height : width;
    }

    public void setWidth(int val) {
        width = val;
    }

    public int getHeight() {
        return flipped ? width : height;
    }

    public void setHeight(int val) {
        height = val;
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    public void toggleFlipped() {
        assert canFlip();

        this.flipped = !this.flipped;
    }

    public boolean getFlipped() {
        return flipped;
    }

    public boolean canFlip() {
        return flippable;
    }

    public ADT_Vector getDimensions() {
        return new ADT_Vector(getWidth(), getHeight());
    }

    @Override
    public int compareTo(ADT_Rectangle o) {
//        int maxThis = this.getWidth();
//        int maxOther = o.getWidth();
//
//        if (this.canFlip() || o.canFlip()) {
//            // Sort on maximum dimension if both can flip.
//            maxThis = Math.max(maxThis, this.getHeight());
//            maxOther = Math.max(maxOther, o.getHeight());
//        }
//
//        if(maxThis > maxOther){
//            return -1;
//        } else if(maxThis == maxOther) {
//            return 0;
//        } else {
//            return 1;
//        }
        int areaThis = this.getHeight() * this.getWidth();
        int areaO = o.getHeight() * o.getWidth();
        
        return areaO - areaThis;
    }

    @Override
    public String toString(){
        return "(" + x + ", " + y + ") " + "width = " + getWidth() + 
                " height = " + getHeight();
    }
}
