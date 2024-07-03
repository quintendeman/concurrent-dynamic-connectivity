package benchmarks.util.generators

import benchmarks.util.QueryType
import benchmarks.util.Scenario
import benchmarks.util.edgeToQuery

import java.io.File
import java.math.BigInteger

class StreamFileScenarioGenerator {
    fun generate(stream_file: String, threads: Int): Scenario {

        val stream = File(stream_file).inputStream()

        val num_nodes_bytes = ByteArray(4)
        stream.read(num_nodes_bytes)
        val num_nodes = BigInteger(num_nodes_bytes).toInt()

        val num_updates_bytes = ByteArray(8)
        stream.read(num_updates_bytes)
        val num_updates_long = BigInteger(num_updates_bytes).toLong()
        val num_updates: Int
        if (num_updates_long >= 100000000) {
            num_updates = 100000000
            println("UPPER BOUND OF BUFFER REACHED")
        } else {
            num_updates = num_updates_long.toInt()
        }

        val queries = Array(threads) {
            if (it == 0) {
                LongArray(num_updates) {
                    val buffer = ByteArray(8)
                    val type_buffer: Int
                    type_buffer = stream.read()
                    stream.read(buffer)
                    val edge = BigInteger(buffer).toLong()
                    if (type_buffer == 0) {
                        edge.edgeToQuery(QueryType.ADD_EDGE)
                    } else {
                        edge.edgeToQuery(QueryType.REMOVE_EDGE)
                    }
                }
            } else {
                LongArray(0)
            }
        }
        return Scenario(num_nodes, threads, LongArray(0), queries)
    }
}
