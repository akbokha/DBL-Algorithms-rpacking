import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * An implementation of the Floor Ceiling Strategy
 * The original Floor Ceiling algorithms considers a fixed width and tries to
 * minimize the number of bins of this width/the height.
 * In our context we use this implementation when we have a fixed height and
 * when rotations of rectangles is not allowed (try to minimize the width).
 *
 * @author Abdel K.
 * date: 08-05-17
 */
public class Strat_FixedHeightFixedOrient_FC extends Strat_AbstractStrat {

    private List<ADT_Rectangle> orderedRectangles;
    private List<Shelf> shelves;
    private final int fixed_height;
    private int rectangleIndex = 0;
    private final int numberOfRectangles;
    private int x_tracker = 0;

     public Strat_FixedHeightFixedOrient_FC (ADT_Area area) {
        super(area);
        this.shelves = new ArrayList<>();
        fixed_height = area.getHeight();
        numberOfRectangles = area.getCount();
    }

    @Override
    public ADT_Area compute() {
        orderedRectangles = getOrderedRectangles();
        ADT_Rectangle first_rec = orderedRectangles.get(rectangleIndex);
        Shelf first_shelf = new Shelf(0, first_rec.getWidth());
        x_tracker += first_rec.getWidth();
        this.shelves.add(first_shelf);
        placeFirstRectangleShelf(first_shelf, first_rec);
        return area;
    }

    private void placeFirstRectangleShelf (Shelf shelf, ADT_Rectangle rec) {
        shelf.floorRecSpace -= rec.getHeight();
        shelf.ceilRecSpace -= rec.getHeight();
        rec.setX(shelf.x);
        rec.setY(0);
        shelf.addRectangle(rec);
        shelf.setLastFloor(rec);
        rectangleIndex++;
        placeRectangles();
    }

    private void placeRectangles() {
        while (rectangleIndex != numberOfRectangles) {
            ADT_Rectangle current_rec = orderedRectangles.get(rectangleIndex);
            setRecDoesNotFitBooleanAllShelves(false);
            setRecDoesNotFitBooleanIfNotFloor(current_rec);
            if (canPlaceItOnACeiling(current_rec)) {
                Shelf bestFitShelf = getBestFitCeiling();
                current_rec.setX((bestFitShelf.x + bestFitShelf.ceiling) - current_rec.getWidth());
                current_rec.setY(bestFitShelf.lastPlacedCeiling.getY() - current_rec.getHeight());
                rectangleIndex++;
                bestFitShelf.addRectangle(current_rec);
                bestFitShelf.setLastCeiling(current_rec);
                bestFitShelf.ceilRecSpace -= current_rec.getHeight();
                bestFitShelf.firstCeil = true;
            } else if (canPlaceItOnAFloor(current_rec)) {
                Shelf bestFitShelf = getBestFitFloor();
                current_rec.setX(bestFitShelf.x);
                current_rec.setY(bestFitShelf.lastPlacedFloor.getY() + bestFitShelf.lastPlacedFloor.getHeight());
                rectangleIndex++;
                bestFitShelf.addRectangle(current_rec);
                bestFitShelf.setLastFloor(current_rec);
                bestFitShelf.floorRecSpace -= current_rec.getHeight();
            } else {
               Shelf newShelf = new Shelf(x_tracker, current_rec.getWidth());
               this.shelves.add(newShelf);
               x_tracker += current_rec.getWidth();
               placeFirstRectangleShelf(newShelf, current_rec);
            }
        }
    }

    private void setRecDoesNotFitBooleanAllShelves(boolean set) {
        for (Shelf shelf : shelves) {
            shelf.recDoesNotFit = set;
        }
    }

    private void setRecDoesNotFitBooleanIfNotFloor(ADT_Rectangle rec) {
        for (Shelf shelf : shelves) {
                if ((rectangleOverlap(shelf, dummyFloor(shelf, rec, shelf.lastPlacedFloor)))  || ((fixed_height - (shelf.lastPlacedFloor.getY() + shelf.lastPlacedFloor.getHeight())) < rec.getHeight())) {
                    shelf.recDoesNotFit = true;
                }
        }
    }

    private boolean canPlaceItOnACeiling(ADT_Rectangle rec) {
        for (Shelf shelf : shelves) {
            if ((shelf.recDoesNotFit || shelf.firstCeil) && !rectangleOverlap(shelf, dummyCeil(shelf, rec, shelf.lastPlacedCeiling))) {
                return true;
            }
        }
        return false;
    }

    private boolean canPlaceItOnAFloor(ADT_Rectangle rec) {
        for (Shelf shelf : shelves) {
            if (! rectangleOverlap(shelf, dummyFloor(shelf, rec, shelf.lastPlacedFloor)) &&
                    ((fixed_height - (shelf.lastPlacedFloor.getY() + shelf.lastPlacedFloor.getHeight())) >= rec.getHeight())) {
                return true;
            }
        }
        return false;
    }

    private Shelf getBestFitCeiling() {
        Shelf shelf = Collections.min(shelves, Comparator.comparingInt(i -> i.ceilRecSpace));
        return shelf;
    }

    private Shelf getBestFitFloor() {
        Shelf shelf = Collections.min(shelves, Comparator.comparingInt(i -> i.floorRecSpace));
        return shelf;
    }

    private ADT_Rectangle dummyCeil(Shelf shelf, ADT_Rectangle rec, ADT_Rectangle lastCeil) {
        int x = (shelf.x + shelf.ceiling) - rec.getWidth();
        int y = lastCeil.getY() - rec.getHeight();
        ADT_Rectangle dummy = new ADT_Rectangle(rec.getWidth(), rec.getHeight(), x, y, false);
        return dummy;
    }

    private ADT_Rectangle dummyFloor(Shelf shelf, ADT_Rectangle rec, ADT_Rectangle lastFloor) {
        int x = shelf.x;
        int y = lastFloor.getY() + lastFloor.getHeight();
        ADT_Rectangle dummy = new ADT_Rectangle(rec.getWidth(), rec.getHeight(), x, y, false);
        return dummy;
    }

    private boolean rectangleOverlap (Shelf shelf, ADT_Rectangle rec) {
        for (Iterator<ADT_Rectangle> rectangles = shelf.getRectangles(); rectangles.hasNext();) {
            ADT_Rectangle cur_rec = rectangles.next();
            if (shelf.checkRectangleOverlap(cur_rec, rec)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sorts the rectangles that can be found in area based on their width
     * @return a List with the all the rectangles /in area such that the list
     * is ordered based on their width.
     */
    private List<ADT_Rectangle> getOrderedRectangles() {
        List<ADT_Rectangle> orderedWidth = new ArrayList<>();
        for(Iterator<ADT_Rectangle> rectangles = area.getRectangles(); rectangles.hasNext();) {
             ADT_Rectangle rec = rectangles.next();
             orderedWidth.add(rec);
         }

        Collections.sort(orderedWidth, new Comparator<ADT_Rectangle>(){
            @Override
            public int compare(ADT_Rectangle rec1, ADT_Rectangle rec2) {
                return (rec2.getWidth() - rec1.getWidth());
            }
        });
        return orderedWidth;
    }

    private class Shelf {

        private final int x;
        private final int ceiling;
        private ADT_Rectangle lastPlacedFloor;
        private ADT_Rectangle lastPlacedCeiling;
        Collection<ADT_Rectangle> rectangles;
        private boolean firstCeil;
        private boolean recDoesNotFit;
        private int floorRecSpace;
        private int ceilRecSpace;

        public Shelf(int x, int ceiling) {
            this.firstCeil = false;
            this.x = x;
            this.ceiling = ceiling;
            this.lastPlacedFloor = null;
            this.lastPlacedCeiling = null;
            rectangles = new ArrayList<>();
            this.floorRecSpace = ceiling;
            this.ceilRecSpace = ceiling;
            lastPlacedCeiling = new ADT_Rectangle(0, 0, (x + ceiling), fixed_height, false);
            this.recDoesNotFit = false;
        }

        // The first item packed on a ceiling can only be one which cannot be packed on the floor below
        public void setFirstCeil() {
            this.firstCeil = true;
        }

        public void addRectangle(ADT_Rectangle rectangle) {
            rectangles.add(rectangle);
        }

        public void setLastFloor(ADT_Rectangle lastFloor) {
            this.lastPlacedFloor = lastFloor;
        }

        public void setLastCeiling(ADT_Rectangle lastCeiling) {
            this.lastPlacedCeiling = lastCeiling;
        }

        public Iterator<ADT_Rectangle> getRectangles() {
            return rectangles.iterator();
        }

        // copied from ADT_Area
        private boolean checkRectangleOverlap(ADT_Rectangle rec1, ADT_Rectangle rec2) {
            assert rec1 != null;
            assert rec2 != null;
            assert rec1.getWidth() != ADT_Rectangle.INF;
            assert rec2.getWidth() != ADT_Rectangle.INF;
            assert rec1.getHeight() != ADT_Rectangle.INF;
            assert rec2.getWidth() != ADT_Rectangle.INF;

            Point l1 = new Point(rec1.getX(), rec1.getY() + rec1.getHeight()); // Top left coordinate of first rectangle
            Point r1 = new Point(rec1.getX() + rec1.getWidth(), rec1.getY()); // Bottom right coordinate of first rectangle
            Point l2 = new Point(rec2.getX(), rec2.getY() + rec2.getHeight()); // Top left coordinate of second rectangle
            Point r2 = new Point(rec2.getX() + rec2.getWidth(), rec2.getY()); // Bottom right coordinate of second rectangle

            if (l1.getX() >= r2.getX() || l2.getX() >= r1.getX()) { // Check if one rectangle is on the left side of the other rectangle.
                return false;
            } else return !(l1.getY() <= r2.getY() || l2.getY() <= r1.getY()); // Check if one rectangle is above the other rectangle.

        }

        private class Point {
            private int x;
            private int y;

            Point(int x, int y) {
                this.x = x;
                this.y = y;
            }

            int getX() {
                return x;
            }

            int getY() {
                return y;
            }
        }
    }
}
