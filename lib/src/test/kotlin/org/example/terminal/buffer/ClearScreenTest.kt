package org.example.terminal.buffer

import org.example.MutableTerminalBufferImpl
import org.example.models.Position
import org.example.screen.MutableScreenImpl
import org.example.screen.adjustToTest
import org.example.screen.inputTrim
import org.example.screen.joinToStr
import org.example.screen.terminalTrim
import kotlin.test.Test
import kotlin.test.assertEquals

class ClearScreenTest {
    @Test
    fun `cleared lines are scrolled`() {
        val input          = """|weru|
                                |oasz|
                                |----|""".inputTrim()

        val expectedScroll = """|weru|
                                |oasz|""".terminalTrim()
        val expectedOutput = """|____|
                                |____|
                                |____|""".terminalTrim()
        val expectedCursorPosition = Position(0, 0)

        val screen = MutableTerminalBufferImpl(4, 3)
        screen.write(input)
        screen.cursor = expectedCursorPosition

        screen.clearScreen()

        assertEquals(expectedScroll + expectedOutput, screen.screenAndScrollback.adjustToTest())
        assertEquals(Position(0, 0), screen.cursor)
        assertEquals(expectedOutput, screen.screenContent.adjustToTest())
    }

}