package rectanglepacking.input;

import rectanglepacking.Area;
import rectanglepacking.Rectangle;

import java.io.File;
import java.util.Scanner;

/**
 * @date Apr 26, 2017
 * @author Adriaan Knapen <a.d.knapen@student.tue.nl>
 */
public class Input {

    private static final String EXTENSION = ".txt";

    private int height;
    private boolean flippable;
    private int amount;
    private Area area;

    public Area read() throws Exception {
        Scanner input = new Scanner(System.in);
        File file;

        System.out.println("Current directory = " + System.getProperty("user.dir"));

        while (true) {
            String filePath = input.nextLine();

            // Check if it has the right extention.
            if (! filePath.endsWith(EXTENSION)) {
                System.out.println("Invalid file specified, should have the '" + EXTENSION + "' type.");
                continue;
            }

            // Retrieve the file.
            file = new File(filePath);

            // Check if the file exists and is a directory.
            if (!file.exists()) {
                System.out.println("File does not exist, please try again.");
            } else if (file.isDirectory()) {
                System.out.println("Folder selected instead of a file, please give a valid file.");
            } else {
                // If so break since we have found the file we where looking for.
                break;
            }
        }

        Scanner fileScanner = new Scanner(file);

        // Read the container height.
        fileScanner.skip("container height: ");
        String res = fileScanner.next();

        if (res.equals("free")) {
            height = 0;
        } else if (res.equals("fixed")) {
            height = fileScanner.nextInt();
        } else {
            throw new Exception("invalid container height specification.");
        }

        // Read flippable allowed.
        fileScanner.skip("rotations allowed: ");
        res = fileScanner.next();

        if (res.equals("no")) {
            flippable = false;
        } else if (res.equals("yes")) {
            flippable = true;
        } else {
            throw new Exception("invalid flippable specification.");
        }

        // Read the number of rectangles
        fileScanner.skip("number of rectangles: ");
        if (fileScanner.hasNextInt()) {
            amount = fileScanner.nextInt(10);
        } else {
            throw new Exception("invalid amount of rectangles specified");
        }

        // Read all rectangles and add them to the area.
        area = new Area(0, this.height, flippable);

        for (int i = 0; i < amount; i++) {
            if (! fileScanner.hasNextInt()) {
                throw new Exception("not enough rectangles specified");
            }

            int x = fileScanner.nextInt();

            if (! fileScanner.hasNextInt()) {
                throw new Exception("not enough rectangles specified");
            }

            int y = fileScanner.nextInt();

            // Add the new rectangle.
            area.add(new Rectangle(x, y, -1, -1, flippable));
        }

        return area;
    }
}
