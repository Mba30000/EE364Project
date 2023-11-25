package com.ee364project.helpers;

public class Pair<AT, BT> {
    private AT A;
    private BT B;

    public Pair(AT A, BT B) {
        this.A = A;
        this.B = B;
    }

    public AT getA() {
        return this.A;
    }

    public BT getB() {
        return this.B;
    }
}
