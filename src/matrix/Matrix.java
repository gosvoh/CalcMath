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
    private double[][] matrix;
    /** Размерность матрицы, высота. */
    private int        n;
    /** Размерность матрицы, длина. */
    private int        m;
    private double     quality;

    /**
     * Конструктор объекта матрицы.
     */
    public Matrix() {
        this.matrix = null;
        this.n = 0;
        this.m = 0;
    }

    /**
     * Вывести указанную матрицу на экран.
     *
     * @param matrix матрица для вывода
     */
    public static void print(double[][] matrix) {
        for (double[] doubles : matrix) {
            for (double aDouble : doubles) System.out.printf("%15.6E", aDouble);
            System.out.println();
        }
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

    /**
     * Вывести указанную матрицу на экран.
     */
    public void print() {
        print(matrix);
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
        quality = Double.parseDouble(strings[2]);
        if (n + 1 != m) throw new IllegalArgumentException("Неполное условие, система не может быть решена!");
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
        if (n == m) {
            transposeMatrix();
            sort(matrix);
            transposeMatrix();
        } else {
            double[][] transposedMatrix = new double[m][n];
            transposeMatrix(matrix, transposedMatrix);
            sort(transposedMatrix);
            transposeMatrix(transposedMatrix, matrix);
        }
    }

    /**
     * Метод, где происходит реальная сортировка
     *
     * @param matrix матрица, которую нужно отсортировать
     */
    private void sort(double[][] matrix) {
        double[] tmpLine;
        double tmpSum;
        double[] lineSums = getLinesSums(matrix);

        for (int i = 1; i < matrix.length; i++) {
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
    }

    /**
     * Получить сумму элементов указанной строки.
     *
     * @param matrix матрица для подсчёта сумм столбцов
     *
     * @return массив сумм элементов строки
     */
    private double[] getLinesSums(final double[][] matrix) {
        double[] ret = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) for (int j = 0; j < matrix[i].length; j++) ret[i] += matrix[i][j];
        return ret;
    }

    /** Транспонирование квадратной матрицы */
    private void transposeMatrix() {
        for (int i = 0; i < matrix.length; i++)
            for (int j = i; j < matrix.length; j++) {
                double tmp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = tmp;
            }
    }

    /**
     * Транспонирование не квадратной матрицы матрицы.
     *
     * @param matrix           исходная матрица
     * @param transposedMatrix транспонированная матрица
     */
    private void transposeMatrix(double[][] matrix, double[][] transposedMatrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                transposedMatrix[j][i] = matrix[i][j];
            }
        }
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

    private void createTriangleMatrix() {
        for (int k = 0; k < matrix.length; k++) {
            double toDel = matrix[k][k];

            if (toDel < quality) {
                for (int i = k; i < matrix.length; i++)
                    if (matrix[i][k] < quality) {
                        matrix[i][k] = 0;
                        swapLines(k, i);
                        break;
                    }
                throw new IllegalArgumentException("Матрица вырожденная, невозможно получить решение!");
            }

            // Создание единицы в элементе с индексом [k][k]
            for (int i = k; i < matrix[k].length; i++) matrix[k][i] /= toDel;

            for (int i = k + 1; i < matrix.length; i++) {
                toDel = matrix[i][k];
                for (int j = 0; j < matrix[i].length; j++) matrix[i][j] -= matrix[k][j] * toDel;
            }
        }
    }

    public void getGaussSolution() {
        double[] ret = new double[matrix.length];

        createTriangleMatrix();

        for (int i = ret.length - 1; i >= 0; i--) {
            ret[i] = matrix[i][matrix[i].length - 1];
            for (int j = 0; j < ret.length; j++) if (i != j) ret[i] -= matrix[i][j] * ret[j];
        }

        for (double v : ret) System.out.printf("%15.6E", v);
    }

    private void swapLines(int line1, int line2) {
        double[] tmpLine = matrix[line1];
        matrix[line1] = matrix[line2];
        matrix[line2] = tmpLine;
    }
}
