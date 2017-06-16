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
            try {
                // Add the best area to the Anytime strategy.
                if (strategy.getClass().equals(Strat_ORP_AnyTime.class)) {
                    Strat_ORP_AnyTime original = (Strat_ORP_AnyTime) strategy;
                    strategy = new Strat_ORP_AnyTime(original.getArea(), original.getPruners(), bestArea);
                }

                ADT_Area result = strategy.compute();
                int area = result.getArea();

                System.err.print("Shotgun result: " + area + " with " + strategy);

                if (area < minimumArea) {
                    System.err.print(" (new best)");
                    minimumArea = area;
                    bestArea = result;
                }

                System.err.print("\n");
            } catch (Exception e) {
                System.err.println("Strategy '" + strategy.getClass() + "' threw an exception:" + e.getMessage());
            }
        }
        return bestArea;
    }   
}