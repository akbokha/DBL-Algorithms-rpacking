
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

    ArrayList<Tuple> dataSet;
    Strat_BT_PrunerInterface[] pruners;
    
    public DataMining(Strat_BT_PrunerInterface[] pruners) {
        this.dataSet = new ArrayList<>();
        this.pruners = pruners;
    }
    
    public void callPruner(Strat_BT_PrunerInterface pruner) {
        
    }
}
