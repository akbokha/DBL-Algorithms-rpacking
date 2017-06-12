import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * This greedy algorithm places a new rectangle next to another top-left or
 * bottom-right corner of a previously placed rectangle. The corner is chosen by
 * the least amount of extra space of the bounding box. If the space is equal,
 * the location with the highest paste number is chosen.
 */
public class Strat_ORP_BinaryTreePacker extends Strat_AbstractStrat {
    static final int NOTSET = -2;
    
    protected BinaryTree binaryTree;
    int recIndex; // the ith rectangle that is currently being placed
    int heightResult = 0;

    Comparator<ADT_Rectangle> version;
    
    // These variables are reset for each rectangle
    ADT_Node bestNode = new ADT_Node(-1, -1); // Best node to place rec
    boolean isFlipped = false; // Is the best rectangle flipped
    int greatestPaste = -1; // Number of sides of rec at bestNode where other rectangles are pasted
    int leastArea = Integer.MAX_VALUE; // Size of bounding box when rec is at bestNode
    
    int fixedHeightValue;
    boolean fixedHeight;
    ADT_Rectangle[] sortedRectangles;
    
    public Strat_ORP_BinaryTreePacker (ADT_Area area) {
        super(area);
        sortedRectangles = this.area.getRectangles().clone();
        Arrays.sort(sortedRectangles, new ADT_SortRecOnArea()); // sort on area
        this.binaryTree = new BinaryTree();
        fixedHeight = this.area.getHeight() != -1;
        if (fixedHeight) {
            fixedHeightValue = this.area.getHeight();
            this.area.setWidth(0);
        }
    }
    
    public Strat_ORP_BinaryTreePacker (ADT_Area area, Comparator<ADT_Rectangle> version) { // version = sort method
        super(area);
        this.version = version;
        sortedRectangles = this.area.getRectangles().clone();
        Arrays.sort(sortedRectangles, version); // sort as specified
        this.binaryTree = new BinaryTree();
        fixedHeight = this.area.getHeight() != -1;
        if (fixedHeight) {
            fixedHeightValue = this.area.getHeight();
            this.area.setWidth(0);
        }
    }
    
    @Override
    public ADT_Area compute() {
        ADT_Rectangle [] rectangles = sortedRectangles;
        for (recIndex = 0; recIndex < rectangles.length; recIndex++) {
            ADT_Rectangle rec = rectangles[recIndex];
            getBestPlacement(rec); // sets bestNode for this rectangle
            if (isFlipped) { // If best solution was to flip, do so.
                rec.setFlipped(true);
            }
            rec.setX(bestNode.point.x);
            rec.setY(bestNode.point.y);
            placeRectangle(rec); // place the rectangle on the bestNode
            
            leastArea = Integer.MAX_VALUE;
            greatestPaste = -1;
        }
        heightResult = area.getHeight();
        if (!fixedHeight) {
            area.setHeight(-1);
        } // For proper output
        return area;
    }
    
    /**
     * getBestPlacement iterates over all the nodes and makes sure that 
     * bestNode will be assigned the node that is the best possible 
     * placement for rec
     * @param rec the rectangle that is considered
     */
    private void getBestPlacement(ADT_Rectangle rec) {
        for(Integer i : binaryTree.points.keySet()){
            HashSet<ADT_Node> set = binaryTree.points.get(i);
            for (ADT_Node node : set) {
                // Use a dummyRectangle to check overlap
                // we do not want to set x and y coordinates, because it is unsure whether this is the bestnode
                ADT_Rectangle dummyRec = dummyRectangle(rec, node);
                if (area.canFlip() && !isSquare(rec)) { // if rotatable & not a square
                    // rotate rectangle
                    dummyRec.setFlipped(true);
                    if (isLocationBetter(node, dummyRec)) {
                        isFlipped = true;
                        if (greatestPaste == 4) {
                            return;
                        }
                    }
                    // undo rotation dummyRec
                    dummyRec.setFlipped(false);
                }
                if (isLocationBetter(node, dummyRec)) {
                    isFlipped = false; // no rotation results in a better placement
                    if (greatestPaste == 4) {
                            return;
                    }
                }
            }
        }
    }
    
      /**
     * Check if there is already a rectangle placed at (x,y). Use for sorted
     * rectangles in BTP.
     * @param x coordinate to be checked
     * @param y coordinate to be checked
     * @param index of the rectangle in the array
     * @return true iff there is a rectangle at (x,y) or if x < 0 or y <0
     */
    public boolean isRectangleAt (int x, int y, int index) {
        if (x < 0 ||  y < 0) {
            return true;
        }
        for (int i = 0; i <= index; i++) {
            ADT_Rectangle rec = sortedRectangles[i];
            boolean x_dim = (x >= rec.getX()) && (x < (rec.getX() + rec.getWidth()));
            boolean y_dim = (y >= rec.getY()) && (y < rec.getY() + rec.getHeight());
            if (x_dim && y_dim) {
                return true;
            }
        }
        return false;
    }
    
        /**
     * Return true if none of the already placed rectangles overlap with the parameter rectangle
     * @pre Rectangle coordinates are non-negative.
     * @param rectangle
     * @return boolean
     */
    public boolean isNewRectangleValid(ADT_Rectangle rectangle) {
        assert rectangle != null;
        assert rectangle.getX() != NOTSET;
        assert rectangle.getY() != NOTSET;
        assert rectangle.getX() >= 0;
        assert rectangle.getY() >= 0;

        if(fixedHeight && rectangle.getY()+rectangle.getHeight()>fixedHeightValue) return false;
        
        // Check if it overlaps
        for(int i=0; i<recIndex; ++i){
            ADT_Rectangle rec = sortedRectangles[i];
            if(rec != rectangle && rec.getX() != NOTSET && area.checkRectangleOverlap(rec, rectangle)) {
                return false;
            }
        }

        return true;
    }
    
    /**
     * dummyRectangle returns a new ADT_Rectangle that can be used to check
     * overlap / exceeding the fixedHeight 
     * @param rec the rec that the dummyRec copies
     * @param node is passed for the x and y coordinates
     * @return a rectangle that is placed at node.point.x and node.point.y with
     * the same dimensions as {@code rec}
     */
    private ADT_Rectangle dummyRectangle (ADT_Rectangle rec, ADT_Node node) {
        int x = node.point.x;
        int y = node.point.y;
        int width = rec.getWidth();
        int height = rec.getHeight();
        return new ADT_Rectangle(width, height, x, y, rec.canFlip());
    }
    
    /**
     * Determines if node is a better location for rec. 
     * A node with paste 4 is always the best (if multiple are available, choose
     * the bottom-leftmost). Otherwise, select the node sorted lexicographically
     * by leastArea, paste and lowest (x,y)
     * @param node
     * @param rec
     * @return if node is a better choice for rec than bestNode
     */
    private boolean isLocationBetter(ADT_Node node, ADT_Rectangle rec){
        // check if placement of rectangle @ node results in overlap
        if (isValidPlacement(node, rec)) {
            int paste = computePaste(node, rec);
            
            // CASE: paste = 4
            if (paste == 4){
                if(greatestPaste == 4){
                    // Determine best by position
                    if(node.point.x<bestNode.point.x || 
                            (node.point.x==bestNode.point.x && 
                            node.point.y<bestNode.point.y)){ 
                        // Replace bestNode
                        greatestPaste = 4;
                        bestNode = node;
                        // area remains the same
                        return true;
                    }
                }else{
                    // Replace bestNode
                    greatestPaste = 4;
                    bestNode = node;
                    // area remains the same
                    return true;
                }
                return false;
            }
            
            int resulting_area = computeResultingArea(node, rec);
            
            // CASE: added area is less
            if (resulting_area < leastArea) { // better resulting area than current best node
                greatestPaste = paste;
                leastArea = resulting_area;
                bestNode = node;
                return true;
            }
          
            // CASE: added area is equal
            if(resulting_area == leastArea){ // same resulting area as current best node
                // Choose by paste
                if (paste > greatestPaste){ // if the placement is better
                    greatestPaste = paste;
                    bestNode = node;
                    return true;
                }
                if (paste == greatestPaste){
                    // Choose by location
                    if(node.point.x<bestNode.point.x || 
                            (node.point.x==bestNode.point.x && 
                            node.point.y<bestNode.point.y)){ 
                        // Replace bestNode
                        greatestPaste = paste;
                        bestNode = node;
                        leastArea = resulting_area;
                        return true;
                    }
                }
            }   
        }
        return false;
    }
    
    /**
     * Check if this rectangle can be placed at the given position by
     * node.point.x and node.point.y 
     * @param node is needed for the x and y coordinates
     * @param rec that is considered (dummyRectangle)
     */
    private boolean isValidPlacement (ADT_Node node, ADT_Rectangle rec){
        if (fixedHeight) {
            // Check if fixed height is exceeded
            boolean exceedsBound = node.point.y + rec.getHeight() > fixedHeightValue;
            return isNewRectangleValid(rec) && !exceedsBound;
        } else { // no fixed heigth
           return isNewRectangleValid(rec); 
        }
    }
    
    /**
     * Check if the rectangle is a square
     * @param rec that is considered
     * @return true if rec.width == rec.height
     */
    private boolean isSquare (ADT_Rectangle rec) {
        int width = rec.getWidth();
        int height = rec.getHeight();
        return width == height;
    }
    
    // compute number of sides that are occupied by a rectangle
    protected int computePaste (ADT_Node node, ADT_Rectangle rec) {
        int paste = 0;
        int x = node.point.x;
        int y = node.point.y;
        int x_rec_bottomRight = node.point.x + rec.getWidth();
        int y_rec_topLeft = node.point.y + rec.getHeight();
        // check left side of possible placement rectangle
        for (int i = y; i <= y_rec_topLeft; i++) {
            if (isRectangleAt((x - 1), i, recIndex)) {
                paste++;
                break;
            } 
        }
        // check bottom side of possible placement rectangle
        for (int i = x; i <= x_rec_bottomRight; i++) {
            if (isRectangleAt(i, (y - 1), recIndex)) {
                paste++;
                break;
            } 
        }
        // check right side of possible placement rectangle
        if (isRectangleAt(x_rec_bottomRight, y, recIndex)) {
            paste++;
        }
        // check top side of possible placement rectangle
        if (isRectangleAt(x, y_rec_topLeft, recIndex)) {
            paste++;
        }
        return paste;
    }
    
    /**
     * 
     * @param node the node that is considered as placement option
     * @param rec the rectangle that is considered
     * @return the resulting area when you place rectangle at
     * (node.point.x, node.point.y)
     */
    private int computeResultingArea (ADT_Node node, ADT_Rectangle rec) {
        int currentWidthBoundingBox = area.getWidth();
        int currentHeightBoundingBox = area.getHeight();
        int newWidthBoundingBox;
        int newHeightBoundingBox;
        
        if (node.point.x + rec.getWidth() > currentWidthBoundingBox) {
            // resultingArea is larger than area of currentBoundingBox
            newWidthBoundingBox = node.point.x + rec.getWidth();
        } else {
            newWidthBoundingBox = currentWidthBoundingBox;
        }
        if (node.point.y + rec.getHeight() > currentHeightBoundingBox) {
            // resultingArea is larger than area of currentBoundingBox
            newHeightBoundingBox = node.point.y + rec.getHeight();
        } else {
            newHeightBoundingBox = currentHeightBoundingBox;
        }
        return (newWidthBoundingBox * newHeightBoundingBox);
    }

    public void placeRectangle(ADT_Rectangle rec) {
        bestNode.rec = rec;
        ADT_Node TopLeftChildNode = new ADT_Node(rec.getX(), (rec.getY() + rec.getHeight()));
        binaryTree.addNode(TopLeftChildNode);
        ADT_Node BottomRightChildNode = new ADT_Node((rec.getX() + rec.getWidth()), rec.getY());
        binaryTree.addNode(BottomRightChildNode);

        if(area.getWidth() < rec.getX()+rec.getWidth()){
            area.setWidth(rec.getX()+rec.getWidth());
        }
        if(area.getHeight() < rec.getY()+rec.getHeight()){
            area.setHeight(rec.getY()+rec.getHeight());
        }

        binaryTree.points.get(bestNode.point.x).remove(bestNode);
        checkNodes(rec);
    }

    /**
     * Checks if a node should be removed from collection
     * And terminates node (node.rec and node.point become null)
     * @pre x and y of rec have to be set
     * @param rec 
     */
    public void checkNodes(ADT_Rectangle rec) {
        // Check left edge of rec
        HashSet<ADT_Node> x_collection = binaryTree.points.get(rec.getX());
        ArrayList<Integer> checkIfEmpty = new ArrayList<>(); // to track x coordinates of nodes that are removed

        for (Iterator<ADT_Node> nodeIterator = x_collection.iterator(); nodeIterator.hasNext();) {
            ADT_Node node = nodeIterator.next();
            if((node.point.y >= rec.getY() && 
                    node.point.y < rec.getY()+rec.getHeight())){
                checkIfEmpty.add(node.point.x);
                node.point = null;
                nodeIterator.remove();
            }
        }

        // Check for bottom edge
        for(int i = rec.getX(); i<rec.getX()+rec.getWidth(); ++i){
            if(binaryTree.points.get(i) != null){
                x_collection = binaryTree.points.get(i);
                for (Iterator<ADT_Node> nodeIterator = x_collection.iterator(); nodeIterator.hasNext();) {
                    ADT_Node node = nodeIterator.next();
                    if(node.point.y == rec.getY()){
                        checkIfEmpty.add(node.point.x);
                        node.point = null;
                        nodeIterator.remove();
                    }
                }
            }
        }
        // check if there are empty collections (and remove them)
        for (Integer i : checkIfEmpty) {
            if (binaryTree.points.get(i).isEmpty() || binaryTree.points.get(i) == null) {
                binaryTree.points.remove(i);
            }
        }
    }
    
    
    protected final class BinaryTree {
        ADT_Node root; 
        protected HashMap<Integer, HashSet<ADT_Node>> points;
        
        public BinaryTree() {
            root = new ADT_Node(0, 0);
            bestNode = root;
            points = new HashMap<>();
            this.addNode(root);
        }
        
        public void addNode(ADT_Node node){
            int x = node.point.x;
            if (points.get(x) == null) {
               // Add collection
               HashSet <ADT_Node> collection = new HashSet<>(); 
               collection.add(node);
               points.put(x, collection);
            } else { // add to collection of key = x
                HashSet<ADT_Node> collection = points.get(x);
                collection.add(node);
            }
        }
    }

    @Override
    public String toString() {
        if (version==null) {
            return super.toString();
        } else {
            return super.toString() + "{" + version.getClass().toString() + "}";
        }
    }
}
