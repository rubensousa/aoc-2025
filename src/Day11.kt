object Day11 {

    @JvmStatic
    fun main(args: Array<String>) {
        val lines = readText("day11.txt")
        val nodes = mutableMapOf<String, Node>()
        lines.forEach { line ->
            parse(line, nodes)
        }
        printAndReport { part1(nodes = nodes) } // 543
        printAndReport { part2(nodes) } // 479511112939968
    }

    fun part1(nodes: Map<String, Node>): Long {
        return getNumberOfPaths(
            nodes = nodes,
            startId = "you",
            endId = "out"
        )
    }

    fun part2(nodes: Map<String, Node>): Long {
        val svrFft = getNumberOfPaths(nodes = nodes, startId = "svr", endId = "fft")
        val fftDac = getNumberOfPaths(nodes = nodes, startId = "fft", endId = "dac")
        val dacOut = getNumberOfPaths(nodes = nodes, startId = "dac", endId = "out")
        val svrDac = getNumberOfPaths(nodes = nodes, startId = "svr", endId = "dac")
        val dacFft = getNumberOfPaths(nodes = nodes, startId = "dac", endId = "fft")
        val fftOut = getNumberOfPaths(nodes = nodes, startId = "fft", endId = "out")
        return (svrDac * dacFft * fftOut) + (svrFft * fftDac * dacOut)
    }

    fun getNumberOfPaths(
        nodes: Map<String, Node>,
        startId: String,
        endId: String,
    ): Long {
        val start = nodes[startId] ?: return 0
        val end = nodes[endId] ?: return 0
        val stack = ArrayDeque<Node>()
        val visits = mutableMapOf<String, Long>()
        stack.addFirst(start)
        visits[startId] = 1
        while (stack.isNotEmpty()) {
            val currentNode = stack.removeFirst()
            if (currentNode.key == end.key) {
                continue
            }
            val runningVisits = visits.remove(currentNode.key) ?: continue
            currentNode.getNodes().forEach { nextNode ->
                val nextNodeCurrentVisits = visits.getOrDefault(nextNode.key, 0L)
                visits[nextNode.key] = nextNodeCurrentVisits + runningVisits
                if (nextNodeCurrentVisits == 0L) {
                    stack.addLast(nextNode)
                }
            }
        }
        return visits[endId] ?: 0L
    }

    data class NodeConnection(
        val from: Node,
        val to: Set<Node>
    )

    data class Node(
        val index: Int,
        val key: String
    ) {

        private val out = mutableSetOf<Node>()

        fun add(node: Node) {
            out.add(node)
        }

        fun getNodes(): Set<Node> = out

    }

    private fun parse(input: String, availableNodes: MutableMap<String, Node>) {
        val separatorIndex = input.indexOf(":")
        val firstNodeId = input.take(separatorIndex)
        val remainingNodeIds = input.substring(separatorIndex + 2, input.length).split(" ")
        val connection = NodeConnection(
            from = availableNodes.getOrPut(firstNodeId) {
                Node(
                    index = availableNodes.size,
                    key = firstNodeId
                )
            },
            to = remainingNodeIds.map { id ->
                availableNodes.getOrPut(id) {
                    Node(
                        index = availableNodes.size,
                        key = id
                    )
                }
            }.toSet()
        )
        connection.to.forEach { node ->
            connection.from.add(node)
        }
    }

}
