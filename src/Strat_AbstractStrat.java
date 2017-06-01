/*
 * Abstract Strategy
 * Will be extended by Concrete strategy implementations
 */


/**
 * @author Abdel
 * 01-05-2017 
 */
public abstract class Strat_AbstractStrat {
    
    /**
     * Contains input data
     */
    protected ADT_AreaExtended area;
    
    /**
     * Constructor
     * @param area contains input data such as 
     * Concrete strategies should call super() in constructor
     */
    public Strat_AbstractStrat(ADT_AreaExtended area) {
        this.area = area;
    }
    
    /**
     * The actual computation behind the placement of the rectangles
     * @return an area such that isValid(), or null if one wasn't found.
     */
    public abstract ADT_AreaExtended compute();
    
}
