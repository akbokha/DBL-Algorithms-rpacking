/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author s147889
 */
public class Strat_BT_PrunerWS implements Strat_BT_PrunerInterface {
    
    ADT_Area area;
    int[] stripsWS;
    int[] stripsRecsTBP;
    
    public Strat_BT_PrunerWS(ADT_Area area) {
        this.area = area;
    }

    @Override
    public boolean reject(ADT_Area area) {
        return false;
    }
    
    
}
