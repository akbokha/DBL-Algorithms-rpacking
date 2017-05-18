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
        int[] stripsEmptySpace; //Array which stores the strips of empty space
        int[] stripsRecsTBP; //Array which stores the strips of rectangles to be placed
        //true = horizontal strips
        stripsEmptySpace = area.getEmptySpaceStrips(true);
        stripsRecsTBP = area.getRectangleStrips(true);
        int horizontalWastedSpace = testStrips(stripsEmptySpace, stripsRecsTBP);
        //false = vertical strips
        stripsEmptySpace = area.getEmptySpaceStrips(false);
        stripsRecsTBP = area.getRectangleStrips(false);
        //When both are true, the strips will NOT fit in both ways (horizontal and vertical)
        int verticalWastedSpace = testStrips(stripsEmptySpace, stripsRecsTBP);
        int wastedSpace = (horizontalWastedSpace > verticalWastedSpace) ? horizontalWastedSpace : verticalWastedSpace;
        //Rejects if totalarea + wasted space is larger than the given area to fit it in
        return wastedSpace + area.getTotalAreaRectangles() > area.getHeight() * area.getWidth();
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
    private int testStrips(int[] stripsEmptySpace, int[] stripsRecsTBP) {
        int wastedSpace = 0;
        int carryOver = 0;
        //Loop through widths of strips to be placed
        for(int i = 0; i < stripsRecsTBP.length && stripsRecsTBP[i] != 0; i++) {
            //If there is enough empty space for all rectangle strips of the same size
            // add the rest of the strips to wasted space
            if(stripsEmptySpace[i] > stripsRecsTBP[i] + carryOver) {
                wastedSpace += stripsEmptySpace[i] - stripsRecsTBP[i] - carryOver;
                carryOver = 0;
            } else if(stripsEmptySpace[i] + carryOver == stripsRecsTBP[i]) {
                //Otherwise search for the next strip where it fits
                carryOver = 0;
            } else if(stripsEmptySpace[i] + carryOver > stripsRecsTBP[i]) {
                carryOver += stripsEmptySpace[i] + carryOver - stripsRecsTBP[i];
            }
        }
        return wastedSpace;
    }
}