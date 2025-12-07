object Day07 {

    @JvmStatic
    fun main(args: Array<String>) {
        val lines = readText("day07.txt")
        val grid = Grid(lines)
        part1(grid).printObject() // 1555
        part2(grid).printObject() // 12895232295789
    }

    fun part1(grid: Grid): Int {
        val splits = MutableList(grid.rows) { MutableList(grid.columns) { 0 } }
        val stack = ArrayDeque<Point>()
        stack.addFirst(grid.start)
        val beamDirection = Direction.DOWN
        var totalSplits = 0
        while (stack.isNotEmpty()) {
            val beamPoint = stack.removeFirst()
            val nextPoint = beamPoint.move(beamDirection)
            if (grid.isSplitter(nextPoint)) {
                val splitPoints = listOf(nextPoint.move(Direction.LEFT), nextPoint.move(Direction.RIGHT))
                if (splits[nextPoint.y][nextPoint.x] == 0) {
                    totalSplits++
                    splits[nextPoint.y][nextPoint.x] = 1
                    splitPoints.forEach { s ->
                        stack.addLast(s)
                    }
                }
            } else if (grid.isWithinBounds(nextPoint)) {
                stack.addLast(nextPoint)
            }
        }
        return totalSplits
    }

    fun part2(grid: Grid): Long {
        val timelines = MutableList(grid.rows) { MutableList(grid.columns) { 0L } }
        timelines[grid.start.y][grid.start.x] = 1L
        for (row in 1 until grid.rows) {
            for (col in 0 until grid.columns) {
                val point = Point(x = col, y = row)
                val topPoint = point.move(Direction.UP)
                var totalTimelines = 0L
                if (grid.isSplitter(point)) {
                    totalTimelines += timelines[topPoint.y][topPoint.x]
                } else {
                    val topRightPoint = point.move(Direction.UP_RIGHT)
                    val topLeftPoint = point.move(Direction.UP_LEFT)
                    if (!grid.isSplitter(topPoint)) {
                        totalTimelines += timelines[topPoint.y][topPoint.x]
                    }
                    if (grid.isSplitter(topRightPoint)) {
                        totalTimelines += timelines[topRightPoint.y][topRightPoint.x]
                    }
                    if (grid.isSplitter(topLeftPoint)) {
                        totalTimelines += timelines[topLeftPoint.y][topLeftPoint.x]
                    }
                }
                timelines[row][col] = totalTimelines
            }
        }
        return timelines.last().sum()
    }

    class Grid(
        private val lines: List<String>
    ) {

        private val values = List(lines.size) { index ->
            lines[index].toMutableList()
        }

        val rows = values.size
        val columns = values[0].size
        val start = Point(x = lines.first().indexOf('S'), y = 0)

        fun isWithinBounds(point: Point): Boolean {
            return point.x in 0..<columns && point.y in 0..<rows
        }

        fun isSplitter(point: Point): Boolean {
            return getValueAt(point) == '^'
        }

        fun getValueAt(point: Point): Char? {
            val row = values.getOrNull(point.y) ?: return null
            return row.getOrNull(point.x)
        }

    }

}
