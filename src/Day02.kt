import kotlin.math.ceil

object Day02 {

    @JvmStatic
    fun main(args: Array<String>) {
        val testInput = readLine("day02.txt")
        val ranges = parseRanges(testInput)
        part1(ranges).printObject() // 30599400849
        part2(ranges).printObject() // 46270373595
    }

    fun part1(ranges: List<Range>): Long {
        var count = 0L
        ranges.forEach { range ->
            for (i in range.start until range.end + 1) {
                if (isInvalidPart1(i)) {
                    count += i
                }
            }
        }
        return count
    }

    fun isInvalidPart1(number: Long): Boolean {
        val text = number.toString()
        if (text.length % 2 == 0) {
            val halfPoint = text.length / 2
            val firstNumber = text.substring(0, halfPoint)
            val secondNumber = text.substring(halfPoint, text.length)
            if (firstNumber == secondNumber) {
                return true
            }
        }
        return false
    }

    fun part2(ranges: List<Range>): Long {
        var count = 0L
        ranges.forEach { range ->
            for (i in range.start until range.end + 1) {
                if (isInvalidPart2(i)) {
                    count += i
                }
            }
        }
        return count
    }

    fun isInvalidPart2(number: Long): Boolean {
        val text = number.toString()
        var windowSize = 1
        val maxWindow = text.length / 2
        while (windowSize <= maxWindow) {
            val segment = text.take(windowSize)
            val requiredMatches = ceil(text.length.toFloat() / windowSize).toInt()
            var matches = 1
            var startIndex = windowSize
            while (startIndex + windowSize <= text.length) {
                val comparisonSegment = text.substring(startIndex, startIndex + windowSize)
                if (comparisonSegment == segment) {
                    matches++
                } else {
                    break
                }
                startIndex += windowSize
            }
            if (matches == requiredMatches) {
                return true
            }
            windowSize++
        }
        return false
    }

    private fun parseRanges(values: String): List<Range> {
        return values.split(",").map { value ->
            val integers = value.split("-")
            Range(
                start = integers[0].toLong(),
                end = integers[1].toLong()
            )
        }
    }

    data class Range(val start: Long, val end: Long)

}
