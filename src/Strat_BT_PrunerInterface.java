interface Strat_BT_PrunerInterface {

    /**
     * Computes if the given problem can be evolved into a valid solution.
     *
     * @param area the problem to be evaluated.
     * @param last the rectangle added last.
     * @return true if this problem cannot evolve further into a valid solution.
     */
    boolean reject(ADT_AreaExtended area, ADT_Rectangle last);

}
