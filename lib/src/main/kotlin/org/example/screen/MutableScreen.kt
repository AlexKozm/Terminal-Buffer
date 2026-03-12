package org.example.screen

import org.example.models.Attributes
import org.example.models.Cell
import org.example.models.Direction
import org.example.models.Position
import org.example.models.ScrolledLines

internal interface MutableScreen : Screen {
    var attributes: Attributes
    override var cursor: Position
    fun moveCursor(direction: Direction, distance: Int)

    fun write(cells: List<Cell>): ScrolledLines
    fun insert(cells: List<Cell>): ScrolledLines
    fun fill(cell: Cell): ScrolledLines

    fun insertEmptyLineAtBottom(): ScrolledLines
    fun clearScreen(): ScrolledLines
    fun clearBuffer(): ScrolledLines
}