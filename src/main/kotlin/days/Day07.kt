package days

object Day07 : Day<Long, List<String>> {
    override val number: Int = 7
    override val expectedPart1Test: Long = 95437L
    override val expectedPart2Test: Long = 24933642L

    override fun part1(input: List<String>): Long {
        val root = parse(input)
        val cc = root.listDir()
        return cc.map { it.size }
            .filter { it < 100000 }
            .sum()
    }

    override fun part2(input: List<String>): Long {
        val root = parse(input)
        val dirList = root.listDir()
        val rootSize = dirList.filter { it.name == "/" }.map { it.size }.single()
        val used = 70000000L - rootSize
        val toFree = 30000000L - used
        return dirList.filter { it.size >= toFree }.minOfOrNull { it.size } ?: -1L
    }


    private fun parse(input: List<String>): Directory {
        val root = Directory("/", null, mutableListOf())
        var currentDirectory: Directory = root
        input.forEach { l ->
            when {
                l.startsWith("$ cd ") -> {
                    val newDir = l.substringAfter("$ cd ")
                    currentDirectory = when (newDir) {
                        "/" -> root
                        ".." -> currentDirectory.parent ?: root // cd ..  on root stays on root
                        else -> (currentDirectory.findDirectory(newDir) as Directory)
                    }
                }

                l.startsWith("$ ls") -> {
                    // do nothing
                }

                else -> {
                    val item = l.split(" ")
                    if (item[0] == "dir") {
                        currentDirectory.children.add(Directory(item[1], currentDirectory, mutableListOf()))
                    } else {
                        currentDirectory.children.add(File(item[1], currentDirectory, item[0].toLong()))
                    }
                }
            }
        }
        return root
    }

    data class DirSummary(val name: String, val size: Long, val parent: String)
    abstract class Node(val name: String, val parent: Directory?) {
        abstract fun size(): Long
        abstract fun isDirectory(): Boolean
    }

    class Directory(
        name: String,
        parent: Directory?,
        val children: MutableList<Node>
    ) : Node(name, parent) {
        override fun size() = children.sumOf { it.size() }
        override fun isDirectory() = true

        fun findDirectory(directoryName: String): Node =
            children.single { child ->
                child is Directory && child.name == directoryName
            }


        fun listDir(): List<DirSummary> =
            children.filter { it.isDirectory() }.flatMap { (it as Directory).listDir() } +
                    DirSummary(name, size(), parent?.name ?: "null")
    }

    class File(name: String, parent: Directory?, val size: Long = 0) : Node(name, parent) {
        override fun size() = size
        override fun isDirectory() = false
    }
}