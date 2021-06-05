package matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Класс, представляющий двумерную матрицу.
 *
 */
@SuppressWarnings({"FieldCanBeLocal", "CheckStyle"})
public class Matrix {

    /**
     * Сама матрица для отображения.
     */
    protected double[][] matrix;
    /**
     * Размерность матрицы.
     */
    protected int        size;
    /**
     * Условный ноль.
     */
    protected double     quality;
    /**
     * Максимальное количество итераций для итерационного метода.
     */
    protected int        maxIterations = 10;

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
    private void create(final int size) {
        matrix = new double[size][];
        for (int i = 0; i < size; i++) matrix[i] = new double[size + 1];
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

    public int getHeight() {
        return size;
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
