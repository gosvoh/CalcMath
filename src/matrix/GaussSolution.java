package matrix;

import utils.MatrixUtils;

/**
 * Класс для нахождения решения матрицы методом Гаусса.
 */
public class GaussSolution extends Matrix {
    public GaussSolution(Matrix matrixObj) {
        this.matrix = new double[matrixObj.matrix.length][matrixObj.matrix[0].length];
        for (int i = 0; i < this.matrix.length; i++)
            System.arraycopy(matrixObj.matrix[i], 0, matrix[i], 0, matrix[i].length);
        this.size = matrixObj.size;
        this.quality = matrixObj.quality;
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
            if (MatrixUtils.isZero(matrix[k][k], quality)) {
                int nonNull = findNonNullElementInColumn(k);
                if ((nonNull != -1)) MatrixUtils.swapLines(matrix, k, nonNull);
                else return 1;
            }

            for (int i = k + 1; i < size; i++) calculateCoefficients(i, k);
        }

        if (MatrixUtils.isZero(matrix[size - 1][size], quality)) return 1;
        if (MatrixUtils.isZero(matrix[size - 1][size], quality) && MatrixUtils.isZero(matrix[size - 1][size - 1], quality))
            return 2;

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
        for (int i = currentColumn + 1; i < size; i++)
            if (!MatrixUtils.isZero(matrix[i][currentColumn], quality)) return i;
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
            if (MatrixUtils.isZero(matrix[currentLine][j], quality)) matrix[currentLine][j] = 0;
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
     * Вызов данного метода запрещён из этого класса!
     */
    @Override
    @Deprecated
    public void init(String path) {
        System.out.println("Вы не можете вызвать этот медот в этом классе!");
    }
}
