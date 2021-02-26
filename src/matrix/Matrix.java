package matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

/** Класс матрицы. */
@SuppressWarnings({"FieldCanBeLocal", "CheckStyle"})
public class Matrix {
    /** Сама матрица для отображения. */
    private double[][] matrix = null;
    /** Размерность матрицы, высота. */
    private int        n;
    /** Размерность матрицы, длина. */
    private int        m;

    /**
     * Конструктор объекта матрицы.
     *
     * @param inputFilePath путь к файлу с матрицей
     *
     * @throws FileNotFoundException выбрасывается в том случае,
     *                               если файл не был найден
     */
    public Matrix(final String inputFilePath) throws FileNotFoundException {
        this.init(inputFilePath);
    }

    /**
     * Метод инициализации элементов матрицы.
     *
     * @param n высота матрицы
     * @param m длина матрицы
     */
    public void create(final int n, final int m) {
        matrix = new double[n][];
        for (int i = 0; i < n; i++) matrix[i] = new double[m];
    }

    /** Вывести матрицу на экран. */
    public void print() {
        for (double[] doubles : matrix) {
            for (double aDouble : doubles) System.out.printf("%15.6E", aDouble);
            System.out.println();
        }
    }

    /**
     * Заполнение матрицы из файла.
     * Сначала считываем первую строку, в которой содержатся параметры
     * матрицы (высота и длина), потом считываем каждую последующую строку,
     * разделяем её на элементы и вносим в массив.
     *
     * @param path путь к файлу с матрицей
     *
     * @throws FileNotFoundException выбрасывается в том случае,
     *                               если файл не был найден
     */
    public void init(final String path) throws FileNotFoundException {
        File inFile = new File(path);
        Scanner scanner = new Scanner(inFile);
        Pattern pattern = Pattern.compile("[ \t]+");
        String string = scanner.nextLine();
        String[] strings = pattern.split(string);
        n = Integer.parseInt(strings[0]);
        m = Integer.parseInt(strings[1]);
        create(n, m);
        for (int i = 0; i < n; i++) {
            string = scanner.nextLine().trim();
            strings = pattern.split(string);
            try {
                for (int j = 0; j < m; j++) matrix[i][j] = Double.parseDouble(strings[j].replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
        }
        scanner.close();
    }

    /** Метод сортировки, путём простой вставки. */
    public void simpleInsertSort() {
        double[] tmpLine;
        double tmpSum;
        double[] lineSums = new double[m];

        matrix = transposeMatrix(matrix);

        for (int i = 0; i < m; i++) {
            lineSums[i] = getLineSum(i);
            if (i == 0) continue;
            tmpLine = matrix[i];
            tmpSum = lineSums[i];

            int j = i;
            while (j > 0 && tmpSum < lineSums[j - 1]) {
                matrix[j] = matrix[j - 1];
                lineSums[j] = lineSums[j - 1];
                j--;
            }
            matrix[j] = tmpLine;
            lineSums[j] = tmpSum;
        }

        matrix = transposeMatrix(matrix);
    }

    /**
     * Получить сумму элементов указанной строки.
     *
     * @param lineNum номер строки для подсчёта суммы
     *
     * @return сумма элементов строки
     */
    private double getLineSum(final int lineNum) {
        double ret = 0;
        for (int i = 0; i < matrix[lineNum].length; i++) ret += matrix[lineNum][i];
        return ret;
    }

    /**
     * Транспонирование матрицы.
     *
     * @param matrix исходная матрица
     *
     * @return транспонированная матрица
     */
    public double[][] transposeMatrix(final double[][] matrix) {
        int n = matrix.length;
        int m = matrix[0].length;
        boolean isSquare = n == m;
        double[][] newMatrix = new double[m][];
        for (int i = 0; i < m; i++) {
            newMatrix[i] = new double[n];
            for (int j = 0; j < n; j++) {
                if (isSquare || (i == 0 && j == 0)) continue;
                newMatrix[i][j] = matrix[j][i];
            }
        }
        return newMatrix;
    }

    /**
     * Вывести матрицу в файл.
     *
     * @param outputFilePath путь до файла для вывода матрицы
     *
     * @throws IOException выбрасывается в том случае,
     *                     если нет доступа к записи в указанный файл
     */
    public void printToFile(final String outputFilePath) throws IOException {
        File outputFile = new File(outputFilePath);
        FileWriter fileWriter = new FileWriter(outputFile);
        fileWriter.write(matrix.length + " " + matrix[0].length + "\n");
        for (double[] doubles : matrix) {
            for (double aDouble : doubles) fileWriter.write(String.format("%15.6E", aDouble).replace(',', '.'));
            fileWriter.write("\n");
        }
        fileWriter.close();
    }
}
