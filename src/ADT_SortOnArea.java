import java.util.Comparator;

public class ADT_SortOnArea implements Comparator<ADT_Rectangle> {
    // Sort rectangles based on their area's (in non-increasing order)

    @Override
    public int compare(ADT_Rectangle o1, ADT_Rectangle o2) {
        int area1 = o1.getWidth() * o1.getHeight();
        int area2 = o2.getWidth() * o2.getHeight();
        return area2 - area1; // non-increasing order
    }
}