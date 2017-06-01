import java.util.ArrayList;
import java.util.Collections;

/**
 * First-Fit Decreasing Height algorithm implementation to solve rectangle packing.
 */
public class Strat_ORP_FFDH extends Strat_AbstractStrat {
    public Strat_ORP_FFDH(ADT_AreaExtended area) {
        super(area);
    }
    @Override
    public ADT_AreaExtended compute() {
        ArrayList<ADT_Rectangle> toBePlaced = new ArrayList<>();
        area.getRectangleIterator().forEachRemaining(toBePlaced::add);
        Collections.sort(toBePlaced, new ADT_SortRecOnWidth()); //Sort list in non-increasing order on width
        //shelf {width of the shelf, height of the shelf, x-position of the shelf}
        ArrayList<Integer[]> shelfs = new ArrayList<>();
        shelfs.add(new Integer[]{toBePlaced.get(0).getWidth(), toBePlaced.get(0).getHeight(), 0});
        toBePlaced.get(0).setX(0);
        toBePlaced.get(0).setY(0);
        toBePlaced.remove(0);//Place first rectangle and remove it from the queue
        //Go on until the queue is empty
        next:
        while(!toBePlaced.isEmpty()) {
            for(Integer[] shelf : shelfs){//Check for all shelfs
                //If the current shelf is high enough, place the rectangle there
                if(toBePlaced.get(0).getHeight() <= area.getHeight() - shelf[1]) {
                    toBePlaced.get(0).setX(shelf[2]);
                    toBePlaced.get(0).setY(shelf[1]);
                    shelf[1] += toBePlaced.get(0).getHeight();
                    toBePlaced.remove(0);
                    continue next;//Continue with the next rectangle
                }
            }
            //If the rectangle is not placed, create a new shelf and place it there
            Integer[] lastShelf = shelfs.get(shelfs.size()-1);
            int shelfWidth = toBePlaced.get(0).getWidth();
            int shelfHeight = toBePlaced.get(0).getHeight();
            int shelfX = lastShelf[0] + lastShelf[2];

            toBePlaced.get(0).setX(shelfX);
            toBePlaced.get(0).setY(0);
            shelfs.add(new Integer[]{shelfWidth, shelfHeight, shelfX});
            toBePlaced.remove(0);
        }
        return area;
    }
    
}
