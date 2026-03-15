package org.example.terminal.buffer

import org.example.MutableTerminalBufferImpl
import org.example.models.Position
import org.example.screen.adjustToTest
import org.example.screen.inputTrim
import org.example.screen.terminalTrim
import kotlin.test.Test
import kotlin.test.assertEquals

class ClearScrollbackTest {
    @Test
    fun `clear scrollback clears partially filled scrollback`() {
        val input          = """|weru|
                                |oasz|
                                |----|""".inputTrim()

        val expectedScroll = ""
        val expectedOutput = """|____|
                                |____|
                                |____|""".terminalTrim()
        val expectedCursorPosition = Position(0, 0)

        val screen = MutableTerminalBufferImpl(4, 3)
        screen.write(input)
        screen.cursor = expectedCursorPosition

        screen.clearScreen()
        screen.clearScrollback()

        assertEquals(expectedScroll + expectedOutput, screen.screenAndScrollback.adjustToTest())
        assertEquals(Position(0, 0), screen.cursor)
    }

}