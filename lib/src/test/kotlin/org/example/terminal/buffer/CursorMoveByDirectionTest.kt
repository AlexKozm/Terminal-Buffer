package org.example.terminal.buffer

import org.example.MutableTerminalBuffer
import org.example.MutableTerminalBufferImpl
import org.example.models.Direction
import org.example.models.Position
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class CursorMoveByDirectionTest {
    @Test
    fun `cursor moves up by 1`() {
        val terminal: MutableTerminalBuffer = MutableTerminalBufferImpl(6, 4)
        terminal.cursor = Position(2, 0)
        terminal.moveCursor(Direction.UP, 1)
        assertEquals(Position(1, 0), terminal.cursor)
    }

    @Test
    fun `cursor moves up by 2`() {
        val terminal: MutableTerminalBuffer = MutableTerminalBufferImpl(6, 4)
        terminal.cursor = Position(2, 0)
        terminal.moveCursor(Direction.UP, 2)
        assertEquals(Position(0, 0), terminal.cursor)
    }

    @Test
    fun `cursor moves up by 100`() {
        val terminal: MutableTerminalBuffer = MutableTerminalBufferImpl(6, 4)
        terminal.cursor = Position(2, 0)
        terminal.moveCursor(Direction.UP, 100)
        assertEquals(Position(0, 0), terminal.cursor)
    }

    @Test
    fun `cursor moves down by 1`() {
        val terminal: MutableTerminalBuffer = MutableTerminalBufferImpl(6, 4)
        terminal.cursor = Position(0, 0)
        terminal.moveCursor(Direction.DOWN, 1)
        assertEquals(Position(1, 0), terminal.cursor)
    }

    @Test
    fun `cursor moves down by 3`() {
        val terminal: MutableTerminalBuffer = MutableTerminalBufferImpl(6, 4)
        terminal.cursor = Position(0, 0)
        terminal.moveCursor(Direction.DOWN, 3)
        assertEquals(Position(3, 0), terminal.cursor)
    }

    @Test
    fun `cursor moves down by 100`() {
        val terminal: MutableTerminalBuffer = MutableTerminalBufferImpl(6, 4)
        terminal.cursor = Position(0, 0)
        terminal.moveCursor(Direction.DOWN, 100)
        assertEquals(Position(3, 0), terminal.cursor)
    }

    @Test
    fun `cursor moves right by 1`() {
        val terminal: MutableTerminalBuffer = MutableTerminalBufferImpl(6, 4)
        terminal.cursor = Position(0, 0)
        terminal.moveCursor(Direction.RIGHT, 1)
        assertEquals(Position(0, 1), terminal.cursor)
    }

    @Test
    fun `cursor moves right by 4`() {
        val terminal: MutableTerminalBuffer = MutableTerminalBufferImpl(6, 4)
        terminal.cursor = Position(0, 1)
        terminal.moveCursor(Direction.RIGHT, 4)
        assertEquals(Position(0, 5), terminal.cursor)
    }

    @Test
    fun `cursor moves right by 100`() {
        val terminal: MutableTerminalBuffer = MutableTerminalBufferImpl(6, 4)
        terminal.cursor = Position(0, 0)
        terminal.moveCursor(Direction.RIGHT, 100)
        assertEquals(Position(0, 5), terminal.cursor)
    }

    @Test
    fun `cursor moves left by 1`() {
        val terminal: MutableTerminalBuffer = MutableTerminalBufferImpl(6, 4)
        terminal.cursor = Position(0, 5)
        terminal.moveCursor(Direction.LEFT, 1)
        assertEquals(Position(0, 4), terminal.cursor)
    }

    @Test
    fun `cursor moves left by 5`() {
        val terminal: MutableTerminalBuffer = MutableTerminalBufferImpl(6, 4)
        terminal.cursor = Position(0, 5)
        terminal.moveCursor(Direction.LEFT, 5)
        assertEquals(Position(0, 0), terminal.cursor)
    }

    @Test
    fun `cursor moves left by 100`() {
        val terminal: MutableTerminalBuffer = MutableTerminalBufferImpl(6, 4)
        terminal.cursor = Position(0, 5)
        terminal.moveCursor(Direction.LEFT, 100)
        assertEquals(Position(0, 0), terminal.cursor)
    }

    @Test
    fun `negative distance throws`() {
        val terminal: MutableTerminalBuffer = MutableTerminalBufferImpl(6, 4)
        assertThrows<IllegalArgumentException> {
            terminal.moveCursor(Direction.LEFT, -1)
        }
    }
}