/// ----------------------------------------------------------------------------
/// @author Alexandru Delegeanu
///
/// @version C++20
///
/// @see https://en.cppreference.com/w/cpp/io/basic_osyncstream
/// @see https://en.cppreference.com/w/cpp/string/basic_string_view
/// @see https://en.cppreference.com/w/cpp/language/lambda
///
/// ----------------------------------------------------------------------------

#include <iomanip>
#include <iostream>
#include <string>
#include <string_view>
#include <syncstream>
#include <thread>
#include <vector>

#include "omp.h"

using namespace std::literals;

///
/// @brief Benchmarks given function and prints results in a sync way to std::cout.
/// @tparam Callable Function type
/// @tparam ...Args Argument types to be passed to given function
/// @param label String info value to print while benchmarking
/// @param callable Function to benchmark
/// @param ...args Arguments to pass to benchmarked function
///
template<typename Callable, typename... Args>
double benchmark(std::string_view label, Callable&& callable, Args&&... args)
{
    static constexpr auto c_label_width{ 35 };
    std::osyncstream sync_cout{ std::cout };

    sync_cout
        << std::setfill('-') << std::setw(80) << '-' << std::setfill(' ') << std::endl
        << ">> " << std::left << std::setw(c_label_width) << label << "-> begin..." << std::endl;
    sync_cout.emit();

    const auto begin = omp_get_wtime();
    const auto result = callable(std::forward<Args>(args)...);
    const auto end = omp_get_wtime();

    const auto duration{ end - begin };

    sync_cout
        << ">> " << std::left << std::setw(c_label_width) << label
        << "-> ended -> result = " << result
        << " in " << std::fixed << std::setprecision(8) << duration << "s" << std::endl;
    sync_cout.emit();

    return duration;
}

///
/// @brief Compares to values but also adds
///        performance overhead for testing purposes.
/// @return true whether a <= b, false otherwise
///
bool isLessOrEqual(int a, int b)
{
    int dummy{ 0 };
    for (int i = 0; i < 100; i++)
    {
        float temp = (float) a / i;
        dummy += (int) temp;
    }

    return (a <= b);
}

namespace sequential {

///
/// @brief Sequential approach of finding min value in array
/// @return minimum value in array
///
int getMin(const std::vector<int>& values)
{
    auto min{ values[0] };
    for (auto val : values)
    {
        if (isLessOrEqual(val, min))
        {
            min = val;
        }
    }
    return min;
}

} // namespace sequential

namespace cpp11Threads {

///
/// @brief Parallel approach of finding min value in array using C++11 threads.
///        Break the array in smaller chunks and spread them between threads.
/// @return minimum value in array
///
int getMin(const std::vector<int>& values)
{
    const auto chunkMin = [&values](int begin, int end, int& outMin) -> void {
        outMin = values[begin];
        for (int i = begin + 1; i < end; ++i)
        {
            if (isLessOrEqual(values[i], outMin))
            {
                outMin = values[i];
            }
        }
        };

    const auto c_threads_count{ omp_get_num_procs() };

    const auto c_gap{ 1000 };
    std::vector<int> results(c_threads_count * c_gap, 0);

    std::vector<std::thread> threads{};
    threads.reserve(c_threads_count);

    const auto c_chunk_size{ values.size() / c_threads_count };
    for (int thread_id = 0; thread_id < c_threads_count; ++thread_id)
    {
        auto begin = thread_id * c_chunk_size;
        auto end = thread_id == (c_threads_count - 1) ? values.size() : (thread_id + 1) * c_chunk_size;

        threads.emplace_back(chunkMin, begin, end, std::ref(results[thread_id * c_gap]));
    }

    for (auto& thread : threads)
    {
        thread.join();
    }

    auto min{ results[0] };
    for (int i = 1; i < c_threads_count; ++i)
    {
        const auto pos{ i * c_gap };
        if (isLessOrEqual(results[pos], min))
        {
            min = results[pos];
        }
    }

    return min;
}

} // namespace cpp11Threads

namespace ompShared {

///
/// @brief Parallel approach of finding min value in array using omp & shared variable.
/// @note Since we are doing things in parallel, checking and assigning of min value with elements is the critical section.
///       So we have to "wait" for every check => no more parallelism...
/// @return minimum value in array
///
int getMin(const std::vector<int>& values)
{
    auto min{ values[0] };

    const auto c_threads_count{ omp_get_num_procs() };
    const auto c_chunk_size{ values.size() / c_threads_count };

#pragma omp parallel
    {
        const auto c_thread_id{ omp_get_thread_num() };
        const auto begin{ c_thread_id * c_chunk_size };
        const auto end{ c_thread_id == c_threads_count - 1 ? values.size() : (c_thread_id + 1) * c_chunk_size };

        for (int i = begin; i < end; ++i)
        {
#pragma omp critical
            if (isLessOrEqual(values[i], min))
            {
                min = values[i];
            }

        }
    }

    return min;
}

} // namespace ompShared

namespace ompPrivate {

///
/// @brief Parallel approach of finding min value in array using omp & private variable.
///        Comparing thread local min with global min is the critical section now.
/// @return minimum value in array
///
int getMin(const std::vector<int>& values)
{
    auto min{ values[0] };
    auto thread_min{ std::numeric_limits<int>::max() };

    const auto c_threads_count{ omp_get_num_procs() };
    const auto c_chunk_size{ values.size() / c_threads_count };

#pragma omp parallel firstprivate(thread_min)
    {
        const auto c_thread_id{ omp_get_thread_num() };
        const auto begin{ c_thread_id * c_chunk_size };
        const auto end{ c_thread_id == c_threads_count - 1 ? values.size() : (c_thread_id + 1) * c_chunk_size };

        for (int i = begin; i < end; ++i)
        {
            if (isLessOrEqual(values[i], thread_min))
            {
                thread_min = values[i];
            }
        }

#pragma omp critical
        {
            if (isLessOrEqual(thread_min, min))
            {
                min = thread_min;
            }
        }
    }

    return min;
}

} // namespace ompPrivate


int main()
{
    const auto c_processors_count{ omp_get_num_procs() };
    std::cout << ">> Processors count: " << c_processors_count << std::endl;

    constexpr auto c_elements_count{ (int) 1e8 };
    std::vector<int> randomValues(c_elements_count);
    std::vector<int> descendingValues(c_elements_count);

    // generate random values
    std::cout << ">> Generating random values..." << std::endl;
    constexpr auto c_seed{ 1000 };
    srand(c_seed);
    for (int i = 0; i < c_elements_count; i++)
    {
        randomValues[i] = rand();
        descendingValues[i] = c_elements_count - i;
    }

    std::cout << ">> Let the tests begin..." << std::endl;

    benchmark("Sequential, random", sequential::getMin, randomValues);
    benchmark("Sequential, descending", sequential::getMin, descendingValues);

    benchmark("C++11 Threads, random", cpp11Threads::getMin, randomValues);
    benchmark("C++11 Threads, descending", cpp11Threads::getMin, descendingValues);

    benchmark("OMP parallel & shared, random", ompShared::getMin, randomValues);
    benchmark("OMP parallel & shared, descending", ompShared::getMin, descendingValues);

    benchmark("OMP parallel & private, random", ompPrivate::getMin, randomValues);
    benchmark("OMP parallel & private, descending", ompPrivate::getMin, descendingValues);

    return 0;
}
