//
// Created by gosvoh on 26.02.2021.
//

#include "includes.h"
#include "Matrix.h"

namespace fs = std::filesystem;

/**
* Получить абсолютный путь к указанному файлу или директории в виде строки.
* Если путь начинается с '/' или с '\', то он считается абсолютным и
* возвращается неизменным, в противном случае он считается относительным
* для текущей рабочей директории.
*
* @param path путь для парса
*
* @return абсолютный путь
*/
const char *getAbsolutePath(std::string path);

int main(int argc, char *argv[]) {
  if (argc < 2) {
	printf("Usage: matrix <input file path> [output file path (not implemented yet)]\n");
	return 1;
  }
  Matrix matrix(getAbsolutePath(argv[1]));
  //matrix.print();
  //matrix.simpleInsertSort();
  //matrix.print();
  //if (argc >= 2) matrix.printToFile(getAbsolutePath(argv[2]));
  return 0;
}

const char *getAbsolutePath(std::string path) {
  std::replace(path.begin(), path.end(), '\\', '/');
  std::string newDir;
  bool isUnix, windowsPath = std::regex_search(path, std::regex("[a-zA-Z]:"));
#ifdef _WIN32
  isUnix = false;
#else
  isUnix = true;
#endif
  if (windowsPath && isUnix) {
	printf("Wrong path!\n");
	exit(1);
  }
  if (path[0] == '/' || windowsPath) return path.c_str();
  else if (path[0] == '~') {
	newDir = isUnix ? getenv("HOME") : (std::string(getenv("HOMEDRIVE")) + std::string(getenv("HOMEPATH")));
	newDir.insert(newDir.end(), newDir.at(newDir.size() - 1) == '/' ? '\0' : '/');
	return path.replace(0, 2, "").insert(0, newDir).c_str();
  } else {
	newDir = fs::current_path().string();
	newDir.insert(newDir.end(), newDir.at(newDir.size() - 1) == '/' ? '\0' : '/');
	return path.insert(0, newDir).c_str();
  }
}