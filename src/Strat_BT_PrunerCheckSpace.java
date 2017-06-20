
/**
 *
 * @author Bastiaan
 */
public class Strat_BT_PrunerCheckSpace implements Strat_BT_PrunerInterface {
    ADT_Rectangle[] recs;
    
    @Override
    public boolean reject(ADT_AreaExtended area, ADT_Rectangle last, int index) {
        if(index > 5) {
            return false;
        }
        recs = area.getRectangles();
        return (checkRight(area, last) && checkLeft(area, last) && checkBottom(area, last)) || (checkBot(area, last) && checkTop(area, last) && leftCheck(area, last));
    }
    
    private boolean checkRight(ADT_AreaExtended area, ADT_Rectangle last) {
        for (ADT_Rectangle rec : recs) {
            if (rec.getWidth() <= area.getWidth() - last.getX() - last.getWidth()) {
                return false;
            }
        }
        
        return true;
    }
    
    private boolean checkLeft(ADT_AreaExtended area, ADT_Rectangle last) {
        for (ADT_Rectangle rec : recs) {
            if (rec.getWidth() <= last.getX()) {
                return false;
            }
        }
        
        return true;
    }
    
    private boolean checkTop(ADT_AreaExtended area, ADT_Rectangle last) {
        for (ADT_Rectangle rec : recs) {
            if (rec.getHeight() <= area.getHeight() - last.getY() - last.getHeight()) {
                return false;
            }
        }
        
        return true;
    }
    
    private boolean checkBot(ADT_AreaExtended area, ADT_Rectangle last) {
        for (ADT_Rectangle rec : recs) {
            if (rec.getHeight() <= last.getY()) {
                return false;
            }
        }
        
        return true;
    }
    
    private boolean leftCheck(ADT_AreaExtended area, ADT_Rectangle last) {
        //Check if the the space given by (x-1,y) -> (possibleX, maxY) is empty
        if(last.getX()-1 < 0) return false;
        for(int y = last.getY(); y < last.getHeight()+last.getY(); y++) {
            if(!area.isEmptyAt(last.getX()-1, y)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean checkBottom(ADT_AreaExtended area, ADT_Rectangle last) {
        if(last.getY()-1 < 0) return false;
        //Check if the the space given by (x+1,y) -> (maxX, possibleY) is empty
        for(int x = last.getX(); x < last.getWidth() + last.getX(); x++) {
            if(!area.isEmptyAt(x, last.getY()-1)) {
                return false;
            }
        }
        new Output_GraphicalOutput(area).draw();
        return true;
    }
}
