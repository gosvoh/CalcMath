import matrix.GaussSolution;
import matrix.Matrix;
import matrix.SeidelSolution;

import java.io.FileNotFoundException;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            printHelp();
            return;
        }
        if (args.length == 1 && (args[0].equals("-help") || args[0].equals("-h"))) printHelp();
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
        System.out.println("Usage:\n" + "matrix -h[elp]\n" + "matrix <input file path>\n" + "Structure of input file:\n" + "* First number \t\t- height of matrix\n" + "* Second number \t- length of matrix\n" + "* Third number \t\t- quality of zero (for Gauss method)\n" + "* Fourth number \t- maximum number of iterations (for Iterative method)\n");
    }
}
