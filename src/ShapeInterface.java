

/**
 * Interface for the ADT of the rectangle.
 * 
 * @date Apr 26, 2017
 * @author Adriaan Knapen <a.d.knapen@student.tue.nl>
 */
public interface ShapeInterface {
    
    /**
     * Returns the width of the rectangle.
     * @return int 0 for infinity
     */
    int getWidth();
    
    /**
     * Returns the height of the rectangle.
     * @return int 0 for infinity
     */
    int getHeight();

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
