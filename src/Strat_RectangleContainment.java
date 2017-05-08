import java.util.ArrayList;
import java.util.Comparator;

public class Strat_RectangleContainment extends Strat_BT_Template {

    private ArrayList<ADT_Rectangle> rectangles;
    int index = 0;

    public Strat_RectangleContainment(ADT_Area area) {
        super(area);

        rectangles = new ArrayList<>(area.getRectangles());
        rectangles.sort((o1, o2) -> {
            int a1 = o1.getArea();
            int a2 = o2.getArea();

            if (a1 > a2) {
                return 1;
            } else if (a1 < a2) {
                return -1;
            } else {
                return 0;
            }
        });
    }

    @Override
    boolean accept(ADT_Area area) {
        return area.isValid() && area.areAllRectanglesPlaced();
    }

    @Override
    ADT_Area first(ADT_Area area) {
        

        return null;
    }

    @Override
    ADT_Area next(ADT_Area area, ADT_Area s) {
        return null;
    }
}
