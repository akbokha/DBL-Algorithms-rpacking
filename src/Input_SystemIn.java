

import java.util.Scanner;

/**
 * @date 01-05-2017
 * @author Adriaan Knapen <a.d.knapen@student.tue.nl>
 */
public class Input_SystemIn extends Input_Scanner {
    public Input_SystemIn() {
        Scanner scanner = new Scanner(System.in);

        setScanner(scanner);
    }
}
