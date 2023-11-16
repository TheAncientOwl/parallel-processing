#include <bits/stdc++.h>
#include <omp.h>

void logRunningMode()
{
    if (omp_in_parallel())
    {
        printf(">> Running in parallel!\n");
    }
    else
    {
        printf(">> Running sequential!\n");
    }
}

int main()
{
    printf(">> Start\n");

    const auto c_processors_count{omp_get_num_procs()};
    printf(">> Processors count: %d\n", c_processors_count);

    const auto c_threads_count{omp_get_num_threads()};
    const auto c_max_threads_count{omp_get_max_threads()};
    printf(">> Threads count: %d\n", c_threads_count);
    printf(">> Max threads count: %d\n", c_max_threads_count);

    const auto c_main_thread_id{omp_get_thread_num()};
    printf(">> Main thread ID: %d\n", c_main_thread_id);

    logRunningMode();

    printf("------------------------\n");
#pragma omp parallel
    {
        const auto c_thread_id{omp_get_thread_num()};
        printf(">> Hello! -> ID: %d\n", c_thread_id);

        if (c_thread_id == 0)
        {
            logRunningMode();

            const auto c_threads_count{omp_get_num_threads()};
            const auto c_max_threads_count{omp_get_max_threads()};
            printf(">> Threads count: %d\n", c_threads_count);
            printf(">> Max threads count: %d\n", c_max_threads_count);
        }
    }
    printf("------------------------\n");

    printf(">> End\n");

    return 0;
}
