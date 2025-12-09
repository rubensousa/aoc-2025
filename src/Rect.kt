import kotlin.math.abs

data class Rect(
    val x1: Int,
    val x2: Int,
    val y1: Int,
    val y2: Int
) : Comparable<Rect> {

    fun contains(point: Point): Boolean {
        return point.x in x1..x2
                && point.y in y1..y2
    }

    fun getArea(): Long {
        val width = abs(x1 - x2).toLong() + 1
        val height = abs(y1 - y2).toLong() + 1
        return width * height
    }

    override fun compareTo(other: Rect): Int {
        return getArea().compareTo(other.getArea())
    }

}