public class Strat_BT_PrunerEmptySpace implements Strat_BT_PrunerInterface {
    
    @Override
    public boolean reject(ADT_AreaExtended area, ADT_Rectangle last) {
        int[] stripsEmptySpace; //Array which stores the strips of empty space
        int[] stripsRecsTBP; //Array which stores the strips of rectangles to be placed
        //true = horizontal strips
        stripsEmptySpace = area.getEmptySpaceStrips(true);
        stripsRecsTBP = area.getRectangleStrips(true);
        boolean horizontalTest = testStrips(stripsEmptySpace, stripsRecsTBP);
        //false = vertical strips
        stripsEmptySpace = area.getEmptySpaceStrips(false);
        stripsRecsTBP = area.getRectangleStrips(false);
        //When both are true, the strips will NOT fit in both ways (horizontal and vertical)
        return testStrips(stripsEmptySpace, stripsRecsTBP) && horizontalTest;
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