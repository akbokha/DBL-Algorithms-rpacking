public class Strat_ORP_AnyTime extends Strat_AbstractStrat {
    
    private int stepsize = 1;
    private int bestArea = Integer.MAX_VALUE;
    private Strat_BT_PrunerInterface[] pruners;
    private ADT_Area bestResult;

    final int MAX_COMPUTE_TIME_MS = 270000;

    Strat_ORP_AnyTime(ADT_Area area) {
        super(area);
    }

    Strat_ORP_AnyTime(ADT_Area area, Strat_BT_PrunerInterface[] pruners) {
        super(area);

        this.pruners = pruners;
    }

    Strat_ORP_AnyTime(ADT_Area area, Strat_BT_PrunerInterface[] pruners, ADT_Area previousResult) {
        super(area);

        bestResult = previousResult;
        this.pruners = pruners;

        ADT_Vector dimensions = previousResult.getDimensions();
        bestArea = dimensions.x * dimensions.y;
    }

    @Override
    public ADT_Area compute() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                bestResult = computeLoop();
            }
        });
        thread.start();

        try {
            Thread.sleep(MAX_COMPUTE_TIME_MS);
            thread.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            return bestResult;
        }
    }

    public ADT_Area computeLoop() {

        int rectanglesArea = area.getTotalAreaRectangles();

        //Used to initialize an average starting width and height
        ADT_Area horizontalStripResult = new Strat_HorizontalStrip(area.clone()).compute();
        //Set initial width and height for a container to the getDimensions
        // of the bottom-left algorithm
        ADT_Vector dimension = horizontalStripResult.getDimensions();
        int width = dimension.x;
        int height = dimension.y;
        areaEx = area.toExtended(width, height);
        while(true) {
            //Make sure that the area gets smaller and smaller until the minimal area is reached.
            if((width-stepsize) * height >= rectanglesArea && width - stepsize >= areaEx.getRectanglesToBePlaced()[0].getWidth()) {
                width -= stepsize;
                if(width * height >= bestArea) {
                    continue;
                }

                int areaSize = width * height;
                System.err.print("W:" + width + "\tH:" + height + "\tArea: " + areaSize + "\tFR: " + ((float) rectanglesArea / (float) areaSize) + "\t");

                //Get a solution with this width and height
                ADT_AreaExtended newArea = createNewSolution(width, height);

                if (newArea == null) {
                    System.err.println("Failed");
                } else {
                    System.err.println("Success");
                }

                //If a solution was set, use it as the new best solution
                if(newArea != null && newArea.getArea() < bestArea) {
                    bestResult = newArea.clone().toArea();
                    bestArea = newArea.getArea();
                } else if (stepsize == 1) {//If stepsize == 1 and no solution is found, increase height
                    if(area.getHeight() != ADT_Area.INF/* || (height+1) * (width) >= bestResult.getWidth() * bestResult.getHeight()*/) {// but if the height was fixed, no better solution can be found
                        break;
                    }
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

        // If the backtracker didn't find any better solution, use the result from the horizontal strip strategy.
        if (bestResult == null) {
            bestResult = horizontalStripResult;
        }

        bestResult.setHeight(area.getHeight());
        bestResult.sortAs(area.getRectangles()); // Sort the area in the same manner as it originally was.

        return bestResult;
    }
    
    private ADT_AreaExtended createNewSolution(int width, int height) {
        ADT_AreaExtended newArea = area.toExtended(width, height);
        
        Strat_CP_BT backtracker = new Strat_CP_BT(newArea);

        if (pruners != null) {
            for (Strat_BT_PrunerInterface pruner : pruners) {
                backtracker.addPruner(pruner);
            }
        }
        
        newArea = backtracker.compute();
        return newArea;
    }
    
}
