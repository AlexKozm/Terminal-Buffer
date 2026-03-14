package org.example.scrollback

import org.example.models.Cell
import org.example.models.Line
import org.example.models.ScrolledLines
import org.example.models.SomeCell

internal class MutableScrollBackImpl(
    private val width: Int,
    private val height: Int
) : MutableScrollBack {
    private val grid = ArrayDeque<Line>()

    override fun append(list: ScrolledLines) {
        if (list.lines.size >= height) {
            grid.clear()
            val toAdd = list.lines.takeLast(height)
            grid.addAll(toAdd)
            return
        }

        val numOfLinesToDrop = (grid.size + list.lines.size - height).coerceAtLeast(0)
        grid.subList(0,  numOfLinesToDrop).clear()
        grid.addAll(list.lines)
    }

    override fun line(lineNumber: Int): Line {
        return grid.getOrNull(lineNumber) ?: Line(List(width) { Cell.Empty })
    }

    override val content: List<Line>
        get() = grid.toList()

    override fun cellAt(row: Int, column: Int): SomeCell {
        return grid.getOrNull(row)?.cells?.getOrNull(column) ?: Cell.Empty
    }
}