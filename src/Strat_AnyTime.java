
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Bastiaan
 */
public class Strat_AnyTime extends Strat_AbstractStrat {
    public Strat_AnyTime(ADT_Area area) {
        super(area);
    }
    
    @Override
    public ADT_Area compute() {
        try {
            //Used to initialize an average starting width and height
            ADT_Area bestArea = new Strat_BottomLeft((ADT_Area) area.clone()).compute();
            ADT_Vector dimension = bestArea.getMinDimensions();
            int width = dimension.x;
            int height = dimension.y;
            
            while(true) {
                //Make sure that the area gets smaller and smaller until minimal area is reached
                if((width-1) * height > area.getTotalAreaRectangles()) {
                    width -= 1;
                } else {
                    break;
                }
                //Get the best solution with this width and height
                ADT_Area newArea = new Strat_BT_Example(new ADT_Area(width, height, area.canFlip())).compute();
                
                //If a solution was set, use it as the new best solution
                if(newArea != null) {
                    bestArea = newArea;
                } else if (area.getHeight() != ADT_Area.INF) {//If height is fixed but not possible to make the area smaller
                    break;
                } else {//If this area is not possible try a smaller height but the same width as bestArea
                    width += 2;
                    height -= 1;
                }
            }
            return bestArea;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Strat_AnyTime.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
