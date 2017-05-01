/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy;
import rectanglepacking.Area;

/**
 * @author Abdel 
 * 01-05-2017
 */
public class dummyImplementation extends AbstractStrategy {
    
    public dummyImplementation (Area area) {
        super(area);
    }
    
    @Override
    public Area computeArea() {
        // To do: dummy implementation
        return new Area(0, 0, true);
    }
    
}
