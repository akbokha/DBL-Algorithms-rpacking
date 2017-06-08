import java.util.ArrayList;

public class Strat_ORP_Shotgun extends Strat_AbstractStrat {
    ArrayList<Strat_AbstractStrat> strategies;

    public Strat_ORP_Shotgun (ADT_Area area, ArrayList<Strat_AbstractStrat> strategies) {
        super(area);
        this.strategies = strategies;
    }
    
    @Override
    public ADT_Area compute() {
        ADT_Area bestArea = null;
        int minimumArea = Integer.MAX_VALUE;
        for (Strat_AbstractStrat strategy : strategies) {
            ADT_Area result = strategy.compute();
            int area = result.getArea();

            System.err.print("Shotgun result: " + area + " with " + strategy);

            if (area < minimumArea) {
                System.err.print(" (new best)");
                minimumArea = area;
                bestArea = result;
            }

            System.err.print("\n");
        }
        return bestArea;
    }   
}