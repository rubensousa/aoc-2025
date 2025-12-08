import java.util.PriorityQueue
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.system.measureTimeMillis

object Day08 {

    @JvmStatic
    fun main(args: Array<String>) {
        val lines = readText("day08.txt")
        val points = parse(lines)
        part1(points).printObject() // 66640
        part2(points).printObject()  // 78894156
    }

    fun part1(points: List<Point3D>): Int {
        val pointCircuits = mutableMapOf<Point3D, Circuit>()
        val outputCircuits = mutableSetOf<Circuit>()
        val connections = buildConnections(points)
        repeat(points.size) {
            val connection = connections.poll()
            val originCircuit = pointCircuits.getOrPut(connection.origin) { Circuit(connection.origin) }
            pointCircuits[connection.target]?.let { targetCircuit ->
                targetCircuit.getPoints().forEach { point ->
                    pointCircuits[point] = originCircuit
                    originCircuit.connect(point)
                }
            }
            outputCircuits.remove(originCircuit)
            originCircuit.connect(connection.target)
            pointCircuits[connection.target] = originCircuit
            pointCircuits[connection.origin] = originCircuit
            outputCircuits.add(originCircuit)
        }
        val topCircuits = outputCircuits.sortedByDescending { circuit -> circuit.size }
        return if (topCircuits.size >= 3) {
            topCircuits[0].size * topCircuits[1].size * topCircuits[2].size
        } else if (topCircuits.size == 2) {
            topCircuits[0].size * topCircuits[1].size
        } else if (topCircuits.size == 1) {
            topCircuits[0].size
        } else {
            0
        }
    }

    fun part2(points: List<Point3D>): Long {
        val pointCircuits = mutableMapOf<Point3D, Circuit>()
        val connections = buildConnections(points)
        while (connections.isNotEmpty()) {
            val connection = connections.poll()
            val originCircuit = pointCircuits.getOrPut(connection.origin) { Circuit(connection.origin) }
            pointCircuits[connection.target]?.let { targetCircuit ->
                targetCircuit.getPoints().forEach { point ->
                    pointCircuits[point] = originCircuit
                    originCircuit.connect(point)
                }
            }
            originCircuit.connect(connection.target)
            pointCircuits[connection.target] = originCircuit
            pointCircuits[connection.origin] = originCircuit

            if (originCircuit.size == points.size) {
                return connection.origin.x * connection.target.x
            }
        }
        return 0L
    }

    private fun buildConnections(points: List<Point3D>): PriorityQueue<Connection> {
        val connections = PriorityQueue<Connection>()
        for (i in 0 until points.size - 1) {
            val referencePoint = points[i]
            for (j in i + 1 until points.size) {
                val targetPoint = points[j]
                connections.offer(
                    Connection(
                        origin = referencePoint,
                        target = targetPoint,
                        distance = targetPoint.distanceTo(referencePoint)
                    )
                )
            }
        }
        return connections
    }

    private fun parse(lines: List<String>): List<Point3D> {
        val points = mutableListOf<Point3D>()
        lines.forEach { line ->
            val numbers = line.split(",")
            val point = Point3D(
                x = numbers[0].toLong(),
                y = numbers[1].toLong(),
                z = numbers[2].toLong()
            )
            points.add(point)
        }
        return points
    }


    data class Connection(
        val origin: Point3D,
        val target: Point3D,
        val distance: Long
    ) : Comparable<Connection> {

        override fun compareTo(other: Connection): Int {
            return distance.compareTo(other.distance)
        }

    }

    class Circuit(origin: Point3D) {

        private val points = mutableSetOf<Point3D>()

        init {
            points.add(origin)
        }

        val size
            get() = points.size

        fun getPoints(): List<Point3D> {
            return points.toList()
        }

        fun connect(point: Point3D) {
            points.add(point)
        }

        override fun toString(): String {
            return "Circuit: $size"
        }

    }

    data class Point3D(val x: Long, val y: Long, val z: Long) {

        fun distanceTo(point: Point3D): Long {
            return sqrt(
                (point.x.toDouble() - x.toDouble()).pow(2)
                        + (point.y.toDouble() - y.toDouble()).pow(2)
                        + (point.z.toDouble() - z.toDouble()).pow(2)
            ).toLong()
        }

    }

}
