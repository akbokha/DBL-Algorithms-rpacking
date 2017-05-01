/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy;
import java.util.Iterator;
import rectanglepacking.Area;
import rectanglepacking.Rectangle;

/**
 * @author Abdel 
 * 01-05-2017
 */
public class DummyImplementation extends AbstractStrategy {
    
    public DummyImplementation(Area area) {
        super(area);
    }
    
    @Override
    public Area computeArea() {
        // Simple dummy implementation that places all rectangles next to eachother in one single strip
        int curX = 0;
        for (Iterator<Rectangle> recs = area.getRectangles(); recs.hasNext();) {
            Rectangle currentRec = recs.next();
            currentRec.setX(curX);
            curX += currentRec.getWidth();
            currentRec.setY(0);
        }

        return area;
    }
    
}
