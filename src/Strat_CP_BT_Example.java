
/**
 *
 * @author Bastiaan
 */
public class Strat_CP_BT_Example extends Strat_BT_Template{
    public Strat_CP_BT_Example(ADT_Area area) {
        super(area);
    }

    @Override
    ADT_Rectangle last() {
        return null;
    }

    @Override
    boolean accept(ADT_Rectangle last) {
        return false;
    }

    @Override
    boolean first() {
        return false;
    }

    @Override
    boolean next() {
        return false;
    }
}
