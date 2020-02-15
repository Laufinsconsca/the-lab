package ru.ssau.tk.sergunin.lab.functions.polynomial;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Polynomial {

    final static String EMPTY_STRING = "";
    final static String POSITIVE_SIGN = " + ";
    final static String NEGATIVE_SIGN = " - ";
    final static String DEGREE_MARK_1 = "x";
    final static String DEGREE_MARK_NTH = "x^";

    private final Map<Integer, Double> members
            = new TreeMap<>(Collections.reverseOrder());

    public Polynomial(Map<Integer, Double> members) {
        this.members.putAll(members);
        deleteMembersWithZeroCoefficient();
    }

    /**
     * Сложение
     */
    public Polynomial add(Polynomial other) {

        Map<Integer, Double> result = new TreeMap<>(members);

        for (Integer currentKey : other.members.keySet()) {
            Double resultValue = other.members.get(currentKey);
            Double currentValue = result.get(currentKey);
            if (currentValue != null) {
                resultValue += currentValue;
            }
            result.put(currentKey, resultValue);
        }
        return new Polynomial(result);
    }

    /**
     * Вычитание
     */
    public Polynomial subtract(Polynomial other) {
        Map<Integer, Double> result = new TreeMap<>(members);

        for (Integer currentKey : other.members.keySet()) {
            Double currentValue = result.get(currentKey);
            if (currentValue != null) {
                double difference = currentValue - other.members.get(currentKey);
                result.put(currentKey, difference);
            } else {
                result.put(currentKey, other.members.get(currentKey));
            }
        }
        return new Polynomial(result);
    }

    /**
     * Умножение
     */
    public Polynomial multiply(Polynomial other) {
        Map<Integer, Double> result = new TreeMap<>();

        for (Entry<Integer, Double> first : members.entrySet()) {
            for (Entry<Integer, Double> second : other.members.entrySet()) {
                Integer amountKey = first.getKey() + second.getKey();
                double productValue = first.getValue() * second.getValue();
                if (result.containsKey(amountKey)) {
                    productValue += result.get(amountKey);
                }
                result.put(amountKey, productValue);
            }
        }
        return new Polynomial(result);
    }


    public Map<Integer, Double> getMembers() {
        return members;
    }

    private void deleteMembersWithZeroCoefficient() {
        members.entrySet().removeIf(pair -> pair.getValue() == 0);
    }

    /**
     * Получение строкового представления знака одночлена в зависимости от знака
     * коэффициента и места размещения одночлена в многочлене. Если коэффициент
     * одночлена отрицательный - возвращается " - "; Если коэффициент одночлена
     * положительный и одночлен первый в многочлене - возвращается пустая
     * строка. Иначе - " + ";
     */
    private String viewSignMonomial(boolean isFirst, Double coefficient) {
        final int MIN_POSITIVE_COEFFICIENT = 0;
        if (coefficient < MIN_POSITIVE_COEFFICIENT) {
            return NEGATIVE_SIGN;
        } else {
            return (isFirst) ? EMPTY_STRING : POSITIVE_SIGN;
        }
    }

    /**
     * Получение строкового представления коэффициента одночлена в зависимости
     * от степени. Если коэффициент равен единице или степень нулевая -
     * возвращается пустая строка. Иначе - возвращается коэффициент.
     */
    private String viewCoefficient(double coefficient, int degree) {
        return ((coefficient != 1) || (degree == 0))
                ? String.valueOf(coefficient)
                : EMPTY_STRING;
    }

    /**
     * Получение строкового представления степени одночлена. Если степень
     * нулевая - возвращается пустая строка. Если степень 1 - возвращается "x".
     * Иначе - возвращается "x^" + степень.
     */
    private String viewDegree(int degree) {
        String result = EMPTY_STRING;
        if (degree != 0) {
            result = (degree == 1) ? DEGREE_MARK_1 : DEGREE_MARK_NTH + degree;
        }
        return result;
    }

    @Override
    public String toString() {
        boolean isFirstMember = true;
        StringBuilder builder = new StringBuilder();
        for (Entry<Integer, Double> current : members.entrySet()) {
            double currentCoefficient = current.getValue();
            int currentDegree = current.getKey();
            builder.append(viewSignMonomial(isFirstMember, currentCoefficient));
            builder.append(viewCoefficient(Math.abs(currentCoefficient),
                    currentDegree));
            builder.append(viewDegree(currentDegree));
            isFirstMember = false;
        }
        return builder.toString();
    }
}