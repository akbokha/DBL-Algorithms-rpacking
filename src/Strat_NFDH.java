
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


/**
 *
 * @author Bastiaan
 */
public class Strat_NFDH extends Strat_AbstractStrat {
    
    public Strat_NFDH(ADT_Area area) {
        super(area);
    }
    
    @Override
    public ADT_Area compute() {
        
        ArrayList<ADT_Rectangle> toBePlaced = new ArrayList<>();
        area.getRectangles().forEachRemaining(ADT_Rectangle::toggleFlipped);
        area.getRectangles().forEachRemaining(toBePlaced::add);
        Collections.sort(toBePlaced); //Sort list in non-increasing order
        ArrayList<ADT_Area> shelf = new ArrayList<>();
        shelf.add(new ADT_Area(toBePlaced.get(0).getHeight(), Integer.MAX_VALUE, false));
        
        for(Iterator<ADT_Rectangle> recs = area.getRectangles(); recs.hasNext();) {
            ADT_Rectangle currentRec = recs.next();
            if(currentRec.getWidth() <= area.getHeight() - shelf.get(0).getWidth()) {
                shelf.get(0).add(currentRec);
            } else {//Place the new shelf in front of the array for easy access
                shelf.add(0, new ADT_Area(toBePlaced.get(0).getHeight(), Integer.MAX_VALUE, false));
            }
        }
        return area;
    }
    
}
