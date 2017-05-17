/**
 * A solver of the containment problem. Requires that the height and width of the area are finite.
 */
class Strat_CP_BT extends Strat_BT_Template {

    private ADT_Rectangle[] rectangles;
    private int index = -1;

    Strat_CP_BT(ADT_Area area) {
        super(area);

        rectangles = area.getRectangles();
    }

    @Override
    protected boolean reject(ADT_Rectangle last) {
        // Check if the last added rectangle is valid
        if (last != null && !area.isNewRectangleValid(last)) {
            return true;
        }

        // Consult all pruners
        return super.reject(last);
    }

    @Override
    ADT_Rectangle last() {
        if (index >= 0) {
            return rectangles[index];
        } else {
            return null;
        }
    }

    @Override
    boolean accept(ADT_Rectangle last) {
        if (last == null) {
            return false;
        } else {
            return index + 1 >= rectangles.length;
        }
    }



    @Override
    boolean first() {
        // Step one level down into the branch.
        index++;
        rectangles[index].setX(-1);
        rectangles[index].setY(0); // No need to also set the x coordinate since next will take care of this.

        return next();
    }

    @Override
    boolean next() {
        // Increment x and check if this coordinate is valid.
        int x = rectangles[index].getX();
        x++;

        // Check if the x-coordinate is still a valid starting coordinate
        if (x + rectangles[index].getWidth() > area.getWidth()) {
            x = 0;

            int y = rectangles[index].getY();
            y++;

            // Check if the y-coordinate is still a valid starting coordinate
            if (y + rectangles[index].getHeight() > area.getHeight()) {
                return false;
            }

            rectangles[index].setY(y);
        }
        rectangles[index].setX(x);

        return true;
    }

    @Override
    void revert() {
        // Revert the rectangle back to the initial place.
        ADT_Rectangle rectangle = rectangles[index];
        rectangle.setX(ADT_Rectangle.NOTSET);
        rectangle.setY(ADT_Rectangle.NOTSET);

        // Move one level higher into the branch.
        index--;
    }

}
