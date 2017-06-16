import java.util.ArrayList;
import java.util.Collection;

/**
 * A template for a backtrack implementation, it uses a composite setup for the pruners. This template is based on the
 * pseudo-code from https://en.wikipedia.org/wiki/Backtracking#Pseudocode.
 */
abstract public class Strat_BT_Template extends Strat_AbstractStrat {

    private Collection<Strat_BT_PrunerInterface> pruners;

    Strat_BT_Template(ADT_AreaExtended areaEx) {
        super(areaEx);
        pruners = new ArrayList<>(5);
    }

    /**
     * Adds a pruner to the list of used pruners during computation.
     * @param pruner the pruner to be added.
     */
    void addPruner(Strat_BT_PrunerInterface pruner) {
        pruners.add(pruner);
    }

    /**
     * Returns a reference to the lastly edited rectangle.
     * @return A reference to the lastly edited rectangle, and null if it is the first rectangle.
     */
    abstract ADT_Rectangle last();

    @Override
    public ADT_AreaExtended compute() {
        if (computeBranch()) {
            return areaEx;
        } else {
            return null;
        }
    }

    /**
     * Attempts to compute a valid solution.
     * @return a valid solution if it exists, else null.
     */
    private boolean computeBranch() {
        long time = System.nanoTime();
        int endTimePruner = 0;
        boolean pruned = false;
        // Dispatch all other calls.
        boolean hasNext = first(); // Select the first branch.
        while (hasNext) {
            ADT_Rectangle last = last();
            long timePruner = System.nanoTime();
            // Check if this iteration can be pruned.
            pruned = reject(last);
                
            endTimePruner = (int)(System.nanoTime()- timePruner);

            // Check if this is a valid solution.
            if (accept(last)) {
                return true;
            }
            // Compute the next result and check if it is valid.
            if (computeBranch()) {
                return true;
            }

            hasNext = next(); // Compute the next branch, if there is one.
        }
        revert();
        int endTime = (int)(System.nanoTime()- time);
        
        DataMining.dataSet.add(new Tuple(areaEx.getCount()-areaEx.getRectanglesToBePlaced().length, 100f*areaEx.getFillRate(), endTime, 100f*areaEx.getTotalAreaRectangles()/(areaEx.getHeight()*areaEx.getWidth()), endTimePruner, pruned));
        
        return false;
    }

    /**
     * Evaluates all pruners to detect if this branch could be rejected.
     * @param last the rectangle changed last.
     * @return true if any of the pruners think that this branch should be rejected, else false.
     */
    protected boolean reject(ADT_Rectangle last) {
        if(last != null) {
            for (Strat_BT_PrunerInterface pruner : pruners) {
                if (pruner.reject(areaEx, last)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Computes if the given problem satisfies all requirements of a valid solution.
     * @param last the last rectangle which was changed.
     * @return true if the given problem is solved, else false.
     */
    abstract boolean accept(ADT_Rectangle last);

    /**
     * Gives the first branch which should be evaluated.
     * @return true if a new branch was created, else false.
     */
    abstract boolean first();

    /**
     * Gives the next branch to be evaluated.
     * @return true if a new branch was created, else false.
     */
    abstract boolean next();

    /**
     * Revert the changes made by first().
     */
    abstract void revert();

}
