/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author s147889
 */
public class Strat_BT_PrunerEmptySpace implements Strat_BT_PrunerInterface {

    @Override
    public boolean reject(ADT_Area area, ADT_Rectangle last) {
        int[] stripsEmptySpace = area.getEmptySpaceStrips(true);
        int[] stripsRecsTBP = area.getRectanlgeStrips(true);
        
        //If strips of the rectangles are longer than the empty space
        // there is no valid solution possible
        if(stripsEmptySpace.length < stripsRecsTBP.length) {
            return true;
        }
        //Loop through widths of strips to be placed
        for(int i = 0; i < stripsRecsTBP.length && stripsRecsTBP[i] != 0; i++) {
            nextRec:
            //Loop through all strips of the same length
            while(stripsRecsTBP[i] > 0) {
                //If there is an empty space the same size as the rectangle strip
                // place it there
                if(stripsEmptySpace[i] > 0) {
                    stripsEmptySpace[i] -= 1;
                    stripsRecsTBP[i]--;
                } else {
                    //Otherwise search for the next strip where it fits
                    for(int j = i; j < stripsEmptySpace.length; j++) {
                        if(stripsEmptySpace[j] != 0) {
                            //By placing the strip in a spot with a larger width
                            // you create a place with j - i width
                            stripsEmptySpace[j] -= 1;
                            stripsEmptySpace[j-i] += 1;
                            stripsRecsTBP[i]--;
                            continue nextRec; //Place next rectangle
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
