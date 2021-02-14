cmake_minimum_required(VERSION 3.4.1)

file(GLOB SOURCES ../libcxx_helper/*.cpp)

add_library(nativefaceswap STATIC nativefaceswap.cpp)

install()
