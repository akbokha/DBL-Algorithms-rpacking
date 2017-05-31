
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author s158881
 */
public class Strat_ORP_BinaryTreePacker extends Strat_AbstractStrat {
    
    BinaryTree binaryTree;
    
    public Strat_ORP_BinaryTreePacker (ADT_AreaExtended area) {
        super(area);
        this.binaryTree = new BinaryTree();
    }
    
    @Override
    public ADT_AreaExtended compute() {
        ADT_Rectangle [] rectangles = area.getRectangles();
        for (int i = 0; i <= rectangles.length; i++) {
            ADT_Rectangle rec = rectangles[i];
            getBestPlacement(rec).placeRectangle(rec);
        }
        return area;
    }
    
    public Node getBestPlacement(ADT_Rectangle rec) {
        Node bestNode;
        int leastArea;
        
        Map<Integer, HashSet> points = binaryTree.getPoints();
        for(Integer i : points.keySet()){
            HashSet set = points.get(i);
            for (Iterator iter = set.iterator(); iter.hasNext();) {
                // to do: check why cast is necessary
                Node node = (Node) iter.next();
                // check if rectangle is rotatable
                if (true) { // rotatable
                    // rotate rectangle
                    // check if placement of rectangle @ node results in overlap
                    if (false) {
                        // check if area < leastArea
                        if (true) {
                            bestNode = node;
                            // leastArea = new resulting 'better' area
                        }
                    }
                }
                // check if placement of rectangle @ node results in overlap
                    if (false) {
                        // check if area < leastArea
                        if (true) {
                            bestNode = node;
                            // leastArea = new resulting 'better' area
                        }
                    }  
            }
        }
        
        
        return node;
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
