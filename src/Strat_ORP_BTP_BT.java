/**
 * This class uses a combination of the BTP and the BT algorithm to optimize
 * cases of 25 rectangles. It uses the BTP to get an upper bound on the size of
 * the bounding box after which is uses the BT (/ AnyTime) algorithm to improve
 * this solution.
 */
public class Strat_ORP_BTP_BT extends Strat_AbstractStrat {

    public Strat_ORP_BTP_BT(ADT_Area area) {
        super(area);
    }

    @Override
    public ADT_Area compute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
