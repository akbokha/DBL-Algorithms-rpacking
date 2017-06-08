import java.util.Comparator;
import java.util.Random;

public class ADT_SortRecRandom implements Comparator<ADT_Rectangle> {
    Random random = new Random();

    @Override
    public int compare(ADT_Rectangle o1, ADT_Rectangle o2) {
       boolean returnOne = random.nextBoolean();
       if (returnOne) {
           return 1;
       } else {
           return -1;
       } 
    }
}
