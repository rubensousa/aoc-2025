import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class Day03Test {

    @Test
    fun `highest joltage`() {
        assertThat(
            Day03.findHighestJoltage(

                convertToList("234234234234278")
            )
        ).isEqualTo(434234234278)
        assertThat(
            Day03.findHighestJoltage(
                convertToList("818181911112111")
            )
        ).isEqualTo(888911112111)
        assertThat(
            Day03.findHighestJoltage(

                convertToList("811111111111119")
            )
        ).isEqualTo(811111111119)
        assertThat(
            Day03.findHighestJoltage(
                convertToList("987654321111111")
            )
        ).isEqualTo(987654321111L)
    }

    private fun convertToList(text: String): List<Int> {
        return text.toList().map { it.digitToInt() }
    }

}