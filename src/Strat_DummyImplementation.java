public class Strat_DummyImplementation extends Strat_AbstractStrat {
    
    public Strat_DummyImplementation(ADT_AreaExtended area) {
        super(area);
    }
    
    @Override
    public ADT_AreaExtended compute() {
        // Simple dummy implementation that places all rectangles next to each other in one single strip
        int curX = 0;
        int widthCurrentRectangle = 0;
        RectangleType[] recs = area.getRectangleTypesToBePlaced();
        for (RectangleType currentRec : recs) {
            while(currentRec.canInstantiate()) {
            // Set the x and y coordinate of the rectangle.
            area.addUnchecked(currentRec.createInstance(curX, 0));
            curX += currentRec.getWidth();

            // If the rectangle is flippable, rotate it such that the longest side is horizontally.
//            if (currentRec.canRotate() && (currentRec.getWidth() < currentRec.getHeight())) {
//                currentRec.
//            }
            }
        }
        return area;
    }
    
}
