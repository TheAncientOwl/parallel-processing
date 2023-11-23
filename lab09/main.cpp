#include <bits/stdc++.h>
#include <omp.h>

#define PRETTY_FUNC() printf("------------------------------------------------\n"); printf(__PRETTY_FUNCTION__); printf("\n");
namespace example {

void base()
{
    PRETTY_FUNC();

#pragma omp parallel
    {
        const auto c_thread_id{ omp_get_thread_num() };
        printf(">> Thread ID: %d\n", c_thread_id);

        if (c_thread_id == 0)
        {
            printf(">> Threads count: %d\n", omp_get_num_threads());
        }

    }
}

void warning()
{
    PRETTY_FUNC();

    // Only first instruction after #pragma will execute in parallel
#pragma omp parallel
    printf("Hello!\n");
    printf("Bye!\n");
}

void showcase()
{
    PRETTY_FUNC();

    const auto c_threads_count{ 4 };
#pragma omp parallel num_threads(c_threads_count)
    {
        printf(">> Hello from thread %d\n", omp_get_thread_num());
    }

    // Note: It may crash.
    //     const auto c_absurd_threads_count{ 50000 };
    // #pragma omp parallel num_threads(c_absurd_threads_count)
    //     {
    //         if (omp_get_thread_num() == 0)
    //         {
    //             printf(">> We wanted %d threads, but we got %d threads.", c_absurd_threads_count, omp_get_num_threads());
    //         }
    //     }

    bool execute_in_parallel{ false };
#pragma omp parallel num_threads(6) if(execute_in_parallel)
    {
        printf(">> Block executed in parallel.\n");
    }
}

void visibility()
{
    PRETTY_FUNC();

    auto shared_variable{ 100 };
    auto private_variable{ 10 };
    auto firstprivate_variable{ 60 };

#pragma omp parallel default(none)
    {
        auto shared_variable{ 10 };
        shared_variable += 1;
        // private_variable += 1;
        // firstprivate_variable += 1;
    }

    printf(">> shared_variable: %d\n", shared_variable);
    printf(">> private_variable: %d\n", private_variable);
    printf(">> firstprivate_variable: %d\n", firstprivate_variable);

    printf("\n");

#pragma omp parallel \
    shared(shared_variable) private(private_variable) firstprivate(firstprivate_variable)
    {
        auto private_variable{ 0 };

        shared_variable += 1;
        private_variable += 1;
        firstprivate_variable += 1;

        if (omp_get_thread_num() == 0)
        {
            printf(">> on thread 0: private_variable: %d\n", private_variable);
            printf(">> on thread 0: firstprivate_variable: %d\n", firstprivate_variable);
        }
    }

    printf(">> shared_variable: %d\n", shared_variable);
    printf(">> private_variable: %d\n", private_variable);
    printf(">> firstprivate_variable: %d\n", firstprivate_variable);
}

void vectorSum()
{
    PRETTY_FUNC();

    const auto c_arr_size{ 100 };
    const auto c_arr_holder = std::make_unique<int[]>(c_arr_size);

    auto arr = c_arr_holder.get();
    for (int i = 0; i < c_arr_size; i++)
    {
        arr[i] = i + 1;
    }

    auto sum{ 0 };
    auto partial_sum{ 0 };

    omp_lock_t mutex{};
    omp_init_lock(&mutex);

#pragma omp parallel firstprivate(partial_sum)
    {
        for (int i = omp_get_thread_num(); i < c_arr_size; i += omp_get_num_threads())
        {
            partial_sum += arr[i];
        }

        omp_set_lock(&mutex);
        sum += partial_sum;
        omp_unset_lock(&mutex);
    }

    printf(">> Sum: %d\n", sum);

    sum = 0;
#pragma omp parallel firstprivate(partial_sum)
    {
#pragma omp single
        {
            printf(">> Single sequence\n");
        }

        for (int i = omp_get_thread_num(); i < c_arr_size; i += omp_get_num_threads())
        {
            partial_sum += arr[i];
        }

#pragma omp atomic
        sum += partial_sum;

        // or critical instead of atomic, but it can use blocks
        // #pragma omp critical
        //         {
        //             sum += partial_sum;
        //         }
    }
    printf(">> Sum: %d\n", sum);
}

} // namespace example

int main()
{
    printf(">> Available cores: %d\n", omp_get_num_procs());
    printf(">> Threads count: %d\n", omp_get_num_threads());

    example::base();
    example::warning();
    example::showcase();
    example::visibility();
    example::vectorSum();

    return 0;
}
