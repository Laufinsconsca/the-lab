package ru.ssau.tk.itenion.functions.powerFunctions;

import ru.ssau.tk.itenion.functions.AbstractMathFunction;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.ui.ConnectableItem;
import ru.ssau.tk.itenion.ui.Item;

@ConnectableItem(name = "Степенная функция", priority = 12, type = Item.FUNCTION, hasParameter = true)
public class PowFunction extends AbstractMathFunction implements MathFunction {
    private static final long serialVersionUID = -1379753316955199587L;
    protected final double pow;

    public PowFunction(double pow) {
        this.pow = pow;
        name = "x^" + pow;
    }

    @Override
    public double apply(double x) {
        return Math.pow(x, pow);
    }

    @Override
    public MathFunction differentiate() {
        return pow != 0. ? new PowFunction(pow - 1).multiply(pow) : new ZeroFunction();
    }

    public Number getPow() {
        return pow;
    }
}