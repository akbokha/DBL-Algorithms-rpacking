import java.util.ArrayList;
import java.util.List;

public class Strat_ORP_BottomLeft extends Strat_AbstractStrat {
    /**
     * Constructor
     *
     * @param area contains input data such as
     *             Concrete strategies should call super() in constructor
     */
    public Strat_ORP_BottomLeft(ADT_AreaExtended area) {
        super(area);
    }

    @Override
    public ADT_AreaExtended compute() {
        List<RectangleType> rects = new ArrayList<>();
        List<ADT_Rectangle> newAreaRectangles = new ArrayList<>();

        //Add all types to a list such that we can sort the list
        for (RectangleType type : area.getRectangleTypesToBePlaced()) {
            rects.add(type);
        }

        //Sort the array in decreasing width order
        rects.sort((o1, o2) -> {
            if(o1.getWidth() == o2.getWidth()){
                return 0;
            } else if(o1.getWidth() > o2.getWidth()) {
                return -1;
            }else {
                return 1;
            }
        });

        //Meat of the algorithm: Pick the largest item and place it at the most bottom left position
        for (RectangleType type : rects) {
            while (type.canInstantiate()) {
                //Pick the first items in the list (eg. the largest, we may want a more efficient data structure here)
                ADT_Rectangle rectangle = type.createInstance();


                //System.out.println("Processing rectangle " + rectangle.getWidth() + "," + rectangle.getHeight());
                ProcessNextRectangle(rectangle, newAreaRectangles);
            }
        }

        //Find the final area width
        int width = 0;
        for (ADT_Rectangle rectangle : newAreaRectangles) {
            if(rectangle.getX() + rectangle.getWidth() > width) {
                width = rectangle.getX() + rectangle.getWidth();
            }
        }

        //Create the final area
        return area;
    }

    private void ProcessNextRectangle(ADT_Rectangle rectangle, List<ADT_Rectangle> newAreaRectangles) {
        int maxHeight = area.getHeight() - rectangle.getHeight();
        //Find a leftmost position in the strip

        //Make sure we start at bottom-left
        rectangle.setX(0);
        rectangle.setY(0);
        while (true) {
            for (int y = 0; y < maxHeight; y++) {
                rectangle.setY(y);
                //Check if this position is valid
                if (checkOverlap(rectangle, newAreaRectangles)) {
                    //Placed the rectangle, we are done here
                    newAreaRectangles.add(rectangle);
                    return;
                }
            }
            //No space on this Y position, move further from the left (aka to the right)
            rectangle.setX(rectangle.getX() + 1);
        }
    }

    private boolean checkOverlap(ADT_Rectangle currentRec, List<ADT_Rectangle> checkedRecs) {
        for(ADT_Rectangle rec : checkedRecs) {
            if (checkRectangleOverlap(currentRec, rec)) {
                return false;
            }
        }

        return true;
    }


    //Directly copied from ADT_Area. We probably should make this a more general API for strategies to use.
    private boolean checkRectangleOverlap(ADT_Rectangle rec1, ADT_Rectangle rec2) {
        assert rec1 != null;
        assert rec2 != null;
        assert rec1.getWidth() != ADT_Rectangle.INF;
        assert rec2.getWidth() != ADT_Rectangle.INF;
        assert rec1.getHeight() != ADT_Rectangle.INF;
        assert rec2.getWidth() != ADT_Rectangle.INF;

        Point l1 = new Point(rec1.getX(), rec1.getY() + rec1.getHeight()); // Top left coordinate of first rectangle
        Point r1 = new Point(rec1.getX() + rec1.getWidth(), rec1.getY()); // Bottom right coordinate of first rectangle
        Point l2 = new Point(rec2.getX(), rec2.getY() + rec2.getHeight()); // Top left coordinate of second rectangle
        Point r2 = new Point(rec2.getX() + rec2.getWidth(), rec2.getY()); // Bottom right coordinate of second rectangle

        if (l1.getX() >= r2.getX() || l2.getX() >= r1.getX()) { // Check if one rectangle is on the left side of the other rectangle.
            return false;
        } else if (l1.getY() <= r2.getY() || l2.getY() <= r1.getY()) { // Check if one rectangle is above the other rectangle.
            return false;
        } else {
            return true;
        }
    }

    private class Point {
        private int x;
        private int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int getX() {
            return x;
        }

        int getY() {
            return y;
        }
    }
}
