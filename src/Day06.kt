import kotlin.math.pow

object Day06 {

    @JvmStatic
    fun main(args: Array<String>) {
        val lines = readText("day06.txt")
        part1(parse(lines)).printObject() // 3968933219902
        part2(lines).printObject() // 6019576291014
    }

    fun part1(problems: List<Problem>): Long {
        var sum = 0L
        problems.forEach { problem ->
            var runningSum = problem.numbers[0]
            for (i in 1 until problem.numbers.size) {
                runningSum = problem.operation.apply(runningSum, problem.numbers[i])
            }
            sum += runningSum
        }
        return sum
    }

    fun part2(lines: List<String>): Long {
        val operationLocations = findOperationLocations(lines)
        val maxLineLength = lines.maxOf { it.length }
        val numberMatrix = MutableList(lines.size - 1) { row ->
            val line = lines[row]
            MutableList(maxLineLength) { column ->
                line.getOrNull(column)?.digitToIntOrNull() ?: 0
            }
        }
        var sum = 0L
        operationLocations.forEachIndexed { index, location ->
            var runningSum = 0L
            val startIndex = location.index
            val endIndex = if (index == operationLocations.size - 1) {
                maxLineLength - 1
            } else {
                operationLocations[index + 1].index - 2
            }
            for (column in endIndex downTo startIndex) {
                var number = 0L
                var currentExponent = 0
                for (row in lines.size - 2 downTo 0) {
                    val matrixValue = numberMatrix[row][column]
                    if (matrixValue != 0) {
                        number += matrixValue * (10.0.pow(currentExponent)).toLong()
                        currentExponent++
                    }
                }
                runningSum = if (column == endIndex) {
                    number
                } else {
                    location.operation.apply(number, runningSum)
                }
            }
            sum += runningSum
        }
        return sum
    }

    private fun findOperationLocations(lines: List<String>): List<OperationLocation> {
        val operations = mutableListOf<OperationLocation>()
        val lastLine = lines.last()
        for (i in 0 until lastLine.length) {
            val lastLine = lines.last()
            if (lastLine[i] == '+') {
                operations.add(OperationLocation(i, Operation.Addition))
            } else if (lastLine[i] == '*') {
                operations.add(OperationLocation(i, Operation.Multiplication))
            }
        }
        return operations
    }


    private fun parse(lines: List<String>): List<Problem> {
        val operations = mutableListOf<Operation>()
        val numbers = linkedMapOf<Int, MutableList<Long>>()
        lines.forEach { line ->
            val split = line.trim().split(" ").filter { it.isNotEmpty() }
            split.forEachIndexed { index, value ->
                val number = value.toLongOrNull()
                if (number != null) {
                    val currentNumbers = numbers.getOrPut(index) { mutableListOf() }
                    currentNumbers.add(number)
                }
            }
        }
        val operationLine = lines.last().trim().split(" ")
            .filter { it.isNotEmpty() }
        operationLine.forEach { value ->
            if (value == "+") {
                operations.add(Operation.Addition)
            } else if (value == "*") {
                operations.add(Operation.Multiplication)
            }
        }
        return numbers.map { entry ->
            Problem(
                numbers = entry.value,
                operation = operations[entry.key]
            )
        }
    }

    data class Problem(val numbers: List<Long>, val operation: Operation)

    data class OperationLocation(val index: Int, val operation: Operation)

    sealed interface Operation {

        fun apply(firstValue: Long, secondValue: Long): Long

        data object Addition : Operation {
            override fun apply(firstValue: Long, secondValue: Long): Long {
                return firstValue + secondValue
            }
        }

        data object Multiplication : Operation {
            override fun apply(firstValue: Long, secondValue: Long): Long {
                return firstValue * secondValue
            }
        }
    }

}
