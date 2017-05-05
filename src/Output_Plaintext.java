

import java.util.Iterator;

public class Output_Plaintext extends Output_AbstractOutput {

    public Output_Plaintext(ADT_Area area) {
        super(area);
    }

    @Override
    public void draw() {
        // Check if the response is valid
        if (! area.isValid()) {
            System.err.println("Invalid output detected");
        }

        // Draw the container height line.
        System.out.print("container height: ");

        if (area.getHeight() != ADT_Rectangle.INF) {
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
        Iterator<ADT_Rectangle> i = area.getRectangles();
        StringBuilder rectangleDimensions = new StringBuilder();
        StringBuilder rectangleLocations = new StringBuilder();
        while (i.hasNext()) {
            ADT_Rectangle rectangle = i.next();
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