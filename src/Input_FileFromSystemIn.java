import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Input_FileFromSystemIn extends Input_Scanner {

    private static final String EXTENSION = ".txt";

    public Input_FileFromSystemIn() {
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
                try {
                    Scanner scanner = new Scanner(file);
                    setScanner(scanner);
                } catch (FileNotFoundException e) {
                    System.out.println("File does not exist, please try again.");
                    continue;
                }
                break;
            }
        }
    }
}
