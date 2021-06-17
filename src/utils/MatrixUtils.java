package utils;

import java.io.FileWriter;
import java.io.IOException;

public class MatrixUtils {

    public static double roundDoubleTo(double value, double quality) {
        return Math.round(value / quality) * quality;
    }

    /**
     * Вывести указанный массив на экран.
     *
     * @param array массив для вывода
     */
    public static void print(final double[] array) {
        for (double v : array) System.out.printf("%.6E ", v);
        System.out.println();
    }

    /**
     * Получить сумму элементов указанной строки.
     *
     * @param matrix матрица для подсчёта сумм столбцов
     *
     * @return массив сумм элементов строки
     */
    public static double[] getLinesSums(final double[][] matrix) {
        double[] ret = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            ret[i] = 0;
            for (int j = 0; j < matrix[i].length; j++) ret[i] += matrix[i][j];
        }
        return ret;
    }

    /**
     * Получить сумму элементов указанной строки без последнего элемента.
     *
     * @param matrix матрица для подсчёта сумм столбцов
     *
     * @return массив сумм элементов строки
     */
    public static double[] getAbsLinesSumsWithoutLast(double[][] matrix) {
        double[] ret = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            ret[i] = 0;
            for (int j = 0; j < matrix[i].length - 1; j++) ret[i] += Math.abs(matrix[i][j]);
        }
        return ret;
    }

    /**
     * Поменять местами указанные строки в указанной матрице.
     *
     * @param matrix матрица для замены строк
     * @param line1  первая строка для обмена
     * @param line2  вторая строка для обмена
     */
    public static void swap(double[][] matrix, final int line1, final int line2) {
        double[] tmpLine = matrix[line1];
        matrix[line1] = matrix[line2];
        matrix[line2] = tmpLine;
    }

    /**
     * Поменять местами указанные строки в указанном массиве.
     *
     * @param array массив для замены строк
     * @param line1 первая строка для обмена
     * @param line2 вторая строка для обмена
     */
    public static void swap(int[] array, final int line1, final int line2) {
        int tmpLine = array[line1];
        array[line1] = array[line2];
        array[line2] = tmpLine;
    }

    /**
     * Permute range into the next "dictionary" ordering.<p>
     * Treats all permutations of the range as a set of "dictionary" sorted
     * sequences.  Permutes the current sequence into the next one of this set.
     * Returns true if there are more sequences to generate.  If the sequence
     * is the largest of the set, the smallest is generated and false returned.
     *
     * @param array array to permute.
     *
     * @return False if wrapped to first permutation, true otherwise.
     */
    public static boolean nextPermutation(int[] array) {
        int len = array.length;
        int i = len - 1;

        while (i > 0) {
            if (array[i - 1] < array[i]) break;
            i--;
        }

        if (i <= 0) return false;

        int j = len - 1;
        while (j >= i) {
            if (array[i - 1] < array[j]) break;
            j--;
        }

        MatrixUtils.swap(array, i - 1, j);

        len--;
        while (i < len) {
            MatrixUtils.swap(array, i, len);
            len--;
            i++;
        }
        return true;
    }

    /**
     * Проверка числа на условный ноль.
     *
     * @param value число для проверки
     *
     * @return true, если |value| < quality
     */
    public static boolean isZero(final double value, final double quality) {
        return Math.abs(value) <= quality;
    }

    /**
     * Метод, где происходит реальная сортировка.
     *
     * @param matrix матрица, которую нужно отсортировать
     */
    private static void sort(double[][] matrix) {
        double[] tmpLine;
        double tmpSum;
        double[] lineSums = MatrixUtils.getLinesSums(matrix);

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
    private static void transposeMatrix(double[][] matrix) {
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
    private static void transposeMatrix(double[][] matrix, double[][] transposedMatrix) {
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
     * @param matrix         - матрица для вывода
     *
     * @throws IOException выбрасывается в том случае,
     *                     если нет доступа к записи в указанный файл
     */
    public static void printToFile(final String outputFilePath, final double[][] matrix) throws IOException {
        FileWriter fileWriter = new FileWriter(outputFilePath);
        fileWriter.write(matrix.length + " " + matrix[0].length + "\n");
        for (double[] doubles : matrix) {
            for (double aDouble : doubles)
                fileWriter.write(String.format("%.6E ", aDouble).replace(',', '.'));
            fileWriter.write("\n");
        }
        fileWriter.close();
    }
}
