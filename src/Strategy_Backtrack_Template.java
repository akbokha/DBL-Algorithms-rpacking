abstract public class Strategy_Backtrack_Template {

    /**
     * Attempts to compute a valid solution.
     *
     * @param area the problem to be solved
     * @return a valid solution if it exists, else null.
     */
    private ADT_Area compute(ADT_Area area) {
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
     * Computes if the given problem can be evolved into a valid solution.
     *
     * @param area the problem to be evaluated.
     * @return true if this problem cannot evolve further into a valid solution.
     */
    abstract boolean reject(ADT_Area area);

    /**
     * Computes if the given problem satisfies all requirements of a valid solution.
     *
     * @param area the problem to be evaluated.
     * @return true if the given problem is solved, else false.
     */
    abstract boolean accept(ADT_Area area);

    /**
     * Gives the first branch which should be evaluated.
     *
     * @param area the state of the problem prior to the branch we are generating.
     * @return the first branch to be evaluated
     */
    abstract ADT_Area first(ADT_Area area);

    /**
     * Gives the next branch to be evaluated.
     *
     * @param area the state of the problem prior to the branch we are currently working on.
     * @param s    the previous problem which was evaluated.
     * @return the next branch to be evaluated.
     */
    abstract ADT_Area next(ADT_Area area, ADT_Area s);
}
