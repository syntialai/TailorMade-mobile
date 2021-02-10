cmake_minimum_required(VERSION 3.4.1)

add_library(opencv_jni_shared STATIC nativefaceswap.cpp)

include_directories(../native/jni/include)
