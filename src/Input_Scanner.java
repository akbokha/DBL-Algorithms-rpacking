import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @date Apr 26, 2017
 * @author Adriaan Knapen <a.d.knapen@student.tue.nl>
 */
public abstract class Input_Scanner implements Input_InputInterface {

    private Scanner scanner = null;

    protected void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public ADT_Area read() throws IllegalArgumentException, IllegalStateException {
        if (this.scanner==null) {
            throw new IllegalStateException("Input_Scanner.read: scanner should be not null");
        }

        // Read the container height.
        scanner.skip("container height: ");
        String res = scanner.next();

        int height;
        switch (res) {
            case "free":
                height = ADT_Rectangle.INF;
                break;
            case "fixed":
                height = scanner.nextInt();
                break;
            default:
                throw new IllegalArgumentException("invalid container height specification.");
        }

        // Read flippable allowed.
        scanner.skip(Pattern.compile("[\\s\\S]*?rotations allowed: "));
        res = scanner.next();

        boolean flippable;
        switch (res) {
            case "no":
                flippable = false;
                break;
            case "yes":
                flippable = true;
                break;
            default:
                throw new IllegalArgumentException("invalid flippable specification.");
        }

        // Read the number of rectangles
        scanner.skip(Pattern.compile("[\\s\\S]*?number of rectangles: "));
        int amount;
        if (scanner.hasNextInt()) {
            amount = scanner.nextInt(10);
        } else {
            throw new IllegalArgumentException("invalid amount of rectangles specified");
        }

        // Read all rectangles and add them to the area.
        ADT_Area area = new ADT_Area(ADT_Rectangle.INF, height, flippable);

        for (int i = 0; i < amount; i++) {
            if (! scanner.hasNextInt()) {
                throw new IllegalArgumentException("not enough rectangles specified");
            }

            int x = scanner.nextInt();

            if (! scanner.hasNextInt()) {
                throw new IllegalArgumentException("not enough rectangles specified");
            }

            int y = scanner.nextInt();

            // Add the new rectangle.
            area.add(new ADT_Rectangle(x, y, ADT_Rectangle.NOTSET, ADT_Rectangle.NOTSET, flippable));
        }

        return area;
    }
}
