/**
 * StrategyPicker that picks a strategy based on data in an ADT_Area object
 */


/**
 * @author Abdel
 * 01-05-2017
 */
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
        return new Strat_FFDH(area);
    }
    
}
