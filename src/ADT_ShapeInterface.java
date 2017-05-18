

/**
 * Interface for the ADT of the rectangle.
 * 
 * @date Apr 26, 2017
 * @author Adriaan Knapen <a.d.knapen@student.tue.nl>
 */
public interface ADT_ShapeInterface {
    
    /**
     * Returns the width of the rectangle.
     * @return int -1 for infinity
     */
    int getWidth();
    
    /**
     * Sets the width of the area for the containment problem
     * @param val value to set
     */
    void setWidth(int val);
    
    /**
     * Returns the height of the rectangle.
     * @return int -1 for infinity
     */
    int getHeight();
    
    /**
     * Sets the height of the area for the containment problem
     * @param val value to set
     */
    void setHeight(int val);

    int getX();

    void setX(int x);

    int getY();

    void setY(int y);

    /**
     * Set the flipped state of the rectangle.
     *
     * @pre {@code ! canFlip()}
     * @param flipped
     */
    void setFlipped(boolean flipped);
    
    /**
     * Toggle the flipped state of the rectangle.
     * @pre {@code ! canFlip()}
     */
    void toggleFlipped();

    /**
     * If this rectangle if flipped or not.
     * @return
     */
    boolean getFlipped();

    boolean canFlip();
}
