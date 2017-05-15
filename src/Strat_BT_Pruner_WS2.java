/*
 * Wasted Space pruning as described in R. E. Korf - Optimal Rectangle Packing
 */

/**
 * date: 12-05-2017
 * @author s158881
 */
public class Strat_BT_Pruner_WS2 implements Strat_BT_PrunerInterface {
   
    private int [] horizontalEmptySpaceStrips;
    private int [] verticalEmptySpaceStrips;
    private int [] horizontalRectangleStrips;
    private int [] verticalRectangleStrips;
    
    @Override
    public boolean reject (ADT_Area area, ADT_Rectangle rec) {
         boolean reject = false;
         
        //this.horizontalEmptySpaceStrips = getEmptySpaceStrips(true);
        //this.verticalEmptySpaceStrips = getEmptySpaceStrips(false);
        //this.horizontalRectangleStrips = getRectangleStrips(true);
        //this.verticalRectangleStrips = getRectangleStrips(false);
        
        
        return reject;
    }
    
}
