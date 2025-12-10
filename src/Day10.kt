object Day10 {

    @JvmStatic
    fun main(args: Array<String>) {
        val machines = readText("day10.txt").map { parseMachine(it) }
        part1(machines).printObject()
        part2(machines).printObject()
    }

    fun part1(machines: List<Machine>): Long {
        return machines.sumOf { machine ->
            part1(machine)
        }
    }

    fun part1(machine: Machine): Long {
        val stack = ArrayDeque<MachineLightState>()
        stack.addFirst(
            MachineLightState(
                lights = List(machine.lightDiagram.size) { false },
                buttonPresses = 0
            )
        )
        while (stack.isNotEmpty()) {
            val machineState = stack.removeFirst()
            machine.buttons.forEach { button ->
                val newLights = toggleLights(machineState.lights, button)
                if (newLights == machine.lightDiagram) {
                    return machineState.buttonPresses + 1L
                }
                stack.addLast(
                    machineState.copy(
                        lights = newLights,
                        buttonPresses = machineState.buttonPresses + 1
                    )
                )
            }
        }
        return 0L
    }

    fun toggleLights(lights: List<Boolean>, button: Set<Int>): List<Boolean> {
        return lights.mapIndexed { index, value ->
            if (button.contains(index)) {
                !value
            } else {
                value
            }
        }
    }

    fun part2(machines: List<Machine>): Long {
        return machines.sumOf { machine ->
            println("Processing machine: $machine")
            part2BruteForce(machine)
        }
    }

    fun part2BruteForce(machine: Machine): Long {
        val stack = ArrayDeque<MachineCounterState>()
        val visitedButtons = mutableSetOf<Map<Int, Int>>()
        val targetCounter = List(machine.joltage.size) { 0 }
        stack.addFirst(
            MachineCounterState(
                counters = machine.joltage,
                buttonPresses = emptyMap()
            )
        )
        while (stack.isNotEmpty()) {
            val machineState = stack.removeFirst()
            for (i in machine.buttons.indices) {
                val button = machine.buttons[i]
                val newCounters = decrementCounters(machineState.counters, button) ?: continue
                if (newCounters == targetCounter) {
                    return machineState.totalButtonPresses() + 1L
                }
                val newButtons = machineState.buttonPresses.toMutableMap()
                val total = newButtons.getOrPut(i) { 0 }
                newButtons[i] = total + 1
                if (!visitedButtons.contains(newButtons)) {
                    visitedButtons.add(newButtons)
                    stack.addLast(
                        machineState.copy(
                            counters = newCounters,
                            buttonPresses = newButtons
                        )
                    )
                }
            }
        }
        return 0L
    }

    fun decrementCounters(counters: List<Int>, button: Set<Int>): List<Int>? {
        val output = counters.toMutableList()
        button.forEach { index ->
            val newValue = output[index] - 1
            if (newValue < 0) {
                return null
            }
            output[index] = newValue
        }
        return output
    }

    data class MachineLightState(
        val lights: List<Boolean>,
        val buttonPresses: Int,
    )

    data class MachineCounterState(
        val counters: List<Int>,
        val buttonPresses: Map<Int, Int>,
    ) {

        fun totalButtonPresses(): Long {
            return buttonPresses.values.sumOf { it.toLong() }
        }

    }

    fun parseMachine(line: String): Machine {
        val splits = line.split(" ")
        val lightDiagram = splits[0].filter { it == '#' || it == '.' }
            .map { it == '#' }
        val buttons = splits.subList(1, splits.size - 1).map { line ->
            val digits = line.filter { it != '(' && it != ')' }.split(",")
            digits.map { it.toInt() }.toSet()
        }
        val joltageValues = splits.last().filter { it != '{' && it != '}' }.split(",")
        val joltage = joltageValues.map { digit ->
            digit.toInt()
        }
        return Machine(
            lightDiagram = lightDiagram,
            buttons = buttons,
            joltage = joltage
        )
    }

    data class Machine(
        val lightDiagram: List<Boolean>,
        val buttons: List<Set<Int>>,
        val joltage: List<Int>
    )

}
