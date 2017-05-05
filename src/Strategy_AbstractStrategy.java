/*
 * Abstract Strategy
 * Will be extended by Concrete strategy implementations
 */


/**
 *
 * @author Abdel
 * 01-05-2017 
 */
public abstract class Strategy_AbstractStrategy {
    
    /**
     * Contains input data
     */
    protected ADT_Area area;
    
    /**
     * Constructor
     * @param area contains input data such as 
     * Concrete strategies should call super() in constructor
     */
    public Strategy_AbstractStrategy(ADT_Area area) {
        this.area = area;
    }
    
    /**
     * The actual computation behind the placement of the rectangles
     * @return an area such that isValid()
     */
    public abstract ADT_Area computeArea();
    
}
