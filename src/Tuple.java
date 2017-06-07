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
    int compTime;
    float expectedFR;

    public Tuple(int depth, float frWhenCalled, int compTime, float expectedFR) {
        this.depth = depth;
        this.frWhenCalled = frWhenCalled;
        this.compTime = compTime;
        this.expectedFR = expectedFR;
    }
}
