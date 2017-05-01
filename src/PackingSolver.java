/*
 * DBL Algorithms - Q4 2016-2017
 * Rectangle Packing - Group 10
 */

/**
 *
 * @author s158881
 */
public class PackingSolver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        InputInterface input;
        if (args.length > 0 && args[0].equals("-f")) {
            input = new FileInput();
        } else {
            input = new SystemInput();
        }

        Area area;
        area = input.read();
        
        StrategyPicker.area = area;
        AbstractStrategy strategy = StrategyPicker.pickStrategy();

        strategy.computeArea();

        AbstractOutput output = new PlainText(area);
        output.draw();
    }
    
}
