object Day11 {

    @JvmStatic
    fun main(args: Array<String>) {
        val lines = readText("day11.txt")
        val nodes = mutableMapOf<String, Set<String>>()
        lines.forEach { line ->
            parse(line, nodes)
        }
        printAndReport { part1(nodes = nodes) } // 543
        printAndReport { part2(nodes) } // 479511112939968
    }

    fun part1(nodes: Map<String, Set<String>>): Long {
        return getNumberOfPaths(
            nodes = nodes,
            startId = "you",
            endId = "out"
        )
    }

    fun part2(nodes: Map<String, Set<String>>): Long {
        val svrFft = getNumberOfPaths(nodes = nodes, startId = "svr", endId = "fft")
        val fftDac = getNumberOfPaths(nodes = nodes, startId = "fft", endId = "dac")
        val dacOut = getNumberOfPaths(nodes = nodes, startId = "dac", endId = "out")
        val svrDac = getNumberOfPaths(nodes = nodes, startId = "svr", endId = "dac")
        val dacFft = getNumberOfPaths(nodes = nodes, startId = "dac", endId = "fft")
        val fftOut = getNumberOfPaths(nodes = nodes, startId = "fft", endId = "out")
        return (svrDac * dacFft * fftOut) + (svrFft * fftDac * dacOut)
    }

    fun getNumberOfPaths(
        nodes: Map<String, Set<String>>,
        startId: String,
        endId: String,
    ): Long {
        val stack = ArrayDeque<String>()
        val visits = mutableMapOf<String, Long>()
        stack.addFirst(startId)
        visits[startId] = 1
        while (stack.isNotEmpty()) {
            val currentNode = stack.removeFirst()
            if (currentNode == endId) {
                continue
            }
            val parentNodeVisits = visits.remove(currentNode) ?: continue
            val childNodes = nodes[currentNode] ?: continue
            childNodes.forEach { nextNode ->
                val childNodeVisits = visits.getOrDefault(nextNode, 0L)
                visits[nextNode] = childNodeVisits + parentNodeVisits
                if (childNodeVisits == 0L) {
                    stack.addLast(nextNode)
                }
            }
        }
        return visits[endId] ?: 0L
    }

    private fun parse(input: String, availableNodes: MutableMap<String, Set<String>>) {
        val separatorIndex = input.indexOf(":")
        val firstNodeId = input.take(separatorIndex)
        val remainingNodeIds = input.substring(separatorIndex + 2, input.length).split(" ")
        availableNodes[firstNodeId] = remainingNodeIds.toSet()
    }

}
