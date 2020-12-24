package util

class HexagonalCoordinate(var x: Int, var y: Int, var z: Int) {

    fun east() : HexagonalCoordinate {
        this.x++
        this.y--
        return this
    }

    fun west() : HexagonalCoordinate {
        this.x--
        this.y++
        return this
    }

    fun northEast() : HexagonalCoordinate {
        this.y--
        this.z++
        return this
    }

    fun northWest() : HexagonalCoordinate {
        this.x--
        this.z++
        return this
    }

    fun southEast() : HexagonalCoordinate {
        this.x++
        this.z--
        return this
    }

    fun southWest() : HexagonalCoordinate {
        this.y++
        this.z--
        return this
    }

    fun surroundingCoordinates() : List<HexagonalCoordinate> {
        return listOf(
            copy().east(),
            copy().southEast(),
            copy().southWest(),
            copy().west(),
            copy().northWest(),
            copy().northEast()
        )
    }

    private fun copy() : HexagonalCoordinate{
        return HexagonalCoordinate(x, y, z)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HexagonalCoordinate

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + z
        return result
    }


}