package org.example.screen

import org.example.models.Attributes
import org.example.models.Cell
import org.example.models.Direction
import org.example.models.Line
import org.example.models.Position
import org.example.models.ScrolledLines
import kotlin.math.*

internal class MutableScreenImpl(override val width: Int, override val height: Int) : MutableScreen {

    private val grid = Array(height) { Array(width) { Cell.EMPTY_CELL } }
    private val cellsTillScreenEnd get() = width - cursor.column + width * (height - cursor.row - 1)
    private val cellsTillLineEnd get() = width - cursor.column
    private val cellsNum = width * height
    private infix fun Int.divideCeil(divisor: Int): Int {
        require(divisor > 0) { "Divisor must be positive" }
        return (this + divisor - 1) / divisor
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
        var t = text
        val scrolled = mutableListOf<Line>()
        while (t.isNotEmpty()) {
            val currentLineText = t.slice(0 ..< cellsTillLineEnd.coerceAtMost(t.length))
            val delta = cellsTillLineEnd - currentLineText.length
            val arr = grid[cursor.row]
            for (i in 1..currentLineText.length) {
                arr[width - i] = arr[width - delta - i]
                arr[width - delta - i] = Cell(text[currentLineText.length - i], attributes)
            }

            t = t.slice(cellsTillLineEnd.coerceAtMost(t.length) ..< t.length)
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
        TODO("Not yet implemented")
    }

    override fun insertEmptyLineAtBottom(): ScrolledLines {
        val scrolledLine = Line(grid.first().toList())
        for (i in 1..< grid.size) {
            grid[i - 1] = grid[i]
        }
        grid[grid.lastIndex] = Array(width) { Cell.EMPTY_CELL }
        return ScrolledLines(listOf(scrolledLine))
    }

    override fun line(lineNumber: Int): Line {
        return Line(grid[lineNumber].toList())
    }

    override val content: List<Line>
        get() = grid.map { Line(it.toList()) }

    override fun cellAt(row: Int, column: Int): Cell {
        require(row >= 0) { "Row must be non-negative" }
        require(row < height) { "Row must be less than screens height=$height" }
        require(column >= 0) { "Column must be non-negative" }
        require(column < width) { "Column must be less than screens width=$width" }
        return grid[row][column]
    }
}