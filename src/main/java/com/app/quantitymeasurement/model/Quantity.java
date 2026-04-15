package com.app.quantitymeasurement.model;

import java.util.function.DoubleBinaryOperator;
import com.app.quantitymeasurement.unit.IMeasurable;
import com.app.quantitymeasurement.unit.SupportsArithmetic;

/**
 * Immutable model representing a value with a unit.
 * Supports comparison, conversion, and arithmetic operations.
 */
public final class Quantity<U extends IMeasurable> {

    private final double value;
    private final U unit;

    private static final double EPSILON = 1e-6;
    private static final double ROUND_SCALE = 1e6;

    // ===== Arithmetic Enum =====
    private enum ArithmeticOperation {
        ADD((a, b) -> a + b),
        SUBTRACT((a, b) -> a - b),
        DIVIDE((a, b) -> {
            if (b == 0.0) throw new ArithmeticException("Division by zero");
            return a / b;
        });

        private final DoubleBinaryOperator operator;

        ArithmeticOperation(DoubleBinaryOperator operator) {
            this.operator = operator;
        }

        double compute(double a, double b) {
            return operator.applyAsDouble(a, b);
        }
    }

    // ===== Constructor =====
    public Quantity(double value, U unit) {
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
        if (!Double.isFinite(value)) throw new IllegalArgumentException("Invalid value");
        this.value = value;
        this.unit = unit;
    }

    // ===== Getters =====
    public double getValue() { return value; }
    public U getUnit() { return unit; }

    // ===== Equality =====
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quantity<?> other)) return false;
        if (unit.getClass() != other.unit.getClass()) return false;

        double base1 = unit.convertToBaseUnit(value);
        double base2 = other.unit.convertToBaseUnit(other.value);

        return Math.abs(base1 - base2) < EPSILON;
    }

    // ===== Conversion =====
    public Quantity<U> convertTo(U targetUnit) {
        validateTargetUnit(targetUnit);
        double base = unit.convertToBaseUnit(value);
        return new Quantity<>(targetUnit.convertFromBaseUnit(base), targetUnit);
    }

    // ===== Arithmetic =====
    public Quantity<U> add(Quantity<? extends IMeasurable> other) {
        validateQuantity(other);
        return new Quantity<>(round(unit.convertFromBaseUnit(
                performArithmetic(other, ArithmeticOperation.ADD))), unit);
    }

    public Quantity<U> add(Quantity<? extends IMeasurable> other, U targetUnit) {
        validateQuantity(other);
        validateTargetUnit(targetUnit);
        validateArithmeticSupport(targetUnit);

        return new Quantity<>(round(targetUnit.convertFromBaseUnit(
                performArithmetic(other, ArithmeticOperation.ADD))), targetUnit);
    }

    public Quantity<U> subtract(Quantity<? extends IMeasurable> other) {
        validateQuantity(other);
        return new Quantity<>(round(unit.convertFromBaseUnit(
                performArithmetic(other, ArithmeticOperation.SUBTRACT))), unit);
    }

    public Quantity<U> subtract(Quantity<? extends IMeasurable> other, U targetUnit) {
        validateQuantity(other);
        validateTargetUnit(targetUnit);
        validateArithmeticSupport(targetUnit);

        return new Quantity<>(round(targetUnit.convertFromBaseUnit(
                performArithmetic(other, ArithmeticOperation.SUBTRACT))), targetUnit);
    }

    public double divide(Quantity<? extends IMeasurable> other) {
        validateQuantity(other);
        return performArithmetic(other, ArithmeticOperation.DIVIDE);
    }

    // ===== Utility =====
    @Override
    public int hashCode() {
        long normalized = Math.round(unit.convertToBaseUnit(value) / EPSILON);
        return Long.hashCode(normalized);
    }

    @Override
    public String toString() {
        return value + " " + unit.getUnitName();
    }

    // ===== Validation =====
    private void validateQuantity(Quantity<? extends IMeasurable> other) {
        if (other == null) throw new IllegalArgumentException("Other cannot be null");
        if (unit.getClass() != other.getUnit().getClass())
            throw new IllegalArgumentException("Different measurement types");
    }

    private void validateTargetUnit(IMeasurable targetUnit) {
        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");
        if (targetUnit.getClass() != unit.getClass())
            throw new IllegalArgumentException("Invalid target unit");
    }

    private void validateArithmeticSupport(IMeasurable unit) {
        if (!(unit instanceof SupportsArithmetic))
            throw new UnsupportedOperationException("Arithmetic not supported");
    }

    private double performArithmetic(Quantity<? extends IMeasurable> other,
                                     ArithmeticOperation op) {

        validateArithmeticSupport(unit);
        validateArithmeticSupport(other.getUnit());

        double base1 = unit.convertToBaseUnit(value);
        double base2 = other.getUnit().convertToBaseUnit(other.getValue());

        return op.compute(base1, base2);
    }

    private double round(double v) {
        return Math.round(v * ROUND_SCALE) / ROUND_SCALE;
    }
}