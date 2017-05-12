
/**
 *
 * @author Bastiaan
 */
public class Strat_BT_Example extends Strat_BT_Template{
    public Strat_BT_Example(ADT_Area area) {
        super(area);
    }
    
    @Override
    boolean accept(ADT_Area area, ADT_Rectangle last) {
        return true;
    }

    @Override
    ADT_Area first(ADT_Area area) {
        return area;
    }

    @Override
    ADT_Area next(ADT_Area area, ADT_Area s) {
        return null;
    }
    
}
