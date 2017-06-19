/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author s147889
 */
public class Tuple {
    int depth;
    float frWhenCalled;
    long compTime;
    float expectedFR;
    int endTimePruner;
    boolean pruned;

    public Tuple(int depth, float frWhenCalled, long compTime, float expectedFR) {
        this.depth = depth;
        this.frWhenCalled = frWhenCalled;
        this.compTime = compTime;
        this.expectedFR = expectedFR;
    }

    Tuple(int depth, float frWhenCalled, long compTime, float expectedFR, int endTimePruner, boolean pruned) {
        this.depth = depth;
        this.frWhenCalled = frWhenCalled;
        this.compTime = compTime;
        this.expectedFR = expectedFR;
        this.endTimePruner = endTimePruner;
        this.pruned = pruned;
    }
}
