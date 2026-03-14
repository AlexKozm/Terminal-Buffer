package org.example.screen

import org.example.models.Attributes
import org.example.models.Cell
import org.example.models.Line
import org.example.models.Position
import org.example.models.ScrolledLines
import org.example.models.SomeCell

internal class MutableScreenImpl(override val width: Int, override val height: Int) : MutableScreen {

    private val grid = Array(height) { Array<SomeCell>(width) { Cell.Empty } }
    private val cellsTillScreenEnd get() = width - cursor.column + width * (height - cursor.row - 1)
    private val cellsTillLineEnd get() = width - cursor.column
    private val cellsNum = width * height
    private infix fun Int.divideCeil(divisor: Int): Int {
        require(divisor > 0) { "Divisor must be positive" }
        return (this + divisor - 1) / divisor
    }
    private val Position.firstAtGrid get() = row == 0 && column == 0
    private val Position.firstAtLine get() = column == 0
    private val Position.lastAtLine get() = column == width - 1
    private val Position.lastAtGrid get() = row == height - 1 && column == width - 1

    private val Position?.next get() = when {
        this == null -> Position(0, 0)
        lastAtGrid -> null
        lastAtLine -> Position(row = row + 1, column = 0)
        else -> copy(column = column + 1)
    }

    private inner class GridIterator(
        startPosition: Position
    ): Iterator<Position> {
        private var cursor: Position? = when {
            startPosition.firstAtGrid -> null
            startPosition.firstAtLine -> startPosition.copy(row = startPosition.row - 1, column = width - 1)
            else -> startPosition.copy(column = startPosition.column - 1)
        }

        override fun next(): Position {
            val localCursor = cursor
            val nextPosition = when {
                localCursor == null -> Position(0, 0)
                localCursor.lastAtLine -> Position(row = localCursor.row + 1, column = 0)
                else -> localCursor.copy(column = localCursor.column + 1)
            }
            cursor = nextPosition
            return nextPosition
        }

        fun toLineStart() {
            val localCursor = cursor
            cursor = when {
                localCursor == null -> null
                else -> localCursor.copy(row = localCursor.row - 1, column = 0)
            }
        }

        fun toEndOfPrevLine() {
            val localCursor = cursor
            cursor = when {
                localCursor == null || localCursor.column == 0 -> null
                else -> localCursor.copy(row = localCursor.row - 1, column = width - 2)
            }
        }

        override fun hasNext(): Boolean {
            return cursor?.lastAtGrid?.not() ?: true
        }
    }


    override var attributes: Attributes = Attributes()
    override var cursor: Position = Position(0, 0)
        set(value) {
            field = Position(
                row = value.row.coerceIn(0 until height),
                column = value.column.coerceIn(0 until width)
            )
        }


    override fun write(text: String): ScrolledLines {
        val scrolled = mutableListOf<Line>()
        for (char in text) {
            grid[cursor.row][cursor.column] = Cell(char, attributes)
            cursor = when {
                cursor == Position(height - 1, width - 1) -> {
                    scrolled += insertEmptyLineAtBottom().lines.first()
                    Position(height - 1, 0)
                }
                cursor.column == width - 1 -> {
                    Position(cursor.row + 1, 0)
                }
                else -> {
                    cursor.copy(column = cursor.column + 1)
                }
            }
        }
        return ScrolledLines(scrolled)
//        val linesToAdd =
//            if (text.length <= cellsTillEnd) 0
//            else (text.length - cellsTillEnd) divideCeil width
//        val scrolled = (1..linesToAdd).map { insertEmptyLineAtBottom().lines.first() }.let { ScrolledLines(it) }
//        cursor =
//            if (linesToAdd >= height) Position(0, 0)
//            else cursor.copy(column = cursor.column - linesToAdd)
//        TODO("Not yet implemented")
    }

    override fun insert(text: String): ScrolledLines {
        val scrolled = mutableListOf<Line>()
        val shiftIterator = GridIterator(cursor)
        val movedChars = mutableListOf<Cell>()
        val textToInsert = text.map { Cell(it, attributes) }.toMutableList()

        var emptyCellsCounter = 0
        while (emptyCellsCounter < text.length) {
            if (!shiftIterator.hasNext()) {
                if (cursor.row == 0) {
                    do {
                        val cell = textToInsert.removeFirstOrNull() ?: movedChars.removeFirst()
                        grid[cursor.row][cursor.column] = cell

                        if (!cursor.lastAtLine) {
                            cursor = cursor.next ?: Position(0, 0)
                        } else {
                            break
                        }
                    } while (true)
                } else {
                    cursor = cursor.copy(row = cursor.row - 1)
                }

                scrolled += insertEmptyLineAtBottom().lines
                shiftIterator.toEndOfPrevLine()
            }

            val pos = shiftIterator.next()
            when (val cell = grid[pos.row][pos.column]) {
                is Cell -> movedChars.add(cell)
                Cell.Empty -> emptyCellsCounter++
            }
        }

        val insertTextIterator = GridIterator(cursor)
        text.map { Cell(it, attributes) }.forEach {
            val pos = insertTextIterator.next()
            grid[pos.row][pos.column] = it
            cursor = pos
        }

        cursor = when {
            cursor == Position(height - 1, width - 1) -> {
                scrolled += insertEmptyLineAtBottom().lines.first()
                Position(height - 1, 0)
            }
            cursor.column == width - 1 -> {
                Position(cursor.row + 1, 0)
            }
            else -> {
                cursor.copy(column = cursor.column + 1)
            }
        }

        val insertShiftIterator = GridIterator(cursor)
        movedChars.forEach {
            val pos = insertShiftIterator.next()
            grid[pos.row][pos.column] = it
        }

        return ScrolledLines(scrolled)
    }

    override fun insertEmptyLineAtBottom(): ScrolledLines {
        val scrolledLine = Line(grid.first().toList())
        for (i in 1..< grid.size) {
            grid[i - 1] = grid[i]
        }
        grid[grid.lastIndex] = Array(width) { Cell.Empty }
        return ScrolledLines(listOf(scrolledLine))
    }

    override fun line(lineNumber: Int): Line {
        return Line(grid[lineNumber].toList())
    }

    override val content: List<Line>
        get() = grid.map { Line(it.toList()) }

    override fun cellAt(row: Int, column: Int): SomeCell {
        require(row >= 0) { "Row must be non-negative" }
        require(row < height) { "Row must be less than screens height=$height" }
        require(column >= 0) { "Column must be non-negative" }
        require(column < width) { "Column must be less than screens width=$width" }
        return grid[row][column]
    }
}