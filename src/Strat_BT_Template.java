import java.util.ArrayList;
import java.util.Collection;

/**
 * A template for a backtrack implementation, it uses a composite setup for the pruners. This template is based on the
 * pseudo-code from https://en.wikipedia.org/wiki/Backtracking#Pseudocode.
 */
abstract public class Strat_BT_Template extends Strat_AbstractStrat {
    int ind = -1;

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
        ind++;
        // Dispatch all other calls.
        boolean hasNext = first(); // Select the first branch.
        while (hasNext) {
            ADT_Rectangle last = last();
            
            // Check if this is a valid solution.
            if (accept(last)) {
                ind--;
                return true;
            }
            
            // Check if this iteration can be pruned.
            if (reject(last, ind)) {
                hasNext = next();
                continue;
            }
            
            // Compute the next result and check if it is valid.
            if (computeBranch()) {
                ind--;
                return true;
            }

            hasNext = next(); // Compute the next branch, if there is one.
        }
        
        revert();
        ind--;
        return false;
    }

    /**
     * Evaluates all pruners to detect if this branch could be rejected.
     * @param last the rectangle changed last.
     * @param index
     * @return true if any of the pruners think that this branch should be rejected, else false.
     */
    protected boolean reject(ADT_Rectangle last, int index) {
        if(last != null) {
            for (Strat_BT_PrunerInterface pruner : pruners) {
                if (pruner.reject(areaEx, last, index)) {
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
