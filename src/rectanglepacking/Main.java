/*
 * DBL Algorithms - Q4 2016-2017
 * Rectangle Packing - Group 10
 */
package rectanglepacking;

import rectanglepacking.input.FileInput;
import rectanglepacking.input.InputInterface;
import rectanglepacking.input.SystemInput;
import rectanglepacking.output.AbstractOutput;
import rectanglepacking.output.PlainText;

/**
 *
 * @author s158881
 */
public class Main {

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

        AbstractOutput output = new PlainText(area);
        output.draw();
    }
    
}
