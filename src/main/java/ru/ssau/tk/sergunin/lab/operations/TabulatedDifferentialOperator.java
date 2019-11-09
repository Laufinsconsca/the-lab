package ru.ssau.tk.sergunin.lab.operations;

import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;

public class TabulatedDifferentialOperator extends TabulatedOperator implements DifferentialOperator<TabulatedFunction> {

    public TabulatedDifferentialOperator() {
        super();
    }

    public TabulatedDifferentialOperator(TabulatedFunctionFactory factory) {
        super(factory);
    }

    protected TabulatedFunctionFactory getFactory() {
        return super.getFactory();
    }

    protected void setFactory(TabulatedFunctionFactory factory) {
        super.setFactory(factory);
    }

    @Override
    public TabulatedFunction derive(TabulatedFunction function) {
        return super.derive(function);
    }
}