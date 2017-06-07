import java.util.Arrays;

/*
 * Variation of the BinaryTreePacker in which we try different sorting variations
 * And pick the one that gives the best result. 
 * The variations are: Area, Width and Height.
 */
public class Strat_ORP_BinaryTreePacker_ReSort extends Strat_ORP_BinaryTreePacker {
    
    ADT_Rectangle[] sortedRectangles_width;
    ADT_Rectangle[] sortedRectangles_height;
    
    int areaResult_area;
    int areaResult_width;
    int areaResult_height;
    
    ADT_Area result_area;
    ADT_Area result_width;
    ADT_Area result_height;
    ADT_Area area_clone;
    ADT_Area returnArea;
    
    int version; // 0 = area, 1 = width, 2 = heigth
    boolean first; // true = first call
    
    public Strat_ORP_BinaryTreePacker_ReSort (ADT_Area area, boolean first, int version) {
        super(area);
        area_clone = area.clone();
        this.first = first;
        // to-do: check if resorting sortedRectangles is faster in general
        switch (version) {
            case 0:
                // area
                this.version = version;
                break;
            case 1:
                // width
                this.version = version;
                sortedRectangles_width = area.getRectangles().clone();
                Arrays.sort(sortedRectangles_width, new ADT_SortRecOnWidth()); // sort on height
                sortedRectangles = sortedRectangles_width;
                break;
            case 2:
                // height
                this.version = version;
                sortedRectangles_height = area.getRectangles().clone();
                Arrays.sort(sortedRectangles_height, new ADT_SortRecOnHeight()); // sort on height
                sortedRectangles = sortedRectangles_height;
                break;
            default:
                break;
        }
    }
    
    @Override
    public ADT_Area compute() {
        result_area = super.compute();
        areaResult_area = heightResult * area.getWidth();
        returnArea = result_area;
        
        
        if (first) {
            // re-run with the rectangles sorted on width
            Strat_ORP_BinaryTreePacker_ReSort widthStrat = new Strat_ORP_BinaryTreePacker_ReSort(area_clone, false, 1);
            result_width = widthStrat.compute();  
            areaResult_width = widthStrat.heightResult * widthStrat.area.getWidth();
            // re-run with the rectangles sorted on height
            Strat_ORP_BinaryTreePacker_ReSort heightStrat = new Strat_ORP_BinaryTreePacker_ReSort(area_clone, false, 2);
            result_height = heightStrat.compute();
            areaResult_width = heightStrat.heightResult * heightStrat.area.getWidth();
        
            if (areaResult_area <= areaResult_width && areaResult_area <= areaResult_width) {
                returnArea = result_area;
            } else {
                if (areaResult_width <= areaResult_height) {
                    returnArea = result_width;
                } else {
                    returnArea = result_height;
                }
           }
        }
        return returnArea;
    }
}
