package ru.ssau.tk.sergunin.lab.functions;

public interface TabulatedFunction extends MathFunction {
    int getCount();

    double getX(int index) throws Throwable;

    double getY(int index) throws Throwable;

    void setY(int index, double value) throws Throwable;

    int indexOfX(double x);

    int indexOfY(double y);

    double leftBound();

    double rightBound();
}
