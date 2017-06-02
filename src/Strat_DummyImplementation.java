/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author Abdel 
 * 01-05-2017
 */
public class Strat_DummyImplementation extends Strat_AbstractStrat {
    
    public Strat_DummyImplementation(ADT_AreaExtended area) {
        super(area);
    }
    
    @Override
    public ADT_AreaExtended compute() {
        // Simple dummy implementation that places all rectangles next to each other in one single strip
        int curX = 0;
        int widthCurrentRentangle = 0;
        RectangleType[] recs = area.getRectangleTypesToBePlaced();
        for (RectangleType currentRec : recs) {
            while(currentRec.canInstantiate()) {
            // Set the x and y coordinate of the rectangle.
            area.addUnchecked(currentRec.createInstance(curX, 0));
            curX += currentRec.getWidth();

            // If the rectangle is flippable, rotate it such that the longest side is horizontally.
//            if (currentRec.canRotate() && (currentRec.getWidth() < currentRec.getHeight())) {
//                currentRec.
//            }
            }
        }
        return area;
    }
    
}
