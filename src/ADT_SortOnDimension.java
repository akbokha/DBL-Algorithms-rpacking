
import java.util.Comparator;


/**
 *
 * @author Bastiaan
 */
public class ADT_SortOnDimension implements Comparator<ADT_Rectangle>{

    @Override
    public int compare(ADT_Rectangle o1, ADT_Rectangle o2) {
        int o1MaxDim = Math.max(o1.getHeight(), o1.getWidth());
        int o2MaxDim = Math.max(o2.getHeight(), o2.getWidth());
        return o2MaxDim - o1MaxDim;
        
    }
    
}
