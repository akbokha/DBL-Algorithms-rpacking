public abstract class Strat_AbstractStrat {
    
    /**
     * Contains input data
     */
    protected ADT_Area area;
    
    protected ADT_AreaExtended areaEx;
    
    /**
     * Constructor
     * @param area contains input data such as 
     * Concrete strategies should call super() in constructor
     */
    public Strat_AbstractStrat(ADT_Area area) {
        this.area = area.clone();
    }
    
    public Strat_AbstractStrat(ADT_AreaExtended area) {
        this.areaEx = area.clone();
    }
    
    /**
     * The actual computation behind the placement of the rectangles.
     * Method can only be called once per object.
     * @return an area such that isValid(), or null if one wasn't found.
     */
    public abstract ADT_Area compute();
    
}
