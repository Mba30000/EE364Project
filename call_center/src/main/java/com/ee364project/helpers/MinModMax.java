package com.ee364project.helpers;

public class MinModMax {
    private Triplet<Double, Double, Double> vals;
    public MinModMax(double min, double mod, double max) {
        this.vals = new Triplet<Double, Double, Double>(min, mod, max);
    }

    public double getMin() {
        return vals.getA();
    }

    public double getMod() {
        return vals.getB();
    }

    public double getMax() {
        return vals.getC();
    }
}
