import java.util.Arrays;

/*
 * Variation of the BinaryTreePacker in which we try different sorting variations
 * And pick the one that gives the best result. 
 * The variations are: Area, Width and Height.
 */
public class Strat_ORP_BinaryTreePacker_ReSort extends Strat_ORP_BinaryTreePacker {
    
    ADT_Rectangle[] sortedRectangles_width;
    ADT_Rectangle[] sortedRectangles_height;
    
    public Strat_ORP_BinaryTreePacker_ReSort (ADT_Area area) {
        super(area);
    }
    
    @Override
    public ADT_Area compute() {
        return area;
    }
    
    
}
