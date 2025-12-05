import kotlin.math.max

object Day05 {

    @JvmStatic
    fun main(args: Array<String>) {
        val input = parse(readText("day05.txt"))
        part1(input).printObject() // 720
        part2(input.ranges).printObject() // 357608232770687
    }

    fun part1(input: Input): Long {
        val freshFound = mutableSetOf<Long>()
        input.available.forEach { availableIngredient ->
            input.ranges.forEach { range ->
                if (availableIngredient in range.min..range.max) {
                    freshFound.add(availableIngredient)
                }
            }
        }
        return freshFound.size.toLong()
    }

    fun part2(ranges: List<Range>): Long {
        val ranges = mergeRanges(ranges)
        return ranges.sumOf { range -> range.length }
    }

    fun mergeRanges(ranges: List<Range>): List<Range> {
        val sortedRanges = ranges.sortedBy { it.min }
        val output = mutableListOf<Range>()
        output.add(sortedRanges.first())
        for (i in 1 until sortedRanges.size) {
            val lastRange = output.last()
            val currentRange = sortedRanges[i]
            if (currentRange.min <= lastRange.max) {
                lastRange.max = max(currentRange.max, lastRange.max)
            } else {
                output.add(currentRange)
            }
        }
        return output
    }

    private fun parse(lines: List<String>): Input {
        val ranges = mutableListOf<Range>()
        val available = mutableListOf<Long>()
        lines.forEach { line ->
            val split = line.split("-")
            if (split.size == 2) {
                ranges.add(
                    Range(
                        min = split[0].toLong(),
                        max = split[1].toLong()
                    )
                )
            } else if (line.isNotEmpty()) {
                available.add(line.toLong())
            }
        }
        return Input(
            available = available,
            ranges = ranges
        )
    }

    data class Input(
        val available: List<Long>,
        val ranges: List<Range>
    )

    data class Range(val min: Long, var max: Long) {
        val length
            get() = (max - min) + 1
    }

}
