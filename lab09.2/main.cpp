#include <bits/stdc++.h>
#include <omp.h>

void benchmark(const std::string& message, int(*func)(long, long), long begin, long end)
{
    printf(">> [%28s] begin...\n", message.c_str());
    const auto start_time{ omp_get_wtime() };
    const auto result{ func(begin, end) };
    const auto end_time{ omp_get_wtime() };
    printf(">> [%28s] end...   -> result: %d in %fs\n", message.c_str(), result, (end_time - start_time));
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


int main()
{
    // constexpr auto c_interval_end{ 3e5 };
    constexpr auto c_interval_end{ 5e5 };

    benchmark("sequentialSolution", solutions::sequentialSolution, 1, c_interval_end);
    benchmark("parallelSolutionWithMutex", solutions::parallelSolutionWithMutex, 1, c_interval_end);
    benchmark("parallelSolutionWithPrivate", solutions::parallelSolutionWithPrivate, 1, c_interval_end);
    benchmark("loadBalancedParallelSolution", solutions::loadBalancedParallelSolution, 1, c_interval_end);

    return 0;
}
