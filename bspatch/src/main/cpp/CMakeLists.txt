
cmake_minimum_required(VERSION 3.22.1)

project("bspatch")

include_directories(${CMAKE_SOURCE_DIR}/bzip2)

aux_source_directory(${CMAKE_SOURCE_DIR}/bzip2/ bzip2_src)

add_library(
        bspatch
        SHARED
        bspatch.cpp
        bspatch.c
        ${bzip2_src})

#find_library(
#        log-lib
#        log)

target_link_libraries(
        bspatch
        log)