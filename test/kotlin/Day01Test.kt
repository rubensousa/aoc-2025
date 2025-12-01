import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class Day01Test {

    @Test
    fun `dial left out of bounds`(){
        assertThat(Day01.Rotation.Left(2).applyTo(0)).isEqualTo(98)
        assertThat(Day01.Rotation.Left(100).applyTo(0)).isEqualTo(0)
    }

    @Test
    fun `dial left within of bounds`(){
        assertThat(Day01.Rotation.Left(1).applyTo(5)).isEqualTo(4)
        assertThat(Day01.Rotation.Left(1).applyTo(99)).isEqualTo(98)
    }

    @Test
    fun `dial right out of bounds`(){
        assertThat(Day01.Rotation.Right(2).applyTo(99)).isEqualTo(1)
        assertThat(Day01.Rotation.Right(100).applyTo(99)).isEqualTo(99)
    }

    @Test
    fun `dial right within bounds`(){
        assertThat(Day01.Rotation.Right(2).applyTo(95)).isEqualTo(97)
    }


}