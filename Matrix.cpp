//
// Created by gosvoh on 26.02.2021.
//

#include "includes.h"
#include "Matrix.h"

Matrix::Matrix(const char *inputFilePath) {
  init(inputFilePath);
}

Matrix::~Matrix() {
  if (matrix == nullptr) return;
  printf("Deleting matrix...\n");
  for (int i = 0; i < n; ++i) {
	delete[] matrix[i];
  }
  delete[] matrix;
  printf("Matrix deleted!");
}

void Matrix::getLinesSums(double **matrix, double *buf, int sizeX, int sizeY) {
  for (int i = 0; i < sizeY; i++) for (int j = 0; j < sizeX; j++) buf[i] += matrix[i][j];
}

void Matrix::init(const char *path) {
  FILE *iFile;
  char *sizes;
  char *newSizes;

  iFile = fopen(path, "r");
  sizes = new char[64];
  fgets(sizes, 64, iFile);
  newSizes = strtok(sizes, " \t");
  n = strtol(newSizes, NULL, 10);
  newSizes = strtok(NULL, " \t");
  m = strtol(newSizes, NULL, 10);
  create(n, m);
  
  delete[] sizes;
  fclose(iFile);
}

void Matrix::create(const int n, const int m) {
  matrix = new double *[n];
  for (int i = 0; i < n; ++i) {
	matrix[i] = new double[m];
  }
  printf("Matrix created!\n");
}

void Matrix::print() {
  for (int i = 0; i < n; ++i) {
	for (int j = 0; j < m; ++j) {
	  printf("%15.6E", matrix[i][j]);
	}
	printf("\n");
  }
}

void Matrix::simpleInsertSort() {
  if (n == m) {
	transposeMatrix();
	sort(matrix, n, m);
	transposeMatrix();
  } else {
	auto **transposedMatrix = new double *[m];
	for (int i = 0; i < m; ++i) transposedMatrix[i] = new double[n];
	transposeMatrix(matrix, transposedMatrix, 0, 0);
	sort(transposedMatrix, n, m);
	transposeMatrix(transposedMatrix, matrix, 0, 0);
	for (int i = 0; i < m; ++i) delete[] transposedMatrix[i];
	delete[] transposedMatrix;
  }
}

void Matrix::printToFile(const char *outputFilePath) {

}

void Matrix::transposeMatrix() {
  for (int i = 0; i < n; ++i) {
	for (int j = 0; j < m; ++j) {
	  double tmp = matrix[i][j];
	  matrix[i][j] = matrix[j][i];
	  matrix[j][i] = tmp;
	}
  }
}

void Matrix::transposeMatrix(double **matrix, double **transposedMatrix, int sizeX, int sizeY) {
  for (int i = 0; i < sizeY; ++i) {
	for (int j = 0; j < sizeX; ++j) {
	  transposedMatrix[i][j] = matrix[j][i];
	}
  }
}

void Matrix::sort(double **matrix, int sizeX, int sizeY) {
  double *tmpLine;
  double tmpSum;
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

  delete tmpLine;
  delete[] lineSums;
}
