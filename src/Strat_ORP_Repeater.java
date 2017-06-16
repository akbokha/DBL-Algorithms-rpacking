/**
 * Repeats BTP2D for a given area on randomly sorted rectangles.
 * Time limit and fill rate limit can be set in constructor.
 */
public class Strat_ORP_Repeater extends Strat_AbstractStrat {
    Strat_AbstractStrat strategy;
    long timeLimit;
    int desiredArea;
    long startTime;
    
    public Strat_ORP_Repeater(ADT_Area area, int msTimeLimit, double desiredFR) {
        super(area);
        this.startTime = PackingSolver.getStartTime();
        this.timeLimit = msTimeLimit;
        this.desiredArea = (int)Math.round(area.getTotalAreaRectangles()/desiredFR);
    }

    /**
     * Repeats strategy until time runs out or fill rate has been reached.
     * timeLimit is checked on starting new calculation.
     */
    @Override
    public ADT_Area compute() {
        ADT_Area bestArea = null;
        int leastArea = Integer.MAX_VALUE;
        // @todo Do not check for time manually, but wait for the interrupt.
        while(System.currentTimeMillis()-startTime < timeLimit && 
                leastArea > desiredArea){
            strategy = new Strat_ORP_BTP2D(area.clone(), null);
            System.err.println("Repeater result: " + area + " with " + strategy);            
            ADT_Area result = strategy.compute();
            int area = result.getArea();
            
            if (area < leastArea) {
                leastArea = area;
                bestArea = result;
            }
        }
        return bestArea;
    }
    
}
