package org.example

import org.example.models.Attributes
import org.example.models.Cell
import org.example.models.Direction
import org.example.models.Line
import org.example.models.Position
import org.example.screen.MutableScreenImpl
import org.example.scrollback.MutableScrollBack
import org.example.scrollback.MutableScrollBackImpl

class MutableTerminalBufferImpl(
    override val width: Int,
    override val height: Int
) : MutableTerminalBuffer {
    private val screen = MutableScreenImpl(width, height)
    private val scrollBack = MutableScrollBackImpl(width, height)

    override var attributes: Attributes
        get() = screen.attributes
        set(value) { screen.attributes = value }

    override var cursor: Position
        get() = screen.cursor
        set(value) { screen.cursor = value }

    override fun moveCursor(direction: Direction, distance: Int) {
        require(distance >= 0) { "Distance should not be negative" }
        cursor = when (direction) {
            Direction.UP -> cursor.copy(row = (cursor.row - distance).coerceAtLeast(0))
            Direction.DOWN -> cursor.copy(row = (cursor.row + distance).coerceAtMost(height - 1))
            Direction.LEFT -> cursor.copy(column = (cursor.column - distance).coerceAtLeast(0))
            Direction.RIGHT -> cursor.copy(column = (cursor.column + distance).coerceAtMost(width - 1))
        }
    }

    override fun write(text: String) {
        val scrolledLines = screen.write(text)
        scrollBack.append(scrolledLines)
    }

    override fun insert(text: String) {
        val scrolledLines = screen.insert(text)
        scrollBack.append(scrolledLines)
    }

    override fun fill(char: Char) {
        screen.fill(char)
    }

    override fun insertEmptyLineAtBottom() {
        val scrolledLines = screen.insertEmptyLineAtBottom()
        scrollBack.append(scrolledLines)
    }

    override fun clearScreen() {
        while (screen.content.any { line -> line.cells.any { it !is Cell.Empty } }) {
            val scrolledLines = screen.insertEmptyLineAtBottom()
            scrollBack.append(scrolledLines)
        }
    }

    override fun clearScrollback() {
        scrollBack.clear()
    }

    override fun line(lineNumber: Int): String {
        return if (lineNumber < 0) {
            scrollBack.content.toStr()
        } else {
            screen.content.toStr()
        }
    }

    override val screenContent: String
        get() = screen.content.toStr()
    override val screenAndScrollback: String
        get() = (scrollBack.content + screen.content).toStr()

    private fun List<Line>.toStr() = joinToString("\n") { it.cells.joinToString {
        when (it) {
            is Cell -> it.char.toString()
            Cell.Empty -> " "
        }
    } }

    override fun charAt(row: Int, column: Int): Char {
        return if (column < 0) {
            scrollBack.cellAt(row, column).charOr(' ')
        } else {
            screen.cellAt(row, column).charOr(' ')
        }
    }

    override fun attributesAt(position: Position): Attributes = with(position) {
        return if (column < 0) {
            scrollBack.cellAt(row, column).attributesOr(Attributes())
        } else {
            screen.cellAt(row, column).attributesOr(Attributes())
        }
    }
}