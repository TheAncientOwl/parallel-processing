#include <bits/stdc++.h>
#include <omp.h>

int main()
{
#pragma omp parallel
    {
        std::cout << "Hello, from lab09!\n";
    }

    return 0;
}
