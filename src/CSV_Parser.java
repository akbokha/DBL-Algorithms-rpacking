import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CSV_Parser {
    
    DataMining data;
    BufferedWriter writer;
    
    CSV_Parser(String string, DataMining data) {
        this.data = data;
        try {
            String newFileName = string + ".csv";
            System.out.println(newFileName);
            File newFile = new File(newFileName);
            writer = new BufferedWriter(new FileWriter(newFile));

            writer.write("Depth,FR when Called,Computation time,Expected FR,Pruner time, Pruned");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {

        }
    }

    public void parse(DataMining dataSet) {
        if(dataSet.size() != 0) {
            try {
                for (int i = 0; i < dataSet.size(); i++) {
                    Tuple t = dataSet.get(i);
                    String s = ",";
                    String out = t.depth + s + t.frWhenCalled + s + Long.toString(t.compTime) + s + t.expectedFR + s + t.endTimePruner + s + t.pruned;
                    writer.write(out);
                    writer.newLine();
                }
            }catch(IOException e) {
                
            }
        }
    }

    void parse() {
        parse(data);
        data.dataSet = new ArrayList<>();
    }
    
    void writerClose() {
        try {
            writer.close();
        } catch (IOException e) {
            
        }
    }
}
