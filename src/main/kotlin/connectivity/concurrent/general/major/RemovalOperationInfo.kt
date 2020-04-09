package connectivity.concurrent.general.major

import java.util.concurrent.atomic.AtomicReference

class RemovalOperationInfo(val u: Int, val v: Int, val additionalRoot: Node) {
    val replacement = AtomicReference<Pair<Int, Int>>()
}