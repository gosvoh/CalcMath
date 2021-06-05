package matrix;

import utils.MatrixUtils;

/**
 * Класс для нахождения решения матрицы итерационным методом.
 */
public class SeidelSolution extends Matrix {
    /**
     * Максимальное количество итераций для решения с контролем
     */
    private final int   MAX_CONTROL_ITERATIONS = 10;
    /** Массив индексов строк матрицы {@link #matrix} */
    private       int[] shortMatrix;

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

        int tmp = checkMatrix(MatrixUtils.getAbsLinesSumsWithoutLast(matrix));
        if (tmp == 2) return false;

        double[][] tempMatrix = matrix.clone();
        for (int i = 0; i < matrix.length; i++) matrix[i] = tempMatrix[shortMatrix[i]];
        System.out.println(this);

        int counter = 0;
        int i = 0;
        if (tmp == 1) {
            System.out.println("ДУС выполняется частично, отслеживается " + MAX_CONTROL_ITERATIONS + " попыток решения с отслеживанием монотонности...");
            i = 1;
            double max = calculateNewApproximations(initApproximations);
            for (; i < MAX_CONTROL_ITERATIONS; i++) {
                double temp = calculateNewApproximations(initApproximations);
                if (max >= temp || (max - temp) < quality) {
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
    private int checkMatrix(double[] lineSums) {
        boolean DUS = false;

        for (int i = 0; i < size; i++) {
            if (MatrixUtils.isZero(matrix[shortMatrix[i]][i], quality)) {
                int nonNullRow = findNonNullElementInColumn(i);
                if (nonNullRow == -1) return 2;
                MatrixUtils.swapLines(shortMatrix, i, nonNullRow);
            }

            if (Math.abs(matrix[shortMatrix[i]][i]) > (lineSums[shortMatrix[i]] - Math.abs(matrix[shortMatrix[i]][i])))
                DUS = true;

            if (Math.abs(matrix[shortMatrix[i]][i]) < (lineSums[shortMatrix[i]] - Math.abs(matrix[shortMatrix[i]][i]))) {
                int tmp = getMaxDifferenceIndex(lineSums, shortMatrix[i]);
                if (tmp == -1) return 2;
                MatrixUtils.swapLines(shortMatrix, i, tmp);
                DUS = checkMatrix(lineSums) == 0 || DUS;
            }
        }

        return DUS ? 0 : 1;
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
     * Получить индекс строки, который больше всего подходит указанной строке. Пример:<br>
     * Если строка выглядит следующим образом - [5 2 0 -2] с индексом 1,
     * то наиболее подходящая позиция для неё - 0, так как индекс наибольшего элемента - 0.
     *
     * @param lineSums сумма строк
     * @param line     текущая строка для поиска индекса
     *
     * @return наилучший индекс
     */
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

    /**
     * Вызов данного метода запрещён из этого класса!
     */
    @Override
    @Deprecated
    public void init(String path) {
        System.out.println("Вы не можете вызвать этот медот в этом классе!");
    }
}
