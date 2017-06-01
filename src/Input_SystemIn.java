import java.util.Scanner;

public class Input_SystemIn extends Input_Scanner {
    public Input_SystemIn() {
        Scanner scanner = new Scanner(System.in);

        setScanner(scanner);
    }
}
