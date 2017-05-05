interface Strat_BT_PrunerItf {

    /**
     * Computes if the given problem can be evolved into a valid solution.
     *
     * @param area the problem to be evaluated.
     * @return true if this problem cannot evolve further into a valid solution.
     */
    boolean reject(ADT_Area area);

}
