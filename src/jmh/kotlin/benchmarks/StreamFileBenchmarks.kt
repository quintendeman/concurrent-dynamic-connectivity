package benchmarks

import benchmarks.util.*
import benchmarks.util.executors.ScenarioExecutor
import benchmarks.util.generators.*
import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit

private const val TOTAL_SCENARIO_SIZE = 15_000_000

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Measurement(iterations = iterations, time = TIME_IN_SECONDS, timeUnit = TimeUnit.SECONDS)
@Warmup(iterations = warmupIterations, time = TIME_IN_SECONDS, timeUnit = TimeUnit.SECONDS)
open class StreamFileBenchmark {
    @Param
    open var graphParams: GraphParams = GraphParams.values()[0]

    lateinit var scenario: Scenario
    lateinit var scenarioExecutor: ScenarioExecutor

    @Param
    open var dcpConstructor: DCPConstructor = DCPConstructor.values()[0]

    @Param("1", "2", "4", "8", "16", "32", "64", "128", "144")
    open var workers: Int = 0

    @Param("4", "99")
    open var readWeight = 1

    @Benchmark
    fun benchmark() {
        scenarioExecutor.run()
    }

    @Setup(Level.Trial)
    fun initialize() {
        scenario = StreamFileScenarioGenerator()
            .generate("/mnt/nvme/fast_query_project/binary_streams/kron_13_query10_binary", workers)
    }

    @Setup(Level.Invocation)
    fun initializeInvocation() {
        scenarioExecutor = ScenarioExecutor(
            scenario,
            { size -> dcpConstructor.constructor()(size, workers + 1) })
    }

    @Setup(Level.Iteration)
    fun flushOut() {
        println()
    }
}
