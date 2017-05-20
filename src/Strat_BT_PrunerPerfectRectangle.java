/**
 * Prunes by dominance created by perfect rectangles of empty space to the left 
 * or below newly placed rectangles.
 * This method is discussed in section 4.3.1 of "Optimal rectangle packing"
 * by Korf et al.
 * 
 * @author Jorrit Olthuis
 */
public class Strat_BT_PrunerPerfectRectangle implements Strat_BT_PrunerInterface {

    @Override
    public boolean reject(ADT_Area area, ADT_Rectangle last) {
        /*
         * If there is a perfect rectangle under or to the left of the position
         * of the current rectangle, this position is dominated, hence reject.
         */
        return (perfectRectBelow((ADT_AreaExtended)area, last) || perfectRectLeft((ADT_AreaExtended)area, last));
    }
    
    /**
     * Checks if there is a perfect rectangle of empty space below last
     * 
     * @param last rectangle to look under for perfect rectangle of empty space
     * @param area the area in which last has been placed
     * @return true if there is such a rectangle
     */
    private boolean perfectRectBelow(ADT_AreaExtended area, ADT_Rectangle last){
        int lowestPoint = Integer.MAX_VALUE; // lowest point of possible perfect rectangle
        int beginX = last.getX();
        int endX = last.getX()+last.getWidth();
        
        // Scan horizontally to find bottom of a possibly perfect rectangle
        int y = last.getY();
        
        if(y==0) return false; // There is no dominance below possible
            
        while(lowestPoint == Integer.MAX_VALUE){ // We haven't reached the bottom of the rectangle
            y--;
            
            // First check right and left border
            if(!(area.isEmptyAt(beginX-1, y) && area.isEmptyAt(beginX+endX, y))){
                // This must be the lowest level, otherwise it is not valid  (missing side border)
                lowestPoint = y;
            }
            
            for(int x = beginX; x < endX; ++x){
                if(!area.isEmptyAt(x, y) && x == beginX){
                    // This possibly the beginning of a line of borders
                    lowestPoint = y;
                }else if(!area.isEmptyAt(x, y) && lowestPoint == Integer.MAX_VALUE){
                    /*
                    We encountered a border halfway on the line, but this line hasn't
                    seen a border yet. This means the rectangle is not perfect.
                    */
                    return false;
                }else if(area.isEmptyAt(x, y) && lowestPoint != Integer.MAX_VALUE){
                    /*
                    We found no border at this point, because we did already find
                    border on this line, the rectangle is not perfect.
                    */
                    return false;
                }
            }
            
            // The bottom of the bounding rectangle is a good bottom
            if(y-1 == 0) lowestPoint = y;
        }

        // All checks passed, so we found a perfect rectangle
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
        int leftMostPoint = Integer.MAX_VALUE; // lowest point of possible perfect rectangle
        int beginY = last.getY();
        int endY = last.getY()+last.getHeight();
        
        // Scan vertically to find bottom of a possibly perfect rectangle
        int x = last.getX();
        if(x==0) return false;
        
        while(leftMostPoint == Integer.MAX_VALUE){
            x--;
            
            // First check upper and lower border
            if(!(area.isEmptyAt(x, beginY-1) && area.isEmptyAt(x, beginY+endY))){
                // This must be the lowest level, otherwise it is not valid (missing side border)
                leftMostPoint = x;
            }
            
            for(int y = beginY; y < endY; ++y){
                if(!area.isEmptyAt(x, y) && y == beginY){
                    // This possibly the beginning of a row of borders
                    leftMostPoint = x;
                }else if(!area.isEmptyAt(x, y) && leftMostPoint == Integer.MAX_VALUE){
                    /*
                    We encountered a border halfway on the line, but this line hasn't
                    seen a border yet. This means the rectangle is not perfect.
                    */
                    return false;
                }else if(area.isEmptyAt(x, y) && leftMostPoint != Integer.MAX_VALUE){
                    /*
                    We found no border at this point, because we did already find
                    border on this line, the rectangle is not perfect.
                    */
                    return false;
                }
            }
            
            // The left side of the bounding rectangle is a good leftmost point
            if(x-1 == 0) leftMostPoint = x;
        }
        
        // All checks passed, so we found a perfect rectangle
        return true;
    }
    
}
