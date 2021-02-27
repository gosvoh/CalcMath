//
// Created by gosvoh on 26.02.2021.
//

#ifndef CALCMATH_MATRIX_H
#define CALCMATH_MATRIX_H

class Matrix {
 private:
  /** Сама матрица для отображения. */
  double **matrix = nullptr;
  /** Размерность матрицы, высота. */
  int n;
  /** Размерность матрицы, длина. */
  int m;

  /**
   * Получить сумму элементов указанной строки.
   *
   * @param matrix матрица для подсчёта сумм столбцов
   *
   * @return массив сумм элементов строки
   */
  void getLinesSums(double **matrix, double *buf, int sizeX, int sizeY);

  /**
   * Метод инициализации элементов матрицы.
   *
   * @param n высота матрицы
   * @param m длина матрицы
   */
  void create(const int n, const int m);

  /**
   * Заполнение матрицы из файла.
   * Сначала считываем первую строку, в которой содержатся параметры
   * матрицы (высота и длина), потом считываем каждую последующую строку,
   * разделяем её на элементы и вносим в массив.
   *
   * @param path путь к файлу с матрицей
   *
   * @throws FileNotFoundException выбрасывается в том случае,
   *                               если файл не был найден
   */
  void init(const char *path);

  /** Транспонирование квадратной матрицы */
  void transposeMatrix();

  /**
   * Транспонирование не квадратной матрицы матрицы.
   *
   * @param matrix           исходная матрица
   * @param transposedMatrix транспонированная матрица
   * @param sizeX ширина     исходной матрицы
   * @param sizeY высота     исходной матрицы
   */
  void transposeMatrix(double **matrix, double **transposedMatrix, int sizeX, int sizeY);

  /**
   * Метод, где происходит реальная сортировка
   *
   * @param matrix матрица, которую нужно отсортировать
   */
  void sort(double **matrix, int sizeX, int sizeY);

 public:
  /**
   * Конструктор объекта матрицы.
   *
   * @param inputFilePath путь к файлу с матрицей
   *
   * @throws FileNotFoundException выбрасывается в том случае,
   *                               если файл не был найден
   */
  explicit Matrix(const char *inputFilePath);

  /** Деструктор матрицы */
  ~Matrix();

  /** Вывести матрицу на экран. */
  void print();

  /** Метод сортировки, путём простой вставки. */
  void simpleInsertSort();

  /**
   * Вывести матрицу в файл.
   *
   * @param outputFilePath путь до файла для вывода матрицы
   *
   * @throws IOException выбрасывается в том случае,
   *                     если нет доступа к записи в указанный файл
   */
  void printToFile(const char *outputFilePath);
};

#endif //CALCMATH_MATRIX_H
