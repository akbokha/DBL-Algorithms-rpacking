import java.util.ArrayList;
import java.util.Collection;

/**
 * A template for a backtrack implementation, it uses a composite setup for the pruners.
 */
abstract public class Strategy_Backtrack_Template {

    private Collection<Strategy_Backtrack_PrunerItf> pruners;

    public Strategy_Backtrack_Template() {
        pruners = new ArrayList<>(5);
    }

    /**
     * Adds a pruner to the list of used pruners during computation.
     * @param pruner the pruner to be added.
     */
    void addPruner(Strategy_Backtrack_PrunerItf pruner) {
        pruners.add(pruner);
    }

    /**
     * Attempts to compute a valid solution.
     * @param area the problem to be solved
     * @return a valid solution if it exists, else null.
     */
    ADT_Area compute(ADT_Area area) {
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
            ADT_Area result = compute(s);
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
        for (Strategy_Backtrack_PrunerItf pruner : pruners) {
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
