/**
 * A solver of the containment problem. Requires that the height and width of the area are finite.
 * @pre None of the rectangles should be already placed.
 * @pre None of the rectangles should be already placed.
 */
class Strat_CP_BT extends Strat_BT_Template {

    private ADT_Rectangle[] rectangles;
    private int index = -1;
    private Output_GraphicalOutput output;

    Strat_CP_BT(ADT_AreaExtended area) {
        super(area);

        RectangleType[] rectangleTypes = area.getRectangleTypesToBePlaced();

        // Sum the amount of rectangles to be placed.
        int count = 0;
        for (RectangleType rectangleType : rectangleTypes) {
            count += rectangleType.getNumberOfinstances();
        }

        rectangles = new ADT_Rectangle[count];
        int index = 0;
        for (RectangleType rectangleType : rectangleTypes) {
            while (rectangleType.canInstantiate()) {
                ADT_Rectangle rectangle = rectangleType.createInstance();
                rectangles[index++] = rectangle;
            }
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
        return last != null && index + 1 >= rectangles.length;
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
        int x = rectangle.getX();
        int y = rectangle.getY();

        do {
            // Increment x and check if this coordinate is valid.
            x++;

            // Check if the x-coordinate is still a valid starting coordinate
            if (x + rectangle.getWidth() > area.getWidth()) {
                // Reset x, make distinction between the first rectangle and all others.
                if (index == 0) {
                    x = Math.max(0, (int) Math.ceil((area.getWidth() - rectangle.getWidth()) / 2f));
                } else {
                    x = 0;
                }

                y++;

                // Check if the y-coordinate is still a valid starting coordinate
                if (y + rectangle.getHeight() > area.getHeight()) {

                    // Rotate if the rectangle can flip.
                    if (rectangle.canFlip() && !rectangle.getFlipped()) {
                        throw new UnsupportedOperationException("Rotations currently do not work.");

                        /*rectangle.setFlipped(true);

                        // Check if the resulting area is valid, if so compute this branch. Else leave it. (1)
                        if (area.isNewRectangleValid(rectangle)) {
                            return first();
                        }*/
                    } else {
                        return false;
                    }
                }
            }
        } while (!area.checkRectangleBordersWith(rectangle));

        area.add(rectangle);
        area.moveRectangle(rectangle, x, y);

        //output.draw();

        return true;
    }

    @Override
    void revert() {
        // Revert the rectangle back to the initial place.
        ADT_Rectangle rectangle = rectangles[index];
        rectangle.setX(ADT_Rectangle.NOTSET);
        rectangle.setY(ADT_Rectangle.NOTSET);

        // @todo remove rectangle from the placed rectangles in area.

        rectangle.setFlipped(false);

        // Move one level higher into the branch.
        index--;
    }

}

/*
 * 1) Could be replaced with call to compute, but causes 10-20% more computational overhead.
 */
