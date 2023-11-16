#include <bits/stdc++.h>
#include <omp.h>

void benchmark(const std::string &label, int (*pf)(const int *, int), const int *arr, int size)
{
    printf(">> Test %s\n", label.c_str());

    const auto start_time{omp_get_wtime()};
    const auto result = pf(arr, size);
    const auto end_time{omp_get_wtime()};

    printf(">> Result = %d\n", result);
    printf(">> Time: %fs\n", end_time - start_time);
    printf("----------------------------\n");
}

int *makeArray(int size)
{
    srand(100);

    const auto arr = new int[size];
    for (int i = 0; i < size; ++i)
    {
        // arr[i] = rand();
        arr[i] = i;
    }
    return arr;
}

int sequentialSolution(const int *arr, int size)
{
    int count{0};
    for (int i = 0; i < size; ++i)
    {
        // printf("%d\n", i);
        if (arr[i] % 2 == 0)
        {
            count += 1;
        }
    }
    return count;
}

void sequentialSolutionWithStep(const int *arr, int size, int start_idx, int step, int &out_result)
{
    for (int i = start_idx; i < size; i += step)
    {
        if (arr[i] % 2 == 0)
        {
            out_result += 1;
        }
    }
}

int parallelSolutionFalseCacheSharing(const int *arr, int size)
{
    const auto c_threads_count{omp_get_num_procs()};
    const auto results = new int[c_threads_count];
    for (int i = 0; i < c_threads_count; ++i)
    {
        results[i] = 0;
    }

    std::vector<std::thread> threads{};
    for (int i = 0; i < c_threads_count; ++i)
    {
        threads.emplace_back(sequentialSolutionWithStep, arr, size, i, c_threads_count, std::ref(results[i]));
    }

    for (auto &thread : threads)
    {
        thread.join();
    }

    int results_sum{0};
    for (int i = 0; i < c_threads_count; ++i)
    {
        results_sum += results[i];
    }

    free(results);

    return results_sum;
}

int parallelSolutionWithoutFalseCacheSharing(const int *arr, int size)
{
    const auto c_threads_count{omp_get_num_procs()};
    const auto c_gap{1000};
    const auto results = new int[c_threads_count * c_gap];
    for (int i = 0; i < c_threads_count; ++i)
    {
        results[i] = 0;
    }

    std::vector<std::thread> threads{};
    for (int i = 0; i < c_threads_count; ++i)
    {
        threads.emplace_back(sequentialSolutionWithStep, arr, size, i, c_threads_count, std::ref(results[i * c_gap]));
    }

    for (auto &thread : threads)
    {
        thread.join();
    }

    int results_sum{0};
    for (int i = 0; i < c_threads_count; ++i)
    {
        results_sum += results[i * c_gap];
    }

    free(results);

    return results_sum;
}

int main()
{
    printf(">> Start\n");

    printf(">> Start num gen\n");
    const auto c_arr_size{1000000000};
    const auto arr = makeArray(c_arr_size);
    printf(">> End num gen\n");

    benchmark("Sequential solution", sequentialSolution, arr, c_arr_size);
    benchmark("Parallel solution with false sharing cache", parallelSolutionFalseCacheSharing, arr, c_arr_size);
    benchmark("Parallel solution without false sharing cache", parallelSolutionWithoutFalseCacheSharing, arr, c_arr_size);

    free(arr);
    printf(">> End\n");

    return 0;
}
