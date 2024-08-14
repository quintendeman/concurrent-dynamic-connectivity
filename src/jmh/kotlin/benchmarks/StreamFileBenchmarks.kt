package benchmarks

import benchmarks.util.*
import benchmarks.util.executors.SuccessiveScenarioExecutor
import benchmarks.util.generators.StreamFileScenarioGenerator
import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Measurement(iterations = iterations, time = TIME_IN_SECONDS, timeUnit = TimeUnit.SECONDS)
@Warmup(iterations = warmupIterations, time = TIME_IN_SECONDS, timeUnit = TimeUnit.SECONDS)
open class StreamFileBenchmark3 {
    @Param
    open var graphParams: GraphParams = GraphParams.values()[0]

    lateinit var scenario: Scenario
    lateinit var scenarioExecutor: SuccessiveScenarioExecutor

    @Param
    open var dcpConstructor: DCPForModificationsConstructor = DCPForModificationsConstructor.values()[0]

    @Param("1", "2", "4", "8", "16", "32", "64", "128", "144")
    open var workers: Int = 0

    @Benchmark
    fun benchmark() {
        scenarioExecutor.run()
    }

    @Setup(Level.Trial)
    fun initialize() {
        scenario = StreamFileScenarioGenerator()
            .generate(System.getenv("CDC_INPUT_FILE"), workers)
    }

    @Setup(Level.Invocation)
    fun initializeInvocation() {
        scenarioExecutor = SuccessiveScenarioExecutor(
            scenario,
            { size -> dcpConstructor.constructor()(size, workers + 1) })
    }

    @Setup(Level.Iteration)
    fun flushOut() {
        println()
    }
}
