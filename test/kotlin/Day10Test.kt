import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class Day10Test {

    @Test
    fun parse() {
        val line = "[#.###] (0,1) (0,2,3,4) (0,1,4) (3,4) {37,29,8,20,35}"

        assertThat(Day10.parseMachine(line)).isEqualTo(
            Day10.Machine(
                lightDiagram = listOf(true, false, true, true, true),
                buttons = listOf(
                    setOf(0, 1),
                    setOf(0, 2, 3, 4),
                    setOf(0, 1, 4),
                    setOf(3, 4)
                ),
                joltage = listOf(37, 29, 8, 20, 35)
            )
        )

    }

    @Test
    fun `example case part1`() {
        val line = "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}"
        val machine = Day10.parseMachine(line)
        assertThat(Day10.part1(machine)).isEqualTo(2)
    }

    @Test
    fun `example case 2 part1`() {
        val line = "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}"
        val machine = Day10.parseMachine(line)
        assertThat(Day10.part1(machine)).isEqualTo(3)
    }

    @Test
    fun `example case 3 part1`() {
        val line = "[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}"
        val machine = Day10.parseMachine(line)
        assertThat(Day10.part1(machine)).isEqualTo(2)
    }

    @Test
    fun `toggle lights`() {
        assertThat(
            Day10.toggleLights(
                lights = listOf(false, false, false, false),
                button = setOf(1, 3)
            )
        ).isEqualTo(listOf(false, true, false, true))
        assertThat(
            Day10.toggleLights(
                lights = listOf(true, true, true, true),
                button = setOf(0)
            )
        ).isEqualTo(listOf(false, true, true, true))
    }

    @Test
    fun `example case part2`() {
        val line = "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}"
        val machine = Day10.parseMachine(line)
        assertThat(Day10.part2(machine)).isEqualTo(10)
    }

    @Test
    fun `example case 2 part2`() {
        val line = "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}"
        val machine = Day10.parseMachine(line)
        assertThat(Day10.part2(machine)).isEqualTo(12)
    }

    @Test
    fun `example case 3 part2`() {
        val line = "[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}"
        val machine = Day10.parseMachine(line)
        assertThat(Day10.part2(machine)).isEqualTo(11)
    }

}