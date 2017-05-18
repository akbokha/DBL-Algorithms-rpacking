
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Bastiaan
 */
public class Strat_ORP_AnyTime extends Strat_AbstractStrat {
    public Strat_ORP_AnyTime(ADT_Area area) {
        super(area);
    }
    
    @Override
    public ADT_Area compute() {
        if(area.getRectangles().length == 10000) {
            return new Strat_ORP_BFDH(area).compute();
        }
        try {
            //Used to initialize an average starting width and height
            ADT_Area bestArea = new Strat_DummyImplementation(area.clone()).compute();
            //Set initial width and height for a container to the dimensions
            // of the bottom-left algorithm
            ADT_Vector dimension = bestArea.getMinDimensions();
            area.setWidth(dimension.x);
            area.setHeight(dimension.y);
            
            while(true) {
                //Make sure that the area gets smaller and smaller until the
                // minimal area is reached
                if((area.getWidth()-1) * area.getHeight() > area.getTotalAreaRectangles()) {
                    area.setWidth(area.getWidth()-1);
                } else {
                    break;
                }
                //Get the best solution with this width and height
                ADT_Area newArea = new Strat_CP_BT(area.clone()).compute();
                
                //If a solution was set, use it as the new best solution
                if(newArea != null) {
                    bestArea = newArea;
                } else if (area.getHeight() != ADT_Area.INF) {//If height is fixed but not possible to make the area smaller
                    break;
                } else {//If this area is not possible try a larger height but the same width as bestArea
                    area.setWidth(area.getWidth()+1);
                    area.setHeight(area.getHeight()+1);
                }
            }
            return bestArea;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Strat_ORP_AnyTime.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
