object Day12 {

    @JvmStatic
    fun main(args: Array<String>) {
        val input = parse(readText("day12.txt"))
        printAndReport { part1(input) } // 599
    }

    fun part1(input: Input): Int {
        return input.regions.sumOf { region ->
            val areaRequired = region.presentQuantities.values.sumOf { total -> 9 * total }
            if (areaRequired <= region.area) 1 else 0
        }
    }

    fun parse(lines: List<String>): Input {
        val lastLine = lines.lastIndexOf("")
        val windows = lines.subList(0, lastLine)
            .filter { it.isNotEmpty() }
            .windowed(size = 4, step = 4)

        val presents = windows.indices.toList()
        val regions = mutableListOf<Region>()
        for (i in lastLine + 1 until lines.size) {
            regions.add(parseRegion(lines[i]))
        }
        return Input(
            presents = presents,
            regions = regions
        )
    }

    private fun parseRegion(line: String): Region {
        val indexOfSeparator = line.indexOf(":")
        val dimensions = line.take(indexOfSeparator).split("x")
        val quantities = line.substring(indexOfSeparator + 2, line.length).split(" ")
            .map { it.toInt() }
        val presentQuantities = mutableMapOf<Int, Int>()
        quantities.forEachIndexed { index, value ->
            presentQuantities[index] = value
        }
        val area = dimensions[0].toInt() * dimensions[1].toInt()
        return Region(
            area = area,
            presentQuantities = presentQuantities
        )
    }

    data class Input(
        val presents: List<Int>,
        val regions: List<Region>
    )

    data class Region(
        val area: Int,
        val presentQuantities: Map<Int, Int>
    )

}
