
import java.util.ArrayList;
import java.util.Collections;


/**
 *
 * @author Bastiaan
 */
public class Strat_FFDH extends Strat_AbstractStrat {
    public Strat_FFDH(ADT_Area area) {
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
        toBePlaced.remove(0);
        
        while(!toBePlaced.isEmpty()) {
            boolean placed = false;
            for(Integer[] shelf : shelfs){
                if(toBePlaced.get(0).getHeight() <= area.getHeight() - shelf[1]) {
                    toBePlaced.get(0).setX(shelf[2]);
                    toBePlaced.get(0).setY(shelf[1]);
                    shelf[1] += toBePlaced.get(0).getHeight();
                    placed = true;
                    break;
                }
            }
            
            if(!placed) {
                Integer[] lastShelf = shelfs.get(shelfs.size()-1);
                int shelfWidth = toBePlaced.get(0).getWidth();
                int shelfHeight = toBePlaced.get(0).getHeight();
                int shelfX = lastShelf[0] + lastShelf[2];
            
                toBePlaced.get(0).setX(shelfX);
                toBePlaced.get(0).setY(0);
                shelfs.add(new Integer[]{shelfWidth, shelfHeight, shelfX});
            }
            toBePlaced.remove(0);
        }
        return area;
    }
    
}
