cmake_minimum_required(VERSION 3.0.0)

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

project(lab08_false_cache_sharing VERSION 0.1.0 LANGUAGES C CXX)

include(CTest)
enable_testing()

add_executable(lab08_false_cache_sharing main.cpp)

find_package(OpenMP)
if(OpenMP_CXX_FOUND)
    target_link_libraries(lab08_false_cache_sharing PUBLIC OpenMP::OpenMP_CXX)
endif()

set(CPACK_PROJECT_NAME ${PROJECT_NAME})
set(CPACK_PROJECT_VERSION ${PROJECT_VERSION})
include(CPack)
