package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static double roundDoubleTo(double value, double quality) {
        return Math.round(value / quality) * quality;
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
    public static double[] getLinesSums(final double[][] matrix) {
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
    public static double[] getAbsLinesSumsWithoutLast(double[][] matrix) {
        double[] ret = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length - 1; j++) ret[i] += Math.abs(matrix[i][j]);
        return ret;
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
     * Поменять местами указанные строки в указанной матрице.
     *
     * @param matrix матрица для замены строк
     * @param line1 первая строка для обмена
     * @param line2 вторая строка для обмена
     */
    public static void swapLines(double[][] matrix, final int line1, final int line2) {
        double[] tmpLine = matrix[line1];
        matrix[line1] = matrix[line2];
        matrix[line2] = tmpLine;
    }

    public static void swapLines(double[] array, final int line1, final int line2) {
        double tmpLine = array[line1];
        array[line1] = array[line2];
        array[line2] = tmpLine;
    }

    private static final Map<Integer, ArrayList<Double>> multiMap = new HashMap<>();

    public static void addValueToMultiMap(Integer power, Double value) {
        ArrayList<Double> tempList;
        if (multiMap.containsKey(power)) {
            tempList = multiMap.get(power);
            if (tempList == null) tempList = new ArrayList<>();
        } else tempList = new ArrayList<>();
        tempList.add(value);
        multiMap.put(power, tempList);
    }

    public static Map<Integer, ArrayList<Double>> getMultiMap() {
        return multiMap;
    }
}
