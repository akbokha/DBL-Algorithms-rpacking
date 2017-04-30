/*
 * DBL Algorithms - Q4 2016-2017
 * Rectangle Packing - Group 10
 */
package rectanglepacking;

import rectanglepacking.input.Input;

/**
 *
 * @author s158881
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Input input = new Input();

        try {
            input.read();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
}
