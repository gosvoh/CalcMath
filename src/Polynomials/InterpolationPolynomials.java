package Polynomials;

public abstract class InterpolationPolynomials {
    Polynomial polynomial;

    public double getPolynomialValue(double value) {
        return polynomial.horner(value);
    }

    public void print() {
        polynomial.print();
    }
}
