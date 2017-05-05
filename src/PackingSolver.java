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
        Input_InputInterface input;
        if (args.length <= 0) {
            input = new Input_SystemIn();
        } else {
            switch (args[0]) {
                case "-f":
                    if (args.length >= 2) {
                        input = new Input_File(args[1]);
                    } else {
                        input = new Input_FileFromSystemIn();
                    }
                    break;
                default:
                    System.err.println("Invalid parameter '" + args[0] + "' given.");
                    return;
            }
        }

        ADT_Area area;
        area = input.read();
        
        StrategyPicker.area = area;
        Strategy_AbstractStrategy strategy = StrategyPicker.pickStrategy();

        strategy.computeArea();

        Output_AbstractOutput output = new Output_Plaintext(area);
        output.draw();
    }
    
}
