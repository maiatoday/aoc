object Day16 {

    fun part1(input: List<String>): Long {
        val transmission = input[0].map { it.digitToInt(16).toString(2).padStart(4, '0') }.joinToString("")
        return decode(transmission)
    }

    private fun decode(transmission: String): Long {
        val packet = Packet(transmission, 0)
        return packet.versionCount()
    }

    fun part2(input: List<String>): Long {
        val transmission = input[0].map { it.digitToInt(16).toString(2).padStart(4, '0') }.joinToString("")
        return evaluate(transmission)
    }

    private fun evaluate(transmission: String): Long {
        val packet = Packet(transmission, 0)
        return packet.value()
    }
}

class Packet(private val transmission: String, private val position: Int) {
    private var currentPosition = position
    private var length: Int = 0
    private var version: Int = 0
    private var typeId: TypeId = TypeId.UNKNOWN

    // LITERAL
    private var literal = ""

    // OPERATOR
    private var lengthId: LengthId = LengthId.NONE
    private var subPackets: MutableList<Packet> = mutableListOf()

    init {
        parseHeader()
        parsePayload()
    }

    fun versionCount(): Long {
        return (version + subPackets.sumOf { it.versionCount() })
    }

    fun value(): Long =
        when (typeId) {
            TypeId.LITERAL -> literal.toLong(2)
            TypeId.UNKNOWN -> {
                throw (Exception("unknown packet"))
            }
            TypeId.SUM -> {
                subPackets.sumOf { it.value() }
            }
            TypeId.PRODUCT -> {
                subPackets.fold(1) { acc, it -> acc * it.value() }
            }
            TypeId.MIN -> {
                subPackets.minOfOrNull { it.value() } ?: 0
            }
            TypeId.MAX -> {
                subPackets.maxOfOrNull { it.value() } ?: 0
            }
            TypeId.GT -> {
                if (subPackets[0].value() > subPackets[1].value()) 1 else 0
            }
            TypeId.LT -> {
                if (subPackets[0].value() < subPackets[1].value()) 1 else 0
            }
            TypeId.EQUAL -> {
                if (subPackets[0].value() == subPackets[1].value()) 1 else 0
            }
        }

    private fun parseHeader() {
        currentPosition = position
        version = readBits(3).toInt(2)
        val typeIdVal = readBits(3).toInt(2)
        typeId = TypeId.fromInt(typeIdVal)
    }

    private fun parsePayload() {
        when (typeId) {
            TypeId.LITERAL -> parseLiteral()
            else -> parseOperator()
        }
    }

    private fun parseOperator() {
        lengthId = if (readBits(1) == "1") {
            LengthId.PACKET_COUNT
        } else {
            LengthId.BIT_COUNT
        }
        when (lengthId) {
            LengthId.BIT_COUNT -> {
                var bitCount = readBits(15).toInt(2)
                while (bitCount > 0) {
                    val packet = Packet(transmission, currentPosition)
                    subPackets.add(packet)
                    currentPosition += packet.length
                    length += packet.length
                    bitCount -= packet.length
                }
            }
            LengthId.PACKET_COUNT -> {
                val packetCount = readBits(11).toInt(2)
                repeat(packetCount) {
                    val packet = Packet(transmission, currentPosition)
                    subPackets.add(packet)
                    currentPosition += packet.length
                    length += packet.length
                }
            }
            else -> {}
        }
    }

    private fun parseLiteral() {
        var more = true
        while (more) {
            more = readBits(1) == "1"
            literal += readBits(4)
        }
    }

    private fun readBits(bitCount:Int):String {
        val s = transmission.substring(currentPosition, currentPosition + bitCount)
        currentPosition += bitCount
        length += bitCount
        return s
    }

    enum class LengthId {
        NONE, BIT_COUNT, PACKET_COUNT
    }

    enum class TypeId(var value: Int) {
        UNKNOWN(-1),
        SUM(0),
        PRODUCT(1),
        MIN(2),
        MAX(3),
        LITERAL(4),
        GT(5),
        LT(6),
        EQUAL(7);

        companion object {
            fun fromInt(value: Int) = values().first { it.value == value }
        }
    }
}



