cmake_minimum_required (VERSION 3.7)

project(calculator-cli)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++14")

find_package(Boost REQUIRED COMPONENTS program_options)
include_directories(${Boost_INCLUDE_DIR})

add_executable(calculator-cli
                src/main.cpp
                src/calculator-cli.cpp
)

include_directories(
                src/
)

target_link_libraries(calculator-cli ${Boost_LIBRARIES})
