import java.util.Comparator;

public class ADT_SortRecOnWidth implements Comparator<ADT_Rectangle> {

    @Override
    public int compare(ADT_Rectangle o1, ADT_Rectangle o2) {
        return o2.getWidth() - o1.getWidth();
    }
}
