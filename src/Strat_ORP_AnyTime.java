public class Strat_ORP_AnyTime extends Strat_AbstractStrat {
    
    int STEPSIZE = 5;
    
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
                } else {//If this area is not possible try one larger width
                    width += STEPSIZE;
                    STEPSIZE--;
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
        Strat_BT_PrunerInterface[] pruners = new Strat_BT_PrunerInterface[]{
            new Strat_BT_PrunerEmptySpace()/*, new Strat_BT_PrunerPerfectRectangle(), new Strat_BT_Pruner_NarrowEmptyStrips()*/, new Strat_BT_Pruner_WS2()
        };
        ADT_AreaExtended newArea = area.toExtended(width, height);
        
        Strat_CP_BT backtracker = new Strat_CP_BT(newArea);
        for(Strat_BT_PrunerInterface pruner : pruners) {
            backtracker.addPruner(pruner);
        }
        
        newArea = backtracker.compute();
        return newArea;
    }
    
}
