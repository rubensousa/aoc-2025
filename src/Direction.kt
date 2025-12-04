enum class Direction(val row: Int, val col: Int) {
    UP_LEFT(-1,-1),
    UP(-1, 0),
    UP_RIGHT(-1,1),
    RIGHT(0, 1),
    DOWN_RIGHT(1,1),
    DOWN(1, 0),
    DOWN_LEFT(1,-1),
    LEFT(0, -1);

    fun opposite(): Direction {
        return when (this) {
            UP_LEFT -> DOWN_RIGHT
            UP -> DOWN
            UP_RIGHT -> DOWN_LEFT
            RIGHT -> LEFT
            DOWN_RIGHT -> UP_LEFT
            DOWN -> UP
            DOWN_LEFT -> UP_RIGHT
            LEFT -> RIGHT
        }
    }
}
