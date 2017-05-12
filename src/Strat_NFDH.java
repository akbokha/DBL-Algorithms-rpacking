
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Bastiaan
 */
//Next-Fit Decreasing Height
public class Strat_NFDH extends Strat_AbstractStrat {
    
    public Strat_NFDH(ADT_Area area) {
        super(area);
    }
    
    @Override
    public ADT_Area compute() {
        
        ArrayList<ADT_Rectangle> toBePlaced = new ArrayList<>();
        area.getRectangles().forEachRemaining(toBePlaced::add);
        Collections.sort(toBePlaced, new ADT_SortRecOnWidth()); //Sort list in non-increasing order on width
        //shelf {width of the shelf, height of the shelf, x-position of the shelf}
        Integer[] shelf = new Integer[]{toBePlaced.get(0).getWidth(), toBePlaced.get(0).getHeight(), 0};
        toBePlaced.get(0).setX(0);
        toBePlaced.get(0).setY(0);
        toBePlaced.remove(0);//Place first rectangle and remove it from the queue
        //Go on until the queue is empty
        while(!toBePlaced.isEmpty()) {
            //If the rectangle fits on the last shelf
            if(toBePlaced.get(0).getHeight() <= area.getHeight() - shelf[1]) {
                toBePlaced.get(0).setX(shelf[2]);
                toBePlaced.get(0).setY(shelf[1]);
                shelf[1] += toBePlaced.get(0).getHeight();
                
            } else {//If the rectangle is not placed, create a new shelf and place it there
                shelf = new Integer[]{toBePlaced.get(0).getWidth(), toBePlaced.get(0).getHeight(), shelf[2] + shelf[0]};
                toBePlaced.get(0).setX(shelf[2]);
                toBePlaced.get(0).setY(0);
            }
            toBePlaced.remove(0);
        }
        return area;
    }
}
