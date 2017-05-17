
/**
 *
 * @author Bastiaan
 */
class Strat_CP_BT_Example extends Strat_BT_Template {

    private ADT_Rectangle[] rectangles;
    private int index = -1;
    private int x;
    private int y;

    Strat_CP_BT_Example(ADT_Area area) {
        super(area);

        rectangles = area.getRectangles();
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
        return area.isNewRectangleValid(last) && index + 1 >= rectangles.length;
    }

    @Override
    boolean first() {
        index++;
        x = -1;
        y = 0;

        //Output_AbstractOutput printer = new Output_GraphicalOutput(area);
        //printer.draw();

        return next();
    }

    @Override
    boolean next() {
        x++;

        // Check if the x-coordinate is still a valid starting coordinate
        if (x + rectangles[index].getWidth() > area.getWidth()) {
            x = 0;
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
        ADT_Rectangle rectangle = rectangles[index--];

        rectangle.setX(ADT_Rectangle.NOTSET);
        rectangle.setY(ADT_Rectangle.NOTSET);
    }

}
