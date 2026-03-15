package org.example

import org.example.models.Attributes
import org.example.models.Position


interface TerminalBuffer {
    val width: Int
    val height: Int
    val cursor: Position

    fun line(lineNumber: Int): String
    val screenContent: String
    val screenAndScrollback: String

    fun charAt(row: Int, column: Int): Char
    fun attributesAt(position: Position): Attributes
}