package project.benchmark;


import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;


public class BenchmarkRunner {

	final public static String[] datasets = {"Users_1000",
//											"Users_5000",
//											"Users_10000",
											"Users_50000"
	};

	final public static boolean debugRun = false;
	
	
    public static void main(String[] args) throws RunnerException {

        Options options = new OptionsBuilder()
                .include(DynamoDBBenchmark.class.getSimpleName())
                .include(ListBenchmark.class.getSimpleName())
                .include(SortedListBenchmark.class.getSimpleName())
                .include(TreeBenchmark.class.getSimpleName())
                .timeout(TimeValue.minutes(1))
                .param("tableName", datasets)
                .measurementIterations(1)
                .warmupIterations(1) // Default is 5
                .warmupTime(TimeValue.seconds(5)) // Default is 10
                .forks(1)
                .build();

        new Runner(options).run();
    }

}