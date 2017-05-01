package rectanglepacking.output;

import rectanglepacking.Area;
import rectanglepacking.ShapeInterface;

public class PlainText implements OutputInterface {

    @Override
    public void draw(Area area) {
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
        System.out.println("number of rectangles: " + area.getAmount());
        
        for (ShapeInterface rectangle : area.getShapes()) {
            System.out.println(rectangle.getX() + " " + rectangle.getY());
        }


    }
}
