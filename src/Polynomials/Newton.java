package Polynomials;

public class Newton extends InterpolationPolynomials {
    public Newton(Grid grid) {
        polynomial = new Polynomial();
        int size = grid.y.length;
        double[] prev_difference = new double[size], difference = new double[size];
        double[] temp, coefficients = new double[size];

        //region Посчитать коэффициенты и разности
        System.arraycopy(grid.y, 0, prev_difference, 0, size);
        coefficients[0] = prev_difference[0];
        for (int i = size - 1; i > 0; i--) {
            for (int j = 0; j < i; j++)
                difference[j] = (prev_difference[j] - prev_difference[j + 1]) / (grid.x[j] - grid.x[j + size - i]);
            temp = prev_difference;
            prev_difference = difference;
            difference = temp;

            coefficients[size - i] = prev_difference[0];
        }
        //endregion

        //region Решение полинома
        Polynomial parentheses, working1, working2;
        parentheses = new Polynomial(coefficients[0], 0);
        working1 = new Polynomial();
        polynomial.sum(parentheses);
        parentheses.sum(new Polynomial(1, 1));
        for (int i = 1; i < size; i++) {
            parentheses.changeX(-grid.x[i - 1]);
            if (working1.isEmpty()) working1.sum(parentheses);
            else working1.mul(parentheses);
            working2 = new Polynomial(working1);
            working2.mul(coefficients[i]);
            polynomial.sum(working2);
        }
        //endregion
    }
}
