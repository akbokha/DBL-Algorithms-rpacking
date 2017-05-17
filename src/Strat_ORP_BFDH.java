
import java.util.ArrayList;
import java.util.Collections;


/**
 *
 * @author Bastiaan
 */
//Best-Fit Decreasing Height
public class Strat_ORP_BFDH extends Strat_AbstractStrat{
    public Strat_ORP_BFDH(ADT_Area area) {
        super(area);
    }
    
    @Override
    public ADT_Area compute() {
        ArrayList<ADT_Rectangle> toBePlaced = new ArrayList<>();
        area.getRectangles().forEachRemaining(toBePlaced::add);
        Collections.sort(toBePlaced, new ADT_SortRecOnWidth()); //Sort list in non-increasing order on width
        //shelf {width of the shelf, height of the shelf, x-position of the shelf}
        ArrayList<Integer[]> shelfs = new ArrayList<>();
        shelfs.add(new Integer[]{toBePlaced.get(0).getWidth(), toBePlaced.get(0).getHeight(), 0});
        toBePlaced.get(0).setX(0);
        toBePlaced.get(0).setY(0);
        toBePlaced.remove(0);//Place the first rectangle and remove it from queue
        //Go on until the list is empty
        while(!toBePlaced.isEmpty()) {
            Integer[] bestShelf = null;
            //Check all shelves for the best fit for the current rectangle
            for(Integer[] shelf : shelfs){
                if(toBePlaced.get(0).getHeight() <= area.getHeight() - shelf[1]) {//If the rectangle fits on the shelf
                    if(bestShelf != null){
                        if(area.getHeight() - bestShelf[1] > area.getHeight() - shelf[1]) {//If the rectangle fits better than a previous one
                            bestShelf = shelf;
                        }
                    } else {
                        bestShelf = shelf;
                    }
                }
            }
            //If the rectangle doesnt fit in any shelf, create a new one
            if(bestShelf == null) {
                Integer[] lastShelf = shelfs.get(shelfs.size()-1);
                int shelfWidth = toBePlaced.get(0).getWidth();
                int shelfHeight = toBePlaced.get(0).getHeight();
                int shelfX = lastShelf[0] + lastShelf[2];
            
                toBePlaced.get(0).setX(shelfX);
                toBePlaced.get(0).setY(0);
                shelfs.add(new Integer[]{shelfWidth, shelfHeight, shelfX});
            } else {//Else place him there
                toBePlaced.get(0).setX(bestShelf[2]);
                toBePlaced.get(0).setY(bestShelf[1]);
                bestShelf[1] += toBePlaced.get(0).getHeight();
            }
            toBePlaced.remove(0);
        }
        return area;
    }
}
