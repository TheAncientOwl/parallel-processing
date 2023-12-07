#include <bits/stdc++.h>
#include <omp.h>

template<typename Callable, typename... Args>
void benchmark(const std::string& message, Callable func, Args&&... args)
{
    printf(">> [%29s] begin...\n", message.c_str());
    const auto start_time{ omp_get_wtime() };
    const auto result{ func(std::forward<Args>(args)...) };
    const auto end_time{ omp_get_wtime() };
    printf(">> [%29s] end...   -> result: %d in %fs\n", message.c_str(), result, (end_time - start_time));
}

bool isPrime(long num)
{
    for (long i = 2; i <= num / 2; i++)
    {
        if (num % i == 0)
        {
            return false;
        }
    }

    return true;
}

///
/// @see lab09.2
///
namespace solutions {

int sequentialSolution(long begin, long end)
{
    auto primes_count{ 0 };
    for (long i = begin; i <= end; i += 1)
    {
        if (isPrime(i))
        {
            primes_count += 1;
        }
    }
    return primes_count;
}

int parallelSolutionWithMutex(long begin, long end)
{
    auto primes_count{ 0 };

#pragma omp parallel
    {
        for (long i = begin + omp_get_thread_num(); i <= end; i += omp_get_num_threads())
        {
            if (isPrime(i))
            {
#pragma omp atomic
                primes_count += 1;
            }
        }
    }

    return primes_count;
}

int parallelSolutionWithPrivate(long begin, long end)
{
    auto primes_count{ 0 };
    auto thread_primes_count{ 0 };

#pragma omp parallel firstprivate(thread_primes_count)
    {
        for (long i = begin + omp_get_thread_num(); i <= end; i += omp_get_num_threads())
        {
            if (isPrime(i))
            {
                thread_primes_count += 1;
            }
        }

#pragma omp atomic
        primes_count += thread_primes_count;
    }

    return primes_count;
}

int loadBalancedParallelSolution(long begin, long end)
{
    auto primes_count{ 1 }; // we skip check for number 2
    auto thread_primes_count{ 0 };

#pragma omp parallel firstprivate(thread_primes_count)
    {
        for (long i = begin + omp_get_thread_num() * 2; i <= end; i += 2 * omp_get_num_threads())
        {
            if (isPrime(i))
            {
                thread_primes_count += 1;
            }
        }

#pragma omp atomic
        primes_count += thread_primes_count;
    }

    return primes_count;
}

} // namespace solutions

namespace omp_solutions {

int parallelForStatic(long begin, long end)
{
    auto primes_count{ 0 };

#pragma omp parallel for
    for (long i = begin; i <= end; i += 1)
    {
        if (isPrime(i))
        {
#pragma omp atomic
            primes_count += 1;
        }
    }

    return primes_count;
}

int parallelForDynamic(long begin, long end)
{
    auto primes_count{ 0 };

#pragma omp parallel for schedule(dynamic)
    for (long i = begin; i <= end; i += 1)
    {
        if (isPrime(i))
        {
#pragma omp atomic
            primes_count += 1;
        }
    }

    return primes_count;
}

int parallelForDynamicCustom(long begin, long end)
{
    auto primes_count{ 0 };

#pragma omp parallel for schedule(dynamic, 1000)
    for (long i = begin; i <= end; i += 1)
    {
        if (isPrime(i))
        {
#pragma omp atomic
            primes_count += 1;
        }
    }

    return primes_count;
}

int parallelForGuided(long begin, long end)
{
    auto primes_count{ 0 };

#pragma omp parallel for schedule(guided)
    for (long i = begin; i <= end; i += 1)
    {
        if (isPrime(i))
        {
#pragma omp atomic
            primes_count += 1;
        }
    }

    return primes_count;
}

int parallelForGuidedCustom(long begin, long end)
{
    auto primes_count{ 0 };

#pragma omp parallel for schedule(guided, 1000)
    for (long i = begin; i <= end; i += 1)
    {
        if (isPrime(i))
        {
#pragma omp atomic
            primes_count += 1;
        }
    }

    return primes_count;
}

} // namespace omp_solutions

namespace showcase {

int minArray(int* arr, int size)
{
    int min{ arr[0] };

    for (int i = 0; i < size; i++)
    {
        if (arr[i] < min)
        {
            min = arr[i];
        }
    }

    return min;
}

int minArrayOmp(int* arr, int size)
{
    int min{ arr[0] };

#pragma omp parallel for
    for (int i = 0; i < size; i++)
    {
#pragma omp critical
        {
            if (arr[i] < min)
            {
                min = arr[i];
            }
        }
    }

    return min;
}

} // namespace showcase

int main()
{
    constexpr auto c_interval_end{ 5e5 };

    // benchmark("sequentialSolution", solutions::sequentialSolution, 1, c_interval_end);
    // benchmark("parallelSolutionWithMutex", solutions::parallelSolutionWithMutex, 1, c_interval_end);
    // benchmark("parallelSolutionWithPrivate", solutions::parallelSolutionWithPrivate, 1, c_interval_end);
    // benchmark("loadBalancedParallelSolution", solutions::loadBalancedParallelSolution, 1, c_interval_end);
    // printf("--------------------------------------------------------------------------\n");
    // benchmark("omp::parallelForStatic", omp_solutions::parallelForStatic, 1, c_interval_end);
    // benchmark("omp::parallelForDynamic", omp_solutions::parallelForDynamic, 1, c_interval_end);
    // benchmark("omp::parallelForDynamicCustom", omp_solutions::parallelForDynamicCustom, 1, c_interval_end);
    // benchmark("omp::parallelForGuided", omp_solutions::parallelForGuided, 1, c_interval_end);
    // benchmark("omp::parallelForGuidedCustom", omp_solutions::parallelForGuidedCustom, 1, c_interval_end);

    constexpr auto c_arr_size{ 50000000 };
    const auto arr_holder = std::make_unique<int[]>(c_arr_size);

    const auto arr = arr_holder.get();
    for (int i = 0; i < c_arr_size; i++)
    {
        arr[i] = c_arr_size - i;
    }

    benchmark("minArray", showcase::minArray, arr, c_arr_size);
    benchmark("minArrayOmp", showcase::minArrayOmp, arr, c_arr_size);

    return 0;
}
