object Day04 {

    @JvmStatic
    fun main(args: Array<String>) {
        val testInput = readText("day04.txt")
        val grid = Grid(testInput)
        part1(grid).printObject() // 1445
        part2(grid).printObject() // 8317
    }

    fun part1(grid: Grid): Int {
        var sum = 0
        for (y in 0 until grid.rows) {
            for (x in 0 until grid.columns) {
                val point = Point(x = x, y = y)
                val value = grid.getValueAt(point)
                if (value == '@') {
                    val adjacent = Direction.entries.count { nextDirection ->
                        grid.getValueAt(point.move(nextDirection)) == '@'
                    }
                    if (adjacent < 4) {
                        sum++
                    }
                }
            }
        }
        return sum
    }

    fun part2(grid: Grid): Int {
        var continueSearching = true
        while (continueSearching) {
            var removed = false
            for (y in 0 until grid.rows) {
                for (x in 0 until grid.columns) {
                    val point = Point(x = x, y = y)
                    val value = grid.getValueAt(point)
                    if (value == '@') {
                        val adjacent = Direction.entries.count { nextDirection ->
                            grid.getValueAt(point.move(nextDirection)) == '@'
                        }
                        if (adjacent < 4) {
                            grid.remove(point)
                            removed = true
                        }
                    }
                }
            }
            continueSearching = removed
        }
        return grid.getRemovedPapers()
    }


    class Grid(
        private val lines: List<String>
    ) {

        private var removedPapers = 0
        private val values = List(lines.size) { index ->
            lines[index].toMutableList()
        }

        val rows = values.size
        val columns = values[0].size

        fun getRemovedPapers(): Int {
            return removedPapers
        }

        fun getValueAt(point: Point): Char? {
            val row = values.getOrNull(point.y) ?: return null
            return row.getOrNull(point.x)
        }

        fun remove(point: Point) {
            val row = values.getOrNull(point.y) ?: return
            row[point.x] = '.'
            removedPapers++
        }

    }

}
