public class Strat_ORP_AnyTime extends Strat_AbstractStrat {
    
    int stepsize = 5;
    Strat_BT_PrunerInterface[] pruners;
    DataMining data;
    
    public Strat_ORP_AnyTime(ADT_Area area) {
        super(area);
        data = new DataMining(pruners);
        /*new Strat_BT_PrunerEmptySpace(), new Strat_BT_Pruner_WS2(), new Strat_BT_PrunerPerfectRectangle(), new Strat_BT_Pruner_NarrowEmptyStrips()*/
        Strat_BT_PrunerInterface pruner = new PrunerDataMining(new Strat_BT_Pruner_WS2(), data.dataSet);
        pruners = new Strat_BT_PrunerInterface[]{pruner};
    }

    @Override
    public ADT_Area compute() {
        int optimalArea = area.getTotalAreaRectangles();
        ADT_Area bestArea = new Strat_ORP_BinaryTreePacker(area.clone()).compute();
        ADT_Vector dim = bestArea.getDimensions();
        int bestDimensens = dim.x * dim.y;

        //Used to initialize an average starting width and height
        ADT_Area testArea = new Strat_DummyImplementation(area.clone()).compute();
        //Set initial width and height for a container to the getDimensions
        // of the bottom-left algorithm
        ADT_Vector dimension = testArea.getDimensions();
        int width = dimension.x;
        int height = dimension.y;
        areaEx = area.toExtended(width, height);
        while(true) {
            //Make sure that the area gets smaller and smaller until the
            // minimal area is reached
            if((width-stepsize) * height >= optimalArea && width - stepsize >= areaEx.getRectanglesToBePlaced()[0].getWidth()) {
                width -= stepsize;
                if(width * height >= bestDimensens) {
                    continue;
                }
                int areaSize = width * height;
                System.err.print("W:" + width + "\tH:" + height + "\tArea: " + areaSize + "\tFR: " + ((float) optimalArea / (float) areaSize) + "\t");

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
                    
                    ADT_Vector vec = bestArea.getDimensions();
                    bestDimensens = vec.x * vec.y;
                } else if (stepsize == 1) {//If stepsize == 1 and no solution is found, increase height
                    if(area.getHeight() != ADT_Area.INF/* || (height+1) * (width) >= bestArea.getWidth() * bestArea.getHeight()*/) {// but if the height was fixed, no better solution can be found
                        break;
                    }
//                    width += stepsize;
                    height += 1;
                } else {//If this area is not possible try one larger width
                    width += stepsize;
                    stepsize = 1;
                }
            } else if(stepsize == 1) {//If stepsize is 1 and cant be made smaller
                break;
            } else {
                stepsize--;
            }
        }
        bestArea.setHeight(area.getHeight());

        CSV_Parser parser = new CSV_Parser();
        parser.parse(".\\", data);
        return bestArea;
    }
    
    ADT_AreaExtended createNewSolution(int width, int height) {
        ADT_AreaExtended newArea = area.toExtended(width, height);
        
        Strat_CP_BT backtracker = new Strat_CP_BT(newArea);
        for(Strat_BT_PrunerInterface pruner : pruners) {
            backtracker.addPruner(pruner);
        }
        
        newArea = backtracker.compute();
        return newArea;
    }
    
}
