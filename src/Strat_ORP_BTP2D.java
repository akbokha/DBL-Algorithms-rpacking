import java.util.BitSet;
import java.util.Comparator;
/**
 * This class modifies the binaryTreePacker such that it is able to do faster
 * calculations, mainly focusing on calculating valid placement and paste.
 * The BitSet class is used to depict the points in the Cartesian Coordinate
 * system. When running out of space, a new BitSet object is created.
 */
public class Strat_ORP_BTP2D extends Strat_ORP_BinaryTreePacker {
    // Settings for BitSet
    static final int INITIALWIDTH = 10000;
    static final int INITIALHEIGHT = 10000;
    static final double UPDATEFACTOR = 1.2;
    
    int bitSetHeight;
    int bitSetWidth;
    BitSet array2d;
    
    public Strat_ORP_BTP2D(ADT_Area area) {
        super(area);
    }
    
    public Strat_ORP_BTP2D(ADT_Area area,  Comparator<ADT_Rectangle> version) {
        super(area, version);
    }

    @Override
    public ADT_Area compute() {
        createBitSet();
        return super.compute();
    }
    
    /**
     * Creating BitSet 2D, assuming 10000 rectangles
     */
    private void createBitSet(){
        if(fixedHeight){
            bitSetHeight = fixedHeightValue;
            bitSetWidth = INITIALWIDTH;
        }else{
            bitSetHeight = INITIALHEIGHT;
            bitSetWidth = INITIALWIDTH;
        }
        array2d = new BitSet(bitSetHeight*bitSetWidth);
    }
    
    /**
     * Copies old BitSet and enlarges current
     */
    private void updateBitSet(){
        int oldHeight = bitSetHeight;
        int oldWidth = bitSetWidth;
        
        /*
        TODO
        Create better method to determine new size of BitSet
        */
        
        // create new bitset
        if(fixedHeight){
            double enlarge = UPDATEFACTOR*10000/recIndex;
            // BitSetHeight has already been set
            bitSetWidth *= enlarge;
        }else{
            double enlarge = UPDATEFACTOR*10000/recIndex;
            bitSetHeight *= enlarge;
            bitSetWidth *= enlarge;
        }
        BitSet newSet = new BitSet(bitSetHeight*bitSetWidth);
        
        // copy all data
        for(int x=0; x<oldWidth; ++x){
            for(int y=0; y<oldHeight; ++y){
                newSet.set(y*bitSetWidth+x, array2d.get(y*oldWidth+x));
            }
        }
    }
    
    private boolean getBitSet(int x, int y){
        assert x>=0 && y>=0;
        assert x<bitSetWidth && y<bitSetHeight;
        return array2d.get(y*bitSetWidth+x);
    }
    
    private void setBitSet(int x, int y){
        assert x>=0 && y>=0;
        if(x>=bitSetWidth || y>=bitSetHeight) throw new IllegalArgumentException("SET - Too large x,y");
        array2d.set(y*bitSetWidth+x);
    }
    
    private void setBitRec(ADT_Rectangle rec){
        // set bottom and top
        for(int x = rec.getX(); x<rec.getX()+rec.getWidth(); ++x){
            setBitSet(x, rec.getY());
            setBitSet(x, rec.getY()+rec.getHeight()-1);
        }
        
        // set left and right
        for(int y = rec.getY()+1; y<rec.getY()+rec.getHeight()-1; ++y){
            setBitSet(rec.getX(), y);
            setBitSet(rec.getX()+rec.getWidth()-1, y);
        }
    }
    
    @Override
    public void placeRectangle(ADT_Rectangle rec){
        super.placeRectangle(rec);
        try { // TODO do not use 
            setBitRec(rec);
        }catch(IllegalArgumentException e){
            System.err.println("Enlarging BitSet");
            updateBitSet();
        }
    }
    
    @Override
    public boolean isRectangleAt(int x, int y, int index){
        if(x<0 || y<0) return true; // Out of bound
        if(fixedHeight && y>fixedHeightValue) return true; // Out of bound
        if(x>=bitSetWidth || y>=bitSetHeight) return false; // There is no bitset here yet
        return getBitSet(x, y);
    }

    @Override
    public boolean isNewRectangleValid(ADT_Rectangle rec){
        if(fixedHeight && rec.getY()+rec.getHeight()>fixedHeightValue) return false;
        
        // Check bottom and top
        for(int x = rec.getX(); x<rec.getX()+rec.getWidth(); ++x){
            if(isRectangleAt(x, rec.getY(), -1)) return false;
            if(isRectangleAt(x, rec.getY()+rec.getHeight()-1, -1)) return false;
        }
        
        // set left and right
        for(int y = rec.getY()+1; y<rec.getY()+rec.getHeight()-1; ++y){
            if(isRectangleAt(rec.getX(), y, -1)) return false;
            if(isRectangleAt(rec.getX()+rec.getWidth()-1, y, -1)) return false;
        }

        return true;
    }
}
