fun main() {
    fun part1(input: List<String>): Int {
        val mapped: List<Boolean> = input.map {
            val ranges: List<IntRange> = it.split(",").map { it.toIntRange("-") }
            val first: IntRange = ranges.first()
            val second: IntRange = ranges.last()
            first in second || second in first
        }
        return mapped.count { it }
    }

    fun part2(input: List<String>): Int {
        return input.map {
            val ranges = it.split(",").map { it.toIntRange("-") }
            val first = ranges.first()
            val second = ranges.last()
            first.any { it in second } || second.any { it in first }
        }.count {it}
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
