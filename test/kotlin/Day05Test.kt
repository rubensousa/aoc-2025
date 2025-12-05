import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class Day05Test {

    @Test
    fun `merge ranges with common min`() {
        val input = listOf(
            Day05.Range(1, 2),
            Day05.Range(1, 3),
            Day05.Range(1, 4)
        )
        assertThat(Day05.part2(input)).isEqualTo(4)
    }

    @Test
    fun `merge ranges with common max`() {
        val input = listOf(
            Day05.Range(1, 3),
            Day05.Range(1, 3),
            Day05.Range(2, 3)
        )
        assertThat(Day05.part2(input)).isEqualTo(3)
    }

    @Test
    fun `merge ranges with multiple steps`() {
        val input = listOf(
            Day05.Range(3, 7),
            Day05.Range(1, 20),
            Day05.Range(19, 20),
            Day05.Range(21, 41)
        )
        assertThat(Day05.part2(input)).isEqualTo(41)
    }

    @Test
    fun `merge ranges with multiple steps 2`() {
        val input = listOf(
            Day05.Range(1, 10),
            Day05.Range(2, 9),
            Day05.Range(3, 8),
            Day05.Range(4, 7),
            Day05.Range(5, 6),
        )
        assertThat(Day05.part2(input)).isEqualTo(10)
    }

    @Test
    fun `merge ranges alternative`() {
        val input = listOf(
            Day05.Range(8, 15),
            Day05.Range(4, 20),
            Day05.Range(1, 5),
        )
        assertThat(Day05.part2(input)).isEqualTo(20)
    }

}