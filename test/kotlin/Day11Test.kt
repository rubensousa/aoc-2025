import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class Day11Test {

    private val nodes = mutableMapOf<String, Set<String>>()

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

    @Test
    fun `edge case part 1`() {
        connect("i", setOf("a", "b", "c", "d"))
        connect("a", setOf("o"))
        connect("b", setOf("a"))
        connect("c", setOf("b"))
        connect("d", setOf("c"))
        assertThat(
            Day11.getNumberOfPaths(
                nodes = nodes,
                startId = "i",
                endId = "o"
            )
        ).isEqualTo(4)
    }

    private fun connect(id: String, ids: Set<String>) {
        nodes[id] = ids
    }


}