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

        if (area.getCount() == 25) { // i.e. versions 25
            // Create a new magazine for the shotgun.
            ArrayList<Strat_AbstractStrat> magazine = new ArrayList<>();

            // Load the magazine
            magazine.add(new Strat_ORP_BinaryTreePacker(area));
            magazine.add(new Strat_ORP_BinaryTreePacker(area, new ADT_SortRecOnArea()));
            magazine.add(new Strat_ORP_BinaryTreePacker(area, new ADT_SortRecOnHeight()));
            magazine.add(new Strat_ORP_BinaryTreePacker(area, new ADT_SortRecOnWidth()));
//            magazine.add(new Strat_ORP_BinaryTreePacker(area, new ADT_SortRecRandom())); // WARNING: Using this might result in non-deterministic behaviour.

            // Forge a new shotgun, load it, and hand it over.
            strategy = new Strat_ORP_Shotgun(area, magazine);
        } else if (area.getCount() == 10000){ // 10000
            return new Strat_ORP_BTP2D(area);
        } else { // 3, 5 and 10
            ADT_Area approximation = new Strat_ORP_BinaryTreePacker(area).compute();
            Strat_BT_PrunerInterface[] pruners = new Strat_BT_PrunerInterface[]{
                /*new Strat_BT_PrunerEmptySpace(), new Strat_BT_Pruner_WS2(), new Strat_BT_PrunerPerfectRectangle(), new Strat_BT_Pruner_NarrowEmptyStrips()*/
            };
            strategy = new Strat_ORP_AnyTime(area, pruners, approximation);
        }

        return strategy;
    }
    
}
