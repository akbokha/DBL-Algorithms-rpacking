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
public class Strategy_DummyImplementation extends Strategy_AbstractStrategy {
    
    public Strategy_DummyImplementation(ADT_Area area) {
        super(area);
    }
    
    @Override
    public ADT_Area computeArea() {
        // Simple dummy implementation that places all rectangles next to each other in one single strip
        int curX = 0;
        for (Iterator<ADT_Rectangle> recs = area.getRectangles(); recs.hasNext();) {
            ADT_Rectangle currentRec = recs.next();
            currentRec.setX(curX);
            curX += currentRec.getWidth();
            currentRec.setY(0);
        }

        return area;
    }
    
}
