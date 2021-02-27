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
            Matrix matrix = new Matrix(inputFilePath);
            matrix.print();
            System.out.println();
            matrix.simpleInsertSort();
            matrix.print();
            System.out.println();
            if (outputFilePath != null) matrix.printToFile(outputFilePath);
        } catch (IOException e) {
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
        path = path.replaceAll("[\"]+", "").replaceAll("/", "\\");
        if (path.startsWith("/") || path.startsWith("\\") || Pattern.matches("[a-zA-Z]:", path.substring(0, 2)))
            return path;
        else if (path.startsWith("~")) return System.getProperty("user.home") + path.substring(1);
        else return System.getProperty("user.dir") + "\\" + path;
    }
}