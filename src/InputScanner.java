


import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @date Apr 26, 2017
 * @author Adriaan Knapen <a.d.knapen@student.tue.nl>
 */
public abstract class InputScanner implements InputInterface {

    private Scanner scanner = null;

    protected void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public Area read() throws IllegalArgumentException, IllegalStateException {
        if (this.scanner==null) {
            throw new IllegalStateException("InputScanner.read: scanner should be not null");
        }

        // Read the container height.
        scanner.skip("container height: ");
        String res = scanner.next();

        int height;
        if (res.equals("free")) {
            height = 0;
        } else if (res.equals("fixed")) {
            height = scanner.nextInt();
        } else {
            throw new IllegalArgumentException("invalid container height specification.");
        }

        // Read flippable allowed.
        scanner.skip(Pattern.compile("[\\s\\S]*?rotations allowed: "));
        res = scanner.next();

        boolean flippable;
        if (res.equals("no")) {
            flippable = false;
        } else if (res.equals("yes")) {
            flippable = true;
        } else {
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
        Area area = new Area(0, height, flippable);

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
            area.add(new Rectangle(x, y, -1, -1, flippable));
        }

        return area;
    }
}
