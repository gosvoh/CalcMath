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
    /** Максимальное количество итераций для решения с контролем */
    private final int        MAX_CONTROL_ITERATIONS = 10;
    /** Сама матрица для отображения. */
    private       double[][] matrix;
    /** Размерность матрицы, высота. */
    private       int        mHeight;
    /** Размерность матрицы, длина. */
    private       int        mLength;
    /** Условный ноль. */
    private       double     quality;
    /** Максимальное количество итераций для итерационного метода. */
    private       int        maxIterations;

    /** Конструктор объекта матрицы. */
    public Matrix() {
        this.matrix = null;
        this.mHeight = 0;
        this.mLength = 0;
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
     * Вывести указанный массив на экран.
     *
     * @param array массив для вывода
     */
    public static void print(final double[] array) {
        for (double v : array) System.out.printf("%15.6E", v);
        System.out.println();
    }

    /**
     * Получить сумму элементов указанной строки.
     *
     * @param matrix матрица для подсчёта сумм столбцов
     *
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
     *
     * @return массив сумм элементов строки
     */
    private static double[] getAbsLinesSumsWithoutLast(double[][] matrix) {
        double[] ret = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length - 1; j++) ret[i] += Math.abs(matrix[i][j]);
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

    /** Вывести матрицу на экран. */
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
        mHeight = Integer.parseInt(strings[0]);
        mLength = Integer.parseInt(strings[1]);
        if (strings.length > 2) quality = Double.parseDouble(strings[2]);
        if (strings.length > 3) maxIterations = Integer.parseInt(strings[3]);
        if (mHeight + 1 != mLength)
            throw new IllegalArgumentException("Неполное условие, система не может быть решена!");
        create(mHeight, mLength);
        for (int i = 0; i < mHeight; i++) {
            string = scanner.nextLine().trim();
            strings = pattern.split(string);
            try {
                for (int j = 0; j < mLength; j++) matrix[i][j] = Double.parseDouble(strings[j].replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
                scanner.close();
                System.exit(1);
            }
        }
        scanner.close();
    }

    /** Метод сортировки, путём простой вставки. */
    @SuppressWarnings("unused")
    public void simpleInsertSort() {
        if (mHeight == mLength) {
            transposeMatrix();
            sort(matrix);
            transposeMatrix();
        } else {
            double[][] transposedMatrix = new double[mLength][mHeight];
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

    /** Транспонирование квадратной матрицы. */
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
        FileWriter fileWriter = new FileWriter(outputFilePath);
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
     * <p> Если на диагонали присутствуют нули,
     * то попытаться от них избавиться, путём перестановки строк. </p>
     *
     * @return <p> <b>0</b> - если матрица решаема </p>
     * <p> <b>1</b> - если матрица вырожденная </p>
     * <p> <b>2</b> - если имеется бесконечно много решений </p>
     */
    public int createTriangleMatrix() {
        for (int k = 0; k < mHeight; k++) {
            if (isZero(matrix[k][k])) {
                int nonNull = findNonNullElementInColumn(k);
                if ((nonNull != -1)) swapLines(k, nonNull);
                else return 1;
            }

            for (int i = k + 1; i < mHeight; i++) calculateCoefficients(i, k);
        }

        if (isZero(matrix[mHeight - 1][mLength - 1])) return 1;
        if (isZero(matrix[mHeight - 1][mLength - 1]) && isZero(matrix[mHeight - 1][mLength - 2])) return 2;

        return 0;
    }

    /**
     * Найти ненулевой элемент в указанном столбце, начиная со следующей строки.
     *
     * @param currentColumn столбец для поиска
     *
     * @return индекс элемента, если он не найден, то -1
     */
    private int findNonNullElementInColumn(int currentColumn) {
        for (int i = currentColumn + 1; i < mHeight; i++)
            if (!isZero(matrix[i][currentColumn])) return i;
        return -1;
    }

    /**
     * Вычислить коэффициенты, начиная с указанной строки и указанного столбца.
     *
     * @param currentLine  текущая строка
     * @param outsideLoopK столбец и строка во внешнем цикле
     */
    private void calculateCoefficients(int currentLine, int outsideLoopK) {
        double multiplier = matrix[currentLine][outsideLoopK] / matrix[outsideLoopK][outsideLoopK];
        matrix[currentLine][outsideLoopK] = 0;
        for (int j = outsideLoopK + 1; j < mLength; j++) {
            matrix[currentLine][j] -= multiplier * matrix[outsideLoopK][j];
            if (isZero(matrix[currentLine][j])) matrix[currentLine][j] = 0;
        }
    }

    /** Преобразовать матрицу в треугольную и вывести решение методом Гаусса. */
    public void getGaussSolution() {
        double[] ret = new double[mHeight];

        for (int i = ret.length - 1; i >= 0; i--) {
            ret[i] = matrix[i][mLength - 1];
            for (int j = 0; j < ret.length; j++)
                if (i != j) ret[i] -= matrix[i][j] * ret[j];
            ret[i] /= matrix[i][i];
        }

        print(ret);
    }

    /**
     * Проверка числа на условный ноль.
     *
     * @param value число для проверки
     *
     * @return true, если |value| < quality
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

    private void swapLines(double[] array, final int line1, final int line2) {
        double tmpLine = array[line1];
        array[line1] = array[line2];
        array[line2] = tmpLine;
    }

    /**
     * Вывести решение матрицы итерационным методом.
     * <p> Если достаточное условие сходимости не выполняется только частично,
     * то пробуем решить матрицу с контролем: </p>
     * <p> Даём {@link #MAX_CONTROL_ITERATIONS} попыток на решение.
     * Если новые приближения больше или равны старым 40% раз подряд,
     * то считаем решение успешным и выполняем оставшиеся итерации без контроля,
     * иначе считаем контроль проваленным.</p>
     *
     * @param initApproximations начальные приближения
     *
     * @return true, если матрица решена, иначе false
     */
    public boolean getIterationSolution(double[] initApproximations) {
        int tmp = checkMatrix(0);

        int[] counter = {0};
        /* Требуется версия Java не ниже 14 */
        switch (tmp) {
            /* Решение без контроля */
            case 0 -> {
                calculateFirstApproximation(initApproximations);
                for (int i = 1; i < maxIterations; i++) calculateNewApproximations(initApproximations);
                print(initApproximations);
                System.out.println();
            }
            case 1, 2 -> { return false;}
            /* Решение с контролем */
            case 3 -> {
                int i;
                calculateFirstApproximation(initApproximations);
                for (i = 1; i < MAX_CONTROL_ITERATIONS; i++) {
                    calculateNewApproximations(initApproximations, counter);
                    if (counter[0] == MAX_CONTROL_ITERATIONS * 0.4) break;
                }
                if (counter[0] != MAX_CONTROL_ITERATIONS * 0.4) return false;
                for (; i < maxIterations; i++) calculateNewApproximations(initApproximations);
            }
        }

        return true;
    }

    /**
     * Вычислить новые приближения.
     *
     * @param approximations предыдущие приближения
     */
    private void calculateNewApproximations(double[] approximations) {
        calculateNewApproximations(approximations, new int[]{-1});
    }

    private void calculateNewApproximations(double[] approximations, int[] counter) {
        boolean check = true;
        for (int i = 0; i < approximations.length; i++) {
            double newApproximation = matrix[i][mLength - 1];
            for (int j = 0; j < mLength - 1; j++)
                if (i != j)
                    newApproximation -= matrix[i][j] * approximations[j];
            newApproximation /= matrix[i][i];
            if (Math.abs(newApproximation) < Math.abs(approximations[i])) approximations[i] = newApproximation;
            else check = false;
        }
        if (check) counter[0]++;
    }

    private void calculateFirstApproximation(double[] approximations) {
        for (int i = 0; i < approximations.length; i++) {
            approximations[i] = matrix[i][mLength - 1];
            for (int j = 0; j < mLength - 1; j++)
                if (i != j)
                    approximations[i] -= matrix[i][j] * approximations[j];
            approximations[i] /= matrix[i][i];
        }
    }

    /**
     * Проверить матрицу на достаточное условие сходимости.
     * Если на диагонали есть ноль, то попытаться убрать его, путём перестановки строк.
     *
     * @return <p> <b>0</b> если условие выполняется </p>
     * <p> <b>1</b> если ДУС выполняется частично </p>
     * <p> <b>2</b> если ДУС не выполняется </p>
     */
    private int checkMatrix(int startingLine) {
        double[] lineSums = getAbsLinesSumsWithoutLast(matrix);
        boolean DUS = true;
        for (int i = startingLine; i < mHeight; i++) {
            if (isZero(matrix[i][i])) {
                int nonNull = findNonNullElementInColumn(i);
                if ((nonNull != -1)) {
                    swapLines(i, nonNull);
                    swapLines(lineSums, i, nonNull);
                } else return 2;
            }

            if (Math.abs(matrix[i][i]) < (lineSums[i] - Math.abs(matrix[i][i]))) {
                int tmp = getMaxDifferenceIndex(lineSums, i);
                if (tmp != -1) {
                    swapLines(i, tmp);
                    if (checkMatrix(i) == 2) return 2;
                }
            }

        }

        return 0;
    }

    private int getMaxDifferenceIndex(double[] lineSums, int line) {
        double maxDiff = lineSums[line] - Math.abs(matrix[line][line]);
        int maxDiffIndex = 0;
        for (int j = 0; j < mLength; j++) {
            double tmp = lineSums[line] - Math.abs(matrix[line][j]);
            if (tmp < maxDiff) {
                maxDiff = tmp;
                maxDiffIndex = j;
            }
        }
        return line < maxDiffIndex ? maxDiffIndex : -1;
    }

    /** @return высота матрицы */
    public int getHeight() {
        return mHeight;
    }

    /** @return длина матрицы */
    public int getLength() {
        return mLength;
    }
}
