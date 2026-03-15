package org.example.screen

import org.example.models.Cell
import org.example.models.Line
import org.example.models.Position
import org.example.models.ScrolledLines
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal fun Line.joinToStr() = cells.joinToString("") {
    when (it) {
        is Cell -> it.char.toString()
        Cell.Empty -> "-"
    }
}
internal fun List<Line>.joinToStr() = joinToString("") { line -> line.joinToStr() }
internal fun ScrolledLines.joinToStr() = lines.joinToStr()

internal fun String.terminalTrim() = this
    .split("|")
    .filterIndexed { index, _ -> index % 2 == 1 }
    .joinToString("")


internal fun String.inputTrim() = terminalTrim()
    .filter { it != '-' }
    .filter { if (it == ' ') error("Use '_' as a space") else true }


internal fun String.adjustToTest() = replace(' ', '_').filter { it != '\n' }

class MutableScreenWriteTest {

    @Test
    fun `write whole screen except last char fills screen and makes no scroll`() {
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
    fun `write whole screen fills screen and makes one scroll`() {
        val input          = """|1234|
                                |1234|
                                |1234|""".inputTrim()

        val expectedScroll = """|1234|""".terminalTrim()
        val expectedOutput = """|1234|
                                |1234|
                                |----|""".terminalTrim()

        val screen = MutableScreenImpl(4, 3)
        val scroll = screen.write(input).joinToStr()
        val contentString = screen.content.joinToStr()

        assertEquals(expectedScroll, scroll.adjustToTest())
        assertEquals(expectedOutput, contentString.adjustToTest())
    }

    @Test
    fun `write less than line left just replaces chars`() {
        val fill           = """|1234|
                                |1c34|
                                |123-|""".inputTrim()
        val newCursorPosition = Position(1, 1)
        val input = "te"

        val expectedScroll = ""
        val expectedOutput = """|1234|
                                |1te4|
                                |123-|""".terminalTrim()

        val screen = MutableScreenImpl(4, 3)
        screen.write(fill).joinToStr()

        screen.cursor = newCursorPosition
        val scroll = screen.write(input).joinToStr()
        val contentString = screen.content.joinToStr()

        assertEquals(expectedScroll, scroll.adjustToTest())
        assertEquals(expectedOutput, contentString.adjustToTest())
    }


    @Test
    fun `write from start till the end of the line`() {
        val fill           = """|weru|
                                |Casz|
                                |cnv-|""".inputTrim()
        val cursorPosition = Position(1, 0)
        val input          = """|----|
                                |TEST|
                                |----|""".inputTrim()

        val expectedScroll = ""
        val expectedOutput = """|weru|
                                |TEST|
                                |cnv-|""".terminalTrim()
        val expectedCursorPosition = Position(2, 0)

        val screen = MutableScreenImpl(4, 3)
        screen.write(fill).joinToStr()

        screen.cursor = cursorPosition
        val scroll = screen.write(input).joinToStr()
        val contentString = screen.content.joinToStr()

        assertEquals(expectedCursorPosition, screen.cursor)
        assertEquals(expectedScroll, scroll.adjustToTest())
        assertEquals(expectedOutput, contentString.adjustToTest())
    }

    @Test
    fun `write more than line left but less that whole screen`() {
        val fill           = """|aCow|
                                |erus|
                                |zxc-|""".inputTrim()
        val cursorPosition = Position(0, 1)
        val input          = """|-TES|
                                |TTES|
                                |TE--|""".inputTrim()

        val expectedScroll = ""
        val expectedOutput = """|aTES|
                                |TTES|
                                |TEc-|""".terminalTrim()
        val expectedCursorPosition = Position(2, 2)

        val screen = MutableScreenImpl(4, 3)
        screen.write(fill).joinToStr()

        screen.cursor = cursorPosition
        val scroll = screen.write(input).joinToStr()
        val contentString = screen.content.joinToStr()

        assertEquals(expectedCursorPosition, screen.cursor)
        assertEquals(expectedScroll, scroll.adjustToTest())
        assertEquals(expectedOutput, contentString.adjustToTest())
    }

    @Test
    fun `write more than whole screen`() {
        val fill           = """|aCow|
                                |erus|
                                |zxc-|""".inputTrim()
        val cursorPosition = Position(0, 1)
        val input          = """|-TES|
                                |TTES|
                                |TTES|
                                |TTES|
                                |TE--|""".inputTrim()

        val expectedScroll = """|aTES|
                                |TTES|""".terminalTrim()
        val expectedOutput = """|TTES|
                                |TTES|
                                |TE--|""".terminalTrim()
        val expectedCursorPosition = Position(2, 2)

        val screen = MutableScreenImpl(4, 3)
        screen.write(fill).joinToStr()

        screen.cursor = cursorPosition
        val scroll = screen.write(input).joinToStr()
        val contentString = screen.content.joinToStr()

        assertEquals(expectedCursorPosition, screen.cursor)
        assertEquals(expectedScroll, scroll.adjustToTest())
        assertEquals(expectedOutput, contentString.adjustToTest())
    }
}