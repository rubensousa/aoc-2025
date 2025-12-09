import java.util.Collections
import java.util.PriorityQueue
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object Day09 {

    @JvmStatic
    fun main(args: Array<String>) {
        val lines = readText("day09.txt")
        val points = lines.map { line ->
            val values = line.split(",")
            Point(x = values[0].toInt(), y = values[1].toInt())
        }
        printAndReport { part1(points) } // 4776487744
        printAndReport { part2(points) } // 2097083020 -> too high
    }

    fun part1(points: List<Point>): Long {
        var maxArea = 0L
        for (i in 0 until points.size - 1) {
            val currentPoint = points[i]
            for (j in i + 1 until points.size) {
                val nextPoint = points[j]
                val height = abs(nextPoint.y - currentPoint.y) + 1
                val width = abs(nextPoint.x - currentPoint.x) + 1
                val newArea = height.toLong() * width.toLong()
                if (newArea > maxArea) {
                    maxArea = newArea
                }
            }
        }
        return maxArea
    }

    fun part2(points: List<Point>): Long {
        val maxX = points.maxOf { it.x }
        val maxY = points.maxOf { it.y }
        val verticalBorderLines = mutableMapOf<Int, MutableSet<Rect>>()
        val horizontalBorderLines = mutableMapOf<Int, MutableSet<Rect>>()
        val rectQueue = PriorityQueue<Rect>(Collections.reverseOrder())
        for (i in 0 until points.size - 1) {
            for (j in i + 1 until points.size) {
                val currentPoint = points[i]
                val nextPoint = points[j]
                val lowestX = min(currentPoint.x, nextPoint.x)
                val highestX = max(currentPoint.x, nextPoint.x)
                val lowestY = min(currentPoint.y, nextPoint.y)
                val highestY = max(currentPoint.y, nextPoint.y)
                if (currentPoint.y == nextPoint.y) {
                    val regions = horizontalBorderLines.getOrPut(currentPoint.y) { mutableSetOf() }
                    regions.add(
                        Rect(
                            x1 = lowestX,
                            x2 = highestX,
                            y1 = currentPoint.y,
                            y2 = currentPoint.y
                        )
                    )
                }
                if (currentPoint.x == nextPoint.x) {
                    val regions = verticalBorderLines.getOrPut(currentPoint.x) { mutableSetOf() }
                    regions.add(
                        Rect(
                            x1 = currentPoint.x,
                            x2 = currentPoint.x,
                            y1 = lowestY,
                            y2 = highestY
                        )
                    )
                }
                rectQueue.add(
                    Rect(
                        x1 = lowestX,
                        x2 = highestX,
                        y1 = lowestY,
                        y2 = highestY
                    )
                )
            }
        }
        val currentPoint = points.last()
        val nextPoint = points[0]
        val rect = Rect(
            x1 = min(currentPoint.x, nextPoint.x),
            x2 = max(currentPoint.x, nextPoint.x),
            y1 = min(currentPoint.y, nextPoint.y),
            y2 = max(currentPoint.y, nextPoint.y)
        )
        if (currentPoint.y == nextPoint.y) {
            val regions = horizontalBorderLines.getOrPut(currentPoint.y) { mutableSetOf() }
            regions.add(rect)
        } else {
            val regions = verticalBorderLines.getOrPut(currentPoint.x) { mutableSetOf() }
            regions.add(rect)
        }
        while (rectQueue.isNotEmpty()) {
            val rect = rectQueue.poll()
            if (isWithinBorder(rect, verticalBorderLines, horizontalBorderLines)) {
                return rect.getArea()
            }
        }
        return 0L
    }

    private fun isWithinBorder(
        rect: Rect,
        verticalBorderLines: Map<Int, Set<Rect>>,
        horizontalBorderLines: Map<Int, Set<Rect>>,
    ): Boolean {
        for (col in rect.x1 until rect.x2) {
            val hasBlockingBorders = verticalBorderLines[col].orEmpty()
                .any { border ->
                    rect.y1 in border.y1..border.y2
                            && border.y2 > rect.y1
                }
            if (hasBlockingBorders) {
                return false
            }
        }
        for (row in rect.y1 until rect.y2) {
            val hasBlockingBorders = horizontalBorderLines[row].orEmpty()
                .any { border ->
                    rect.x2 in border.x1..border.x2
                            && border.x2 > rect.x2
                }
            if (hasBlockingBorders) {
                return false
            }
        }
        for (col in rect.x2 downTo rect.x1 + 1) {
            val hasBlockingBorders = verticalBorderLines[col].orEmpty()
                .any { border ->
                    rect.y2 in border.y1..border.y2
                            && border.y2 > rect.y2
                }
            if (hasBlockingBorders) {
                return false
            }
        }
        for (row in rect.y2 downTo rect.y1 + 1) {
            val hasBlockingBorders = horizontalBorderLines[row].orEmpty()
                .any { border ->
                    rect.x1 in border.x1..border.x2
                            && border.x1 < rect.x1
                }
            if (hasBlockingBorders) {
                return false
            }
        }
        return true
    }

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

}
