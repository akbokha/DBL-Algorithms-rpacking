
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
    
    private Node getBestPlacement(ADT_Rectangle rec) {
        Map<Integer, HashSet> points = binaryTree.getPoints();
        for(Integer i : points.keySet()){
            HashSet set = points.get(i);
            for (Iterator iter = set.iterator(); iter.hasNext();) { 
                // iterate over all points
                Node node = (Node) iter.next(); // to do: check why cast is necessary
                // check if rectangle is rotatable
                ADT_Rectangle dummyRec = dummyRectangle(rec, node);
                if (area.canFlip()) { // rotatable
                    // rotate dummyRec
                    isLocationBetter(node, dummyRec);
                    // undo rotation dummyRec
                }
                isLocationBetter(node, dummyRec);
            }
        }
        return bestNode;
    }
    
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
        if (area.getHeight() != (-1)) { 
            if (area.isNewRectangleValid(rec)) {
            // if greatest paste == 4
            if(greatestPaste == 4){
                return false;
            }
            int paste = 0;
            // compute paste
            // if this paste == 4
            if(true){
                greatestPaste = 4;
                bestNode = node;
                // area remains the same
                return true;
            }
            int resulting_area = 0;
            // compute resulting_area 
            // check if resutling_area < leastArea
            if (resulting_area < leastArea) {
                greatestPaste = paste;
                leastArea = resulting_area;
                bestNode = node;
                return true;
            }
            
            if(resulting_area == leastArea){
                if(paste > greatestPaste){
                    greatestPaste = paste;
                    bestNode = node;
                    return true;
                }
            }
            }
        } else {
            
            
        }
        return false;
    }
    
    private boolean isValidPlacement (Node node, ADT_Rectangle rec){
        if (area.getHeight() != (-1)) {
            return area.isNewRectangleValid(rec);
        } else {
            boolean exceedsBound = node.point.y + rec.getHeight() > area.getHeight();
            return area.isNewRectangleValid(rec) && !exceedsBound;
        }
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