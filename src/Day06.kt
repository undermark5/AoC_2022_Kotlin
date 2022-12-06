
fun main() {
    val numberRegex = Regex("\\d+")
    val crateRegex = Regex("\\[[A-Z]]")

    fun part1(input: List<String>): Int {
        return input.first().windowed(4).map { it.toSet() }.indexOfFirst { it.size == 4 } + 4
    }

    fun part2(input: List<String>): Int {
        return input.first().windowed(14).map { it.toSet() }.indexOfFirst { it.size == 14 } + 14
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 19)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
