/*
 * DBL Algorithms - Q4 2016-2017
 * Rectangle Packing - Group 10
 */

/**
 * @author s158881
 */
public class PackingSolver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        Input_InputInterface input = null;
        boolean graphical = false;

        // Handle all program arguments.
        int index = 0;
        while (args.length > index) {
            switch (args[index]) {
                case "-f":
                    if (args.length >= index + 2) {
                        String filePath = args[++index];
                        input = new Input_File(filePath);
                    } else {
                        input = new Input_FileFromSystemIn();
                    }
                    break;
                case "-g":
                    graphical = true;
                    break;
                default:
                    System.err.println("Invalid parameter '" + args[index] + "' given.");
                    return;
            }

            ++index;
        }

        if (input==null) {
            input = new Input_SystemIn();
        }

        ADT_Area area;
        area = input.read();
        
        StrategyPicker.area = area;
        Strat_AbstractStrat strategy = StrategyPicker.pickStrategy();

        strategy.compute();

        // Do the plaintext output.
        Output_AbstractOutput output = new Output_Plaintext(area);
        output.draw();

        // Also add the graphical output if it was specified in the program arguments.
        if (graphical) {
            Output_AbstractOutput graphicalOutput = new Output_GraphicalOutput(area);
            graphicalOutput.draw();
        }
    }
    
}
