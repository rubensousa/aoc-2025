import java.util.Collections
import java.util.PriorityQueue
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object Day09 {

    @JvmStatic
    fun main(args: Array<String>) {
        val points = readPoints("day09.txt")
        printAndReport { part1(points) } // 4776487744
        printAndReport { part2(points) } // 1560299548
    }

    fun readPoints(path: String): List<Point> {
        val lines = readText(path)
        return lines.map { line ->
            val values = line.split(",")
            Point(x = values[0].toInt(), y = values[1].toInt())
        }
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
        // The input size hints at this: we don't really need to use the real size points
        val scaledGrid = scaleGrid(points)
        val grid = GridState(scaledGrid.scaledPoints)
        grid.fill()
        val scaledRectanglesWithinPath = grid.getRectanglesWithinPath()
        val originalRectanglesWithinPath = scaledRectanglesWithinPath.map { rect ->
            scaledGrid.revertScale(rect)
        }
        return originalRectanglesWithinPath.maxOf { rect -> rect.getArea() }
    }

    fun scaleGrid(points: List<Point>): ScaledGrid {
        val xScaledCoordinates = mutableMapOf<Int, Int>()
        val xOriginalCoordinates = mutableMapOf<Int, Int>()
        val yScaledCoordinates = mutableMapOf<Int, Int>()
        val yOriginalCoordinates = mutableMapOf<Int, Int>()
        points.sortedBy { it.x }
            .distinctBy { it.x }
            .forEachIndexed { index, point ->
                xScaledCoordinates[point.x] = index
                xOriginalCoordinates[index] = point.x
            }
        points.sortedBy { it.y }
            .distinctBy { it.y }
            .forEachIndexed { index, point ->
                yScaledCoordinates[point.y] = index
                yOriginalCoordinates[index] = point.y
            }
        return ScaledGrid(
            originalPoints = points,
            scaledPoints = points.map { point ->
                Point(
                    x = xScaledCoordinates[point.x]!!,
                    y = yScaledCoordinates[point.y]!!
                )
            },
            xCoordinates = xOriginalCoordinates,
            yCoordinates = yOriginalCoordinates
        )
    }

    data class ScaledGrid(
        val originalPoints: List<Point>,
        val scaledPoints: List<Point>,
        val xCoordinates: Map<Int, Int>,
        val yCoordinates: Map<Int, Int>
    ) {

        fun revertScale(rect: Rect): Rect {
            return Rect(
                x1 = xCoordinates[rect.x1]!!,
                y1 = yCoordinates[rect.y1]!!,
                x2 = xCoordinates[rect.x2]!!,
                y2 = yCoordinates[rect.y2]!!
            )
        }

    }

    class GridState(
        private val points: List<Point>
    ) {

        private val tileChar = '#'
        private val rows = points.maxOf { it.y } + 1
        private val columns = points.maxOf { it.x } + 1
        private val cells = MutableList(rows) { row ->
            MutableList(columns) { col ->
                if (points.contains(Point(col, row))) {
                    tileChar
                } else {
                    '.'
                }
            }
        }

        fun fill() {
            for (i in 0 until points.size) {
                val currentPoint = points[i]
                val nextPoint = if (i == points.size - 1) {
                    points[0]
                } else {
                    points[i + 1]
                }
                fillBorder(currentPoint, nextPoint)
            }
            val directions = listOf(Direction.LEFT, Direction.UP, Direction.RIGHT, Direction.DOWN)
            for (row in 0 until rows) {
                for (col in 0 until columns) {
                    if (cells[row][col] == tileChar) {
                        continue
                    }
                    val point = Point(x = col, y = row)
                    var matches = 0
                    directions.forEach { direction ->
                        var currentPoint = point
                        while (currentPoint.y in 0..<rows && currentPoint.x in 0..<columns) {
                            currentPoint = currentPoint.move(direction)
                            if (isPartOfPath(row = currentPoint.y, col = currentPoint.x)) {
                                matches++
                                break
                            }
                        }
                    }
                    if (matches == directions.size) {
                        fillPoint(point)
                    }
                }
            }
        }

        fun getRectanglesWithinPath(): List<Rect> {
            val output = mutableListOf<Rect>()
            for (i in 0 until points.size - 1) {
                for (j in i + 1 until points.size) {
                    val firstCorner = points[i]
                    val secondCorner = points[j]
                    val x1 = min(firstCorner.x, secondCorner.x)
                    val x2 = max(firstCorner.x, secondCorner.x)
                    val y1 = min(firstCorner.y, secondCorner.y)
                    val y2 = max(firstCorner.y, secondCorner.y)
                    var valid = true
                    for (row in y1..y2) {
                        for (col in x1..x2) {
                            if (!isPartOfPath(row = row, col = col)) {
                                valid = false
                            }
                        }
                    }
                    if (valid) {
                        output.add(Rect(x1 = x1, x2 = x2, y1 = y1, y2 = y2))
                    }
                }
            }
            return output
        }

        private fun fillPoint(point: Point) {
            cells[point.y][point.x] = tileChar
        }

        private fun isPartOfPath(row: Int, col: Int): Boolean {
            if (row !in 0..<rows) {
                return false
            }
            if (col !in 0..<columns) {
                return false
            }
            return cells[row][col] == tileChar
        }

        private fun fillBorder(currentPoint: Point, nextPoint: Point) {
            val startX = min(currentPoint.x, nextPoint.x)
            val endX = max(currentPoint.x, nextPoint.x)
            val startY = min(currentPoint.y, nextPoint.y)
            val endY = max(currentPoint.y, nextPoint.y)
            if (startX == endX) {
                for (row in startY until endY + 1) {
                    cells[row][startX] = tileChar
                }
            } else {
                for (col in startX until endX + 1) {
                    cells[startY][col] = tileChar
                }
            }
        }

        override fun toString(): String {
            val stringBuilder = StringBuilder()
            cells.forEach { row ->
                row.forEach { value ->
                    stringBuilder.append(value)
                }
                stringBuilder.appendLine()
            }

            return stringBuilder.toString()
        }
    }

}
