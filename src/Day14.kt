


sealed interface Direction {
    object Down : Direction
    object Left : Direction
    object Right : Direction
    object Unknown : Direction
}

data class Cave(val data: List<MutableList<Char>>)
fun main() {

    fun canMove(currentPos: Pair<Int, Int>, cave: List<MutableList<Char>>): Direction {
        if (currentPos.second + 1 == cave.size) return Direction.Unknown
        val downLeft = (currentPos.first - 1) to (currentPos.second + 1)
        val downRight = (currentPos.first + 1) to (currentPos.second + 1)
        val down = (currentPos.first) to (currentPos.second + 1)
        return when {
            cave.getOrNull(down.reversed) == '.' -> Direction.Down
            cave.getOrNull(downLeft.reversed) == '.' -> Direction.Left
            cave.getOrNull(downRight.reversed) == '.' -> Direction.Right
            else -> Direction.Unknown
        }
    }

    fun move(currentPos: Pair<Int, Int>, direction: Direction): Pair<Int, Int> {
        return when (direction) {
            Direction.Down -> currentPos.first to (currentPos.second + 1)
            Direction.Left -> currentPos.first - 1 to currentPos.second + 1
            Direction.Right -> currentPos.first + 1 to currentPos.second + 1
            Direction.Unknown -> Int.MAX_VALUE to Int.MAX_VALUE
        }
    }

    fun dropGrain(cave: List<MutableList<Char>>, lowestSurface: Int): Boolean {
        val initialLanding = cave.map { it[500] }.indexOfFirst { it == '#' || it == 'O' } - 1
        if (initialLanding < 0)
            return false
        else {
            var currentPos = 500 to initialLanding
            var currentDirection: Direction = canMove(currentPos, cave)

            while (currentDirection != Direction.Unknown) {
                currentPos = move(currentPos, currentDirection)
                currentDirection = canMove(currentPos, cave)
            }
            cave[currentPos.reversed] = 'O'
            return currentPos.second >= lowestSurface
        }
    }

    fun part1(input: List<String>): Int {
        val cave = List(1000) { MutableList(1000) { '.' } }
        var lowestSurface = 0
        input.map {
            it.split(" -> ").map {
                it.split(",").map {
                    it.toInt()
                }.toPair()
            }.windowed(2, 1).map {
                val (start, end) = it.toPair()
                val (x1, y1) = start
                val (x2, y2) = end
                if (x1 == x2) {
                    val min = minOf(y1, y2)
                    val max = maxOf(y1, y2)
                    for (i in min..max) {
                        cave[i][x1] = '#'
                    }
                } else {
                    val min = minOf(x1, x2)
                    val max = maxOf(x1, x2)
                    for (i in min..max) {
                        cave[y1][i] = '#'
                    }
                }
                lowestSurface = maxOf(lowestSurface, maxOf(y1, y2))
            }
        }
        var grains = 0
        var isSettled: Boolean
        do {
            grains++
            isSettled = dropGrain(cave, lowestSurface)
        } while (!isSettled)
        return grains - 1
    }

    fun part2(input: List<String>): Int {
        val cave = List(1000) { MutableList(10000) { '.' } }
        var lowestSurface = 0
        input.map {
            it.split(" -> ").map {
                it.split(",").map {
                    it.toInt()
                }.toPair()
            }.windowed(2, 1).map {
                val (start, end) = it.toPair()
                val (x1, y1) = start
                val (x2, y2) = end
                if (x1 == x2) {
                    val min = minOf(y1, y2)
                    val max = maxOf(y1, y2)
                    for (i in min..max) {
                        cave[i][x1] = '#'
                    }
                } else {
                    val min = minOf(x1, x2)
                    val max = maxOf(x1, x2)
                    for (i in min..max) {
                        cave[y1][i] = '#'
                    }
                }
                lowestSurface = maxOf(lowestSurface, maxOf(y1, y2))
            }
        }
        cave[lowestSurface + 2].replaceAll { '#' }
        var grains = 0
        do {
            grains++
            dropGrain(cave, lowestSurface)
        } while (cave[(500 to 0).reversed] == '.')
        return grains
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}

fun List<List<Char>>.trimmed(): List<List<Char>> {
    val top = this.indexOfFirst { it.any { it != '.' } }.coerceAtLeast(0)
    val bottom = this.indexOfLast { it.any { it != '.' } }.coerceAtLeast(0)
    val left = this.map{it.indexOfFirst { it != '.' }}.filter{it >= 0 }.min().coerceAtLeast(0)
    val right = this.maxOf { it.indexOfLast { it != '.' } }.coerceAtLeast(0)
    return this.slice(top..bottom).map { it.slice(left..right) }
}


