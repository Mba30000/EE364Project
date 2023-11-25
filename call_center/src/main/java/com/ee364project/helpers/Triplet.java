package com.ee364project.helpers;

public class Triplet<AT, BT, CT> extends Pair<AT, BT> {
    private CT C;

    public Triplet(AT A, BT B, CT C) {
        super(A, B);
        this.C = C;
    }

    public CT getC() {
        return this.C;
    }
}
