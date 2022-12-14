import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("input", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

fun <E> MutableList<E>.push(c: E) {
    this.add(0, c)
}

fun <E> MutableList<E>.pop(): E {
    return this.removeAt(0)
}

fun <E> MutableList<E>.peek(): E {
    return this.first()
}

data class MutablePair<T, O>(var first: T, var second: O)

fun String.toIntRange(delim:String = ".."):IntRange {
    val split = this.split(delim)
    return split.first().toInt()..split.last().toInt()
}

operator fun <R> (() -> R).times(paddingSize: Int): List<R> {
    val value = this()
    return MutableList(paddingSize) { value }
}

operator fun IntRange.contains(intRange: IntRange): Boolean {
    return first <= intRange.first && intRange.last <= last
}

operator fun <E> List<MutableList<E>>.set(pair: Pair<Int,Int>, value: E) {
    this[pair.first][pair.second] = value
}

operator fun <E> List<List<E>>.get(pair: Pair<Int, Int>): E {
    return this[pair.first][pair.second]
}

fun <E> List<List<E>>.getOrNull(pair:Pair<Int,Int>): E? {
    return this.getOrNull(pair.first)?.getOrNull(pair.second)
}

open class Node<E>(val value: E) {
    var visited = false
    var neighbors = listOf<Node<E>>()
}


fun <E> List<E>.toPair():Pair<E,E> {
    return first() to last()
}

val <A, B> Pair<A, B>.reversed: Pair<B, A>
    get() = second to first