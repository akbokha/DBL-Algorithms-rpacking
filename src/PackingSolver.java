/*
 * DBL Algorithms - Q4 2016-2017
 * Rectangle Packing - Group 10
 */

public class PackingSolver {
    private static long curTime;
    private static ADT_Area result;
    final static private int TIME_LIMIT = 60 * 5 - 15; // Stop 15 seconds early, just to be on the safe side.
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        curTime = System.currentTimeMillis(); // to check running time
        
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

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                result = strategy.compute();
            }
        });
        thread.start();

        long msToWait = TIME_LIMIT * 1000 - (System.currentTimeMillis() - PackingSolver.getStartTime());

        boolean finished = false;
        try {
            thread.join(msToWait);

            finished = thread.isInterrupted();
            if(! finished) {
                thread.stop();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (! finished) {
            result = strategy.getIntermediateResult();
        }

        if (result == null) {
            System.err.println("\nstrategy computation failed");
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
    
    /**
     * Returns time at which process started.
     */
    public static long getStartTime(){
        return curTime;
    }
    
}
