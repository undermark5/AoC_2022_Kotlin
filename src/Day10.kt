fun main() {
    fun part1(input: List<String>): Long {
        var cycleCount = 0
        var xRegister = 1
        val cyclesOfInterest = mutableListOf(20,60,100,140,180,220)
        var sum = 0L
        input.forEach {
            if (it.startsWith("noop")) cycleCount++
            if (it.startsWith("addx")) {
                cycleCount += 2
                val currentCycle = if (cyclesOfInterest.isNotEmpty() && cycleCount >= cyclesOfInterest.peek()) {
                   cyclesOfInterest.pop()
                } else {
                    0
                }
                sum += xRegister * currentCycle
                xRegister += it.split(" ").last().toInt()
            }
        }
        return sum
    }

    fun part2(input: List<String>): String {
        var cycleCount = 0
        var xRegister = 1
        val cyclesOfInterest = mutableListOf(20,60,100,140,180,220)
        var sum = 0L
        val screen = List(6) {MutableList(40) {'.'} }
        var drawingCoords = 0 to 0
        var isAdding = false
        var instructionPointer = 0
        while (drawingCoords != 6 to 0) {
            if (drawingCoords.second in (xRegister -1)..(xRegister+1)) {
                screen[drawingCoords] = '#'
            }
            var (y, x) = drawingCoords
            x++
            if (x >= 40) {
                x = 0
                y++
            }
            drawingCoords = y to x
            val instruction = input[instructionPointer]
            if (isAdding) {
                xRegister += instruction.split(" ").last().toInt()
                instructionPointer++
                isAdding = false
            } else {
                if (instruction.startsWith("noop")) {
                    instructionPointer++
                } else {
                    isAdding = true
                }
            }


        }
        input.forEach {
            if (it.startsWith("noop")) cycleCount++
            if (it.startsWith("addx")) {
                cycleCount += 2
                val currentCycle = if (cyclesOfInterest.isNotEmpty() && cycleCount >= cyclesOfInterest.peek()) {
                    cyclesOfInterest.pop()
                } else {
                    0
                }
                sum += xRegister * currentCycle
                xRegister += it.split(" ").last().toInt()
            }
        }
        if (drawingCoords.second in (xRegister -1)..(xRegister+1)) {
            screen[drawingCoords] = '#'
        }
        var (y, x) = drawingCoords
        x++
        if (x >= 40) {
            x = 0
            y++
        }
        return screen.joinToString("\n") { it.joinToString("") }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    println(part1(testInput))
    check(part1(testInput) == 13140L)
    check(part2(testInput) == """##..##..##..##..##..##..##..##..##..##..
###...###...###...###...###...###...###.
####....####....####....####....####....
#####.....#####.....#####.....#####.....
######......######......######......####
#######.......#######.......#######.....""")

    val input = readInput("Day10")
    println(part1(input))
//    println(part2(testInput))
    println(part2(input))
}
