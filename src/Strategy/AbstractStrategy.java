/*
 * Abstract Strategy
 * Will be extended by Concrete strategy implementations
 */
package Strategy;
import rectanglepacking.Area;

/**
 *
 * @author Abdel
 * 01-05-2017 
 */
public abstract class AbstractStrategy {
    
    /**
     * Contains input data
     */
    protected Area area;
    
    /**
     * Constructor
     * @param area contains input data such as 
     * Concrete strategies should call super() in constructor
     */
    public AbstractStrategy(Area area) {
        this.area = area;
    }
    
    /**
     * The actual computation behind the placement of the rectangles
     * @return an area such that isValid()
     */
    public abstract Area computeArea();
    
}
