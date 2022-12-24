import java.util.TreeMap
import kotlin.math.max

fun defaultRow() = ".".repeat(7).toCharArray().toMutableList()

sealed interface RockPiece {
    fun canShift(topLeft: Pair<Int, Int>, dir: Int, currentRows: Map<Int, MutableList<Char>>): Boolean
    fun canFall(topLeft: Pair<Int, Int>, currentRows: Map<Int, MutableList<Char>>): Boolean
    fun getRow(index: Int): List<Char>

    val dimension: Pair<Int, Int>
    val bottomLeft: Pair<Int, Int>

    class Bar : RockPiece {
        override val dimension: Pair<Int, Int> = 4 to 1
        override val bottomLeft: Pair<Int, Int> = 0 to 0
        override fun canShift(topLeft: Pair<Int, Int>, dir: Int, currentRows: Map<Int, MutableList<Char>>): Boolean {
            val row = currentRows.getOrDefault(topLeft.second, defaultRow())
            return if (dir == -1) {
                row.getOrNull(topLeft.first + dir) == '.'
            } else {
                row.getOrNull(topLeft.first + dimension.first - 1 + dir) == '.'
            }
        }

        override fun canFall(topLeft: Pair<Int, Int>, currentRows: Map<Int, MutableList<Char>>): Boolean {
            return currentRows.getOrDefault(topLeft.second, defaultRow())
                .slice(topLeft.first until (topLeft.first + dimension.first)).run {
                    size == 4 && this.all { it == '.' }
                }
        }

        override fun getRow(index: Int): List<Char> {
            return when (index) {
                0 -> listOf('#', '#', '#', '#')
                else -> emptyList()
            }
        }
    }

    class Plus : RockPiece {
        override val dimension: Pair<Int, Int> = 3 to 3
        override val bottomLeft: Pair<Int, Int> = 0 to -2
        override fun canShift(topLeft: Pair<Int, Int>, dir: Int, currentRows: Map<Int, MutableList<Char>>): Boolean {
            val topRow = currentRows.getOrDefault(topLeft.second, defaultRow())
            val middleRow = currentRows.getOrDefault(topLeft.second - 1, defaultRow())
            val bottomRow = currentRows.getOrDefault(topLeft.second - 2, defaultRow())
            return if (dir == -1) {
                topRow.getOrNull(topLeft.first + 1 + dir) == '.' && middleRow.getOrNull(topLeft.first + dir) == '.' && bottomRow.getOrNull(
                    topLeft.first + 1 + dir
                ) == '.'
            } else {
                topRow.getOrNull(topLeft.first + 1 + dir) == '.' && middleRow.getOrNull(topLeft.first + dimension.first - 1 + dir) == '.' && bottomRow.getOrNull(
                    topLeft.first + 1 + dir
                ) == '.'
            }
        }

        override fun canFall(topLeft: Pair<Int, Int>, currentRows: Map<Int, MutableList<Char>>): Boolean {
            return currentRows.getOrDefault(topLeft.second - 1, defaultRow())
                .slice(topLeft.first until (topLeft.first + dimension.first)).run {
                    size == 3 && this.all { it == '.' }
                } && currentRows.getOrDefault(topLeft.second - 2, defaultRow()).getOrNull(topLeft.first + 1) == '.'
        }

        override fun getRow(index: Int): List<Char> {
            return when (index) {
                0, 2 -> listOf('.', '#', '.')
                1 -> listOf('#', '#', '#')
                else -> emptyList()
            }
        }
    }

    class Angle : RockPiece {
        override val dimension: Pair<Int, Int> = 3 to 3
        override val bottomLeft: Pair<Int, Int> = 0 to -2
        override fun canShift(topLeft: Pair<Int, Int>, dir: Int, currentRows: Map<Int, MutableList<Char>>): Boolean {
            val topRow = currentRows.getOrDefault(topLeft.second, defaultRow())
            val middleRow = currentRows.getOrDefault(topLeft.second - 1, defaultRow())
            val bottomRow = currentRows.getOrDefault(topLeft.second - 2, defaultRow())
            return if (dir == -1) {
                topRow.getOrNull(topLeft.first + 2 + dir) == '.' && middleRow.getOrNull(topLeft.first + 2 + dir) == '.' && bottomRow.getOrNull(
                    topLeft.first + dir
                ) == '.'
            } else {
                topRow.getOrNull(topLeft.first + 2 + dir) == '.' && middleRow.getOrNull(topLeft.first + 2 + dir) == '.' && bottomRow.getOrNull(
                    topLeft.first + dimension.first - 1 + dir
                ) == '.'
            }
        }

        override fun canFall(topLeft: Pair<Int, Int>, currentRows: Map<Int, MutableList<Char>>): Boolean {
            return currentRows.getOrDefault(topLeft.second - 2, defaultRow())
                .slice(topLeft.first until (topLeft.first + dimension.first)).run {
                    size == 3 && this.all { it == '.' }
                }
        }

        override fun getRow(index: Int): List<Char> {
            return when (index) {
                0, 1 -> listOf('.', '.', '#')
                2 -> listOf('#', '#', '#')
                else -> emptyList()
            }
        }
    }

    class Line : RockPiece {
        override val dimension: Pair<Int, Int> = 1 to 4
        override val bottomLeft: Pair<Int, Int> = 0 to -3
        override fun canShift(topLeft: Pair<Int, Int>, dir: Int, currentRows: Map<Int, MutableList<Char>>): Boolean {
            val topRow = currentRows.getOrDefault(topLeft.second, defaultRow())
            val middleRow1 = currentRows.getOrDefault(topLeft.second - 1, defaultRow())
            val middleRow2 = currentRows.getOrDefault(topLeft.second - 2, defaultRow())
            val bottomRow = currentRows.getOrDefault(topLeft.second - 3, defaultRow())
            return topRow.getOrNull(topLeft.first + dir) == '.' && middleRow1.getOrNull(topLeft.first + dir) == '.' && middleRow2.getOrNull(
                topLeft.first + dir
            ) == '.' && bottomRow.getOrNull(topLeft.first + dir) == '.'
        }

        override fun canFall(topLeft: Pair<Int, Int>, currentRows: Map<Int, MutableList<Char>>): Boolean {
            return currentRows.getOrDefault(topLeft.second - 3, defaultRow()).getOrNull(topLeft.first) == '.'
        }

        override fun getRow(index: Int): List<Char> {
            return when (index) {
                0, 1, 2, 3 -> listOf('#')
                else -> emptyList()
            }
        }
    }

    class Square : RockPiece {
        override val dimension: Pair<Int, Int> = 2 to 2
        override val bottomLeft: Pair<Int, Int> = 0 to -1
        override fun canShift(topLeft: Pair<Int, Int>, dir: Int, currentRows: Map<Int, MutableList<Char>>): Boolean {
            val topRow = currentRows.getOrDefault(topLeft.second, defaultRow())
            val bottomRow = currentRows.getOrDefault(topLeft.second - 1, defaultRow())
            return if (dir == -1) {
                topRow.getOrNull(topLeft.first + dir) == '.' && bottomRow.getOrNull(topLeft.first + dir) == '.'
            } else {
                topRow.getOrNull(topLeft.first + dimension.first - 1 + dir) == '.' && bottomRow.getOrNull(topLeft.first + dimension.first - 1 + dir) == '.'
            }
        }

        override fun canFall(topLeft: Pair<Int, Int>, currentRows: Map<Int, MutableList<Char>>): Boolean {
            return currentRows.getOrDefault(topLeft.second - 1, defaultRow())
                .slice(topLeft.first until (topLeft.first + dimension.first)).run {
                    size == 2 && this.all { it == '.' }
                }
        }

        override fun getRow(index: Int): List<Char> {
            return when (index) {
                0, 1 -> listOf('#', '#')
                else -> emptyList()
            }
        }
    }

    companion object {
        val order = listOf(
            Bar::class.constructors.first(),
            Plus::class.constructors.first(),
            Angle::class.constructors.first(),
            Line::class.constructors.first(),
            Square::class.constructors.first()
        )
    }
}

fun addPieceToChamber(chamber: MutableMap<Int, MutableList<Char>>, piece: RockPiece, topLeft: Pair<Int, Int>) {
    val verticalRange = (topLeft.second - (piece.dimension.second - 1))..topLeft.second
    verticalRange.reversed().forEachIndexed { index, value ->
        chamber.getOrPut(value) { defaultRow() }.apply {
            piece.getRow(index).forEachIndexed { index, value ->
                if (value == '#') {
                    this[index + topLeft.first] = value
                }
            }
        }
    }
}

fun addPieceToChamber(chamber: MutableList<Pair<Int, MutableList<Char>>>, piece: RockPiece, topLeft: Pair<Int, Int>) {
    val temp = chamber.toMap().toMutableMap()
    val verticalRange = (topLeft.second - (piece.dimension.second - 1))..topLeft.second
    verticalRange.reversed().forEachIndexed { index, value ->
        temp.getOrPut(value) { defaultRow() }.apply {
            piece.getRow(index).forEachIndexed { index, value ->
                if (value == '#') {
                    this[index + topLeft.first] = value
                }
            }
        }
    }
    chamber.clear()
    chamber.addAll(temp.map { (key, value) -> key to value })
}

fun main() {
    val order = RockPiece.order

//    fun addPieceToChamber(chamber: MutableMap<Int, MutableList<Char>>, piece: RockPiece, topLeft: Pair<Int, Int>) {
//        val verticalRange = (topLeft.second - (piece.dimension.second - 1))..topLeft.second
//        verticalRange.reversed().forEachIndexed { index, value ->
//            chamber.getOrPut(value) { defaultRow() }.apply {
//                piece.getRow(index).forEachIndexed { index, value ->
//                    if (value == '#') {
//                        this[index + topLeft.first] = value
//                    }
//                }
//            }
//        }
//    }

    fun part1(input: List<String>): Int {
        val jetDirs = input.first().toCharArray()
        val chamber = TreeMap<Int, MutableList<Char>>()
        var highestRock = -1
        var currentJetIndex = 0
        for (i in 0 until 2022) {
            val piece = order[i % order.size].call()

            var bottomLeft = 2 to highestRock + 3
            var topLeft = bottomLeft.first to bottomLeft.second + (piece.dimension.second)
            print("")
            while (true) {
                val verticalRange = bottomLeft.second..topLeft.second
                val dir = if (jetDirs[currentJetIndex] == '<') -1 else 1
                val currentRows = chamber.filterKeys { it in verticalRange }
                if (piece.canShift(topLeft, dir, currentRows)) {
                    topLeft = topLeft.first + dir to topLeft.second
                    bottomLeft = topLeft.first to topLeft.second - (piece.dimension.second - 1)
                }
                if (bottomLeft.second.toLong() - 1 >= 0 && piece.canFall(
                        topLeft.first to topLeft.second - 1,
                        chamber.filterKeys { it in verticalRange.map { it - 1 } })
                ) {
                    topLeft = topLeft.first to topLeft.second - 1
                    bottomLeft = topLeft.first to topLeft.second - (piece.dimension.second - 1)
                } else {
                    addPieceToChamber(chamber, piece, topLeft)
                    highestRock = max(highestRock, topLeft.second)
                    break
                }
                currentJetIndex++
                currentJetIndex %= jetDirs.size
            }
            currentJetIndex++
            currentJetIndex %= jetDirs.size
        }
        println(
            chamber.asIterable().reversed().joinToString("\n", postfix = "\n+-------+") { (key, value) ->
                value.joinToString(
                    "",
                    prefix = "|",
                    postfix = "|"
                )
            })
        return chamber.size
    }

    fun trimChamber(chamber: MutableList<Pair<Int, MutableList<Char>>>) {
        if (chamber.size > 100) {
            val sorted = chamber.sortedBy { it.first }
            val extra = chamber.size - 100
            val toRemove = sorted.take(extra).map { it.first }.toSet()
            chamber.removeIf { it.first in toRemove }
        }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part1(testInput) == 3068)

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}
