

/**
 * Interface for the ADT of the rectangle.
 * 
 * @date Apr 26, 2017
 * @author Adriaan Knapen <a.d.knapen@student.tue.nl>
 */
public interface ADT_ShapeInterface {
    
    /**
     * Returns the width of the rectangle.
     * @return int a negative constant for infinity, else the width of the shape
     */
    int getWidth();
    
    /**
     * Returns the height of the rectangle.
     * @return int a negative constant for infinity, else the height of the shape
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

    /**
     * Checks wether the shape is placed within the coordinate system or not.
     * @return true when the shape is placed, false otherwise.
     */
    boolean isPlaced();

    int getArea();
}
