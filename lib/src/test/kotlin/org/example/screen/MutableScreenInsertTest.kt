package org.example.screen

import org.example.models.Position
import kotlin.test.Test
import kotlin.test.assertEquals

class MutableScreenInsertTest {
    @Test
    fun `insert whole screen except last char fills screen and makes no scroll`() {
        val input          = """|1234|
                                |1234|
                                |123-|""".inputTrim()

        val expectedScroll = ""
        val expectedOutput = """|1234|
                                |1234|
                                |123-|""".terminalTrim()

        val screen = MutableScreenImpl(4, 3)
        val scroll = screen.write(input).joinToStr()
        val contentString = screen.content.joinToStr()

        assertEquals(expectedScroll, scroll.adjustToTest())
        assertEquals(expectedOutput, contentString.adjustToTest())
    }

    @Test
    fun `insert whole screen fills screen and makes one scroll`() {
        val input          = """|1234|
                                |1234|
                                |1234|""".inputTrim()

        val expectedScroll = """|1234|""".terminalTrim()
        val expectedOutput = """|1234|
                                |1234|
                                |----|""".terminalTrim()

        val screen = MutableScreenImpl(4, 3)
        val scroll = screen.insert(input).joinToStr()
        val contentString = screen.content.joinToStr()

        assertEquals(expectedScroll, scroll.adjustToTest())
        assertEquals(expectedOutput, contentString.adjustToTest())
    }

    @Test
    fun `insert from start one char moving one char`() {
        val fill           = """|Cw--|
                                |----|
                                |----|""".inputTrim()
        val cursorPosition = Position(0, 0)
        val input          = """|a---|
                                |----|
                                |----|""".inputTrim()

        val expectedScroll = ""
        val expectedOutput = """|aCw-|
                                |----|
                                |----|""".terminalTrim()
        val expectedCursorPosition = Position(0, 1)

        val screen = MutableScreenImpl(4, 3)
        screen.write(fill).joinToStr()

        screen.cursor = cursorPosition
        val scroll = screen.insert(input).joinToStr()
        val contentString = screen.content.joinToStr()

        assertEquals(expectedCursorPosition, screen.cursor)
        assertEquals(expectedScroll, scroll.adjustToTest())
        assertEquals(expectedOutput, contentString.adjustToTest())
    }

    @Test
    fun `insert chars to middle of line moving other chars for a few lines`() {
        val fill           = """|wCr-|
                                |----|
                                |----|""".inputTrim()
        val cursorPosition = Position(0, 1)
        val input          = """|aszx|
                                |cv--|
                                |----|""".inputTrim()

        val expectedScroll = ""
        val expectedOutput = """|wasz|
                                |xcvC|
                                |r---|""".terminalTrim()
        val expectedCursorPosition = Position(1, 3)

        val screen = MutableScreenImpl(4, 3)
        screen.write(fill).joinToStr()

        screen.cursor = cursorPosition
        val scroll = screen.insert(input).joinToStr()
        val contentString = screen.content.joinToStr()

        assertEquals(expectedCursorPosition, screen.cursor)
        assertEquals(expectedScroll, scroll.adjustToTest())
        assertEquals(expectedOutput, contentString.adjustToTest())
    }


    @Test
    fun `insert chars moving other chars for a few lines and scrolling`() {
        val fill           = """|wero|
                                |C---|
                                |----|""".inputTrim()
        val cursorPosition = Position(1, 0)
        val input          = """|aszx|
                                |cvnm|
                                |eruv|""".inputTrim()

        val expectedScroll = """|wero|
                                |aszx|""".inputTrim()
        val expectedOutput = """|cvnm|
                                |eruv|
                                |C---|""".terminalTrim()
        val expectedCursorPosition = Position(2, 0)

        val screen = MutableScreenImpl(4, 3)
        screen.write(fill).joinToStr()

        screen.cursor = cursorPosition
        val scroll = screen.insert(input).joinToStr()
        val contentString = screen.content.joinToStr()

        assertEquals(expectedCursorPosition, screen.cursor)
        assertEquals(expectedScroll, scroll.adjustToTest())
        assertEquals(expectedOutput, contentString.adjustToTest())
    }
}