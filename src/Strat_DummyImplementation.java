/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Iterator;

/**
 * @author Abdel 
 * 01-05-2017
 */
public class Strat_DummyImplementation extends Strat_AbstractStrat {
    
    public Strat_DummyImplementation(ADT_Area area) {
        super(area);
    }
    
    @Override
    public ADT_Area compute() {
        // Simple dummy implementation that places all rectangles next to each other in one single strip
        int curX = 0;
        for (Iterator<ADT_Rectangle> recs = area.getRectangles(); recs.hasNext();) {
            ADT_Rectangle currentRec = recs.next();

            // Set the x and y coordinate of the rectangle.
            currentRec.setY(0);
            currentRec.setX(curX);

            // If the rectangle is flippable, rotate it such that the longest side is horizontally.
            if (currentRec.canFlip() && (currentRec.getWidth() < currentRec.getHeight())) {
                currentRec.toggleFlipped();
            }

            curX += currentRec.getWidth();
        }

        return area;
    }
    
}
