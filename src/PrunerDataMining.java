
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
public class PrunerDataMining implements Strat_BT_PrunerInterface{
    
    Strat_BT_PrunerInterface pruner;
    ArrayList<Tuple> dataSet;

    public PrunerDataMining(Strat_BT_PrunerInterface pruner, ArrayList<Tuple> dataSet) {
        this.pruner = pruner;
        this.dataSet = dataSet;
    }
    
    @Override
    public boolean reject(ADT_AreaExtended area, ADT_Rectangle last) {
        int depth = area.getCount();
        float frWhenCalled = area.getFillRate();
        long time = System.currentTimeMillis();
        float expectedFR = area.getTotalAreaRectangles()/(area.getHeight()*area.getWidth());
        
        boolean prune = pruner.reject(area, last);
        time -= System.currentTimeMillis();
        
        if(prune) {
            System.err.println("pruned");
            Tuple tuple = new Tuple(depth, frWhenCalled, (int) time, expectedFR);
            dataSet.add(tuple);
        }
        return prune;
    }
    
}
