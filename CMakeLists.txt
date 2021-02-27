# cmake_minimum_required(VERSION <specify CMake version here>)
project(CalcMath)

set(CMAKE_CXX_STANDARD 17)
set (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -static-libstdc++ -static-libgcc")

add_executable(CalcMath main.cpp Matrix.cpp Matrix.h includes.h)