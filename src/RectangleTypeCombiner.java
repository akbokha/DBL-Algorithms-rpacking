import java.util.ArrayList;

/**
 * Created by s157035 on 12-5-2017.
 */
public class RectangleTypeCombiner {
    ArrayList<RectangleType> types = new ArrayList<>();
    private boolean rotationAllowed;

    public RectangleType[] combineRectangleTypes(ADT_Rectangle[] rectangles, boolean rotationAllowed) {

        //Iterate over the input rectangles
        for (ADT_Rectangle currentRec : rectangles) {
            //Check if we already have a definition for this rectangle's getDimensions
            RectangleType existingType = containsDimensions(currentRec.getWidth(), currentRec.getHeight());

            if(existingType != null) {
                //If we have a definition for these dimension already, simply increase it's count
                existingType.increaseInstances();
            }
            else {
                //If we do not have a definition for this yet, create one and add it to the list
                RectangleType newType = new RectangleType(currentRec.getWidth(), currentRec.getHeight(), rotationAllowed);
                types.add(newType);
            }
        }

        //Create the final result array.
        RectangleType[] result = new RectangleType[types.size()];
        for (int i = 0; i < types.size(); i++) {
            result[i] = types.get(i);
        }
        return result;
    }

    private RectangleType containsDimensions(int width, int height) {

        if(rotationAllowed) {
            int smallest = width > height ? height : width;
            int largest = width > height ? width : height;
            for(int i = 0; i < types.size(); i++) {
                RectangleType curType = types.get(i);

                if(curType.getSmallestDimension() == smallest && curType.getLargestDimension() == largest) {
                    return curType;
                }
            }
        }
        else {
            for(int i = 0; i < types.size(); i++) {
                RectangleType curType = types.get(i);

                if(curType.getWidth() == width && curType.getHeight() == height) {
                    return curType;
                }
            }
        }

        return null;
    }
}
