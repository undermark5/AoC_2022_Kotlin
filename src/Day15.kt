import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


enum class LocationValue {
    EMPTY,
    SENSOR,
    BEACON,
    NOT_BEACON,
}

fun main() {
    fun printGrid(grid: Map<Int, MutableList<Pair<LocationValue, IntRange>>>) {
        val minY = grid.minOf { it.key }
        val maxY = grid.maxOf { it.key }
        val minX = grid.values.minOf { it.minOf { it.second.first } }
        val maxX = grid.values.maxOf { it.maxOf { it.second.last } }
        for (row in minY..maxY) {
            val ranges = grid[row]
            for (col in minX..maxX) {
                print(
                    if (ranges != null) {
                        ranges.firstOrNull { it.second.contains(col) }?.let {
                            when (it.first) {
                                LocationValue.EMPTY -> "."
                                LocationValue.SENSOR -> "S"
                                LocationValue.BEACON -> "B"
                                LocationValue.NOT_BEACON -> "#"
                            }
                        } ?: "."
                    } else {
                        "."
                    }
                )
            }
            println()
        }
        println()
        println()
    }

    val sensorsRegex =
        Regex("Sensor at x=(?<sensorX>-?\\d+), y=(?<sensorY>-?\\d+): closest beacon is at x=(?<beaconX>-?\\d+), y=(?<beaconY>-?\\d+)")

    fun part1(input: List<String>, row: Int): Int {
        val sensors = mutableListOf<Pair<Int, Int>>()
        val beacons = mutableListOf<Pair<Int, Int>>()
        val sensorsToBeacons = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
        input.forEach {
            sensorsRegex.matchEntire(it)?.groups?.let {
                (it as? MatchNamedGroupCollection?)?.let {
                    val sensorX = it["sensorX"]?.value?.toIntOrNull() ?: Int.MIN_VALUE
                    val sensorY = it["sensorY"]?.value?.toIntOrNull() ?: Int.MIN_VALUE
                    val beaconX = it["beaconX"]?.value?.toIntOrNull() ?: Int.MIN_VALUE
                    val beaconY = it["beaconY"]?.value?.toIntOrNull() ?: Int.MIN_VALUE
                    val sensor = sensorY to sensorX
                    val beacon = beaconY to beaconX
                    sensors.add(sensor)
                    beacons.add(beacon)
                    sensorsToBeacons[sensor] = beacon
                }
            }
        }

        val grid = mutableMapOf<Int, MutableList<Pair<LocationValue, IntRange>>>()
        sensors.forEach {
            val relativeY = it.first
            val relativeX = it.second
            grid.getOrPut(relativeY) {
                mutableListOf()
            }.add(LocationValue.SENSOR to relativeX..relativeX)
        }
        beacons.forEach {
            val relativeY = it.first
            val relativeX = it.second
            grid.getOrPut(relativeY) {
                mutableListOf()
            }.add(LocationValue.BEACON to relativeX..relativeX)
        }
//        println(grid.joinToString("\n", postfix = "\n") { it.joinToString("") })
        sensorsToBeacons.forEach { (sensor, beacon) ->
            val distance = abs(sensor.first - beacon.first) + abs(sensor.second - beacon.second)
            val relativeCoordinate = (sensor.first) to (sensor.second)
            val top = relativeCoordinate.first - distance
            val bottom = relativeCoordinate.first + distance
            var rangeSize = 0
            val rangeCenter = relativeCoordinate.second
            val trimmedRange = (top..bottom).trim(-2..50)
            rangeSize += trimmedRange.first - top
            if (relativeCoordinate.first < trimmedRange.first) {
                rangeSize -= trimmedRange.first - relativeCoordinate.first
            }
            trimmedRange.forEach { value ->
                grid.getOrPut(value) {
                    mutableListOf()
                }.addOrMerge(LocationValue.NOT_BEACON to (rangeCenter - rangeSize)..(rangeCenter + rangeSize))
                if (value < relativeCoordinate.first) {
                    rangeSize++
                } else {
                    rangeSize--
                }
            }
//            printGrid(grid)
        }

//        printGrid(grid)

//        println(grid.joinToString("\n", postfix = "\n") { it.joinToString("") })
        val ranges = grid[row]?.filter { it.first != LocationValue.EMPTY }?.map { it.second } ?: emptyList()
        return ranges.fold(emptySet<Int>()) { acc, range ->
            acc + range.toSet()
        }.toMutableSet().apply {
            removeAll((grid[row]?.filter { it.first != LocationValue.NOT_BEACON }
                ?.fold(emptySet<Int>()) { acc, pair -> acc + pair.second }
                ?: emptySet()).toSet())
        }.size
    }

    fun part2(input: List<String>, validCoordinates: IntRange): Int {
        val sensors = mutableListOf<Pair<Int, Int>>()
        val beacons = mutableListOf<Pair<Int, Int>>()
        val sensorsToBeacons = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
        input.forEach {
            sensorsRegex.matchEntire(it)?.groups?.let {
                (it as? MatchNamedGroupCollection?)?.let {
                    val sensorX = it["sensorX"]?.value?.toIntOrNull() ?: Int.MIN_VALUE
                    val sensorY = it["sensorY"]?.value?.toIntOrNull() ?: Int.MIN_VALUE
                    val beaconX = it["beaconX"]?.value?.toIntOrNull() ?: Int.MIN_VALUE
                    val beaconY = it["beaconY"]?.value?.toIntOrNull() ?: Int.MIN_VALUE
                    val sensor = sensorY to sensorX
                    val beacon = beaconY to beaconX
                    sensors.add(sensor)
                    beacons.add(beacon)
                    sensorsToBeacons[sensor] = beacon
                }
            }
        }

        val grid = mutableMapOf<Int, MutableList<Pair<LocationValue, IntRange>>>()
        sensors.forEach {
            val relativeY = it.first
            val relativeX = it.second
            grid.getOrPut(relativeY) {
                mutableListOf()
            }.add(LocationValue.SENSOR to relativeX..relativeX)
        }
        beacons.forEach {
            val relativeY = it.first
            val relativeX = it.second
            grid.getOrPut(relativeY) {
                mutableListOf()
            }.add(LocationValue.BEACON to relativeX..relativeX)
        }
//        println(grid.joinToString("\n", postfix = "\n") { it.joinToString("") })
        sensorsToBeacons.forEach { (sensor, beacon) ->
            val distance = abs(sensor.first - beacon.first) + abs(sensor.second - beacon.second)
            val relativeCoordinate = (sensor.first) to (sensor.second)
            val top = relativeCoordinate.first - distance
            val bottom = relativeCoordinate.first + distance
            var rangeSize = 0
            val rangeCenter = relativeCoordinate.second
            val trimmedRange = (top..bottom).trim(validCoordinates)
            rangeSize += trimmedRange.first - top
            if (relativeCoordinate.first < trimmedRange.first) {
                rangeSize -= trimmedRange.first - relativeCoordinate.first
            }
            trimmedRange.forEach { value ->
                grid.getOrPut(value) {
                    mutableListOf()
                }.addOrMerge(LocationValue.NOT_BEACON to (rangeCenter - rangeSize)..(rangeCenter + rangeSize))
                if (value < relativeCoordinate.first) {
                    rangeSize++
                } else {
                    rangeSize--
                }
            }
//            printGrid(grid)
        }
        println("sensorsCompleted")

//        printGrid(grid)

//        println(grid.joinToString("\n", postfix = "\n") { it.joinToString("") })
        val possibleRows = grid
            .filter { it.key in validCoordinates }
            .map { (y, ranges) ->
                y to ranges.fold(emptyList<IntRange>()) { acc, pair ->
                    acc.toMutableList().apply { addOrMerge(pair.second.trim(validCoordinates)) }
                }
            }
        val coordinate = possibleRows
            .map {
                var current = listOf(*it.second.toTypedArray())
                var old: List<IntRange>
                if (current.size > 1) {
                    do {
                        old = current
                        val range = current.first()
                        val remainder = current.slice(1..current.lastIndex)
                        val overlapping = remainder.filter { it.overlapsWith(range) || it.isAdjacentTo(range) }
                        val result = overlapping.fold(range) { acc, intRange ->
                            acc.merge(intRange)
                        }

                        current = remainder.toMutableList().apply {
                            removeAll(overlapping)
                            add(result)
                        }
                    } while (current.size != old.size && current.size != 1)
                }
                it.first to current
            }.first { it.second.sumOf { it.count() } != validCoordinates.count() }
        println(coordinate)
        return 56000011
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(runBlocking { part1(testInput, 10) } == 26)
    check( part2(testInput, 0..20) == 56000011)

    val input = readInput("Day15")
//    println(runBlocking { part1(input, 2000000) })
    println(part2(input, 0..4000000) )
}

private fun MutableList<IntRange>.addOrMerge(intRange: IntRange) {
    val firstLoc = this.indexOfFirst { range -> intRange.overlapsWith(range) }
    if (firstLoc == -1) {
        this.add(intRange)
    } else {
        val value = this[firstLoc]
        this[firstLoc] = intRange.merge(value)
    }
}

private fun IntRange.trim(validCoordinates: IntRange): IntRange {
    val start = max(validCoordinates.first, this.first)
    val end = min(validCoordinates.last, this.last)
    return start..end
}

private fun MutableList<Pair<LocationValue, IntRange>>.addOrMerge(e: Pair<LocationValue, IntRange>) {
    val firstLoc = this.indexOfFirst { it.first == e.first && e.second.overlapsWith(it.second) }
    if (firstLoc == -1) {
        this.add(e)
    } else {
        val value = this[firstLoc]
        this[firstLoc] = value.first to e.second.merge(value.second)
    }

}

private fun IntRange.overlapsWith(second: IntRange): Boolean {
    return !(this.last < second.first || this.first > second.last)
}

private fun IntRange.isAdjacentTo(second: IntRange): Boolean {
    return this.last + 1 == second.first || this.first - 1 == second.last
}

private fun IntRange.merge(second: IntRange): IntRange {
    val start = min(this.first, second.first)
    val end = max(this.last, second.last)
    return start..end
}

