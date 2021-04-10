package matrix;

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
            System.out.println("Usage: matrix <input file path> [output file path]");
            return;
        }
        String inputFilePath;
        String outputFilePath = null;
        inputFilePath = getAbsolutePath(args[0]);
        if (args.length > 1) outputFilePath = getAbsolutePath(args[1]);
        try {
            Matrix matrix = new Matrix();
            matrix.init(inputFilePath);

            matrix.print();
            System.out.println();

            int tmp = matrix.getIterationSolution(new double[matrix.getmHeight()]) ? 0 : 1;
            if (tmp != 0) tmp = matrix.createTriangleMatrix();
            matrix.print();
            System.out.println();

            if (tmp == 0) matrix.getGaussSolution();
            else System.out.println("Решений нет или бесконечно много!");


            if (outputFilePath != null) matrix.printToFile(outputFilePath);
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
}
