public class ADT_Node {
    ADT_Rectangle rec = null;
    ADT_Vector point = new ADT_Vector(-1, -1);
        
    /**
     * @pre no rectangle at x,y
     * @param x 
     * @param y 
     */
    public ADT_Node (int x, int y) {
        this.point = new ADT_Vector(x, y);
    }
    
        
    @Override
    public String toString(){
        String result = new String();
        if (rec != null && point != null){
            result = "ERROR - BOTH POINT AND REC WERE FILLED";
        } else if (rec == null && point != null){
            result = "(" + point.x + ", " + point.y + ")";
        } else if (rec != null && point == null){
            result = rec.toString();
        } else{
            result = "Terminating node";
        }
        return result;
    }
}
        