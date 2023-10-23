## 1. Dev environment:
```
* IDE: VSCode
* OS: (WSL) Ubuntu 22.04.3 LTS on Windows 10 x86_64
* 16GB RAM 3200MHz
* 12 Threads (8 cores, 12 logical processors)
```

## 2. How to run:
```
# on linux linux:
$ ./run-on-linux.sh (on linux)

# cross-platform using javac & java:
$ cd src
$ javac ./MainFull.java
$ java MainFull
```

## 3. Solutions 
```
>> Added benchmarks (end of interval from 200000 to 800000) and demo
>> All classes are copied in MainFull.java for easy compilation
```

### 3.1 Sequential Solution 1
```
/**
 * @brief Compute the count using naive divisors algorithm
 *        (passing from i:[1 to number], check if number divisible by i).
 */
```

### 3.2 Sequential Solution 2
```
/**
 * @brief Compute the count using improved naive divisors algorithm
 *        (passing from i:[1 to number / 2], check if number divisible by i).
 */
```

### 3.3 Sequential Solution 3
```
/**
 * @brief Compute divisorsCount array -> find max
 *        -> find numbers with max divisors count.
 *        OBS: no parallelism, race conditions on divisorsCount array
 */
```

### 3.4 Parallel Solution 1 For Sequential Solution 1
```
/**
 * @brief Divide the range into chunks and compute for each chunk
 *        (on separate threads) using SequentialSolution1 algorithm.
 */
```

### 3.5 Parallel Solution 1 For Sequential Solution 2
```
/**
 * @brief Divide the range into chunks and compute for each chunk
 *        (on separate threads) using SequentialSolution2 algorithm.
 */
```

### 3.5 Parallel Solution 2 For Sequential Solution 1
```
/**
 * @brief Spread the numbers in the interval evenly accross all threads.
 *        Use SequentialSolution1 divisors approach
 */
```

### 3.6 Parallel Solution 2 For Sequential Solution 2
```
/**
 * @brief Spread the numbers in the interval evenly accross all threads.
 *        Use SequentialSolution2 divisors approach
 */
```

## 4. Observations
```
1. Sequential solution 3 is the fastest, but needs the most amount of RAM (for divisors array).
2. The second approach (divisors from 1 to x / 2 is 2 times faster than the previous one > "hah, who could have thought?:)"
3. For sequential solutions 1 and 2, parallelisation brings a boost in performance of 
    * 5-6 times faster (for parallel approach 1)
    * 7-8 times faster (for parallel approach 2)
4. The boost in performance with the parallel approaches are noticeable when the interval gets bigger and bigger
```

## 5. Conclusions?
```
1. Benchmark, benchmark, benchmark
2. Find a suite of algorithms
3. Benchmark, benchmark, benchmark
4. Pick the best option depending on your needs and hardware (RAM, processors, motherboard, etc...)
```
