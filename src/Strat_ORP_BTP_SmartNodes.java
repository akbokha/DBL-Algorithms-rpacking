import java.util.Comparator;
import java.util.HashSet;

public class Strat_ORP_BTP_SmartNodes extends Strat_ORP_BinaryTreePacker {
    HashSet<Integer> nodeTrackerX; // to track the widths (and heights if rotations are allowed) of the input rectangles
    HashSet<Integer> nodeTrackerY; // to track the widths (and heights if rotations are allowed) of the input rectangles
    
    public Strat_ORP_BTP_SmartNodes(ADT_Area area) {
        super(area);
        smartNodeInit();
    }
    
    public Strat_ORP_BTP_SmartNodes(ADT_Area area, Comparator<ADT_Rectangle> version) { // for other sorting methods
        super(area, version);
        smartNodeInit();
    }
    
    private void smartNodeInit () {
        nodeTrackerX = new HashSet<>();
        nodeTrackerY = new HashSet<>();
        fillSet();
    }
    
    private void fillSet() {
        for (int i = 0; i != sortedRectangles.length; i++) {
            nodeTrackerX.add(sortedRectangles[i].getWidth());
            nodeTrackerY.add(sortedRectangles[i].getHeight());
            if (fixedHeight) {
                nodeTrackerX.add(sortedRectangles[i].getHeight());
                nodeTrackerY.add(sortedRectangles[i].getWidth());
            }
        }
    }
    
    protected void addSmartNodes(ADT_Rectangle rec) {
        int topRightX = rec.getX() + rec.getWidth();
        int topRightY = rec.getY() + rec.getHeight();
        for (Integer dimension : nodeTrackerX) {
            if (topRightX - dimension >= 0) {
                ADT_Node node = new ADT_Node(topRightX - dimension, (rec.getY() + rec.getHeight()));
                binaryTree.addNode(node);
            }
        }
        for (Integer dimension : nodeTrackerY) {
            if (topRightY - dimension >= 0) {
                ADT_Node node = new ADT_Node((rec.getX() + rec.getWidth()), topRightY - dimension);
                binaryTree.addNode(node);  
            }
        }
        
    }
    
    @Override
    public void placeRectangle(ADT_Rectangle rec) {
        bestNode.rec = rec;
        ADT_Node TopLeftChildNode = new ADT_Node(rec.getX(), (rec.getY() + rec.getHeight()));
        binaryTree.addNode(TopLeftChildNode);
        ADT_Node BottomRightChildNode = new ADT_Node((rec.getX() + rec.getWidth()), rec.getY());
        binaryTree.addNode(BottomRightChildNode);
        
        addSmartNodes(rec);

        if(area.getWidth() < rec.getX()+rec.getWidth()){
            area.setWidth(rec.getX()+rec.getWidth());
        }
        if(area.getHeight() < rec.getY()+rec.getHeight()){
            area.setHeight(rec.getY()+rec.getHeight());
        }

        binaryTree.points.get(bestNode.point.x).remove(bestNode);
        checkNodes(rec);
    }  
    
    @Override
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
        for (int i = y; i <= y_rec_topLeft; i++) {
            if (isRectangleAt(x_rec_bottomRight, i, recIndex)) {
                paste++;
                break;
            } 
        }
        // check top side of possible placement rectangle
        for (int i = x; i <= x_rec_bottomRight; i++) {
            if (isRectangleAt(i, y_rec_topLeft, recIndex)) {
                paste++;
                break;
            } 
        }
        return paste;
    }
}
