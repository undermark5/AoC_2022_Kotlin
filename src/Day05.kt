
fun main() {
    val numberRegex = Regex("\\d+")
    val crateRegex = Regex("\\[[A-Z]]")

    fun part1(input: List<String>): String {
        val stacksText = input.takeWhile { it.isNotBlank() }
        val steps = input.slice(input.indexOf("")..input.lastIndex).filterNot { it.isBlank() }.map {
            val numbers = numberRegex.findAll(it, 0).map { it.value.toInt() }.toList()
            Triple(numbers[0], numbers[1], numbers[2])
        }
        val numStacks = stacksText.last().last().digitToInt()
        val stacks = MutableList<MutableList<String>>(numStacks) { mutableListOf() }
        stacksText.map { it.windowed(4, 4, true).map { it.trim() } }.forEach {
            it.forEachIndexed { index, s ->
                if (s.isNotBlank() && s.matches(crateRegex)) {
                    stacks[index].add("${s[1]}")
                }
            }
        }
        steps.forEach { (count, fromOffset, toOffset) ->
            val from = fromOffset - 1
            val to = toOffset - 1
            val toMove = stacks[from].take(count)
            stacks[from] = stacks[from].takeLast(stacks[from].size - count).toMutableList()
            stacks[to].addAll(0, toMove.reversed())
            print("")
        }
        return stacks.map { it.firstOrNull() ?: " " }.joinToString(separator = "")
    }

    fun part2(input: List<String>): String {
        val stacksText = input.takeWhile { it.isNotBlank() }
        val steps = input.slice(input.indexOf("")..input.lastIndex).filterNot { it.isBlank() }.map {
            val numbers = numberRegex.findAll(it, 0).map { it.value.toInt() }.toList()
            Triple(numbers[0], numbers[1], numbers[2])
        }
        val numStacks = stacksText.last().last().digitToInt()
        val stacks = MutableList<MutableList<String>>(numStacks) { mutableListOf() }
        stacksText.map { it.windowed(4, 4, true).map { it.trim() } }.forEach {
            it.forEachIndexed { index, s ->
                if (s.isNotBlank() && s.matches(crateRegex)) {
                    stacks[index].add("${s[1]}")
                }
            }
        }
        steps.forEach { (count, fromOffset, toOffset) ->
            val from = fromOffset - 1
            val to = toOffset - 1
            val toMove = stacks[from].take(count)
            stacks[from] = stacks[from].takeLast(stacks[from].size - count).toMutableList()
            stacks[to].addAll(0, toMove)
            print("")
        }
        return stacks.map { it.firstOrNull() ?: " " }.joinToString(separator = "")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
