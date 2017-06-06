import java.util.BitSet;

/**
 * This class modifies the binaryTreePacker such that it is able to do faster
 * calculations, mainly focusing on calculating valid placement and paste.
 * The BitSet class is used to depict the points in the Cartesian Coordinate
 * system. When running out of space, a new BitSet object is created.
 */
public class Strat_ORP_BTP2D extends Strat_ORP_BinaryTreePacker {
    
    public Strat_ORP_BTP2D(ADT_Area area) {
        super(area);
    }

    @Override
    public ADT_Area compute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
