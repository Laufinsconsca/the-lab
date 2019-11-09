package ru.ssau.tk.sergunin.lab.functions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import ru.ssau.tk.sergunin.lab.exceptions.InterpolationException;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayTabulatedFunction extends AbstractTabulatedFunction implements Insertable, Removable, Serializable {
    private static final long serialVersionUID = 3990511369369675738L;
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private double[] xValues;
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private double[] yValues;
    private int count;

    @JsonCreator
    public ArrayTabulatedFunction(@JsonProperty(value = "xValues") double[] xValues, @JsonProperty(value = "yValues") double[] yValues) {
        checkLengthIsTheSame(xValues, yValues);
        checkSorted(xValues);
        if (xValues.length < 2) {
            throw new IllegalArgumentException("Array less than minimum length");
        }
        this.count = xValues.length;
        this.xValues = Arrays.copyOf(xValues, count);
        this.yValues = Arrays.copyOf(yValues, count);
    }

    public ArrayTabulatedFunction(MathFunction source, double xFrom, double xTo, int count) {
        if (count < 2) {
            throw new IllegalArgumentException("Count less than minimum length");
        }
        this.count = count;
        if (xFrom > xTo) {
            xFrom = xFrom - xTo;
            xTo = xFrom + xTo;
            xFrom = -xFrom + xTo;
        }
        xValues = new double[count];
        yValues = new double[count];
        double buff = xFrom;
        double step = (xTo - xFrom) / (count - 1);
        for (int i = 0; i < count; i++) {
            xValues[i] = buff;
            yValues[i] = source.apply(buff);
            buff += step;
        }
    }

    @Override
    protected int floorIndexOfX(double x) {
        if (x < xValues[0]) {
            throw new IllegalArgumentException("Argument x less than minimal x in tabulated function");
        }
        for (int i = 0; i < count; i++) {
            if (xValues[i] > x) {
                return i - 1;
            }
        }
        return count;
    }

    @Override
    protected double extrapolateLeft(double x) {
        return interpolate(x, xValues[0], xValues[1], yValues[0], yValues[1]);
    }

    @Override
    protected double extrapolateRight(double x) {
        return interpolate(x, xValues[count - 2], xValues[count - 1], yValues[count - 2], yValues[count - 1]);
    }

    @Override
    protected double interpolate(double x, int floorIndex) {
        if (x < xValues[floorIndex] || xValues[floorIndex + 1] < x) {
            throw new InterpolationException();
        }
        return interpolate(x, xValues[floorIndex], xValues[floorIndex + 1], yValues[floorIndex], yValues[floorIndex + 1]);
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public double getX(int index) throws ArrayIndexOutOfBoundsException {
        return xValues[index];
    }

    @Override
    public double getY(int index) throws ArrayIndexOutOfBoundsException {
        return yValues[index];
    }

    @Override
    public void setY(int index, double value) throws ArrayIndexOutOfBoundsException {
        yValues[index] = value;
    }

    @Override
    public int indexOfX(double x) {
        int i;
        for (i = 0; i < count; i++) {
            if (xValues[i] == x) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int indexOfY(double y) {
        int i;
        for (i = 0; i < count; i++) {
            if (yValues[i] == y) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public double leftBound() {
        return xValues[0];
    }

    @Override
    public double rightBound() {
        return xValues[count - 1];
    }

    @Override
    public void insert(double x, double y) {
        int i = indexOfX(x);
        if (i != -1) {
            setY(i, y);
        } else {
            int index = x < xValues[0] ? 0 : floorIndexOfX(x);
            double[] xTmp = new double[count + 1];
            double[] yTmp = new double[count + 1];
            if (index == 0) {
                xTmp[0] = x;
                yTmp[0] = y;
                System.arraycopy(xValues, 0, xTmp, 1, count);
                System.arraycopy(yValues, 0, yTmp, 1, count);
                count++;
            } else if (index == count) {
                System.arraycopy(xValues, 0, xTmp, 0, count);
                System.arraycopy(yValues, 0, yTmp, 0, count);
                xTmp[count] = x;
                yTmp[count] = y;
                count++;
            } else {
                System.arraycopy(xValues, 0, xTmp, 0, index + 1);
                System.arraycopy(yValues, 0, yTmp, 0, index + 1);
                xTmp[index + 1] = x;
                yTmp[index + 1] = y;
                System.arraycopy(xValues, index + 1, xTmp, index + 2, (count - index - 1));
                System.arraycopy(yValues, index + 1, yTmp, index + 2, (count - index - 1));
                count++;
            }
            this.xValues = xTmp;
            this.yValues = yTmp;
        }
    }

    @Override
    public void remove(int index) throws ArrayIndexOutOfBoundsException {
        if (index < 0 || index >= count) {
            throw new ArrayIndexOutOfBoundsException("Index is out of bounds");
        }
        double[] xTmp = new double[count - 1];
        double[] yTmp = new double[count - 1];
        if (index == 0) {
            System.arraycopy(xValues, 1, xTmp, 0, count - 1);
            System.arraycopy(yValues, 1, yTmp, 0, count - 1);
        } else if (index == (count - 1)) {
            System.arraycopy(xValues, 0, xTmp, 0, count - 1);
            System.arraycopy(yValues, 0, yTmp, 0, count - 1);
        } else {
            System.arraycopy(xValues, 0, xTmp, 0, index);
            System.arraycopy(yValues, 0, yTmp, 0, index);
            System.arraycopy(xValues, index + 1, xTmp, index, (count - index - 1));
            System.arraycopy(yValues, index + 1, yTmp, index, (count - index - 1));
        }
        count--;
        this.xValues = xTmp;
        this.yValues = yTmp;
    }

    @Override
    @NotNull
    public Iterator<Point> iterator() {
        return new Iterator<>() {
            int i = 0;

            public boolean hasNext() {
                return i != count;
            }

            public Point next() {
                if (i == count) {
                    throw new NoSuchElementException();
                }
                return new Point(xValues[i], yValues[i++]);
            }
        };
    }
}