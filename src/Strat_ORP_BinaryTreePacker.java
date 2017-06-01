
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * This greedy algorithm places a new rectangle next to another top-left or
 * bottom-right corner of a previously placed rectangle. The corner is chosen by
 * the least amount of extra space of the bounding box. If the space is equal,
 * the location with the highest paste number is chosen.
 * 
 * @author Abdel and Jorrit
 * @date May 31st, 2017
 */
public class Strat_ORP_BinaryTreePacker extends Strat_AbstractStrat {
    
    BinaryTree binaryTree;
    
    Node bestNode = new Node(); // Best node to place rec
    int leastArea = Integer.MAX_VALUE; // Size of bounding box when rec is at bestNode
    int greatestPaste = 0; // Number of sides of rec at bestNode where other rectangles are pasted
    
    public Strat_ORP_BinaryTreePacker (ADT_AreaExtended area) {
        super(area);
        this.binaryTree = new BinaryTree();
    }
    
    @Override
    public ADT_AreaExtended compute() {
        ADT_Rectangle [] rectangles = area.getRectangles();
        for (int i = 0; i < rectangles.length; i++) {
                greatestPaste = 0;
                ADT_Rectangle rec = rectangles[i];
                getBestPlacement(rec);
                area.moveRectangle(rec, bestNode.point.x, bestNode.point.y);
                bestNode.placeRectangle(rec);
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
        Map<Integer, HashSet> points = binaryTree.getPoints();
        for(Integer i : points.keySet()){
            HashSet set = points.get(i);
            for (Iterator iter = set.iterator(); iter.hasNext();) { // iterate over nodes
                Node node = (Node) iter.next(); // to do: check why cast is necessary
                ADT_Rectangle dummyRec = dummyRectangle(rec, node);
                if (area.canFlip()) { // if rotatable
                    // rotate rectangle
                    dummyRec.setFlipped(true);
                    dummyRec.setHeight(dummyRec.getWidth());
                    dummyRec.setWidth(dummyRec.getHeight());
                    isLocationBetter(node, dummyRec);
                    // undo rotation dummyRec
                    dummyRec.setHeight(dummyRec.getWidth());
                    dummyRec.setWidth(dummyRec.getHeight());
                    dummyRec.setFlipped(false);
                }
                isLocationBetter(node, dummyRec);
            }
        }
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
        if (area.getHeight() != (-1)) {
            // no fixed heigth
            return area.isNewRectangleValid(rec);
        } else {
            // Check if fixed height is exceeded
            boolean exceedsBound = node.point.y + rec.getHeight() > area.getHeight();
            return area.isNewRectangleValid(rec) && !exceedsBound;
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
            if (! area.isEmptyAt(x, i)) {
                paste++;
                break;
            } 
        }
        // check bottom side of possible placement rectangle
        for (int i = x; i <= x_rec_bottomRight; i++) {
            if (! area.isEmptyAt(i, y)) {
                paste++;
                break;
            } 
        }
        // check right side of possible placement rectangle
        if (! area.isEmptyAt(x_rec_bottomRight, y)) {
            paste++;
        }
        // check top side of possible placement rectangle
        if (! area.isEmptyAt(x, y_rec_topLeft)) {
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
        ADT_Vector point = null;
        Node node_TL = null;
        Node node_BR = null;
        
        /**
         * @pre no rectangle at x,y
         * @param x 
         * @param y 
         */
        public Node (int x, int y) {
            point = new ADT_Vector(x, y);
        }
        
        public Node(){
            // create terminating node
        }
        
        public void placeRectangle(ADT_Rectangle rec) {
            this.rec = rec;
            this.point = null;
            this.node_TL  = new Node(rec.getX(), (rec.getY() + rec.getHeight()));
            this.node_BR = new Node((rec.getX() + rec.getWidth()), rec.getY());
            checkNodes(rec);
        }
        
        /**
         * Checks if a node should be removed from collection
         * And terminates node (node.rec and node.point become null)
         * @param rec 
         */
        private void checkNodes(ADT_Rectangle rec) {
            int x = rec.getX();
            int y = rec.getY();
            
            HashSet<Node> x_collection;
            Map points = binaryTree.getPoints();
            if (points.get(x) != null) {
                // To Do: Check why this cast is necessary
                x_collection =  (HashSet<Node>) points.get(x);
                for (Node node : x_collection) {
                    if (node.point.y == y ){
                        binaryTree.removeNode(node);
                        node.rec = null;
                        node.point = null;
                    }
                }
                for (int i = 0; i < rec.getWidth(); i++) {
                    int x_cor = rec.getX() + i;
                    x_collection =  (HashSet<Node>) points.get(x_cor);
                    for (Node node : x_collection) {
                        if (node.point.y == y) {
                            binaryTree.removeNode(node);
                            node.rec = null;
                            node.point = null;
                        }
                    }
                }
            }
            
        }
    }
    
    private class BinaryTree {
        Node root; 
        private final HashMap<Integer, HashSet<Node>> points;
        
        public BinaryTree() {
            root = new Node(0, 0);
            points = new HashMap<>();
            addNode(root);
        }
        
        public HashMap getPoints() {
            return this.points;
        }
        
        public void addNode(Node node){
            int x = node.point.x;
            if (points.get(x) == null) {
               // Add collection
               HashSet <Node> collection = new HashSet<>(); 
               collection.add(node);
               points.put(x, collection);
            } else { // add to collection of key = x
                HashSet collection = points.get(x);
                collection.add(node);
            }
        }
        
        /**
         * @pre node /in points
         * @param node 
         */
        public void removeNode(Node node) {
            int x = node.point.x;
            HashSet collection = points.get(x);
            collection.remove(node);
            if (collection.isEmpty()) {
                points.remove(x);
            }
        }
    }
}