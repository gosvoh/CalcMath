package matrix;

import utils.MatrixUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Класс матрицы.
 */
@SuppressWarnings({"FieldCanBeLocal", "CheckStyle"})
public class Matrix {
    /**
     * Максимальное количество итераций для решения с контролем
     */
    private final int MAX_CONTROL_ITERATIONS = 10;
    /**
     * Сама матрица для отображения.
     */
    private double[][] matrix;
    /**
     * Размерность матрицы.
     */
    private int size;
    /**
     * Условный ноль.
     */
    private double quality;
    /**
     * Максимальное количество итераций для итерационного метода.
     */
    private int maxIterations = 10;

    private int[] shortMatrix;

    /**
     * Конструктор объекта матрицы.
     */
    public Matrix() {
        this.matrix = null;
        this.size = 0;
    }

    /**
     * Метод инициализации элементов матрицы.
     *
     * @param size размерность матрицы
     */
    public void create(final int size) {
        matrix = new double[size][];
        for (int i = 0; i < size; i++) matrix[i] = new double[size + 1];
    }

    /**
     * Вывести матрицу на экран.
     */
    public void print() {
        MatrixUtils.print(matrix);
        System.out.println();
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
        size = Integer.parseInt(strings[0]);
        if (strings.length > 2) quality = Double.parseDouble(strings[2]);
        if (strings.length > 3) maxIterations = Integer.parseInt(strings[3]);
        create(size);
        for (int i = 0; i < size; i++) {
            string = scanner.nextLine().trim();
            strings = pattern.split(string);
            try {
                for (int j = 0; j < size + 1; j++) matrix[i][j] = Double.parseDouble(strings[j].replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
                scanner.close();
                System.exit(1);
            }
        }
        scanner.close();
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
        for (int k = 0; k < size; k++) {
            if (isZero(matrix[k][k])) {
                int nonNull = findNonNullElementInColumn(k);
                if ((nonNull != -1)) MatrixUtils.swapLines(matrix, k, nonNull);
                else return 1;
            }

            for (int i = k + 1; i < size; i++) calculateCoefficients(i, k);
        }

        if (isZero(matrix[size - 1][size])) return 1;
        if (isZero(matrix[size - 1][size]) && isZero(matrix[size - 1][size - 1])) return 2;

        return 0;
    }

    /**
     * Найти ненулевой элемент в указанном столбце, начиная со следующей строки.
     *
     * @param currentColumn столбец для поиска
     * @return индекс элемента, если он не найден, то -1
     */
    private int findNonNullElementInColumn(int currentColumn) {
        for (int i = currentColumn + 1; i < size; i++)
            if (!isZero(matrix[i][currentColumn])) return i;
        return -1;
    }

    /**
     * Вычислить коэффициенты, начиная с указанной строки и указанного столбца.
     *
     * @param currentLine текущая строка
     * @param outsideLoop столбец и строка во внешнем цикле
     */
    private void calculateCoefficients(int currentLine, int outsideLoop) {
        double multiplier = matrix[currentLine][outsideLoop] / matrix[outsideLoop][outsideLoop];
        matrix[currentLine][outsideLoop] = 0;
        for (int j = outsideLoop + 1; j < size + 1; j++) {
            matrix[currentLine][j] -= multiplier * matrix[outsideLoop][j];
            if (isZero(matrix[currentLine][j])) matrix[currentLine][j] = 0;
        }
    }

    /**
     * Вывести решение методом Гаусса.
     */
    public void getGaussSolution() {
        double[] ret = new double[size];

        for (int i = ret.length - 1; i >= 0; i--) {
            ret[i] = matrix[i][size];
            for (int j = 0; j < ret.length; j++)
                if (i != j) ret[i] -= matrix[i][j] * ret[j];
            ret[i] /= matrix[i][i];
        }

        MatrixUtils.print(ret);
    }

    /**
     * Проверка числа на условный ноль.
     *
     * @param value число для проверки
     * @return true, если |value| < quality
     */
    private boolean isZero(final double value) {
        return Math.abs(value) <= quality;
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
     * @return true, если матрица решена, иначе false
     */
    public boolean getIterationSolution(double[] initApproximations) {
        shortMatrix = new int[matrix.length];
        for (int i = 0; i < shortMatrix.length; i++) shortMatrix[i] = i;

        int tmp = checkMatrix(MatrixUtils.getAbsLinesSumsWithoutLast(matrix));
        if (tmp == 2) return false;

        double[][] tempMatrix = matrix.clone();
        for (int i = 0; i < matrix.length; i++) matrix[i] = tempMatrix[shortMatrix[i]];
        print();

        int counter = 0;
        int i;
        if (tmp == 1) {
            System.out.println("ДУС выполняется частично, отслеживается " + MAX_CONTROL_ITERATIONS + " попыток решения с отслеживанием монотонности...");
            i = 1;
            double max = calculateNewApproximations(initApproximations);
            for (; i < MAX_CONTROL_ITERATIONS; i++) {
                double temp = calculateNewApproximations(initApproximations);
                if (max > temp) {
                    max = temp;
                    counter++;
                    if (isZero(max) || counter == 4) break;
                } else counter = 0;
            }
            if (counter != 4) return false;
        }
        calcWithoutControl(0, initApproximations);

        return true;
    }

    private void calcWithoutControl(int i, double[] initApproximations) {
        for (; i < maxIterations; i++) if (isZero(calculateNewApproximations(initApproximations))) break;
        System.out.printf("Приближения на %d итерации:\n", i);
        MatrixUtils.print(initApproximations);
        System.out.println();
    }

    /**
     * Вычислить новые приближения.
     *
     * @param approximations предыдущие приближения
     */
    private double calculateNewApproximations(double[] approximations) {
        double max = 0;
        double approx;
        for (int i = 0; i < approximations.length; i++) {
            approx = approximations[i];
            approximations[i] = matrix[i][size];
            for (int j = 0; j < size; j++) if (i != j) approximations[i] -= matrix[i][j] * approximations[j];
            approximations[i] /= matrix[i][i];
            max = Math.max(Math.abs(approximations[i] - approx), Math.abs(max));
        }
        return max;
    }

    /**
     * Проверить матрицу на достаточное условие сходимости.
     * Если на диагонали есть ноль, то попытаться убрать его, путём перестановки строк.
     *
     * @return <p> <b>0</b> если условие выполняется </p>
     * <p> <b>1</b> если ДУС выполняется частично </p>
     * <p> <b>2</b> если ДУС не выполняется </p>
     */
    private int checkMatrix(double[] lineSums) {
        boolean DUS = false;

        for (int i = 0; i < size; i++) {
            if (isZero(matrix[shortMatrix[i]][i])) {
                int nonNullRow = findNonNullElementInColumn(i);
                if (nonNullRow == -1) return 2;
                MatrixUtils.swapLines(shortMatrix, i, nonNullRow);
            }

            if (Math.abs(matrix[shortMatrix[i]][i]) > (lineSums[shortMatrix[i]] - Math.abs(matrix[shortMatrix[i]][i]))) DUS = true;

            if (Math.abs(matrix[shortMatrix[i]][i]) < (lineSums[shortMatrix[i]] - Math.abs(matrix[shortMatrix[i]][i]))) {
                int tmp = getMaxDifferenceIndex(lineSums, shortMatrix[i]);
                if (tmp == -1) return 2;
                MatrixUtils.swapLines(shortMatrix, i, tmp);
                DUS = checkMatrix(lineSums) == 0 || DUS;
            }
        }

        return DUS ? 0 : 1;
    }

    private int getMaxDifferenceIndex(double[] lineSums, int line) {
        double maxDiff = lineSums[line] - Math.abs(matrix[line][line]);
        int maxDiffIndex = -1;
        for (int j = 0; j < size; j++) {
            double tmp = lineSums[line] - Math.abs(matrix[line][j]);
            if (tmp < maxDiff) {
                maxDiff = tmp;
                maxDiffIndex = j;
            }
        }
        return maxDiffIndex;
    }

    public int getHeight() {
        return size;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (double[] doubles : matrix) {
            for (double aDouble : doubles) sb.append(String.format("%15.6E", aDouble));
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
