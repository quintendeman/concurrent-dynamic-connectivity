package benchmarks.util

import connectivity.sequential.general.DynamicConnectivity
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicInteger

class SuccessiveScenarioExecutor(val scenario: Scenario, dcpConstructor: (Int) -> DynamicConnectivity) {
    private val dcp = dcpConstructor(scenario.nodes)

    private val pos = AtomicInteger()
    private val threads: Array<Thread>

    init {
        for (edge in scenario.initialEdges)
            dcp.addEdge(edge.from(), edge.to())

        threads = Array(scenario.threads) { threadId ->
            Thread {
                val queries = scenario.queries[0]
                while (true) {
                    val id = pos.incrementAndGet()
                    if (id >= queries.size) break
                    val query = queries[id]
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