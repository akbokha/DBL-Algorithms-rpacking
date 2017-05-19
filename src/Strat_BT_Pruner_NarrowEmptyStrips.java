/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Prunes by dominance, i.e. prunes rectangle placements if they leave
 * narrow empty horizontal strips that are not bounded on both sides,
 * or narrow empty vertical strips that are not bounded above and below.
 *  
 * @author Phung
 */
public class Strat_BT_Pruner_NarrowEmptyStrips implements Strat_BT_PrunerInterface {
    
    /**
     * Computes if the given problem can be evolved into a valid solution.
     *
     * @param area the problem to be evaluated.
     * @param last the rectangle added last.
     * @return true if this problem cannot evolve further into a valid solution.
     */
    @Override
    public boolean reject(ADT_Area area, ADT_Rectangle last) {
    boolean[] rectangleHeightDisallowed; // array specifying for each rectangle width, the heights of empty space that are disallowed below the rectangle
    boolean[] rectangleWidthDisallowed; // array specifies for each rectangle height, the widths of empty space that are disallowed to the left of the rectangle
    int[] rectanglesToBePlaced; // array which stores the rectangles to be placed
    boolean rotations; // rotations are allowed
    rotations = area.canFlip();

    // horizontal rectangle strips
    rectanglesToBePlaced = area.getRectangleStrips(true);

    // create one binary matrix if rotations are allowed
    if (rotations) {
        for (int i = 0; i < area.getCount(); i++) {
            // if rectangle w
            // boolean[i] rectangleWidthDisallowed = true;
        }
    } else {
        // create two binary matrices
    }
    // if all future placements fit below or to the left of the candidate rectangle
    // then reject (return true)
    return (narrowStripsBelow((ADT_AreaExtended)area, last) || narrowStripsLeft((ADT_AreaExtended)area, last));
    }
    
    /**
     * Checks if all rectangles less than or equal to h to be placed can be
     * placed in the empty strip below the rectangle. h is the minimum dimension
     * if rotations are allowed.
     * If all placements are possible, then do not allow the original rectangle
     * to be placed in this candidate position.
     * 
     * @param last rectangle to look under for perfect rectangle of empty space
     * @param area the area in which last has been placed
     * @return true if there is such a rectangle
     */
    private boolean narrowStripsBelow(ADT_AreaExtended area, ADT_Rectangle last){
        for (int i = 0; i < area.getCount(); i++) {
        }
        return true;
    }
    
    /**
     * Checks if all rectangles less than or equal to w to be placed can be
     * placed in the empty strip to the left of the rectangle.
     * w is the minimum dimension if rotations are allowed.
     * If all placements are possible, then do not allow the original rectangle
     * to be placed in this candidate position.
     * 
     * @param last rectangle to look under for perfect rectangle of empty space
     * @param area the area in which last has been placed
     * @return true if there is such a rectangle
     */
    private boolean narrowStripsLeft(ADT_AreaExtended area, ADT_Rectangle last){
        for (int i = 0; i < area.getCount(); i++) {
        }
        return true;
    }
}