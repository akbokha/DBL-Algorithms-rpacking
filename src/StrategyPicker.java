import java.util.ArrayList;

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
        Strat_AbstractStrat strategy;

        if (area.getCount() >= 25) { // i.e. versions 25 and 10000
            // Create a new magazine for the shotgun.
            ArrayList<Strat_AbstractStrat> magazine = new ArrayList<>();

            // Load the magazine
            magazine.add(new Strat_ORP_BinaryTreePacker(area));
            magazine.add(new Strat_ORP_BinaryTreePacker(area));

            // Forge a new shotgun, load it, and hand it over.
            strategy = new Strat_ORP_Shotgun(area, magazine);
        } else { // 3, 5 and 10
            ADT_Area approximation = new Strat_ORP_BinaryTreePacker(area).compute();
            strategy = new Strat_ORP_AnyTime(area, null, approximation);
        }

        return strategy;
    }
    
}
