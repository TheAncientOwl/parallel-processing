#include <bits/stdc++.h>

void hello() 
{
    printf("Hello!\n");
}

void helloID(int id)
{
    printf("Hello from thread %d!\n", id);
}

void makeSum(int x, int y, int& out)
{
    out = x + y;
}

void testHello()
{
    std::thread thread1{ hello };
    std::thread thread2{ helloID, 0 };

    thread1.join();
    thread2.join();

    const int x{1};
    const int y{2};
    int sum{0};
    std::thread thread_sum{makeSum, x, y, std::ref(sum)};
    thread_sum.join();
    printf("%d + %d = %d\n", x, y, sum);

    std::vector<std::thread> threads{};

    for (int i = 0; i < 10; ++i)
    {
        threads.emplace_back(helloID, i + 1);
    }

    for (auto& thread : threads)
    {
        thread.join();
    }
}

void increment(int& x, int iterations_count)
{
    for (int i = 0; i < iterations_count; ++i)
    {
        ++x;
    }
}

void incrementWithMutex(int& x, int iterations_count, std::mutex& mutex)
{
    for (int i = 0; i < iterations_count; ++i)
    {
        std::lock_guard<std::mutex> lock{ mutex };
        ++x;
    }
}

void incrementWithAtomic(std::atomic<int>& x, int iterations_count)
{
    for (int i = 0; i < iterations_count; ++i)
    {
        ++x;
    }
}

void testRaceCondition()
{
    {
        int x{ 0 };
        const int it_count{ 1000000 };

        std::thread thread1{increment, std::ref(x), it_count};
        std::thread thread2{increment, std::ref(x), it_count};

        thread1.join();
        thread2.join();
        printf("X should be %d, X = %d\n", it_count * 2, x);
    }

    {
        int x{ 0 };
        std::mutex mutex{};
        const int it_count{ 1000000 };

        std::thread thread1{incrementWithMutex, std::ref(x), it_count, std::ref(mutex)};
        std::thread thread2{incrementWithMutex, std::ref(x), it_count, std::ref(mutex)};

        thread1.join();
        thread2.join();
        printf("X should be %d, X = %d\n", it_count * 2, x);
    }

    {
        std::atomic<int> x{ 0 };
        const int it_count{ 1000000 };

        std::thread thread1{incrementWithAtomic, std::ref(x), it_count};
        std::thread thread2{incrementWithAtomic, std::ref(x), it_count};

        thread1.join();
        thread2.join();
        printf("X should be %d, X = %d\n", it_count * 2, x.load());
    }
}

int main()
{
    // testHello();
    testRaceCondition();

    printf("THE END!\n");
    return 0;
}
