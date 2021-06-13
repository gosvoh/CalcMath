package matrix;

import utils.MatrixUtils;

/**
 * Класс для нахождения решения матрицы итерационным методом.
 */
public class SeidelSolution extends Matrix {
    /**
     * Максимальное количество итераций для решения с контролем
     */
    private final int      MAX_CONTROL_ITERATIONS = 10;
    /** Массив индексов строк матрицы {@link #matrix} */
    private       int[]    shortMatrix;
    /** Массив сумм строк матрицы {@link #matrix} */
    private       double[] lineSums;

    public SeidelSolution(Matrix matrixObj) {
        this.matrix = new double[matrixObj.matrix.length][matrixObj.matrix[0].length];
        for (int i = 0; i < this.matrix.length; i++)
            System.arraycopy(matrixObj.matrix[i], 0, matrix[i], 0, matrix[i].length);
        this.size = matrixObj.size;
        this.quality = matrixObj.quality;
        this.maxIterations = matrixObj.maxIterations;
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
        shortMatrix = new int[matrix.length];
        for (int i = 0; i < shortMatrix.length; i++) shortMatrix[i] = i;

        lineSums = MatrixUtils.getAbsLinesSumsWithoutLast(matrix);
        int tmp = checkMatrix();
        System.out.println("Изменённая матрица:\n" + this);
        if ((tmp & 2) == 2) return false;

        int counter = 0;
        int i = 0;
        if (tmp == 1) {
            System.out.println("ДУС выполняется частично, отслеживается " + MAX_CONTROL_ITERATIONS + " попыток решения с отслеживанием монотонности...");
            i = 1;
            double max = calculateNewApproximations(initApproximations);
            for (; i < MAX_CONTROL_ITERATIONS; i++) {
                double temp = calculateNewApproximations(initApproximations);
                if (max >= temp || Math.abs(max - temp) < quality) {
                    max = temp;
                    counter++;
                    if (MatrixUtils.isZero(max, quality) || counter == 4) break;
                } else counter = 0;
            }
            if (counter != 4) return false;
        }
        System.out.println(counter == 0 ? "Дус выполняется полностью" : "Итерации монотонно убывают" + ", решаем без проверки...");

        // Данные вычисления производятся всего лишь один раз, так что не вижу смысла переносить их в отдельный метод.
        for (; i < maxIterations; i++)
            if (MatrixUtils.isZero(calculateNewApproximations(initApproximations), quality)) break;
        System.out.printf("Приближения на %d итерации:\n", i);
        MatrixUtils.print(initApproximations);
        System.out.println();

        return true;
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
    public int checkMatrix() {
        if (checkOnZero(shortMatrix)) {
            boolean condition = findBestIndexes();
            compileMatrix();
            return condition ? 0 : 2;
        }
        if (SCC(shortMatrix)) return 2;
        else return 1;
    }

    /**
     * Попытаться найти лучшую перестановку индексов строк.<p>
     * Используется реализация алгоритма <b><nobr>std::nex_permutation</nobr></b> на Java
     *
     * @return True если найдена, в противном случае false.
     */
    private boolean findBestIndexes() {
        boolean found = false;
        int[] temp = new int[size];
        for (int i = 0; i < temp.length; i++) temp[i] = i;

        do {
            if (SCC(temp)) {
                System.arraycopy(temp, 0, shortMatrix, 0, temp.length);
                return true;
            }
            if (!found && !checkOnZero(temp)) {
                found = true;
                System.arraycopy(temp, 0, shortMatrix, 0, temp.length);
            }
        } while (MatrixUtils.nextPermutation(temp));

        return found;
    }

    /**
     * Преобразовать матрицу в соответствии с индексами короткой матрицы {@link #shortMatrix}
     */
    private void compileMatrix() {
        double[][] tempMatrix = matrix.clone();
        for (int i = 0; i < matrix.length; i++) matrix[i] = tempMatrix[shortMatrix[i]];
    }

    /**
     * Проверка матрицы на ДУС.
     *
     * @param indexes Массив индексов строк.
     *
     * @return True, если ДУС выполняется, в противном случае false.
     */
    private boolean SCC(int[] indexes) {
        int check = 0;
        for (int i = 0; i < size; i++) {
            if (lineSums[indexes[i]] > 2 * Math.abs(matrix[indexes[i]][i])) return false;
            if (lineSums[indexes[i]] == 2 * Math.abs(matrix[indexes[i]][i])) {
                if (check != size - 1) check++;
                else return false;
            }
        }
        return true;
    }

    /**
     * Проверить диагональ матрицы на нули.
     *
     * @param indexes Массив индексов строк.
     *
     * @return False если нули не обнаружены, в противном случае true.
     */
    private boolean checkOnZero(int[] indexes) {
        for (int i = 0; i < size; i++) if (MatrixUtils.isZero(matrix[indexes[i]][i], quality)) return true;
        return false;
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
