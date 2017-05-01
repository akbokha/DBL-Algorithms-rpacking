

import java.util.Scanner;

/**
 * @date 01-05-2017
 * @author Adriaan Knapen <a.d.knapen@student.tue.nl>
 */
public class SystemInput extends InputScanner {
    public SystemInput() {
        Scanner scanner = new Scanner(System.in);

        setScanner(scanner);
    }
}
