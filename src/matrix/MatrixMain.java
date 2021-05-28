package matrix;

import utils.MatrixUtils;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Класс запуска первой лабораторной
 * по предмету "Вычислительная математика".
 */
@SuppressWarnings({"unused", "CheckStyle"})
public class MatrixMain {
    /**
     * Точка входа в программу.
     *
     * @param args аргументы командной строки
     */
    public static void main(final String[] args) {
        if (args.length < 1) {
            printHelp();
            return;
        }
        if (args.length == 1 && (args[0].equals("-help") || args[0].equals("-h"))) printHelp();
        String inputFilePath;
        String outputFilePath = null;
        inputFilePath = getAbsolutePath(args[0]);
        if (args.length > 1) outputFilePath = getAbsolutePath(args[1]);

        try {
            /*Matrix matrix = new Matrix();
            matrix.init(inputFilePath);

            matrix.print();

            int tmp = matrix.getIterationSolution(new double[matrix.getHeight()]) ? 0 : 1;
            if (tmp != 0) {
                System.out.println("Систему невозможно решить итерационным методом, применяется метод Гаусса...");
                System.out.println("Треугольная матрица:");
                tmp = matrix.createTriangleMatrix();
                System.out.println(matrix);

                if (tmp == 0) matrix.getGaussSolution();
                else System.out.println("Решений нет или бесконечно много!");
            }

            if (outputFilePath != null) MatrixUtils.printToFile(outputFilePath, matrix.getMatrix());*/

            Polynomial polynomial = new Polynomial(inputFilePath);
            Polynomial.print(polynomial.first);
            Polynomial.print(polynomial.second);
            Polynomial.print(Polynomial.mul(polynomial.first, polynomial.second));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
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
        System.out.println("""
                Usage:
                matrix -h[elp]
                matrix <input file path> [output file path]
                Structure of input file:
                * First number \t\t- height of matrix
                * Second number \t- length of matrix
                * Third number \t\t- quality of zero (for Gauss method)
                * Fourth number \t- maximum number of iterations (for Iterative method)
                """);
    }
}
