class Main {
    private static final long N = (long) 6e5;

    public static void main(String[] args) {
        {
            System.out.println(">> Sequential start...");
            double testStart = System.currentTimeMillis();
            int count = Prime.count(1, N);
            double testEnd = System.currentTimeMillis();

            System.out.println(String.format(">> Sequential test duration = %f seconds, count = %d",
                    ((testEnd - testStart) / 1000), count));
        }

        {
            int processorsCount = Runtime.getRuntime().availableProcessors();

            System.out.println(">> Parallel 1 start...");
            double testStart = System.currentTimeMillis();
            int count = Prime.countParallel(1, N, processorsCount);
            double testEnd = System.currentTimeMillis();

            System.out.println(String.format(">> Parallel test duration 1 = %f seconds, count = %d on %d threads",
                    ((testEnd - testStart) / 1000), count, processorsCount));
        }

        {
            int processorsCount = Runtime.getRuntime().availableProcessors();

            System.out.println(">> Parallel 2 start...");
            double testStart = System.currentTimeMillis();
            int count = Prime.countParallel(1, N, processorsCount, processorsCount);
            double testEnd = System.currentTimeMillis();

            System.out.println(String.format(">> Parallel test duration 2 = %f seconds, count = %d on %d threads",
                    ((testEnd - testStart) / 1000), count, processorsCount));
        }
    }
}
