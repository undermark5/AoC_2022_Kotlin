import java.math.BigInteger

data class Monkey(
    val id: String,
    private var _items: MutableList<Long>,
    val operation: (Long, Long) -> Long,
    val testValue: Int,
    val successPartner: Int,
    val failPartner: Int,
) {

    var inspectionCount = 0L
        private set

    val items: List<Long> get() = _items

    fun takeTurn(): List<Pair<Int, Long>> {
        inspectionCount += _items.size
        return _items.map{ operation(it, Long.MAX_VALUE) }.map { it / 3.toLong() }.map {
            if (it % testValue.toLong() == 0L ) {
                successPartner to it
            } else {
                failPartner to it
            }
        }.also { _items.clear() }
    }

    fun takeTurnPart2(): List<Pair<Int, Long>> {
        inspectionCount += _items.size
        return _items.map{ operation(it, testValue.toLong()) }.map {
            if (it == 0L ) {
                successPartner to it
            } else {
                failPartner to it
            }
        }.also { _items.clear() }
    }

    fun catchItem(worryLevel: Long) {
        _items.add(worryLevel)
    }
}

val monkeyInstructionsRegex = Regex("Monkey (?<id>\\d+):\\n*\\s*Starting items: (?<starting>(?:\\d+(?:, )?)*)\\n*\\s*Operation: new = (?<left>[old\\d]+) (?<op>[*+]) (?<right>[old\\d]+)\\n\\s*Test: divisible by (?<divisor>\\d+)\\n\\s*If true: throw to monkey (?<success>\\d+)\\n\\s*If false: throw to monkey (?<failure>\\d+)")

fun main() {
    fun part1(input: List<String>): Long {
        val monkeys = input.joinToString("\n").split("\n\n").map {
            monkeyInstructionsRegex.matchEntire(it)?.groups as MatchNamedGroupCollection
        }.map {
            val id = it["id"]
            val starting = it["starting"]
            val op = it["op"]
            val left = it["left"]
            val right = it["right"]
            val divisor = it["divisor"]
            val success = it["success"]
            val failure = it["failure"]
            val operator: (Long, Long, Long) -> Long = if (op?.value == "*") {
                { left, right, _ ->
                    left * right
                }
            } else {
                { left, right, _ ->
                    left + right
                }
            }
            val operation: (Long, Long) -> Long = { input, divisor ->
                val leftValue = if (left?.value == "old") {
                    input
                } else {
                    left?.value?.toLong() ?: 0L
                }

                val rightValue = if (right?.value == "old") {
                    input
                } else {
                    right?.value?.toLong() ?: 0L
                }
                operator(leftValue, rightValue, divisor)
            }
            Monkey(
                id?.value ?: "",
                starting?.value?.split(", ")?.mapNotNull { it.toLongOrNull() }?.toMutableList() ?: mutableListOf(),
                operation,
                divisor?.value?.toInt() ?: 0,
                success?.value?.toInt() ?: 0,
                failure?.value?.toInt() ?: 0
            )
        }
        for (i in 0 until 20) {
            for (monkey in monkeys) {
                val itemsThrown = monkey.takeTurn()
                itemsThrown.forEach {
                    monkeys[it.first].catchItem(it.second)
                }
            }
        }
        return monkeys.sortedByDescending { it.inspectionCount }.map{it.inspectionCount }.take(2).reduce(Long::times)
    }

    fun part2(input: List<String>): Long {
        val monkeys = input.joinToString("\n").split("\n\n").map {
            monkeyInstructionsRegex.matchEntire(it)?.groups as MatchNamedGroupCollection
        }.map {
            val id = it["id"]
            val starting = it["starting"]
            val op = it["op"]
            val left = it["left"]
            val right = it["right"]
            val divisor = it["divisor"]
            val success = it["success"]
            val failure = it["failure"]
            val operator: (Long, Long, Long) -> Long = if (op?.value == "*") {
                { left, right, divisor ->
                    ((left % divisor) * (right % divisor)) % divisor
                }
            } else {
                { left, right, divisor ->
                    ((left % divisor) + (right % divisor)) % divisor
                }
            }
            val operation: (Long, Long) -> Long = { input, divisor ->
                val leftValue = if (left?.value == "old") {
                    input
                } else {
                    left?.value?.toLong() ?: 0L
                }

                val rightValue = if (right?.value == "old") {
                    input
                } else {
                    right?.value?.toLong() ?: 0L
                }

                operator(leftValue, rightValue, divisor)
            }
            Monkey(
                id?.value ?: "",
                starting?.value?.split(", ")?.mapNotNull { it.toLongOrNull() }?.toMutableList() ?: mutableListOf(),
                operation = operation,
                divisor?.value?.toInt() ?: 0,
                success?.value?.toInt() ?: 0,
                failure?.value?.toInt() ?: 0
            )
        }
        for (i in 0 until 10000) {
            for (monkey in monkeys) {
                val itemsThrown = monkey.takeTurnPart2()
                itemsThrown.forEach {
                    monkeys[it.first].catchItem(it.second)
                }
            }
            print("")
        }
        return monkeys.sortedByDescending { it.inspectionCount }.map{it.inspectionCount }.take(2).reduce(Long::times)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    println(part1(testInput))
    check(part1(testInput) == 10605L)
    check(part2(testInput) == 2713310158L)

    val input = readInput("Day11")
    println(part1(input))
//    println(part2(testInput))
    println(part2(input))
}
