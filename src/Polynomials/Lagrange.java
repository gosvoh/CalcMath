package Polynomials;

public class Lagrange extends InterpolationPolynomials {
    public Lagrange(Grid grid) {
        polynomial = new Polynomial();
        double coefficient;
        Polynomial working, parentheses = new Polynomial(1, 1);
        for (int i = 0; i < grid.y.length; i++) {
            coefficient = grid.y[i];
            working = new Polynomial();
            for (int j = 0; j < grid.x.length; j++) {
                if (i == j) continue;
                coefficient /= grid.x[i] - grid.x[j];
                parentheses.changeX(-grid.x[j]);
                if (working.isEmpty()) working.sum(parentheses);
                else working.mul(parentheses);
            }
            working.mul(coefficient);
            polynomial.sum(working);
        }
    }
}
