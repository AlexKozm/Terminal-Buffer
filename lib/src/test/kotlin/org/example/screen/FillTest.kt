package org.example.screen

import org.example.models.Position
import kotlin.test.Test
import kotlin.test.assertEquals

class FillTest {
    @Test
    fun `fill some line in the middle of empty screen`() {
        val expectedOutput = """|----|
                                |****|
                                |----|""".terminalTrim()
        val cursorPosition = Position(1, 2)

        val screen = MutableScreenImpl(4, 3)
        screen.cursor = cursorPosition
        screen.fill('*')
        val contentString = screen.content.joinToStr()

        assertEquals(cursorPosition, screen.cursor)
        assertEquals(expectedOutput, contentString.adjustToTest())
    }

    @Test
    fun `fill last line with existed text`() {
        val fill           = """|1234|
                                |5678|
                                |901-|""".inputTrim()
        val cursorPosition = Position(2, 3)

        val expectedOutput = """|1234|
                                |5678|
                                |****|""".terminalTrim()

        val screen = MutableScreenImpl(4, 3)
        screen.write(fill)

        screen.cursor = cursorPosition
        screen.fill('*')

        val contentString = screen.content.joinToStr()

        assertEquals(cursorPosition, screen.cursor)
        assertEquals(expectedOutput, contentString.adjustToTest())
    }
}