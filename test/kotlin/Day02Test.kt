import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class Day02Test {

    @Test
    fun `invalidation returns true for part2`() {
        assertThat(Day02.isInvalidPart2(111)).isEqualTo(true)
        assertThat(Day02.isInvalidPart2(11)).isEqualTo(true)
        assertThat(Day02.isInvalidPart2(1111)).isEqualTo(true)
        assertThat(Day02.isInvalidPart2(1010)).isEqualTo(true)
        assertThat(Day02.isInvalidPart2(446446)).isEqualTo(true)
        assertThat(Day02.isInvalidPart2(2121212121)).isEqualTo(true)
        assertThat(Day02.isInvalidPart2(18561856)).isEqualTo(true)
        assertThat(Day02.isInvalidPart2(1188511885)).isEqualTo(true)
    }

    @Test
    fun `invalidation returns false for part2`() {
        assertThat(Day02.isInvalidPart2(101)).isEqualTo(false)
        assertThat(Day02.isInvalidPart2(185184)).isEqualTo(false)
        assertThat(Day02.isInvalidPart2(185185184)).isEqualTo(false)
        assertThat(Day02.isInvalidPart2(11111115)).isEqualTo(false)
    }


}