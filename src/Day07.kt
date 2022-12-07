import java.math.BigInteger

sealed interface TerminalLine {
    sealed class Command : TerminalLine {
        class CdCommand(val arg: String) : Command()
        object LsCommand : Command()
    }

    sealed class Listing(val name: String) : TerminalLine {
        abstract val size: BigInteger

        class FileListing(override val size: BigInteger, name: String) : Listing(name)
        class DirListing(name: String) : Listing(name) {
            var parent: DirListing? = null
            val contents: MutableList<Listing> = mutableListOf()
            override val size get() = contents.sumOf { it.size }
        }
    }
}

fun main() {
    fun part1(input: List<String>): BigInteger {
        val mapped: List<TerminalLine> = input.map {
            if (it.startsWith("$ cd")) {
                TerminalLine.Command.CdCommand(it.split(" ").last())
            } else if (it.startsWith("$ ls")) {
                TerminalLine.Command.LsCommand
            } else {
                if (it.startsWith("dir")) {
                    TerminalLine.Listing.DirListing(it.split(" ")[1])
                } else {
                    val data = it.split(" ")
                    TerminalLine.Listing.FileListing(data[0].toBigInteger(), data[1])
                }
            }
        }
        val dirs = mutableMapOf<String, TerminalLine.Listing.DirListing>("/" to TerminalLine.Listing.DirListing("/"))

        var currentDir = dirs["/"]
        mapped.forEach {
            when (it) {
                is TerminalLine.Command.CdCommand -> {
                    if (it.arg != "..") {
                        if (it.arg == "/") {
                            currentDir = dirs["/"]
                        } else {
                            val dirName = it.arg
                            currentDir =
                                currentDir?.contents?.filterIsInstance(TerminalLine.Listing.DirListing::class.java)
                                    ?.firstOrNull { it.name == dirName }
                        }
                    } else {
                        currentDir = currentDir?.parent
                    }
                }

                TerminalLine.Command.LsCommand -> {

//                    dirs[currentDir] = TerminalLine.Listing.DirListing(currentDir)
                }

                is TerminalLine.Listing.DirListing -> {
                    currentDir?.contents?.add(it)
                    it.parent = currentDir
                }

                is TerminalLine.Listing.FileListing -> {
                    currentDir?.contents?.add(it)
                }
            }
        }
        val root = dirs["/"]
        val dirListings = root?.dirs()
        return dirListings?.map { it.size }?.filter { it <= 100000.toBigInteger() }?.sumOf { it } ?: BigInteger.ZERO
    }

    fun part2(input: List<String>): BigInteger {
        val mapped: List<TerminalLine> = input.map {
            if (it.startsWith("$ cd")) {
                TerminalLine.Command.CdCommand(it.split(" ").last())
            } else if (it.startsWith("$ ls")) {
                TerminalLine.Command.LsCommand
            } else {
                if (it.startsWith("dir")) {
                    TerminalLine.Listing.DirListing(it.split(" ")[1])
                } else {
                    val data = it.split(" ")
                    TerminalLine.Listing.FileListing(data[0].toBigInteger(), data[1])
                }
            }
        }
        val dirs = mutableMapOf<String, TerminalLine.Listing.DirListing>("/" to TerminalLine.Listing.DirListing("/"))

        var currentDir = dirs["/"]
        mapped.forEach {
            when (it) {
                is TerminalLine.Command.CdCommand -> {
                    if (it.arg != "..") {
                        if (it.arg == "/") {
                            currentDir = dirs["/"]
                        } else {
                            val dirName = it.arg
                            currentDir =
                                currentDir?.contents?.filterIsInstance(TerminalLine.Listing.DirListing::class.java)
                                    ?.firstOrNull { it.name == dirName }
                        }
                    } else {
                        currentDir = currentDir?.parent
                    }
                }

                TerminalLine.Command.LsCommand -> {

//                    dirs[currentDir] = TerminalLine.Listing.DirListing(currentDir)
                }

                is TerminalLine.Listing.DirListing -> {
                    currentDir?.contents?.add(it)
                    it.parent = currentDir
                }

                is TerminalLine.Listing.FileListing -> {
                    currentDir?.contents?.add(it)
                }
            }
        }
        val root = dirs["/"]
        val dirListings: List<TerminalLine.Listing.DirListing>? = root?.dirs()
        val usedSpace = root!!.size
        val freeSpace = (70000000).toBigInteger() - usedSpace
        val spaceRequired: BigInteger = 30000000.toBigInteger() - freeSpace
        return dirListings?.map { it.size }?.filter { it >= spaceRequired }?.min() ?: BigInteger.ZERO
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
//    println(part1(testInput))
    check(part1(testInput) == 95437L.toBigInteger())
//    println(part2(testInput))
//    check(part2(testInput) == 24933642L)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}

private fun TerminalLine.Listing.DirListing.dirs(): List<TerminalLine.Listing.DirListing> {
    return listOf(this) + this.contents.filterIsInstance<TerminalLine.Listing.DirListing>().flatMap { it.dirs() }
}
