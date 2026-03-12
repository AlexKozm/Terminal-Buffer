package org.example

import org.example.models.Attributes
import org.example.models.Direction
import org.example.models.Position

interface MutableTerminalBuffer : TerminalBuffer {
    var attributes: Attributes

    /**
     * Moves cursor within the frame of the screen.
     * If action can't be applied, then does nothing
     */
    override var cursor: Position

    /**
     * Moves cursor within the frame of the screen.
     * If action can't be applied, then does nothing
     */
    fun moveCursor(direction: Direction, distance: Int)

    /**
     * Inserts [text] over existing one.
     * If inserted text is inserted beyond the border of the terminal,
     * it goes to next line.
     *
     * This function could change the positon of the cursor
     */
    fun write(text: String)

    /**
     * Moves text after cursor to right and inserts [text]
     * If moved or inserted text is inserted beyond the border of the terminal,
     * it goes to next line.
     *
     * This function could change the positon of the cursor
     */
    fun insert(text: String)

    /**
     * Fills the whole line with [char].
     *
     * This does not change the positon of the cursor
     */
    fun fill(char: Char)

    /**
     * This does not change the positon of the cursor
     */
    fun insertEmptyLineAtBottom()

    /**
     * This does not change the positon of the cursor
     */
    fun clearScreen()

    /**
     * This does not change the positon of the cursor
     */
    fun clearScrollback()
}