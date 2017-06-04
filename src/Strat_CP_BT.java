/**
 * A solver of the containment problem. Requires that the height and width of the area are finite.
 * @pre None of the rectangles should be already placed.
 */
class Strat_CP_BT extends Strat_BT_Template {

    private ADT_Rectangle[] rectangles;
    private int index = -1;

    Strat_CP_BT(ADT_AreaExtended areaEx) {
        super(areaEx);

        assert areaEx.getRectanglesToBePlaced().length == areaEx.getCount(); // Check that none of the rectangles are placed.

        ADT_Rectangle[] allRectangles = areaEx.getRectanglesToBePlaced();

        rectangles = new ADT_Rectangle[allRectangles.length];
        int index = 0;
        for (ADT_Rectangle rec : allRectangles) {
            rectangles[index++] = rec;
        }
    }

    @Override
    protected boolean reject(ADT_Rectangle last) {
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
        // Step one level down into the branch and retrieve a pointer to the first rectangle.
        ADT_Rectangle rectangle = rectangles[++index];
        int x = 0;
        int y = 0;

        // Make distinction between the first rectangle and all others.
        if (index == 0) {
            // Let the first rectangle start with its center in the center such that it will only evaluate the top right corner.
            x = Math.max(0, (int) Math.ceil((areaEx.getWidth() - rectangle.getWidth()) / 2));
            y = Math.max(0, (int) Math.ceil((areaEx.getHeight() - rectangle.getHeight()) / 2));
        }

        ADT_Vector next = findNextPosition(rectangle, x, y);

        if (next != null) {
            areaEx.add(index, next.x, next.y);
        } else {
            return false;
        }

        return true;
    }

    @Override
    boolean next() {
        // Retrieve a pointer to the currently placed rectangle.
        ADT_Rectangle rectangle = rectangles[index];

        int x = rectangle.getX() + 1;
        int y = rectangle.getY();

        // Remove it
        areaEx.remove(index);

        ADT_Vector next = findNextPosition(rectangle, x, y);

        if (next != null) {
            areaEx.add(index, next.x, next.y);
        } else {
            return false;
        }

        return true;
    }

    private ADT_Vector findNextPosition(ADT_Rectangle rectangle, int x, int y) {
        x++;
        while (true) {
            // Increment x and check if this coordinate is valid.

            // Check if the x-coordinate is still a valid starting coordinate
            if (x + rectangle.getWidth() > areaEx.getWidth()) {
                // Reset x, make distinction between the first rectangle and all others.
                if (index == 0) {
                    x = Math.max(0, (int) Math.ceil((areaEx.getWidth() - rectangle.getWidth()) / 2f));
                } else {
                    x = 0;
                }

                y++;

                // Check if the y-coordinate is still a valid starting coordinate
                if (y + rectangle.getHeight() > areaEx.getHeight()) {

                    // Rotate if the rectangle can flip.
                    if (rectangle.canFlip() && !rectangle.getFlipped()) {
                        throw new UnsupportedOperationException("Rotations currently do not work.");

                        /*rectangle.setFlipped(true);

                        // Check if the resulting areaEx is valid, if so compute this branch. Else leave it. (1)
                        if (areaEx.isNewRectangleValid(rectangle)) {
                            return first();
                        }*/
                    } else {
                        return null;
                    }
                }
            }

            int displacement = areaEx.checkIntersection(x, y, rectangle.getWidth(), rectangle.getHeight());
            if (displacement == 0) {
                break;
            } else {
                x += displacement;
            }
        }

        return new ADT_Vector(x, y);
    }

    @Override
    void revert() {
        // Move one level higher into the branch. Is not needed when first never managed to achieve this.
        if (areaEx.getRectangleIsPlaced(index)) {
            areaEx.remove(index);
        }

        index--;
    }

}

/*
 * 1) Could be replaced with call to compute, but causes 10-20% more computational overhead.
 */
