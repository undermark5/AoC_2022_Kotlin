fun main() {
    fun part1(input: List<String>): Int {
        val intList = input.flatMap {
            val size = it.length / 2
            val first = it.take(size).toSet()
            val second = it.takeLast(size).toSet()
            val intersect = first.intersect(second)
            check(intersect.size == 1)
            intersect.map {
                when (it) {
                    in 'a'..'z' -> it - 'a' + 1
                    in 'A'..'Z' -> it - 'A' + 27
                    else -> 0
                }
            }
        }
        return intList.sum()
    }

    fun part2(input: List<String>): Int {
        val groups = input.windowed(3, 3)
        return groups.flatMap {
            it.map { it.toSet() }.reduce(Set<Char>::intersect).map {
                when (it) {
                    in 'a'..'z' -> it - 'a' + 1
                    in 'A'..'Z' -> it - 'A' + 27
                    else -> 0
                }
            }
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    val part1 = part1(testInput)
    check(part1 == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}