
import java.util.Comparator;


/**
 *
 * @author Bastiaan
 */
public class ADT_SortRecOnWidth implements Comparator<ADT_Rectangle> {

    @Override
    public int compare(ADT_Rectangle o1, ADT_Rectangle o2) {
        if(o1.getWidth() == o2.getWidth()){
            return 0;
        } else if(o1.getWidth() > o2.getWidth()) {
            return -1;
        }else {
            return 1;
        }
    }
}
