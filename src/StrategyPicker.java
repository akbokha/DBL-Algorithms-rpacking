/**
 * StrategyPicker that picks a strategy based on data in an Area object
 */


/**
 * @author Abdel
 * 01-05-2017
 */
public class StrategyPicker {
    
    /**
     * Contains input data
     */
    public static Area area;
    
    /**
     * Constructor
     * @param area contains input data such as 
     * store data that will be used to actually pick the strategy
     */
    public StrategyPicker (Area area) {
        StrategyPicker.area = area;
    }
    
    public static AbstractStrategy pickStrategy() {
        return new DummyImplementation(area);
    }
    
}
