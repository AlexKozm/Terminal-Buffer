package org.example.screen

import org.example.models.Position
import kotlin.test.Test
import kotlin.test.assertEquals

class InsertEmptyLineAtBottomTest {
    @Test
    fun `insert from start one char moving one char`() {
        val fill           = """|as__|
                                |____|
                                |_as-|""".inputTrim()
        val cursorPosition = Position(1, 1)

        val expectedScroll = """|as__|""".terminalTrim()
        val expectedOutput = """|____|
                                |_as-|
                                |----|""".terminalTrim()

        val screen = MutableScreenImpl(4, 3)
        screen.write(fill).joinToStr()

        screen.cursor = cursorPosition
        val scroll = screen.insertEmptyLineAtBottom().joinToStr()
        val contentString = screen.content.joinToStr()

        assertEquals(cursorPosition, screen.cursor)
        assertEquals(expectedScroll, scroll.adjustToTest())
        assertEquals(expectedOutput, contentString.adjustToTest())
    }

    @Test
    fun `insert even if there are empty lines`() {
        val fill           = """|as--|
                                |----|
                                |----|""".inputTrim()
        val cursorPosition = Position(1, 1)

        val expectedScroll = """|as--|""".terminalTrim()
        val expectedOutput = """|----|
                                |----|
                                |----|""".terminalTrim()

        val screen = MutableScreenImpl(4, 3)
        screen.write(fill).joinToStr()

        screen.cursor = cursorPosition
        val scroll = screen.insertEmptyLineAtBottom().joinToStr()
        val contentString = screen.content.joinToStr()

        assertEquals(cursorPosition, screen.cursor)
        assertEquals(expectedScroll, scroll.adjustToTest())
        assertEquals(expectedOutput, contentString.adjustToTest())
    }
}