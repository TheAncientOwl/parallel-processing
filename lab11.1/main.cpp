#include <bits/stdc++.h>
#include <omp.h>

#define PRETTY_FUNC() printf("------------------------------------------------\n"); printf(__PRETTY_FUNCTION__); printf("\n");

///
/// @brief 
///     1. no. threads = no. cores
///     2. no. threads = OMP_NUM_THREADS env variable
///     3. no. threads = omp_set_num_threads(num)
///     4. threads set by num_threads
///
namespace num_threads_control {

///
/// @brief 1. no. threads = no. cores 
///        2. no. threads = OMP_NUM_THREADS env variable
///
void env()
{
#pragma omp parallel
    {
#pragma omp master
        printf("Threads count: %d\n", omp_get_num_threads());
    }

}

///
/// @brief 3. no. threads = omp_set_num_threads(num)
///
void set()
{
    omp_set_num_threads(4);
#pragma omp parallel
    {
#pragma omp master
        printf("Threads count: %d\n", omp_get_num_threads());
    }
}

///
/// @brief 4. no. threads set by num_threads
///
void directive()
{
#pragma omp parallel num_threads(16)
    {
#pragma omp master
        printf("Threads count: %d\n", omp_get_num_threads());
    }
}

} // namespace num_threads_control

namespace critical_atomic {

void test()
{
    for (int j = 0; j < 10; j++)
    {
        int counter{ 0 };

        // shared(contor) by default
#pragma omp parallel shared(counter)
        for (int i = 0; i < 1000; ++i)
        {
#pragma omp critical
            counter += 1;
        }
        printf("Counter: %d\n", counter);
    }
}

} // namespace critical_atomic

namespace barrier {

void test()
{
#pragma omp parallel
    {
        printf(">> Hello from thread %d!\n", omp_get_thread_num());

        for (int i = 0; i < 100000000; i++)
        {
            float test{ 3 / 2 };
        }

#pragma omp barrier

        printf("<< Bye from thread %d!\n", omp_get_thread_num());
    }
}

} // namespace barrier

namespace parallel_for {

void array_sum()
{
    constexpr auto c_arr_size{ 10000 };
    int arr[c_arr_size];
    for (int i = 0; i < c_arr_size; i++)
    {
        arr[i] = i + 1;
    }

    int sum{ 0 };

    // #pragma omp parallel
    //     {
    // #pragma omp for
    //         for (int i = 0; i < c_arr_size; i++)
    //         {
    //             printf(">> Thread(%d) -> iteration(%d)\n", omp_get_thread_num(), i);
    // #pragma omp critical
    //             sum += arr[i];
    //         }
    //     }

#pragma omp parallel for schedule(dynamic, 10)
    for (int i = 0; i < c_arr_size; i++)
    {
        printf(">> Thread(%d) -> iteration(%d)\n", omp_get_thread_num(), i);
#pragma omp critical
        sum += arr[i];
    }

    printf("Sum: %d\n", sum);
}

void benchmark()
{

}

} // namespace parallel_for


int main()
{
    // num_threads_control::env();
    // num_threads_control::set();
    // num_threads_control::directive();

    // critical_atomic::test();

    // barrier::test();

    // parallel_for::array_sum();
    parallel_for::benchmark();

    return 0;
}
