import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @date 01-05-2017
 * @author Adriaan Knapen <a.d.knapen@student.tue.nl>
 */
public class Input_File extends Input_Scanner {

    private static final String EXTENSION = ".txt";

    public Input_File(String filePath) {
        File file;

        // Check if it has the right extention.
        if (! filePath.endsWith(EXTENSION)) {
            System.out.println("Invalid file specified, should have the '" + EXTENSION + "' type.");
            return;
        }

        // Retrieve the file.
        file = new File(filePath);

        // Check if the file exists and is a directory.
        if (!file.exists()) {
            System.out.println("File does not exist.");
        } else if (file.isDirectory()) {
            System.out.println("Folder selected instead of a file, please give a valid file.");
        } else {
            // Found a valid file.
            try {
                Scanner scanner = new Scanner(file);
                setScanner(scanner);
            } catch (FileNotFoundException e) {
                System.out.println("An exception was raised during loading the file specified: " + e.getMessage());
            }
        }
    }
}
