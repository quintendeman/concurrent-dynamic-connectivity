package benchmarks.util.generators

import benchmarks.util.QueryType
import benchmarks.util.Scenario
import benchmarks.util.edgeToQuery
import benchmarks.util.MAX_BITS_PER_NODE
import benchmarks.util.bidirectionalEdge

import java.io.File
import java.math.BigInteger
import kotlin.random.Random

class StreamFileScenarioGenerator {
    fun generate(stream_file: String, threads: Int): Scenario {

        val stream = File(stream_file).inputStream()

        val num_nodes_bytes = ByteArray(4)
        stream.read(num_nodes_bytes)
        num_nodes_bytes.reverse()
        val num_nodes = BigInteger(num_nodes_bytes).toInt()
        print("Num nodes: ")
        println(num_nodes)

        val num_updates_bytes = ByteArray(8)
        stream.read(num_updates_bytes)
        num_updates_bytes.reverse()
        val num_updates_long = BigInteger(num_updates_bytes).toLong()
        print("Num updates: ")
        println(num_updates_long)
        val num_updates: Int
        val max_updates = 100000000
        if (num_updates_long >= max_updates) {
            num_updates = max_updates
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
                    buffer.reverse()
                    val edge = BigInteger(buffer).toLong()

                    val src = edge.toInt().toLong()
                    val dst = edge - src
                    val shifted_dst = dst shr (32 - MAX_BITS_PER_NODE)
                    val real_edge = shifted_dst + src

                    if (type_buffer == 0) {
                        real_edge.edgeToQuery(QueryType.ADD_EDGE)
                    } else {
                        real_edge.edgeToQuery(QueryType.REMOVE_EDGE)
                    }
                }
            } else {
                LongArray(0)
            }
        }

        return Scenario(num_nodes, threads, LongArray(0), queries)
    }
}

class StreamFileQueryScenarioGenerator {
    private val rnd = Random(343)

    fun generate(stream_file: String, threads: Int): Scenario {

        val stream = File(stream_file).inputStream()

        val num_nodes_bytes = ByteArray(4)
        stream.read(num_nodes_bytes)
        num_nodes_bytes.reverse()
        val num_nodes = BigInteger(num_nodes_bytes).toInt()
        print("Num nodes: ")
        println(num_nodes)

        val num_updates_bytes = ByteArray(8)
        stream.read(num_updates_bytes)
        num_updates_bytes.reverse()
        val num_updates_long = BigInteger(num_updates_bytes).toLong()
        val num_updates: Int
        val max_updates = 100000000
        if (num_updates_long >= max_updates) {
            num_updates = max_updates
            println("UPPER BOUND OF BUFFER REACHED")
        } else {
            num_updates = num_updates_long.toInt()
        }

        val init_edges = LongArray(num_updates) {
            val buffer = ByteArray(8)
            val type_buffer: Int
            type_buffer = stream.read()
            stream.read(buffer)
            buffer.reverse()
            val edge = BigInteger(buffer).toLong()

            val src = edge.toInt()
            val dst = (edge - src).toInt()
            val real_edge = bidirectionalEdge(src, dst)

            if (type_buffer == 0) {
                real_edge.edgeToQuery(QueryType.ADD_EDGE)
            } else {
                real_edge.edgeToQuery(QueryType.REMOVE_EDGE)
            }
        }

        val num_queries = 100000000
        print("Num queries: ")
        println(num_queries)
        val queries = Array(threads) {
            if (it == 0) {
                LongArray(num_queries) {
                    val src = rnd.nextInt(num_nodes).toLong()
                    val dst = rnd.nextInt(num_nodes).toLong()
                    val query_edge = (src shl MAX_BITS_PER_NODE) + dst
                    query_edge.edgeToQuery(QueryType.CONNECTED)
                }
            } else {
                LongArray(0)
            }
        }

        return Scenario(num_nodes, threads, init_edges, queries)
    }
}
