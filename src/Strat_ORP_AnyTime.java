
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
        if(area.getRectangles().length >= 10000) {
            return new Strat_ORP_BFDH(area).compute();
        }
        try {
            //Used to initialize an average starting width and height
            ADT_Area bestArea = new Strat_DummyImplementation(area.clone()).compute();
            //Set initial width and height for a container to the getDimensions
            // of the bottom-left algorithm
            ADT_Vector dimension = bestArea.getMinimalDimensions();
            int width = dimension.x;
            int height = dimension.y;
            while(true) {
                //Make sure that the area gets smaller and smaller until the
                // minimal area is reached
                if((width-1) * height >= area.getTotalAreaRectangles() && width > area.getMaximalRectangleWidth()) {
                    width -= 1;
                } else {
                    break;
                }

                System.err.print("W:" + width + "\tH:" + height + "\t");

                //Get the best solution with this width and height
                ADT_Area newArea = area.clone();
                newArea.setWidth(width);
                newArea.setHeight(height);
                newArea = (new Strat_CP_BT(newArea)).compute();

                System.err.println(newArea);

                //If a solution was set, use it as the new best solution
                if(newArea != null) {
                    bestArea = newArea;
                } else if (area.getHeight() != ADT_Area.INF) {//If height is fixed
                    break;
                } else {//If this area is not possible try a larger height but the same width as bestArea
                    width += 1;
                    height += 1;
                }
            }
            return bestArea;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Strat_ORP_AnyTime.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
