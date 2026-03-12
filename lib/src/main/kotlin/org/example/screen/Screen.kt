package org.example.screen

import org.example.models.Cell
import org.example.models.Line
import org.example.models.Position

internal interface Screen {
    val width: Int
    val height: Int
    val cursor: Position

    fun line(lineNumber: Int): Line
    val content: List<Line>

    fun cellAt(row: Int, column: Int): Cell
}