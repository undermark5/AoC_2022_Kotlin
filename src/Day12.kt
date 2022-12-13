import kotlinx.coroutines.*
import java.lang.Exception

class HeightNode(value: Char) : Node<Char>(value) {
    var currentMinPath = Int.MAX_VALUE
}

fun main() {
//    fun bfs(queue: MutableList<Node<Int>>, stepCount: Int, end: Node<Int>): Int {
//        val current = queue.pop()
//        if (current == end) {
//            return stepCount
//        }
//        val possibleLocations = current.neighbors.filter { !it.visited && it.value <= current.value + 1 }
//        queue.addAll(possibleLocations.onEach {it.visited = true})
//        return bfs(queue, stepCount + 1, end)
//    }

    fun findPath(current: Node<Char>, end: Node<Char>, grid: List<List<Node<Char>>>): Int {
        val queue = mutableListOf(current to 0)
        current.visited = true
        while (queue.isNotEmpty()) {
            val current = queue.pop()
            if (current.first === end) {
                return current.second
            }
            current.first.neighbors.forEach {
                if (!it.visited && it.value - 1 <= current.first.value) {
                    it.visited = true
                    queue.add(it to (current.second + 1))
                }
            }
        }
        println(grid.joinToString("\n") { it.joinToString("") { if (it.visited) "#" else "${it.value}" } })
        return Int.MAX_VALUE
    }

    suspend fun findPathSuspend(current: Node<Char>, end: Node<Char>, grid: List<List<Node<Char>>>): Int {
        val queue = mutableListOf(current to 0)
        current.visited = true
        while (queue.isNotEmpty()) {
            yield()
            val current = queue.pop()
            if (current.first === end) {
                return current.second
            }
            current.first.neighbors.forEach {
                if (!it.visited && it.value - 1 <= current.first.value) {
                    it.visited = true
                    queue.add(it to (current.second + 1))
                }
            }
        }
//        println(grid.joinToString("\n") { it.joinToString("") { if (it.visited) "#" else "${it.value}" } })
        return Int.MAX_VALUE
    }

    fun part1(input: List<String>): Int {
        val start = Node('a')
        val end = Node('z')
        val nodeGrid: List<List<Node<Char>>> =
            input.map { it.map { if (it == 'S') start else if (it == 'E') end else Node(it) } }
        nodeGrid.forEachIndexed { row, nodes ->
            nodes.forEachIndexed { col, node ->
                val neighbors = listOfNotNull(
                    try {
                        nodeGrid[(row - 1) to col]
                    } catch (e: Exception) {
                        null
                    },
                    try {
                        nodeGrid[(row + 1) to col]
                    } catch (e: Exception) {
                        null
                    }, try {
                        nodeGrid[(row) to (col - 1)]
                    } catch (e: Exception) {
                        null
                    }, try {
                        nodeGrid[(row) to (col + 1)]
                    } catch (e: Exception) {
                        null
                    }
                )
                node.neighbors = neighbors
            }
        }
        return findPath(start, end, nodeGrid)
    }

    suspend fun part2(input: List<String>): Int {
        val coroutineScope = CoroutineScope(Dispatchers.Default)
        val deferred = input.mapIndexed { row, line ->
            line.mapIndexed { col, c ->
                if (c != 'a') {
                    coroutineScope.async { Int.MAX_VALUE }
                } else {
                    coroutineScope.async {
                        val start = Node('a')
                        val end = Node('z')
                        val coord = row to col
                        val nodeGrid: List<List<Node<Char>>> =
                            input.mapIndexed { row, line ->
                                line.mapIndexed { col, it ->
                                    if (it == 'S') {
                                        Node('a')
                                    } else if (it == 'E') {
                                        end
                                    } else if ((row to col) == coord) {
                                        start
                                    } else {
                                        Node(it)
                                    }
                                }
                            }
                        nodeGrid.forEachIndexed { row, nodes ->
                            yield()
                            nodes.forEachIndexed { col, node ->
                                val neighbors = listOfNotNull(
                                    try {
                                        nodeGrid[(row - 1) to col]
                                    } catch (e: Exception) {
                                        null
                                    },
                                    try {
                                        nodeGrid[(row + 1) to col]
                                    } catch (e: Exception) {
                                        null
                                    }, try {
                                        nodeGrid[(row) to (col - 1)]
                                    } catch (e: Exception) {
                                        null
                                    }, try {
                                        nodeGrid[(row) to (col + 1)]
                                    } catch (e: Exception) {
                                        null
                                    }
                                )
                                node.neighbors = neighbors
                            }
                        }
                        findPathSuspend(start, end, nodeGrid)
                    }
                }
            }
        }.flatten()
        val paths = deferred.awaitAll()
        coroutineScope.cancel()
        return paths.min()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    println(part1(testInput))
    check(part1(testInput) == 31)
    runBlocking {
        check(part2(testInput) == 29)
    }

    val input = readInput("Day12")
    println(part1(input))
    runBlocking {
        println(part2(input))
    }
}
