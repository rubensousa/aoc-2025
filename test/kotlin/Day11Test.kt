import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class Day11Test {

    private val nodes = mutableMapOf<String, Day11.Node>()

    @Test
    fun `test case part 2`() {
        connect("svr", setOf("aaa", "bbb"))
        connect("aaa", setOf("fft"))
        connect("fft", setOf("ccc"))
        connect("bbb", setOf("tty"))
        connect("tty", setOf("ccc"))
        connect("ccc", setOf("ddd","eee"))
        connect("ddd", setOf("hub"))
        connect("hub", setOf("fff"))
        connect("eee", setOf("dac"))
        connect("dac", setOf("fff"))
        connect("fff", setOf("ggg", "hhh"))
        connect("ggg", setOf("out"))
        connect("hhh", setOf("out"))
        assertThat(
            Day11.part2(
                nodes = nodes,
            )
        ).isEqualTo(2)
    }

    private fun connect(id: String, ids: Set<String>) {
        val node = getNode(id)
        nodes[id] = node
        ids.forEach { id ->
            val child = getNode(id)
            node.add(child)
        }
    }

    private fun getNode(id: String): Day11.Node {
        return nodes.getOrPut(id) { Day11.Node(index = nodes.size, key = id) }
    }


}