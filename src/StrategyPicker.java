public class StrategyPicker {
    
    /**
     * Contains input data
     */
    static ADT_Area area;
    
    /**
     * Constructor
     * @param area contains input data which will be used to pick the strategy.
     */
    public StrategyPicker (ADT_Area area) {
        StrategyPicker.area = area;
    }
    
    static Strat_AbstractStrat pickStrategy() {
        if (area.getVersion() > 10) { // i.e. versions 25 and 10000
            return new Strat_ORP_BinaryTreePacker(area);
        } else { // 3, 5 and 10
            return new Strat_ORP_AnyTime(area);
        }
    }
    
}
