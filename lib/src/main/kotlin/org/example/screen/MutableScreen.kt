package org.example.screen

import org.example.models.Attributes
import org.example.models.Cell
import org.example.models.Direction
import org.example.models.Position
import org.example.models.ScrolledLines
import javax.swing.table.TableColumn

internal interface MutableScreen : Screen {
    var attributes: Attributes
    override var cursor: Position

    fun write(text: String): ScrolledLines
    fun insert(text: String): ScrolledLines

    fun fill(char: Char)

    fun insertEmptyLineAtBottom(): ScrolledLines
}