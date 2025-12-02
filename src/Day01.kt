import kotlin.math.abs

object Day01 {

    @JvmStatic
    fun main(args: Array<String>) {
        val testInput = readText("day01.txt")
        val rotations = parseRotations(testInput)
        part1(rotations).printObject() // 1078
        part2(rotations).printObject() // 6412
    }

    private fun parseRotations(values: List<String>): List<Rotation> {
        return values.map { value ->
            val intValue = value.substring(1, value.length).toInt()
            if (value[0] == 'R') {
                Rotation.Right(intValue)
            } else {
                Rotation.Left(intValue)
            }
        }
    }

    fun part1(rotations: List<Rotation>): Int {
        var currentValue = 50
        var count = 0
        rotations.forEach { rotation ->
            currentValue = rotation.applyTo(currentValue)
            if (currentValue == 0) {
                count++
            }
        }
        return count
    }

    fun part2(rotations: List<Rotation>): Int {
        var currentValue = 50
        var count = 0
        rotations.forEach { rotation ->
            val fullRevolutions = abs(rotation.getRelativeValue()) / 100
            count += fullRevolutions
            val nextValue = rotation.applyTo(currentValue)
            if (currentValue != 0) {
                if (nextValue == 0) {
                    count++
                } else if (nextValue > currentValue && rotation is Rotation.Left) {
                    count++
                } else if (nextValue < currentValue && rotation is Rotation.Right) {
                    count++
                }
            }
            currentValue = nextValue
        }
        return count
    }

    fun part2NonOptimal(rotations: List<Rotation>): Int {
        var currentValue = 50
        var count = 0
        rotations.forEach { rotation ->
            val increment = when (rotation) {
                is Rotation.Left -> -1
                is Rotation.Right -> 1
            }
            repeat(rotation.value) {
                currentValue += increment
                if (currentValue < 0) {
                    currentValue = 99
                } else if (currentValue == 100) {
                    currentValue = 0
                }
                if (currentValue == 0) {
                    count++
                }
            }
        }
        return count
    }

    sealed interface Rotation {

        val value: Int

        fun getRelativeValue(): Int

        fun applyTo(current: Int): Int

        data class Left(override val value: Int) : Rotation {
            override fun applyTo(current: Int): Int {
                val target = current - value
                if (target < 0) {
                    return 99 - abs(target + 1) % 100
                }
                return target
            }

            override fun getRelativeValue(): Int {
                return -value
            }
        }

        data class Right(override val value: Int) : Rotation {
            override fun applyTo(current: Int): Int {
                return (current + value) % 100
            }

            override fun getRelativeValue(): Int {
                return value
            }
        }
    }

}
