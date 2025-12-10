import com.microsoft.z3.Context
import com.microsoft.z3.IntNum

object Day10 {

    @JvmStatic
    fun main(args: Array<String>) {
        val machines = readText("day10.txt").map { parseMachine(it) }
        printAndReport { part1(machines) }
        printAndReport { part2(machines) }
    }

    fun part1(machines: List<Machine>): Int {
        return machines.sumOf { machine ->
            part1(machine)
        }
    }

    data class MachineState(
        val lights: List<Boolean>,
        val buttonPresses: Int,
    )

    fun part1(machine: Machine): Int {
        val stack = ArrayDeque<MachineState>()
        stack.addFirst(
            MachineState(
                lights = List(machine.lightDiagram.size) { false },
                buttonPresses = 0
            )
        )
        while (stack.isNotEmpty()) {
            val machineState = stack.removeFirst()
            machine.buttons.forEach { button ->
                val newLights = toggleLights(machineState.lights, button)
                if (newLights == machine.lightDiagram) {
                    return machineState.buttonPresses + 1
                }
                stack.addLast(
                    machineState.copy(
                        lights = newLights,
                        buttonPresses = machineState.buttonPresses + 1
                    )
                )
            }
        }
        return 0
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

    fun part2(machines: List<Machine>): Int {
        return machines.sumOf { machine ->
            part2(machine)
        }
    }

    /**
     * Linear system of equations
     * Example:
     * - Buttons: (3) (1, 3) (2)
     * - Matrix:
     * ```
     * 0 0 0
     * 0 1 0
     * 0 0 1
     * 1 1 0
     * ```
     * If joltage is: (3, 5, 4, 7), then:
     *
     * ```
     * x1 * (0, 0, 0, 1) + x2 * (0, 1, 0, 1) + x3 * (0, 0, 1, 0) = (3, 5, 4, 7)
     * ```
     * We want to minimize x1, x2 and x3, but since there's N solutions, this is non-trivial to calculate manually.
     * I used microsoft Z3 bindings that solves the system
     */
    fun part2(machine: Machine): Int = Context().use { context ->
        /**
         * First step: create the button variables that are part of the system
         */
        val optimizeContext = context.mkOptimize()
        val startValue = context.mkInt(0)
        val buttonReferences = machine.buttons.indices
            .map { context.mkIntConst("b$it") }
            .toTypedArray()
        buttonReferences.forEach { buttonReference ->
            optimizeContext.Add(context.mkGe(buttonReference, startValue))
        }

        /**
         * Second step: assign button references for each joltage
         * and create a function that sums them to the target value
         */
        machine.joltage.forEachIndexed { index, value ->
            val joltageButtons = machine.buttons.mapIndexedNotNull { buttonIndex, button ->
                if (button.contains(index)) buttonIndex else null
            }
            val joltageButtonReferences = joltageButtons.map { buttonIndex ->
                buttonReferences[buttonIndex]
            }.toTypedArray()
            val target = context.mkInt(value)
            val sumOfPresses = context.mkAdd(*joltageButtonReferences)
            optimizeContext.Add(context.mkEq(sumOfPresses, target))
        }

        /**
         * Third step: define number of presses to be the minimum sum of all button values
         * and then calculate the model
         */
        val buttonPresses = context.mkIntConst("presses")
        optimizeContext.Add(context.mkEq(buttonPresses, context.mkAdd(*buttonReferences)))
        optimizeContext.MkMinimize(buttonPresses)
        optimizeContext.Check()
        val problemModel = optimizeContext.model
        val output = problemModel.evaluate(buttonPresses, true)
        return (output as? IntNum)?.int ?: 0
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
