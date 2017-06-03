import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This greedy algorithm places a new rectangle next to another top-left or
 * bottom-right corner of a previously placed rectangle. The corner is chosen by
 * the least amount of extra space of the bounding box. If the space is equal,
 * the location with the highest paste number is chosen.
 */
public class Strat_ORP_BinaryTreePacker extends Strat_AbstractStrat {
    
    BinaryTree binaryTree;
    int recIndex; // the ith rectangle that is currently being placed
    
    Node bestNode = new Node(-1, -1); // Best node to place rec
    int leastArea = Integer.MAX_VALUE; // Size of bounding box when rec is at bestNode
    int greatestPaste = -1; // Number of sides of rec at bestNode where other rectangles are pasted
    int fixedHeightValue;
    boolean fixedHeight;
    boolean isFlipped = false; // Is the best rectangle flipped
    ADT_Rectangle[] sortedRectangles;
    
    public Strat_ORP_BinaryTreePacker (ADT_Area area) {
        super(area);
        sortedRectangles = area.getRectangles().clone();
        Arrays.sort(sortedRectangles, new ADT_SortOnArea()); // sort on area
        this.binaryTree = new BinaryTree();
        fixedHeight = area.getHeight() != -1;
        if (fixedHeight) {
            fixedHeightValue = area.getHeight();
            area.setWidth(0);
        }
    }
    
    @Override
    public ADT_Area compute() {
        ADT_Rectangle [] rectangles = sortedRectangles;
        for (recIndex = 0; recIndex < rectangles.length; recIndex++) {
            ADT_Rectangle rec = rectangles[recIndex];
            getBestPlacement(rec);
            if (isFlipped) { // If best solution was to flip, do so.
                rec.setFlipped(true);
            }
            rec.setX(bestNode.point.x);
            rec.setY(bestNode.point.y);
            bestNode.placeRectangle(rec);
            
            leastArea = Integer.MAX_VALUE;
            greatestPaste = -1;
        }
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
            HashSet<Node> set = binaryTree.points.get(i);
            for (Node node : set) {
                ADT_Rectangle dummyRec = dummyRectangle(rec, node);
                if (area.canFlip()) { // if rotatable
                    // rotate rectangle
                    dummyRec.setFlipped(true);
                    if (isLocationBetter(node, dummyRec)) {
                        isFlipped = true;
                    }
                    // undo rotation dummyRec
                    dummyRec.setFlipped(false);
                }
                if (isLocationBetter(node, dummyRec)) {
                    isFlipped = false;
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
     * dummyRectangle returns a new ADT_Rectangle that can be used to check
     * overlap / exceeding the fixedHeight 
     * @param rec the rec that the dummyRec copies
     * @param node is passed for the x and y coordinates
     * @return a rectangle that is placed at node.point.x and node.point.y with
     * the same dimensions as {@code rec}
     */
    private ADT_Rectangle dummyRectangle (ADT_Rectangle rec, Node node) {
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
     * by leastArea, paste
     * @param node
     * @param rec
     * @return if node is a better choice for rec than bestNode
     */
    private boolean isLocationBetter(Node node, ADT_Rectangle rec){
        // check if placement of rectangle @ node results in overlap
        if (isValidPlacement(node, rec)) {
            // if greatest paste == 4
            if (greatestPaste == 4){
                return false;
            }
            int paste = computePaste(node, rec);
            if (paste == 4){
                greatestPaste = 4;
                bestNode = node;
                // area remains the same
                return true;
            }
            int resulting_area = computeResultingArea(node, rec);
            if (resulting_area < leastArea) { // better resulting area than current best node
                greatestPaste = paste;
                leastArea = resulting_area;
                bestNode = node;
                return true;
            }
          
            if(resulting_area == leastArea){ // same resulting area as current best node
                if (paste > greatestPaste){ // if the placement is better
                    greatestPaste = paste;
                    bestNode = node;
                    return true;
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
    private boolean isValidPlacement (Node node, ADT_Rectangle rec){
        if (fixedHeight) {
            // Check if fixed height is exceeded
            boolean exceedsBound = node.point.y + rec.getHeight() > fixedHeightValue;
            return area.isNewRectangleValid(rec) && !exceedsBound;
        } else { // no fixed heigth
           return area.isNewRectangleValid(rec); 
        }
    }
    
    // compute number of sides that are occupied by a rectangle
    private int computePaste (Node node, ADT_Rectangle rec) {
        int paste = 0;
        int x = node.point.x;
        int y = node.point.y;
        int x_rec_bottomRight = node.point.x + rec.getWidth();
        int y_rec_topLeft = node.point.y + rec.getHeight();
        // check left side of possible placement rectangle
        for (int i = y; i <= y_rec_topLeft; i++) {
            if (isRectangleAt(x-1, i, recIndex)) {
                paste++;
                break;
            } 
        }
        // check bottom side of possible placement rectangle
        for (int i = x; i <= x_rec_bottomRight; i++) {
            if (isRectangleAt(i, y-1, recIndex)) {
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
    private int computeResultingArea (Node node, ADT_Rectangle rec) {
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
        
    private class Node {
        ADT_Rectangle rec = null;
        ADT_Vector point = new ADT_Vector(-1, -1);
        /**
         * @pre no rectangle at x,y
         * @param x 
         * @param y 
         */
        public Node (int x, int y) {
            this.point = new ADT_Vector(x, y);
        }
        
//        public Node(){
//            // create terminating node
//        }
        
        public void placeRectangle(ADT_Rectangle rec) {
            this.rec = rec;
            
            if(area.getWidth() < rec.getX()+rec.getWidth()){
                area.setWidth(rec.getX()+rec.getWidth());
            }
            if(area.getHeight() < rec.getY()+rec.getHeight()){
                area.setHeight(rec.getY()+rec.getHeight());
            }
            
            Node TopLeftChildNode = new Node(rec.getX(), (rec.getY() + rec.getHeight()));
            binaryTree.addNode(TopLeftChildNode);
            Node BottomRightChildNode = new Node((rec.getX() + rec.getWidth()), rec.getY());
            binaryTree.addNode(BottomRightChildNode);
            
            binaryTree.points.get(bestNode.point.x).remove(bestNode);
            checkNodes(rec);
        }
        
        /**
         * Checks if a node should be removed from collection
         * And terminates node (node.rec and node.point become null)
         * @pre x and y of rec have to be set
         * @param rec 
         */
        private void checkNodes(ADT_Rectangle rec) {
            // Check left edge of rec
            HashSet<Node> x_collection = binaryTree.points.get(rec.getX());
            for(Node node : x_collection){
                if((node.point.y >= rec.getY() && 
                        node.point.y < rec.getY()+rec.getHeight())){
                    binaryTree.removeNode(node);
                }
            }
            
            // Check for bottom edge
            for(int i = rec.getX(); i<rec.getX()+rec.getWidth(); ++i){
                if(binaryTree.points.get(i) != null){
                    for(Node node : binaryTree.points.get(i)){
                        if(node.point.y == rec.getY()){
                            binaryTree.removeNode(node);
                        }
                    }
                }
            }
        }
        
        @Override
        public String toString(){
            String result = new String();
            if(rec != null && point != null){
                result = "ERROR - BOTH POINT AND REC WERE FILLED";
            }else if(rec == null && point != null){
                result = "(" + point.x + ", " + point.y + ")";
            }else if(rec != null && point == null){
                result = rec.toString();
            }else{
                result = "Terminating node";
            }
            return result;
        }
    }
    
    private final class BinaryTree {
        Node root; 
        private HashMap<Integer, HashSet<Node>> points;
        
        public BinaryTree() {
            root = new Node(0, 0);
            bestNode = root;
            points = new HashMap<>();
            this.addNode(root);
        }
        
        public void addNode(Node node){
            int x = node.point.x;
            if (points.get(x) == null) {
               // Add collection
               HashSet <Node> collection = new HashSet<>(); 
               collection.add(node);
               points.put(x, collection);
            } else { // add to collection of key = x
                HashSet<Node> collection = points.get(x);
                collection.add(node);
            }
        }
        
        /**
         * Removes node from tree and terminates it
         * @pre node /in points
         * @param node 
         */
        public void removeNode(Node node) {
            int x = node.point.x;
            node.point = null;
            HashSet<Node> collection = points.get(x);
            collection.remove(node);
            if (collection.isEmpty()) {
                points.remove(x);
            }
        }
    }
}