package org.example.scrollback

import org.example.models.Cell
import org.example.models.Line

internal interface ScrollBack {
    fun line(lineNumber: Int): Line
    val content: List<Line>

    fun cellAt(row: Int, column: Int): Cell
}