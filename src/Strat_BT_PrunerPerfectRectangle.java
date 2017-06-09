/**
 * Prunes by dominance created by perfect rectangles of empty space to the left 
 * or below newly placed rectangles.
 * This method is discussed in section 4.3.1 of "Optimal rectangle packing"
 * by Korf et al.
 */
public class Strat_BT_PrunerPerfectRectangle implements Strat_BT_PrunerInterface {

    @Override
    public boolean reject(ADT_AreaExtended area, ADT_Rectangle last) {
        /*
         * If there is a perfect rectangle under or to the left of the position
         * of the current rectangle, this position is dominated, hence reject.
         */
        if(perfectRectBelow(area, last)) return true;
        return perfectRectLeft(area, last);
    }
    
    /**
     * Checks if there is a perfect rectangle of empty space below last
     * 
     * @param last rectangle to look under for perfect rectangle of empty space
     * @param area the area in which last has been placed
     * @return true if there is such a rectangle
     */
    private boolean perfectRectBelow(ADT_AreaExtended area, ADT_Rectangle last){
        int x = last.getX();
        int y = last.getY() - 1;
        int possibleY = y;
        int maxX = last.getX() + last.getWidth() - 1;
        int minY = 0;
        
        //Check if it is even possible to have an open space beneath the last rectangle
        if(y < 0 || !area.isEmptyAt(x, y)) return false;
        //Set a possible y coordinate for the open space rectangle
        // from the lower border of the last rectangle to the nearest lowest rectangle or border
        while(possibleY > minY && area.isEmptyAt(x, possibleY)) possibleY--;
        //Check if the the space given by (x,y) -> (maxX, possibleY -1) is empty
        for(x = last.getX(); x <= maxX; x++) {
            for(y = last.getY() - 1; y > possibleY; y--) {
                if(!area.isEmptyAt(x, y)) {
                    return false;
                }
            }
        }
        
        //Check that the borders are enclosing
        x = last.getX() - 1;
        for(y = last.getY() - 1; y > possibleY; y--) {
            if(x >= 0) {
                if(area.isEmptyAt(x, y)) return false;
            }
            if(maxX + 1 > area.getWidth() - 1) {
                if(area.isEmptyAt(maxX + 1, y)) return false;
            }
        }
        y = possibleY + 1;
        for(x = last.getX(); x <= maxX; x++) {
            if(area.isEmptyAt(x, y)) return false;
        }
        
        return true;
    }
    
    /**
     * Checks if there is a perfect rectangle of empty space left of last
     * 
     * @param last rectangle to look left for perfect rectangle of empty space
     * @param area the area in which last has been placed
     * @return true if there is such a rectangle
     */
    private boolean perfectRectLeft(ADT_AreaExtended area, ADT_Rectangle last){
        int x = last.getX() - 1;
        int y = last.getY();
        int possibleX = x;
        int minX = 0;
        int maxY = y + last.getHeight() - 1;
        
        //Check if it is even possible to have an open space beneath the last rectangle
        if(x < 0 || !area.isEmptyAt(x, y)) return false;
        //Set a possible y coordinate for the open space rectangle
        // from the left border of the last rectangle to the nearest left rectangle or border
        while(possibleX > minX && area.isEmptyAt(possibleX, y)) possibleX--;
        //Check if the the space given by (x,y) -> (possibleX - 1, possibleY -1) is empty
        for(x = last.getX() - 1; x > possibleX; x--) {
            for(y = last.getY(); y <= maxY; y++) {
                if(!area.isEmptyAt(x, y)) {
                    return false;
                }
            }
        }
        
        //Check that the borders are enclosing
        x = possibleX + 1;
        for(y = last.getY(); y <= maxY; y++) {
            if(area.isEmptyAt(x, y)) return false;
        }
        y = last.getY() - 1;
        for(x = last.getX(); x > possibleX; x--) {
            if(y >= 0) {
                if(area.isEmptyAt(x, y)) return false;
            }
            if(maxY < area.getHeight()-1) {
                if(area.isEmptyAt(x, maxY + 1)) return false;
            }
        }
        
        return true;
    }
    
}
