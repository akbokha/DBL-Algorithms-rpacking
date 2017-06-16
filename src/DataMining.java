
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author s147889
 */
public class DataMining {

    ArrayList<Tuple> dataSet = new ArrayList<>();
    Strat_BT_PrunerInterface[] pruners;
    
    public DataMining() {
        this.dataSet = new ArrayList<>();
    }

    public Tuple get(int i) {
        return dataSet.get(i);
    }

    public int size() {
        return dataSet.size();
    }
}
