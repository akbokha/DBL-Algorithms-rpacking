import java.util.ArrayList;
import java.util.Collection;

/**
 * A template for a backtrack implementation, it uses a composite setup for the pruners.
 */
abstract public class Strat_BT_Template extends Strat_AbstractStrat {

    private Collection<Strat_BT_PrunerItf> pruners;

    public Strat_BT_Template(ADT_Area area) {
        super(area);
        pruners = new ArrayList<>(5);
    }

    /**
     * Adds a pruner to the list of used pruners during computation.
     * @param pruner the pruner to be added.
     */
    void addPruner(Strat_BT_PrunerItf pruner) {
        pruners.add(pruner);
    }

    @Override
    public ADT_Area compute() {
        return computeBranch(area);
    }

    /**
     * Attempts to compute a valid solution.
     * @param area the problem to be solved
     * @return a valid solution if it exists, else null.
     */
    ADT_Area computeBranch(ADT_Area area) {
        // Check if this iteration can be pruned.
        if (reject(area)) {
            return null;
        }

        // Check if this is a valid solution.
        if (accept(area)) {
            return area;
        }

        // Dispatch all other calls.
        ADT_Area s = first(area); // Select the first branch.
        while (s != null) {
            // Compute the next result and check if it is valid.
            ADT_Area result = computeBranch(s);
            if (result != null) {
                return result;
            }

            s = next(area, s); // Get the next branch.
        }

        return null;
    }

    /**
     * Evaluates all pruners to detect if this branch could be rejected.
     * @param area the problem to be evaluated.
     * @return true if any of the pruners think that this branch should be rejected, else false.
     */
    private boolean reject(ADT_Area area) {
        for (Strat_BT_PrunerItf pruner : pruners) {
            if (pruner.reject(area)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Computes if the given problem satisfies all requirements of a valid solution.
     * @param area the problem to be evaluated.
     * @return true if the given problem is solved, else false.
     */
    abstract boolean accept(ADT_Area area);

    /**
     * Gives the first branch which should be evaluated.
     * @param area the state of the problem prior to the branch we are generating.
     * @return the first branch to be evaluated
     */
    abstract ADT_Area first(ADT_Area area);

    /**
     * Gives the next branch to be evaluated.
     * @param area the state of the problem prior to the branch we are currently working on.
     * @param s    the previous problem which was evaluated.
     * @return the next branch to be evaluated.
     */
    abstract ADT_Area next(ADT_Area area, ADT_Area s);
}
