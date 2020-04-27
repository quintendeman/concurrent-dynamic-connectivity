package benchmarks.util

import connectivity.sequential.general.DynamicConnectivity
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater

const val workAmount = 40

class ScenarioExecutor(val scenario: Scenario, dcpConstructor: (Int) -> DynamicConnectivity) {
    private val dcp = dcpConstructor(scenario.nodes)

    private val threads: Array<Thread>

    private val operationsExecuted = AtomicInteger(0)

    init {
        for (edge in scenario.initialEdges)
            dcp.addEdge(edge.from(), edge.to())

        val operationsNeeded = scenario.threads * scenario.queries[0].size / 2

        threads = Array(scenario.threads) { threadId ->
            Thread {
                val queries = scenario.queries[threadId]
                var i = 0
                while (true) {
                    val query = queries[i++]
                    when (query.type()) {
                        QueryType.CONNECTED -> {
                            dcp.connected(query.from(), query.to())
                        }
                        QueryType.ADD_EDGE -> {
                            dcp.addEdge(query.from(), query.to())
                        }
                        QueryType.REMOVE_EDGE -> {
                            dcp.removeEdge(query.from(), query.to())
                        }
                    }
                    work(workAmount)
                    val ops = operationsExecuted.incrementAndGet()
                    if (ops >= operationsNeeded) break
                }
            }
        }
    }

    fun run() {
        threads.forEach { it.start() }
        threads.forEach { it.join() }
    }

    private inline fun work(amount: Int) {
        val p = 1.0 / amount
        val r = ThreadLocalRandom.current()
        while (true) {
            if (r.nextDouble() < p) break
        }
    }
}