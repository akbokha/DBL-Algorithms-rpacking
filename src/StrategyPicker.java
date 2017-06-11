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
        Strat_AbstractStrat strategy = null;

        switch(area.getCount()){
            case 3:
                strategy = BT_generator();
                break;
            case 5:
                strategy = BT_generator();
                break;
            case 10:
                strategy = BT_generator();
                break;
            case 25:
                // Create a new magazine for the shotgun.
                ArrayList<Strat_AbstractStrat> magazine = new ArrayList<>();

                // Load the magazine
                magazine.add(new Strat_ORP_BinaryTreePacker(area));
                magazine.add(new Strat_ORP_BinaryTreePacker(area, new ADT_SortRecOnHeight()));
                magazine.add(new Strat_ORP_BinaryTreePacker(area, new ADT_SortRecOnWidth()));
//                magazine.add(new Strat_ORP_BinaryTreePacker(area, new ADT_SortRecRandom())); // WARNING: Using this might result in non-deterministic behaviour.

                // Forge a new shotgun, load it, and hand it over.
                strategy = new Strat_ORP_Shotgun(area, magazine);
                break;
            case 10000:
                // Create a new magazine for the shotgun.
                ArrayList<Strat_AbstractStrat> magazine10k = new ArrayList<>();
                
                magazine10k.add(new Strat_ORP_BTP2D(area, new ADT_SortRecOnArea()));
                magazine10k.add(new Strat_ORP_BTP2D(area, new ADT_SortRecOnWidth()));
                magazine10k.add(new Strat_ORP_BTP2D(area, new ADT_SortRecOnHeight()));        
//                magazine10k.add(new Strat_ORP_BTP2D(area, new ADT_SortRecRandom())); // Repeat until 4:30-ish

                // Forge a new shotgun, load it, and hand it over.
                strategy = new Strat_ORP_Shotgun(area, magazine10k);
                break;
            default:
                // This should be able to solve any input
                strategy = new Strat_ORP_BTP2D(area);
                break;
        }
        return strategy;
    }
    
    static private Strat_AbstractStrat BT_generator(){
        ADT_Area approximation = new Strat_ORP_BinaryTreePacker(area).compute();
        Strat_BT_PrunerInterface[] pruners = new Strat_BT_PrunerInterface[]{
            new Strat_BT_PrunerPerfectRectangle(), new Strat_BT_Pruner_NarrowEmptyStrips()
        };
        return new Strat_ORP_AnyTime(area, pruners, approximation);         
    }
    
}
