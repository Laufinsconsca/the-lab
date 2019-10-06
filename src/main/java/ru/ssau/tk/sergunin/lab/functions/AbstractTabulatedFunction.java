package ru.ssau.tk.sergunin.lab.functions;

import ru.ssau.tk.sergunin.lab.exceptions.ArrayIsNotSortedException;
import ru.ssau.tk.sergunin.lab.exceptions.DifferentLengthOfArraysException;

public abstract class AbstractTabulatedFunction implements TabulatedFunction {

    protected static void checkLengthIsTheSame(double[] xValues, double[] yValues) {
        if (xValues.length != yValues.length) {
            throw new DifferentLengthOfArraysException();
        }
    }

    protected static void checkSorted(double[] xValues) {
        for (int i = 1; i < xValues.length; i++) {
            if (xValues[i] < xValues[i - 1]) {
                throw new ArrayIsNotSortedException();
            }
        }
    }

    protected abstract int floorIndexOfX(double x) throws IllegalArgumentException;

    protected abstract double extrapolateLeft(double x);

    protected abstract double extrapolateRight(double x);

    protected abstract double interpolate(double x, int floorIndex);

    protected double interpolate(double x, double leftX, double rightX, double leftY, double rightY) {
        return leftY + (rightY - leftY) * (x - leftX) / (rightX - leftX);
    }

    @Override
    public double apply(double x) {
        if (x < leftBound()) {
            return extrapolateLeft(x);
        } else if (x > rightBound()) {
            return extrapolateRight(x);
        } else if (indexOfX(x) != -1) {
            return getY(indexOfX(x));
        } else {
            return interpolate(x, floorIndexOfX(x));
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append(getClass().getSimpleName());
        //for (StringBuilder buff : this){
        string.append(getClass().getSimpleName());
        // }
        return StringBuilder.class.toString();
    }
}
