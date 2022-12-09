fun main() {
    fun updateTail(headLoc: Pair<Int, Int>, tailLoc: Pair<Int, Int>): Pair<Int, Int> {
        if (headLoc.first == tailLoc.first && headLoc.second == tailLoc.second) {
            return tailLoc
        }
        if (headLoc.first == tailLoc.first && (headLoc.second == tailLoc.second - 1 || headLoc.second == tailLoc.second + 1)) {
            return tailLoc
        }

        if (headLoc.second == tailLoc.second && (headLoc.first == tailLoc.first - 1 || headLoc.first == tailLoc.first + 1)) {
            return tailLoc
        }

        if ((headLoc.first == tailLoc.first + 1 || headLoc.first == tailLoc.first - 1) && (headLoc.second == tailLoc.second + 1 || headLoc.second == tailLoc.second - 1)) {
            return tailLoc
        }

        if (headLoc.first == tailLoc.first && headLoc.second == tailLoc.second + 2) {
            return tailLoc + (0 to 1)
        }

        if (headLoc.first == tailLoc.first && headLoc.second == tailLoc.second - 2) {
            return tailLoc + (0 to -1)
        }

        if (headLoc.second == tailLoc.second && headLoc.first == tailLoc.first + 2) {
            return tailLoc + (1 to 0)
        }

        if (headLoc.second == tailLoc.second && headLoc.first == tailLoc.first - 2) {
            return tailLoc + (-1 to 0)
        }

        if ((headLoc.first == tailLoc.first + 1 || headLoc.first == tailLoc.first + 2) && headLoc.second == tailLoc.second + 2) {
            return tailLoc + (1 to 1)
        }

        if ((headLoc.first == tailLoc.first + 1|| headLoc.first == tailLoc.first + 2) && headLoc.second == tailLoc.second - 2) {
            return tailLoc + (1 to -1)
        }

        if ((headLoc.second == tailLoc.second + 1 || headLoc.second == tailLoc.second + 2) && headLoc.first == tailLoc.first + 2) {
            return tailLoc + (1 to 1)
        }

        if ((headLoc.second == tailLoc.second + 1||headLoc.second == tailLoc.second + 2) && headLoc.first == tailLoc.first - 2) {
            return tailLoc + (-1 to 1)
        }

        if ((headLoc.first == tailLoc.first - 1 || headLoc.first == tailLoc.first - 2) && headLoc.second == tailLoc.second + 2) {
            return tailLoc + (-1 to 1)
        }

        if ((headLoc.first == tailLoc.first - 1 || headLoc.first == tailLoc.first - 2) && headLoc.second == tailLoc.second - 2) {
            return tailLoc + (-1 to -1)
        }

        if ((headLoc.second == tailLoc.second - 1 || headLoc.second == tailLoc.second - 2) && headLoc.first == tailLoc.first + 2) {
            return tailLoc + (1 to -1)
        }

        if ((headLoc.second == tailLoc.second - 1||headLoc.second == tailLoc.second - 2) && headLoc.first == tailLoc.first - 2) {
            return tailLoc + (-1 to -1)
        }

        return tailLoc
    }

    fun part1(input: List<String>): Int {
        val tailLocs = mutableSetOf<Pair<Int,Int>>()
        var headLoc = 0 to 0
        var tailLoc = 0 to 0
        tailLocs.add(tailLoc)
        input.forEach {
            val split = it.split(" ")
            val dir = split.first()
            val distance = split.last().toInt()

            val headVector = when (dir) {
                "R" -> 1 to 0
                "L" -> -1 to 0
                "U" -> 0 to 1
                "D" -> 0 to -1
                else -> 0 to 0
            }

            for (i in 0 until distance) {
                headLoc += headVector
                tailLoc = updateTail(headLoc, tailLoc)
                tailLocs.add(tailLoc)
            }
        }
        return tailLocs.size
    }

    fun part2(input: List<String>): Int {
        val tailVisitedLocs = mutableSetOf<Pair<Int,Int>>()
        var headLoc = 0 to 0
        var tailLocs = MutableList(9) {0 to 0}
        tailVisitedLocs.add(tailLocs.last())
        input.forEach {
            val split = it.split(" ")
            val dir = split.first()
            val distance = split.last().toInt()

            val headVector = when (dir) {
                "R" -> 1 to 0
                "L" -> -1 to 0
                "U" -> 0 to 1
                "D" -> 0 to -1
                else -> 0 to 0
            }

            for (i in 0 until distance) {
                headLoc += headVector
                for (i in 0 until 9) {
                    val newLoc = if (i == 0) updateTail(headLoc, tailLocs[i])
                    else updateTail(tailLocs[i - 1], tailLocs[i])
                    tailLocs[i] =  newLoc
                }
                tailVisitedLocs.add(tailLocs.last())
            }
        }
        return tailVisitedLocs.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
//    check(part1(testInput) == 13)
    check(part2(testInput) == 36)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}

private operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return (first + other.first) to (second + other.second)
}
