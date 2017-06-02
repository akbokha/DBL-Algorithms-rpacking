import java.util.ArrayList;
import java.util.List;

/**
 * Prunes by strip dominance. (i.e. prunes rectangle placements if they leave
 * a strip on the left or bottom of the newly placed rectangle, provided that
 * all rectangles that might fit there, fit there together.)
 *
 * @author Jorrit
 * @date June 1st 2017
 */
public class Strat_BT_Pruner_NarrowEmptyStrips implements Strat_BT_PrunerInterface {
    private ADT_AreaExtended area;
    private int stripHeight = -1;
    private int stripWidth = -1;
    // At which percentage should placement not be tried
    private final double STRICTNESS = 0.95;
    // Solve by estimation since we expect to have enough space anyway
    private final double ESTIMATE = 0.7;

    @Override
    public boolean reject(ADT_AreaExtended area, ADT_Rectangle last) {
        this.area = area;
        
        // To ensure immediate return if bottom returns true
        if(bottomStrip(last)){
            return true;
        }else{
            return leftStrip(last);
        }
    }

    /**
     * Returns true if there is dominance below
     */
    private boolean bottomStrip(ADT_Rectangle last){
        if(hasSolidBottom(last)){ // check strip below
            // Determine set of all rectangles that can be placed
            List<ADT_Rectangle> fittingRecs = new ArrayList<>();
            for(ADT_Rectangle type : area.getRectanglesToBePlaced()){
                if(area.canFlip()){
                    if (type.getWidth() <= stripHeight) {
                        fittingRecs.add(type);
                    }
                }else{
                    if (type.getHeight() <= stripHeight) {
                        fittingRecs.add(type);
                    }
                }
            }
            // Place rectangles in area
            ADT_AreaExtended stripArea = new ADT_AreaExtended(last.getWidth(), stripHeight, 
                    area.canFlip(), fittingRecs.toArray());
            
            // First check if they might fit by comparing size
            int fillRate = stripArea.getTotalAreaRectanglesToBePlaced()/(stripArea.getWidth()*stripArea.getHeight());
            if(fillRate < STRICTNESS){
                // Call algorithm
                if(stripArea.getCount() > 10 || fillRate < ESTIMATE){ // estimate containment
                    ADT_Area result = (new Strat_ORP_BinaryTreePacker((ADT_Area) area)).compute();
                    if(result.getWidth() <= last.getWidth()) return true;
                    return false;
                }else{ // containment problem
                    if((new Strat_CP_BT(stripArea)).compute() != null){
                        // there is a solution
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Returns true if there is dominance on the left
     */
    private boolean leftStrip(ADT_Rectangle last){
        if(hasSolidLeft(last)){ // check strip below
            // Determine set of all rectangles that can be placed
            List<ADT_Rectangle> fittingRecs = new ArrayList<>();
            for(ADT_Rectangle type : area.getRectanglesToBePlaced()){
                if(area.canFlip()){
                    if (type.getHeight()<= stripWidth) {
                        fittingRecs.add(type);
                    }
                }else{
                    if (type.getWidth()<= stripWidth) {
                        fittingRecs.add(type);
                    }
                }
            }
            // Place rectangles in area
            ADT_AreaExtended stripArea = new ADT_AreaExtended(stripWidth, last.getHeight(), 
                    area.canFlip(), fittingRecs.toArray());
            
            // First check if they might fit by comparing size
            int fillRate = stripArea.getTotalAreaRectanglesToBePlaced()/(stripArea.getWidth()*stripArea.getHeight());
            if(fillRate < STRICTNESS){
                // Call algorithm
                if(stripArea.getCount() > 10 || fillRate < ESTIMATE){ // estimate containment
                    ADT_Area result = (new Strat_ORP_BinaryTreePacker((ADT_Area) area)).compute();
                    if(result.getWidth() <= last.getWidth()) return true;
                }else{ // containment problem
                    if((new Strat_CP_BT(stripArea)).compute() != null){
                        // there is a solution
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private boolean hasSolidBottom(ADT_Rectangle last){
        int stripY = last.getY()-1;
        while(area.isEmptyAt(last.getX(), stripY)) stripY--; // Determine height of strip
        if(stripY+1 == last.getY()) return false; // There is no space
        
        // Check that whole strip is empty
        for(int i=last.getX()+1; i<last.getX()+last.getWidth(); ++i){
            int j;
            for(j = last.getY()-1; j>stripY; --j){
                if(!area.isEmptyAt(i, stripY)) return false;
                // There are points within the strip
            }
            if(area.isEmptyAt(i, stripY)) return false;
            // The strip bottom is not solid
        }
        stripHeight = last.getY() - stripY - 1;
        return true;
    }
    
    private boolean hasSolidLeft(ADT_Rectangle last){
        int stripX = last.getX()-1;
        while(area.isEmptyAt(stripX, last.getY())) stripX--; // Determine width of strip
        if(stripX+1 == last.getX()) return false; // There is no space
        
        for(int i=last.getY()+1; i<last.getY()+last.getHeight(); ++i){
            int j;
            for(j = last.getX()-1; j>stripX; --j){
                if(!area.isEmptyAt(j, i)) return false;
                // There are points within the strip
            }
            if(area.isEmptyAt(stripX, i)) return false;
            // The strip left side is not solid
        }
        stripWidth = last.getX() - stripX - 1;
        return true;
    }
}