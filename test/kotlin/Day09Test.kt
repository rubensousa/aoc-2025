import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class Day09Test {

    @Test
    fun `example case`() {
        val points = listOf(
            Point(7, 1),
            Point(11, 1),
            Point(11, 7),
            Point(9, 7),
            Point(9, 5),
            Point(2, 5),
            Point(2, 3),
            Point(7, 3)
        )
        assertThat(Day09.part2(points)).isEqualTo(24)
    }

    @Test
    fun `example case with filling`() {
        val points = listOf(
            Point(7, 1),
            Point(11, 1),
            Point(11, 7),
            Point(9, 7),
            Point(9, 5),
            Point(2, 5),
            Point(2, 3),
            Point(7, 3)
        )
        val grid = Day09.GridState(points)
        grid.fill()
        grid.printObject()
        val area = grid.getRectanglesWithinPath().maxOf { it.getArea() }
        assertThat(area).isEqualTo(24)
    }

    @Test
    fun `example case with scaling`() {
        val points = listOf(
            Point(7, 1),
            Point(11, 1),
            Point(11, 7),
            Point(9, 7),
            Point(9, 5),
            Point(2, 5),
            Point(2, 3),
            Point(7, 3)
        )
        val scaledGrid = Day09.scaleGrid(points)
        val grid = Day09.GridState(scaledGrid.scaledPoints)
        grid.fill()
        grid.printObject()
        val area = grid.getRectanglesWithinPath().map { rectangle ->
            scaledGrid.revertScale(rectangle)
        }.maxOf { it.getArea() }
        assertThat(area).isEqualTo(24)
    }


}