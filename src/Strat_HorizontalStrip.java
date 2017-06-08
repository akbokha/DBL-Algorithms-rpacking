public class Strat_HorizontalStrip extends Strat_AbstractStrat {
    
    public Strat_HorizontalStrip(ADT_Area area) {
        super(area);
    }
    
    @Override
    public ADT_Area compute() {
        // Simple dummy implementation that places all rectangles next to each other in one single strip
        int curX = 0;
        ADT_Rectangle[] recs = area.getRectangles();
        for (ADT_Rectangle currentRec : recs) {
            if (currentRec.canFlip() && currentRec.getHeight() > currentRec.getWidth()) {
                currentRec.setFlipped(true);
            }

            currentRec.setX(curX);
            currentRec.setY(0);
            curX += currentRec.getWidth();
        }
        return area;
    }
    
}
