package matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Класс матрицы.
 */
@SuppressWarnings({"FieldCanBeLocal", "CheckStyle"})
public class Matrix {
    /**
     * Сама матрица для отображения.
     */
    private double[][] matrix;
    /**
     * Размерность матрицы, высота.
     */
    private int sizeY;
    /**
     * Размерность матрицы, длина.
     */
    private int sizeX;
    /**
     * Условный ноль.
     */
    private double quality;

    /**
     * Конструктор объекта матрицы.
     */
    public Matrix() {
        this.matrix = null;
        this.sizeY = 0;
        this.sizeX = 0;
    }

    /**
     * Вывести указанную матрицу на экран.
     *
     * @param matrix матрица для вывода
     */
    public static void print(final double[][] matrix) {
        for (double[] doubles : matrix) {
            for (double aDouble : doubles) System.out.printf("%15.6E", aDouble);
            System.out.println();
        }
    }

    /**
     * Получить сумму элементов указанной строки.
     *
     * @param matrix матрица для подсчёта сумм столбцов
     * @return массив сумм элементов строки
     */
    private static double[] getLinesSums(final double[][] matrix) {
        double[] ret = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length; j++) ret[i] += matrix[i][j];
        return ret;
    }

    /**
     * Получить сумму элементов указанной строки без последнего элемента.
     *
     * @param matrix матрица для подсчёта сумм столбцов
     * @return массив сумм элементов строки
     */
    private static double[] getLinesSumsWithoutLast(final double[][] matrix) {
        double[] ret = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length - 1; j++) ret[i] += matrix[i][j];
        return ret;
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
     * Вывести матрицу на экран.
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
     * @throws FileNotFoundException    выбрасывается в том случае,
     *                                  если файл не был найден
     * @throws IllegalArgumentException если количество строк не совпадает с
     *                                  количеством неизвестных переменных
     */
    public void init(final String path) throws FileNotFoundException {
        File inFile = new File(path);
        Scanner scanner = new Scanner(inFile);
        Pattern pattern = Pattern.compile("[ \t]+");
        String string = scanner.nextLine();
        String[] strings = pattern.split(string);
        sizeY = Integer.parseInt(strings[0]);
        sizeX = Integer.parseInt(strings[1]);
        quality = Double.parseDouble(strings[2]);
        if (sizeY + 1 != sizeX) throw new IllegalArgumentException("Неполное условие, система не может быть решена!");
        create(sizeY, sizeX);
        for (int i = 0; i < sizeY; i++) {
            string = scanner.nextLine().trim();
            strings = pattern.split(string);
            try {
                for (int j = 0; j < sizeX; j++) matrix[i][j] = Double.parseDouble(strings[j].replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
        }
        scanner.close();
    }

    /**
     * Метод сортировки, путём простой вставки.
     */
    @SuppressWarnings("unused")
    public void simpleInsertSort() {
        if (sizeY == sizeX) {
            transposeMatrix();
            sort(matrix);
            transposeMatrix();
        } else {
            double[][] transposedMatrix = new double[sizeX][sizeY];
            transposeMatrix(matrix, transposedMatrix);
            sort(transposedMatrix);
            transposeMatrix(transposedMatrix, matrix);
        }
    }

    /**
     * Метод, где происходит реальная сортировка.
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
     * Транспонирование квадратной матрицы.
     */
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
     * @throws IOException выбрасывается в том случае,
     *                     если нет доступа к записи в указанный файл
     */
    public void printToFile(final String outputFilePath) throws IOException {
        File outputFile = new File(outputFilePath);
        FileWriter fileWriter = new FileWriter(outputFile);
        fileWriter.write(matrix.length + " " + matrix[0].length + "\n");
        for (double[] doubles : matrix) {
            for (double aDouble : doubles)
                fileWriter.write(String.format("%15.6E", aDouble).replace(',', '.'));
            fileWriter.write("\n");
        }
        fileWriter.close();
    }

    /**
     * Преобразовать нашу матрицу в треугольную.
     *
     * @return <p> 0 - если матрица решаема </p>
     * <p> 1 - если матрица вырожденная </p>
     * <p> 2 - если имеется бесконечно много решений </p>
     */
    public int createTriangleMatrix() {
        for (int k = 0; k < sizeY; k++) {
            if (isZero(matrix[k][k])) {
                int nonNull = findNonNullElement(k);
                if ((nonNull != -1)) swapLines(k, nonNull);
                else return 1;
            }

            for (int i = k + 1; i < sizeY; i++) {
                calculateCoefficients(i, i, k);
            }
        }

        if (isZero(matrix[sizeY - 1][sizeX - 1]))
            return 1;
        if (isZero(matrix[sizeY - 1][sizeX - 1]) && isZero(matrix[sizeY - 1][sizeX - 2]))
            return 2;

        return 0;
    }

    private int findNonNullElement(int currentLine) {
        for (int i = currentLine + 1; i < sizeY; i++)
            if (!isZero(matrix[i][currentLine])) return i;
        return -1;
    }

    /**
     * Вычислить коэффициенты, начиная с указанной строки.
     *
     * @param currentLine начальная строка
     */
    private void calculateCoefficients(int currentLine, int currentColumn, int workingLine) {
        double multiplier = matrix[currentLine][workingLine] / matrix[workingLine][workingLine];
        matrix[currentLine][workingLine] = 0;
        for (int j = currentColumn; j < sizeX; j++) {
            matrix[currentLine][j] -= multiplier * matrix[workingLine][currentColumn];
            if (isZero(matrix[currentColumn][j])) matrix[currentColumn][j] = 0;
        }
        print();
        System.out.println();
    }

    /**
     * Преобразовать матрицу в треугольную и вывести решение методом Гаусса.
     */
    public void getGaussSolution() {
        double[] ret = new double[sizeY];

        for (int i = ret.length - 1; i >= 0; i--) {
            ret[i] = matrix[i][sizeX - 1];
            for (int j = 0; j < ret.length; j++)
                if (i != j) ret[i] -= matrix[i][j] * ret[j];
            ret[i] /= matrix[i][i];
        }

        for (double v : ret) System.out.printf("%15.6E", v);
    }

    /**
     * Проверка числа на условный ноль.
     *
     * @param value число для проверки
     * @return true, если -quality < value < quality
     */
    private boolean isZero(final double value) {
        return Math.abs(value) <= quality;
    }

    /**
     * Поменять местами указанные строки.
     *
     * @param line1 первая строка для обмена
     * @param line2 вторая строка для обмена
     */
    private void swapLines(final int line1, final int line2) {
        double[] tmpLine = matrix[line1];
        matrix[line1] = matrix[line2];
        matrix[line2] = tmpLine;
    }

    public void getIterationSolution() {

    }

    private boolean checkDiagonal() {
        for (int i = 0; i < sizeY; i++) {
            if (isZero(matrix[i][i])) {
                for (int j = i; j < sizeY; j++) {

                }
            }
        }
        return true;
    }
}
