package org.example.scrollback

import org.example.models.Cell
import org.example.models.Line
import org.example.models.SomeCell

internal interface ScrollBack {
    fun line(lineNumber: Int): Line
    val content: List<Line>

    fun cellAt(row: Int, column: Int): SomeCell
}