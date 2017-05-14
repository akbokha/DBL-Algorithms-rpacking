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
        int[] stripsEmptySpace = null; //Array which stores the strips of empty space
        int[] stripsRecsTBP = null; //Array which stores the strips of rectangles to be placed
        //true = horizontal strips
        //stripsEmptySpace = area.getEmptySpaceStrips(true);
        //stripsRecsTBP = area.getRectanlgeStrips(true);
        if(testStrips(stripsEmptySpace, stripsRecsTBP)) return true;
        //false = vertical strips
        //stripsEmptySpace = area.getEmptySpaceStrips(false);
        //stripsRecsTBP = area.getRectanlgeStrips(false);
        return testStrips(stripsEmptySpace, stripsRecsTBP);
    }
    /**
     * Computes if the strips of the to be placed rectangles fit into the strips
     * of the empty space.
     * 
     * @param stripsEmptySpace an array with the index i the width or height of
     * a strip and <code>stripsEmptySpace[i]</code> the number of strips with
     * the same width or height. The strips are the strips of empty space.
     * @param stripsRecsTBP the same as stripsEmptySpace but then for the 
     * rectangles that still have to be placed.
     * @return if the strips of the rectangles that still have to be placed
     * fit in the strips of the empty space
     */
    private boolean testStrips(int[] stripsEmptySpace, int[] stripsRecsTBP) {
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
