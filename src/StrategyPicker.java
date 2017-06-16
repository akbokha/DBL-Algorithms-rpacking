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
        int count = area.getCount();

        // Create a new magazine for the shotgun.
        ArrayList<Strat_AbstractStrat> magazine = new ArrayList<>();

        // Ensure we always have some result.
        magazine.add(new Strat_HorizontalStrip(area));

        // @todo add another fast strategy for cases which make the BTP fail.

        // Favor the binary tree packer without location tracking for small amounts of rectangles.
        if (count <= 25) {
//            magazine.add(new Strat_ORP_BinaryTreePacker(area));
//            magazine.add(new Strat_ORP_BinaryTreePacker(area, new ADT_SortRecOnHeight()));
//            magazine.add(new Strat_ORP_BinaryTreePacker(area, new ADT_SortRecOnWidth()));
//            magazine.add(new Strat_ORP_BTP_SmartNodes(area));
//            magazine.add(new Strat_ORP_BTP_SmartNodes(area, new ADT_SortRecOnHeight()));
//            magazine.add(new Strat_ORP_BTP_SmartNodes(area, new ADT_SortRecOnWidth()));
//            magazine.add(new Strat_ORP_BinaryTreePacker(area, new ADT_SortRecRandom())); // WARNING: Using this might result in non-deterministic behaviour.
        } else {
            magazine.add(new Strat_ORP_BTP2D(area, new ADT_SortRecOnArea()));
            magazine.add(new Strat_ORP_BTP2D(area, new ADT_SortRecOnWidth()));
            magazine.add(new Strat_ORP_BTP2D(area, new ADT_SortRecOnHeight()));
//            magazine.add(new Strat_ORP_BTP2D(area, new ADT_SortRecRandom())); // Repeat until 4:30-ish
        }

        // Use the AnyTime for 10 or less rectangles.
        if (count <= 10) {
//            Strat_BT_PrunerInterface[] pruners = new Strat_BT_PrunerInterface[]{
//                    new Strat_BT_PrunerPerfectRectangle(), new Strat_BT_Pruner_NarrowEmptyStrips()
//            };
            magazine.add(new Strat_ORP_AnyTime(area));
        }

        // Forge a new shotgun, load it, and hand it over.
        return new Strat_ORP_Shotgun(area, magazine);
    }
}
