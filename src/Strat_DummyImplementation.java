public class Strat_DummyImplementation extends Strat_AbstractStrat {
    
    public Strat_DummyImplementation(ADT_AreaExtended area) {
        super(area);
    }
    
    @Override
    public ADT_AreaExtended compute() {
        // Simple dummy implementation that places all rectangles next to each other in one single strip
        int curX = 0;
        int widthCurrentRectangle = 0;
        ADT_Rectangle[] recs = areaEx.getRectangles();
        for (ADT_Rectangle currentRec : recs) {
            
        }
        return areaEx;
    }
    
}
