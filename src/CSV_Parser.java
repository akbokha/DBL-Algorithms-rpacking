import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSV_Parser {

    public void parse(String path, DataMining dataSet) {
        if(dataSet.size() != 0) {
            String newFileName = path + ".csv";
            System.out.println(newFileName);
            File newFile = new File(newFileName);
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));

                writer.write("Depth,FR when Called,Computation time,Expected FR");
                writer.newLine();

                for (int i = 0; i < dataSet.size(); i++) {
                    Tuple t = dataSet.get(i);
                    String s = ",";
                    String out = t.depth + s + t.frWhenCalled + s + t.compTime + s + t.expectedFR;
                    writer.write(out);
                    writer.newLine();
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {

            }
        }
    }
}
