import java.util.Comparator;

public class ADT_SortRecOnHeight implements Comparator<ADT_Rectangle> {
    // Sort rectangles based on their area's (in non-increasing order)

    @Override
    public int compare(ADT_Rectangle o1, ADT_Rectangle o2) {
        return o2.getHeight() - o1.getHeight();
    }
}