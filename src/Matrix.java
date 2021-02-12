import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

@SuppressWarnings("FieldCanBeLocal")
public class Matrix {
    /**
     * Сама матрица для отображения
     */
    private double[][] matrix;
    /**
     * Размерность матрицы, где n - это высота, а m - длина
     */
    private int n, m;

    /**
     * Конструктор объекта матрицы
     *
     * @param inputFilePath путь к файлу с матрицей
     * @throws FileNotFoundException выбрасывается в том случае, если файл не был найден
     */
    public Matrix(String inputFilePath) throws FileNotFoundException {
        this.init(inputFilePath);
    }

    /**
     * Метод инициализации элементов матрицы
     *
     * @param n высота матрицы
     * @param m длина матрицы
     */
    public void create(int n, int m) {
        matrix = new double[n][];
        for (int i = 0; i < n; i++) matrix[i] = new double[m];
    }

    /**
     * Вывести матрицу на экран
     */
    public void print() {
        for (double[] doubles : matrix) {
            for (double aDouble : doubles) System.out.printf("%15.6E", aDouble);
            System.out.println();
        }
    }

    /**
     * Заполнение матрицы из файла.
     * Сначала считываем первую строку, в которой содержатся параметры матрицы (высота и длина),
     * потом счиываем каждую последующую строку, разделяем её на элементы и вносим в массив.
     *
     * @param path путь к файлу с матрицей
     * @throws FileNotFoundException выбрасывается в том случае, если файл не был найден
     */
    public void init(String path) throws FileNotFoundException {
        File inFile = new File(path);
        Scanner scanner = new Scanner(inFile);
        Pattern pattern = Pattern.compile("[ \t]+");
        String string = scanner.nextLine();
        String[] strings = pattern.split(string);
        n = Integer.parseInt(strings[0]);
        m = Integer.parseInt(strings[1]);
        create(n, m);
        for (int i = 0; i < n; i++) {
            string = scanner.nextLine();
            strings = pattern.split(string);
            for (int j = 0; j < m; j++) matrix[i][j] = Double.parseDouble(strings[j]);
        }
        scanner.close();
    }

    /**
     * Метод сортировки, путём простой вставки
     */
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

        System.out.println(Arrays.toString(lineSums));
        matrix = transposeMatrix(matrix);
    }

    /**
     * Получить сумму элементов указанной строки
     *
     * @param lineNum номер строки для подсчёта суммы
     * @return сумма элементов строки
     */
    private double getLineSum(int lineNum) {
        double ret = 0;
        for (int i = 0; i < matrix[lineNum].length; i++) ret += matrix[lineNum][i];
        return ret;
    }

    /**
     * Транспонирование матрицы
     *
     * @param matrix исходная матрица
     * @return транспонированная матрица
     */
    public double[][] transposeMatrix(double[][] matrix) {
        int n = matrix.length, m = matrix[0].length;
        double[][] newMatrix = new double[m][];
        for (int i = 0; i < m; i++) {
            newMatrix[i] = new double[n];
            for (int j = 0; j < n; j++) newMatrix[i][j] = matrix[j][i];
        }
        return newMatrix;
    }
}
