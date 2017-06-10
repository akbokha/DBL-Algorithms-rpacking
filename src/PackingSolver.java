/*
 * DBL Algorithms - Q4 2016-2017
 * Rectangle Packing - Group 10
 */

public class PackingSolver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        long curTime = System.currentTimeMillis(); // to check running time

        Input_Scanner input = null;
        boolean graphical = false;

        // Handle all program arguments.
        int index = 0;
        while (args.length > index) {
            switch (args[index]) {
                case "-f":
                    if (args.length >= index + 2) {
                        String filePath = args[++index];
                        input = new Input_File(filePath);

                        System.err.println("Input file used: '" + filePath + "'");
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

        ADT_Area result = strategy.compute();

        if (result == null) {
            System.err.println("strategy computation failed");
            return;
        }

        // Do the plaintext output.
        Output_AbstractOutput output = new Output_Plaintext(result);
        output.draw();

        // Also add the graphical output if it was specified in the program arguments.
        if (graphical) {
            Output_AbstractOutput graphicalOutput = new Output_GraphicalOutput(result);
            graphicalOutput.draw();
        }
        
        long endTime = System.currentTimeMillis(); // end time
        System.err.println((endTime - curTime) + "ms");
    }
    
}
