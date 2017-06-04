import java.util.logging.Level;
import java.util.logging.Logger;

public class Strat_ORP_AnyTime extends Strat_AbstractStrat {
    
    int STEPSIZE = 1;
    
    public Strat_ORP_AnyTime(ADT_Area area) {
        super(area);
    }

    @Override
    public ADT_Area compute() {
        //Used to initialize an average starting width and height
        ADT_Area bestArea = new Strat_DummyImplementation(area.clone()).compute();
        //Set initial width and height for a container to the getDimensions
        // of the bottom-left algorithm
        ADT_Vector dimension = bestArea.getDimensions();
        int width = dimension.x;
        int height = dimension.y;
        areaEx = area.toExtended(width, height);
        while(true) {
            //Make sure that the area gets smaller and smaller until the
            // minimal area is reached
            if((width-STEPSIZE) * height >= areaEx.getTotalAreaRectanglesToBePlaced() && width-STEPSIZE >= areaEx.getRectanglesToBePlaced()[0].getWidth()) {
                width -= STEPSIZE;
                System.err.print("W:" + width + "\tH:" + height + "\t");

                //Get a solution with this width and height
                ADT_AreaExtended newArea = createNewSolution(width, height);

                if (newArea == null) {
                    System.err.println("Failed");
                } else {
                    System.err.println("Success");
                }

                //If a solution was set, use it as the new best solution
                if(newArea != null) {
                    bestArea = newArea.clone().toArea();
                } else if (STEPSIZE == 1) {//If stepsize == 1 and no solution is found, increase height
                    if(area.getHeight() != ADT_Area.INF) {// but if the height was fixed, no better solution can be found
                        break;
                    }
                    width += STEPSIZE;
                    height += 1;
                } else {//If this area is not possible try a larger height but the same width as bestArea
                    width += STEPSIZE;
                    STEPSIZE = 1;
                }
            } else if(STEPSIZE == 1) {//If stepsize is 1 and cant be made smaller
                break;
            } else {
                STEPSIZE--;
            }
        }
        return bestArea;
    }
    
    ADT_AreaExtended createNewSolution(int width, int height) {
        // @todo move this clone-like function to the ADT_Area
        ADT_Rectangle[] rectangles = new ADT_Rectangle[areaEx.getCount()];
        ADT_Rectangle[] oldRectangles = areaEx.getRectangles();
        for (int i = 0; i < oldRectangles.length; i++) {
            rectangles[i] = oldRectangles[i].clone();
        }

        ADT_AreaExtended newArea = new ADT_AreaExtended(width, height, areaEx.canFlip(), rectangles);
        newArea = (new Strat_CP_BT(newArea)).compute();
        return newArea;
    }
    
}
