/**
 * A solver of the containment problem. Requires that the height and width of the area are finite.
 * @pre All rectangles 
 */
class Strat_CP_BT extends Strat_BT_Template {

    private ADT_Rectangle[] rectangles;
    private int index = -1;
    private Output_GraphicalOutput output;

    Strat_CP_BT(ADT_AreaExtended area) {
        super(area);

        rectangles = area.getRectangles();

        // Reset the position of the rectangles (should be moved to the anytime algorithm for efficiency reasons).
        for (int i = 0; i < rectangles.length; i++) {
            rectangles[i].setX(ADT_Rectangle.NOTSET);
            rectangles[i].setY(ADT_Rectangle.NOTSET);
        }

        //output = new Output_GraphicalOutput(area);
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
        // Step one level down into the branch and retrieve a pointer to the currently placed rectangle.
        ADT_Rectangle rectangle = rectangles[++index];

        // Make distinction between the first rectangle and all others.
        if (index == 0) {
            // Let the first rectangle start with its center in the center such that it will only evaluate the top right corner.
            rectangle.setX(Integer.MAX_VALUE - rectangle.getWidth() - 1); // Note: rectangle width has to be subtracted in order to prevent an overflow.
            rectangle.setY(Math.max(0, (int) Math.ceil((area.getHeight() - rectangle.getHeight()) / 2)));
        } else {
            // Start at the bottom left.
            rectangle.setX(-1);
            rectangle.setY(0);
        }
        return next();
    }

    @Override
    boolean next() {
        // Retrieve a pointer to the currently placed rectangle.
        ADT_Rectangle rectangle = rectangles[index];

        // Increment x and check if this coordinate is valid.
        int x = rectangle.getX();
        x++;

        // Check if the x-coordinate is still a valid starting coordinate
        if (x + rectangle.getWidth() > area.getWidth()) {
            // Reset x, make distinction between the first rectangle and all others.
            if (index == 0) {
                x = Math.max(0, (int) Math.ceil((area.getWidth() - rectangle.getWidth()) / 2f));
            } else {
                x = 0;
            }

            int y = rectangle.getY();
            y++;

            // Check if the y-coordinate is still a valid starting coordinate
            if (y + rectangle.getHeight() > area.getHeight()) {

                // Rotate if the rectangle can flip.
                if (rectangle.canFlip() && !rectangle.getFlipped()) {
                    rectangle.setFlipped(true);

                    // Check if the resulting area is valid, if so compute this branch. Else leave it. (1)
                    if (area.isNewRectangleValid(rectangle)) {
                        return first();
                    }
                } else {
                    return false;
                }
            }

            rectangle.setY(y);
        }
        rectangle.setX(x);

        //output.draw();

        return true;
    }

    @Override
    void revert() {
        // Revert the rectangle back to the initial place.
        ADT_Rectangle rectangle = rectangles[index];
        rectangle.setX(ADT_Rectangle.NOTSET);
        rectangle.setY(ADT_Rectangle.NOTSET);

        rectangle.setFlipped(false);

        // Move one level higher into the branch.
        index--;
    }

}

/*
 * 1) Could be replaced with call to compute, but causes 10-20% more computational overhead.
 */
