//
// Created by gosvoh on 26.02.2021.
//

#include "includes.h"
#include "Matrix.h"

Matrix::Matrix(const char *inputFilePath) { // NOLINT(cppcoreguidelines-pro-type-member-init)
  init(inputFilePath);
}

Matrix::~Matrix() {
  if (!_matrix) return;
  for (int i = 0; i < size_y_; ++i) {
	delete[] _matrix[i];
  }
  delete[] _matrix;
}

void Matrix::getLinesSums(double **matrix, double *buf, int sizeX, int sizeY) {
  for (int i = 0; i < sizeY; i++) for (int j = 0; j < sizeX; j++) buf[i] += matrix[i][j];
}

void Matrix::init(const char *path) {
  FILE *iFile = fopen(path, "r");
  if (iFile == nullptr) {
	printf("Cannot open file!\n");
	return;
  }

  char *line;
  const int arraySize = 1024;
  char *linePointer;
  const char *delim = " \t";

  line = new char[arraySize];
  fgets(line, arraySize, iFile);
  linePointer = strtok(line, delim);
  size_y_ = (int)strtol(linePointer, nullptr, 10);
  linePointer = strtok(nullptr, delim);
  size_x_ = (int)strtol(linePointer, nullptr, 10);
  create(size_x_, size_y_);
  for (int i = 0; i < size_y_; ++i) {
	fgets(line, arraySize, iFile);
	for (int j = 0; j < size_x_; ++j) {
	  if (j == 0) linePointer = strtok(line, delim);
	  else linePointer = strtok(nullptr, delim);
	  _matrix[i][j] = strtod(linePointer, nullptr);
	}
  }

  delete[] line;
  fclose(iFile);
}

void Matrix::create(const int sizeX, const int sizeY) {
  _matrix = new double *[sizeY];
  for (int i = 0; i < sizeY; ++i) {
	_matrix[i] = new double[sizeX];
  }
}

void Matrix::print() {
  print(_matrix, size_x_, size_y_);
}

void Matrix::print(double **matrix, int sizeX, int sizeY) {
  if (matrix == nullptr) {
	printf("Matrix is NULL\n");
	return;
  }
  for (int i = 0; i < sizeY; ++i) {
	for (int j = 0; j < sizeX; ++j) {
	  printf("%15.6E", matrix[i][j]);
	}
	printf("\n");
  }
}

void Matrix::simpleInsertSort() {
  if (size_y_ == size_x_) {
	transposeMatrix();
	sort(_matrix, size_y_, size_x_);
	transposeMatrix();
  } else {
	auto **transposedMatrix = new double *[size_x_];
	for (int i = 0; i < size_x_; ++i) transposedMatrix[i] = new double[size_y_];

	transposeMatrix(_matrix, transposedMatrix, size_x_, size_y_);
	sort(transposedMatrix, size_y_, size_x_);
	transposeMatrix(transposedMatrix, _matrix, size_y_, size_x_);

	for (int i = 0; i < size_y_; ++i) {
	  delete[] transposedMatrix[i];
	}
	delete[] transposedMatrix;
  }
}

void Matrix::printToFile(const char *outputFilePath) {
  FILE *oFile = fopen(outputFilePath, "w");
  if (oFile == nullptr) {
	printf("Cannot open file!\n");
	return;
  }

  fprintf(oFile, "%d %d\n", size_y_, size_x_);
  for (int i = 0; i < size_y_; ++i) {
	for (int j = 0; j < size_x_; ++j) {
	  fprintf(oFile, "%15.6E ", _matrix[i][j]);
	}
	fprintf(oFile, "\n");
  }

  fclose(oFile);
}

void Matrix::transposeMatrix() {
  for (int i = 0; i < size_y_; ++i) {
	for (int j = 0; j < size_x_; ++j) {
	  double tmp = _matrix[i][j];
	  _matrix[i][j] = _matrix[j][i];
	  _matrix[j][i] = tmp;
	}
  }
}

void Matrix::transposeMatrix(double **matrix, double **transposedMatrix, int mainSizeX, int mainSizeY) {
  if (!matrix || !transposedMatrix) return;
  for (int i = 0; i < mainSizeY; ++i) {
	for (int j = 0; j < mainSizeX; ++j) {
	  transposedMatrix[j][i] = matrix[i][j];
	}
  }
}

void Matrix::sort(double **matrix, int sizeX, int sizeY) {
  if (!matrix) return;
  double *tmpLine;
  double tmpSum;    // удалять нельзя, так как ссылается на строку в матрице!
  auto *lineSums = new double[sizeY];
  getLinesSums(matrix, lineSums, sizeX, sizeY);

  for (int i = 1; i < sizeY; ++i) {
	tmpLine = matrix[i];
	tmpSum = lineSums[i];

	int j = i;
	while (j > 0 && tmpSum < lineSums[i - 1]) {
	  matrix[j] = matrix[j - 1];
	  lineSums[j] = lineSums[j - 1];
	  j--;
	}
	matrix[j] = tmpLine;
	lineSums[j] = tmpSum;
  }

  delete[] lineSums;
}
