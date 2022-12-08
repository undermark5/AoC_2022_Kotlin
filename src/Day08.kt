fun main() {
    fun part1(input: List<String>): Int {
        val trees = input.map {
            it.toCharArray().map {
                it.digitToInt()
            }
        }

        val transposed = MutableList(
            trees.first().size
        ) { col ->
            MutableList(trees.size) { row ->
                trees[row][col]
            }
        }
        var count = 0
        for (i in trees.indices) {
            for (j in trees[i].indices) {
                val value = trees[i][j]
                val row = trees[i]
                val col = transposed[j]
                val rowFor = row.slice(0..j)
                val colFor = col.slice(0..i)
                val rowRev = row
                    .slice(j..row.lastIndex)
                    .reversed()
                val colRev = col
                    .slice(i..col.lastIndex)
                    .reversed()
                val views = listOf(
                    rowFor, colFor, rowRev, colRev
                )
                if (views.any {
                        it.slice(0 until it.lastIndex)
                            .all { it < value }
                    }) {
                    count++
                }
            }
        }
        return count
    }

    fun part2(input: List<String>): Int {
        val trees = input.map {
            it.toCharArray().map {
                it.digitToInt()
            }
        }
        val transposed = MutableList(
            trees.first().size) { col ->
            MutableList(trees.size){ row ->
                trees [row][col]
            }
        }
        var max = 0
        for (i in trees.indices) {
            for (j in trees[i].indices) {
                val value=trees [i][j]
                val row = trees [i]
                val col = transposed[j]
                val rowFor = row.slice(0..j).reversed()
                val colFor = col.slice(0..i).reversed()
                val rowRev = row
                    .slice(j..row.lastIndex)
                val colRev = col
                    .slice(i..col.lastIndex)

                val views = listOf(
                    rowFor,colFor,rowRev,colRev
                )
                val cur = views.map {
                    if (it.size == 1) return@map 0
                    val view = it.slice(1..it.lastIndex)
                    val index = view
                        .indexOfFirst {
                            it >= value
                        }
                    if (index < 0)
                        view.size
                    else
                        view.slice(0..index).size
                }.reduce {a, b -> a * b}
                if (cur > max) max = cur
            }
        }
        return max
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
