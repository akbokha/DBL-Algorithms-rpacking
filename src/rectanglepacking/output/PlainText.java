package rectanglepacking.output;

import rectanglepacking.Area;
import rectanglepacking.Rectangle;
import java.util.Iterator;

public class PlainText extends AbstractOutput {

    public PlainText(Area area) {
        super(area);
    }

    @Override
    public void draw() {
        // Draw the container height line.
        System.out.print("container height: ");

        if (area.getHeight() > 0) {
            System.out.println("fixed " + area.getHeight());
        } else {
            System.out.println("free");
        }

        // Draw the rotations allowed line.
        System.out.print("rotations allowed: ");

        if (area.canFlip()) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }

        // Draw the number of rectangles.
        System.out.println("number of rectangles: " + area.getCount());

        // Iterate through all rectangle to get their dimensions and locations.
        Iterator<Rectangle> i = area.getRectangles();
        StringBuilder rectangleDimensions = new StringBuilder();
        StringBuilder rectangleLocations = new StringBuilder();
        while (i.hasNext()) {
            Rectangle rectangle = i.next();
            rectangleDimensions.append(rectangle.getWidth()).append(" ").append(rectangle.getHeight()).append("\n");

            if (area.canFlip()) {
                if (rectangle.getFlipped()) {
                    rectangleLocations.append("yes ");
                } else {
                    rectangleLocations.append("no ");
                }
            }
            rectangleLocations.append(rectangle.getX()).append(" ").append(rectangle.getY()).append("\n");
        }

        // Print all rectangles
        System.out.print(rectangleDimensions.toString());
        System.out.println("placement of rectangles");
        System.out.print(rectangleLocations.toString());
    }
}
