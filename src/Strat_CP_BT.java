/**
 * A solver of the containment problem. Requires that the height and width of the area are finite.
 * @pre None of the rectangles should be already placed.
 * @modifies Does not maintain the order of the rectangles.
 */
class Strat_CP_BT extends Strat_BT_Template {

    private ADT_Rectangle[] rectangles;
    private boolean[] initialFlipped;
    private int index = -1;

    Strat_CP_BT(ADT_AreaExtended areaEx) {
        super(areaEx);

        assert this.areaEx.getRectanglesToBePlaced().length == this.areaEx.getCount(); // Check that none of the rectangles are placed.

        rectangles = this.areaEx.getRectanglesToBePlaced();

        // Only support flipping rectangles if it is required.
        if (this.areaEx.canFlip()) {
            // Store the initial flipped status.
            initialFlipped = new boolean[rectangles.length];

            int areaHeight = this.areaEx.getHeight();
            int areaWidth = this.areaEx.getWidth();

            for (int i = 0; i < rectangles.length; i++) {
                ADT_Rectangle rectangle = rectangles[i];

                // Check if it is required to rotate in either one of the directions.
                if (rectangle.getHeight() > areaHeight || rectangle.getWidth() > areaWidth) {
                    rectangle.toggleFlipped();
                    rectangle.flippable = false;
                } else if (rectangle.getWidth() > areaHeight || rectangle.getHeight() > areaWidth || rectangle.getHeight() == rectangle.getWidth()) {
                    rectangle.flippable = false;
                }

                // If one of the rectangles still doesn't fit, then this containment problem cannot be solved.
                if (rectangle.getHeight() > areaHeight || rectangle.getWidth() > areaWidth) {
                    rectangles = null;
                    return;
                }

                initialFlipped[i] = rectangles[i].getFlipped();
            }
        }
    }

    @Override
    public ADT_AreaExtended compute() {
        // Rectangles will be set to null in the constructor if one of the rectangles doesn't fit within the area.
        if (rectangles == null) {
            return null;
        } else {
            return super.compute();
        }
    }

    @Override
    protected boolean reject(ADT_Rectangle last) {
        // Consult all pruners but not earlier than depth 6
        if(index > 0 && index < 9) {
            return super.reject(last);
        } else {
            return false;
        }
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
        
        if(index > 0) {
            ADT_Rectangle previousRectangle = rectangles[index-1];
            if((rectangle.compareTo(previousRectangle) == 0)) {
                x = previousRectangle.getX() + previousRectangle.getWidth();
                y = previousRectangle.getY();
            }
        }

        if (rectangle.getHeight() > areaEx.getHeight()) {
            rectangle.toggleFlipped();

            assert rectangle.getHeight() <= areaEx.getHeight();
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

        int x = rectangle.getX();
        int y = rectangle.getY();

        // Remove it
        areaEx.remove(index);
        
        ADT_Vector next = findNextPosition(rectangle, x + 1, y);

        if (next != null) {
            areaEx.add(index, next.x, next.y);
        } else {
            return false;
        }

        return true;
    }

    private ADT_Vector findNextPosition(ADT_Rectangle rectangle, int x, int y) {
        int maxX = (index != 0) ? areaEx.getWidth() : (int) Math.ceil((areaEx.getWidth() + rectangle.getWidth()) / 2f);
        int maxY = (index != 0) ? areaEx.getHeight() : (int) Math.ceil((areaEx.getHeight() + rectangle.getHeight()) / 2f);
        
        while (true) {
            // Check if the x-coordinate is still a valid starting coordinate
            if (x + rectangle.getWidth() > maxX) {
                // Reset x, make distinction between the first rectangle and all others.
                x = 0;
                
                y++;
            }
            
            // Check if the y-coordinate is still a valid starting coordinate
            if (y + rectangle.getHeight() > maxY) {

                // Rotate if the rectangle can flip.
                if (rectangle.canFlip() && rectangle.getFlipped() == initialFlipped[index]) {

                    rectangle.toggleFlipped();

                    return findNextPosition(rectangle, 0, 0);
                } else {
                    return null;
                }
            }
            
            int res = areaEx.checkIntersection(x, y, rectangle.getWidth(), rectangle.getHeight());
            if (res == 0) {
                break;
            } else {
                x = res;
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

        // Check if rotation is currently used.
        if (initialFlipped!=null) {
            rectangles[index].setFlipped(initialFlipped[index]);
        }

        index--;
    }

}

/*
 * 1) Could be replaced with call to compute, but causes 10-20% more computational overhead.
 */