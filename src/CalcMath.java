import Polynomials.Grid;
import Polynomials.Lagrange;
import Polynomials.Newton;
import Polynomials.Polynomial;
import matrix.GaussSolution;
import matrix.Matrix;
import matrix.SeidelSolution;
import utils.MatrixUtils;

import java.io.FileNotFoundException;
import java.util.regex.Pattern;

public class CalcMath {
    public static void main(String[] args) {
        if (args.length < 1) {
            printHelp();
            return;
        }
        if (args.length == 1 && (args[0].equals("-help") || args[0].equals("-h"))) printHelp();

        if (args[0].equals("pol")) runPolynomialMethods();
        else runMatrixMethods(args);

    }

    /**
     * Получить абсолютный путь к указанному файлу или директории в виде строки.
     * Если путь начинается с '/' или с '\', то он считается абсолютным и
     * возвращается неизменным, в противном случае он считается относительным
     * для текущей рабочей директории.
     *
     * @param path путь для парса
     *
     * @return абсолютный путь
     */
    private static String getAbsolutePath(String path) {
        path = path.contains("\\") ? path.replaceAll("\\\\", "/") : path;
        if (path.startsWith("/") || Pattern.matches("[a-zA-Z]:", path.substring(0, 2))) return path;
        else if (path.startsWith("~")) {
            String homePath = System.getProperty("user.home");
            return homePath.endsWith("/") ? homePath + path.substring(1) : homePath + "/" + path.substring(1);
        } else {
            String workingDirectory = System.getProperty("user.dir");
            return workingDirectory.endsWith("/") ? workingDirectory + path : workingDirectory + "/" + path;
        }
    }

    private static void printHelp() {
        System.out.println("Usage:\n" + "CalcMath -h[elp]\t- print this message\n" + "CalcMath pol\t\t- run polynomial methods" + "CalcMath <input file path>\n" + "Structure of input file:\n" + "* First number \t\t- height of matrix\n" + "* Second number \t- length of matrix\n" + "* Third number \t\t- quality of zero (for Gauss method)\n" + "* Fourth number \t- maximum number of iterations (for Iterative method)\n");
    }

    private static void runMatrixMethods(String[] args) {
        String inputFilePath = getAbsolutePath(args[0]);
        Matrix matrix;
        try {
            matrix = new Matrix();
            matrix.init(inputFilePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            matrix = null;
        }

        if (matrix == null) return;
        System.out.println("Начальная матрица:");
        System.out.println(matrix);

        System.out.println("Итерационный метод:");
        SeidelSolution seidelSolution = new SeidelSolution(matrix);
        int tmp = seidelSolution.getIterationSolution(new double[matrix.getHeight()]) ? 0 : 1;
        if (tmp != 0) System.out.println("Систему невозможно решить итерационным методом");

        System.out.println("Метод Гаусса:");
        GaussSolution gauss = new GaussSolution(matrix);
        System.out.println("Треугольная матрица:");
        tmp = gauss.createTriangleMatrix();
        System.out.println(gauss);
        if (tmp == 0) gauss.getGaussSolution();
        else System.out.println("Решений нет или бесконечно много!");
    }

    private static void runPolynomialMethods() {
        Grid grid = new Grid(1, 16, 7);
        Lagrange lagrange = new Lagrange(grid);
        System.out.print("X: ");
        MatrixUtils.print(grid.x);
        System.out.print("Y: ");
        MatrixUtils.print(grid.y);
        System.out.println("---------------------------------------------");
        lagrange.print();
        print(grid, lagrange);
        System.out.println(lagrange.getPolynomialValue(11));
        System.out.println("---------------------------------------------");
        Newton newton = new Newton(grid);
        newton.print();
        print(grid, newton);
        /*Polynomial pol = new Polynomial(1, 1);
        pol.sum(new Polynomial(1, 0));
        Polynomial pol1 = new Polynomial(1, 1);
        pol1.sum(new Polynomial(1, 0));
        pol.mul(pol1);
        pol.print();*/
    }

    private static void print(Grid grid, Polynomials.InterpolationPolynomials polynomial) {
        double half = (grid.x[1] - grid.x[0]) / 2;
        System.out.printf("%11s\t\t%13s\t%13s\n", "X", "Y(X)", "P(X)");
        int i, j;
        double d1, d2, d3;
        for (i = 0, j = 0; i < grid.y.length - 1; i++) {
            System.out.printf("%2d) %13.6E\t%13.6E\t%13.6E\n", j++, grid.x[i], grid.y[i], polynomial.getPolynomialValue(grid.x[i]));
            d1 = Grid.function(grid.x[i] + half);
            d2 = polynomial.getPolynomialValue(grid.x[i] + half);
            d3 = Math.abs(Math.abs(d1) - Math.abs(d2));
            System.out.printf("%2d) %13.6E\t%13.6E\t%13.6E %s\n", j++, grid.x[i] + half, d1, d2, d3 <= Polynomial.quality ? "" : String.format("(Error: %.6E)", d3));
        }
        System.out.printf("%2d) %13.6E\t%13.6E\t%13.6E\n\n", j, grid.x[i], grid.y[i], polynomial.getPolynomialValue(grid.x[i]));
    }
}
