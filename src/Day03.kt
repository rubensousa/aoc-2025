import kotlin.math.max

object Day03 {

    @JvmStatic
    fun main(args: Array<String>) {
        val testInput = readText("day03.txt").map { string ->
            string.map { char -> char.digitToInt() }
        }
        part1(testInput).printObject() // 17332
        part2(testInput).printObject() // 172516781546707
    }

    fun part1(banks: List<List<Int>>): Long {
        var sum = 0L
        banks.forEach { bank ->
            var max = 0
            for (start in 0 until bank.size - 1) {
                for (end in start + 1 until bank.size) {
                    val joltage = bank[start] * 10 + bank[end]
                    max = max(joltage, max)
                }
            }
            sum += max
        }
        return sum
    }

    fun part2(banks: List<List<Int>>): Long {
        var sum = 0L
        banks.forEach { bank ->
            sum += findHighestJoltage(bank)
        }
        return sum
    }

    fun findHighestJoltage(batteries: List<Int>): Long {
        val stack = ArrayDeque<JoltageState>()
        val maxBatteries = 12
        val maxByBatterySize = MutableList(maxBatteries + 1) { 0L }
        var max = 0L
        for (i in 0 until batteries.size - (maxBatteries - 1)) {
            stack.addLast(
                JoltageState(
                    sum = batteries[i].toLong(),
                    lastIndex = i,
                    numberOfBatteries = 1
                )
            )
        }

        while (stack.isNotEmpty()) {
            val currentJoltage = stack.removeFirst()
            val numberOfBatteries = currentJoltage.numberOfBatteries
            val remainingBatteries = maxBatteries - numberOfBatteries
            var maxConsecutiveJoltage = 0L
            for (i in currentJoltage.lastIndex + 1 until batteries.size) {
                val currentBatteryJoltage = batteries[i].toLong()
                if (currentBatteryJoltage <= maxConsecutiveJoltage) {
                    // No point in searching for the same value, just continue
                    continue
                }
                maxConsecutiveJoltage = currentBatteryJoltage
                val nextJoltage = currentJoltage.sum * 10 + currentBatteryJoltage
                max = max(nextJoltage, max)
                val nextNumberOfBatteries = currentJoltage.numberOfBatteries + 1

                // Just continue if the next sequence is higher for the battery size we already found
                // Otherwise, it means another sequence won by now
                if (nextNumberOfBatteries < maxBatteries
                    && maxByBatterySize[nextNumberOfBatteries] <= nextJoltage
                    && currentJoltage.lastIndex + remainingBatteries < batteries.size
                ) {
                    maxByBatterySize[nextNumberOfBatteries] = nextJoltage
                    stack.addFirst(
                        currentJoltage.copy(
                            lastIndex = i,
                            sum = nextJoltage,
                            numberOfBatteries = nextNumberOfBatteries
                        )
                    )
                }
            }
        }
        return max
    }

    data class JoltageState(
        val lastIndex: Int,
        val sum: Long,
        val numberOfBatteries: Int,
    )

}
