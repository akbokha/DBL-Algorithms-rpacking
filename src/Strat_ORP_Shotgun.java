import java.util.ArrayList;

public class Strat_ORP_Shotgun extends Strat_AbstractStrat {
    ArrayList<Strat_AbstractStrat> strategies;
    ArrayList<ADT_Area> resulting_areas;
    
    public Strat_ORP_Shotgun (ADT_Area area, ArrayList<Strat_AbstractStrat> strategies) {
        super(area);
        this.strategies = strategies;
        this.resulting_areas = new ArrayList<>();
    }
    
    @Override
    public ADT_Area compute() {
        ADT_Area bestArea = null;
        int minimumArea = Integer.MAX_VALUE;
        for (Strat_AbstractStrat strategy : strategies) {
            resulting_areas.add(strategy.compute());
        }
        for (ADT_Area area_result : resulting_areas) {
            int result = area_result.getDimensions().x * area_result.getDimensions().y;
            if (result < minimumArea) {
                bestArea = area_result;
                minimumArea = result;
            }
        }
        return bestArea;
    }   
}